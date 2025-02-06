package ro.flexbiz.util.commons.calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * A simple configurable expression parser.
 */
public class ExpressionParser<T> {
	private final HashMap<String, Symbol> prefix = new HashMap<>();
	private final HashMap<String, Symbol> infix = new HashMap<>();
	private final HashSet<String> otherSymbols = new HashSet<>();
	private final HashSet<String> primary = new HashSet<>();
	private final HashMap<String, String[]> calls = new HashMap<>();
	private final HashMap<String, String[]> groups = new HashMap<>();

	private final Processor<T> processor;
	private int strongImplicitOperatorPrecedence = -1;
	private int weakImplicitOperatorPrecedence = -1;

	public static String unquote(final String s) {
		final StringBuilder sb = new StringBuilder();
		final int len = s.length() - 1;
		for (int i = 1; i < len; i++) {
			char c = s.charAt(i);
			if (c == '\\') {
				c = s.charAt(++i);
				switch (c) {
				case 'b':
					sb.append('\b');
					break;
				case 'f':
					sb.append('\f');
					break;
				case 'n':
					sb.append('\n');
					break;
				case 't':
					sb.append('\t');
					break;
				case 'r':
					sb.append('\r');
					break;
				default:
					sb.append(c);
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public ExpressionParser(final Processor<T> processor) {
		this.processor = processor;
	}

	/**
	 * Adds "apply" brackets with the given precedence. Used for function calls or
	 * array element access.
	 */
	public void addApplyBrackets(final int precedence, final String open, final String separator, final String close) {
		infix.put(open, new Symbol(precedence, separator, close));
		if (separator != null) {
			otherSymbols.add(separator);
		}
		otherSymbols.add(close);
	}

	/**
	 * Adds "call" brackets, parsed eagerly after identifiers.
	 */
	public void addCallBrackets(final String open, final String separator, final String close) {
		calls.put(open, new String[] { separator, close });
		otherSymbols.add(open);
		if (separator != null) {
			otherSymbols.add(separator);
		}
		otherSymbols.add(close);
	}

	/**
	 * Adds grouping. If the separator is null, only a single element will be
	 * permitted. If the separator is empty, whitespace will be sufficient for
	 * element separation. Used for parsing lists or overriding the operator
	 * precedence (typically with parens and a null separator).
	 */
	public void addGroupBrackets(final String open, final String separator, final String close) {
		groups.put(open, new String[] { separator, close });
		otherSymbols.add(open);
		if (separator != null) {
			otherSymbols.add(separator);
		}
		otherSymbols.add(close);
	}

	public void addPrimary(final String... names) {
		for (final String name : names) {
			primary.add(name);
		}
	}

	public void addTernaryOperator(final int precedence, final String primaryOperator, final String secondaryOperator) {
		infix.put(primaryOperator, new Symbol(precedence, secondaryOperator, null));
		otherSymbols.add(secondaryOperator);
	}

	/**
	 * Add prefixOperator, infixOperator or postfix operators with the given
	 * precedence.
	 */
	public void addOperators(final OperatorType type, final int precedence, final String... names) {
		for (final String name : names) {
			if (type == OperatorType.PREFIX) {
				prefix.put(name, new Symbol(precedence, type));
			} else {
				infix.put(name, new Symbol(precedence, type));
			}
		}
	}

	/**
	 * Implicit operator is applied between operators separated by space, 
	 * it currently does not work between operator and group separator: 
	 * eg. 2(3) - invalid; 2 3 = 6
	 */
	public void setImplicitOperatorPrecedence(final boolean strong, final int precedence) {
		if (strong) {
			strongImplicitOperatorPrecedence = precedence;
		} else {
			weakImplicitOperatorPrecedence = precedence;
		}
	}

	/**
	 * Returns all symbols registered via add...Operator and add...Bracket calls.
	 * Useful for tokenizer construction.
	 */
	public Iterable<String> getSymbols() {
		final HashSet<String> result = new HashSet<>();
		result.addAll(otherSymbols);
		result.addAll(infix.keySet());
		result.addAll(prefix.keySet());
		result.addAll(primary);
		return result;
	}

	/**
	 * Parser the given expression using a simple StreamTokenizer-based parser.
	 * Leftover tokens will cause an exception.
	 */
	public T parse(final String expr) {
		final Tokenizer tokenizer = new Tokenizer(new Scanner(expr), getSymbols());
		tokenizer.nextToken();
		final T result = parse(tokenizer);
		if (tokenizer.currentType != Tokenizer.TokenType.EOF) {
			throw tokenizer.exception("Leftover input.", null);
		}
		return result;
	}

	/**
	 * Parser an expression from the given tokenizer. Leftover tokens will be
	 * ignored and may be handled by the caller.
	 */
	public T parse(final Tokenizer tokenizer) {
		try {
			return parseOperator(tokenizer, -1);
		} catch (final ParsingException e) {
			throw e;
		} catch (final Exception e) {
			throw tokenizer.exception(e.getMessage(), e);
		}
	}

	private T parsePrefix(final Tokenizer tokenizer) {
		final String token = tokenizer.currentValue;
		final Symbol prefixSymbol = prefix.get(token);
		if (prefixSymbol == null) {
			return parsePrimary(tokenizer);
		}
		tokenizer.nextToken();
		final T operand = parseOperator(tokenizer, prefixSymbol.precedence);
		return processor.prefixOperator(tokenizer, token, operand);
	}

	private T parseOperator(final Tokenizer tokenizer, final int precedence) {
		T left = parsePrefix(tokenizer);

		while (true) {
			final String token = tokenizer.currentValue;
			final Symbol symbol = infix.get(token);
			if (symbol == null) {
				if (token.equals("") || otherSymbols.contains(token)) {
					break;
				}
				// Implicit operator
				final boolean strong = tokenizer.leadingWhitespace.isEmpty();
				final int implicitPrecedence = strong ? strongImplicitOperatorPrecedence : weakImplicitOperatorPrecedence;
				if (!(implicitPrecedence > precedence)) {
					break;
				}
				final T right = parseOperator(tokenizer, implicitPrecedence);
				left = processor.implicitOperator(tokenizer, strong, left, right);
			} else {
				if (!(symbol.precedence > precedence)) {
					break;
				}
				tokenizer.nextToken();
				if (symbol.type == null) {
					if (symbol.close == null) {
						// Ternary
						final T middle = parseOperator(tokenizer, -1);
						tokenizer.consume(symbol.separator);
						final T right = parseOperator(tokenizer, symbol.precedence);
						left = processor.ternaryOperator(tokenizer, token, left, middle, right);
					} else {
						// Group
						final List<T> list = parseList(tokenizer, symbol.separator, symbol.close);
						left = processor.apply(tokenizer, left, token, list);
					}
				} else {
					switch (symbol.type) {
					case INFIX: {
						final T right = parseOperator(tokenizer, symbol.precedence);
						left = processor.infixOperator(tokenizer, token, left, right);
						break;
					}
					case INFIX_RTL: {
						final T right = parseOperator(tokenizer, symbol.precedence - 1);
						left = processor.infixOperator(tokenizer, token, left, right);
						break;
					}
					case SUFFIX:
						left = processor.suffixOperator(tokenizer, token, left);
						break;
					default:
						throw new IllegalStateException();
					}
				}
			}
		}
		return left;
	}

	//  Precondition: Opening paren consumed
	//  Postcondition: Closing paren consumed
	List<T> parseList(final Tokenizer tokenizer, final String separator, final String close) {
		final ArrayList<T> elements = new ArrayList<>();
		if (!tokenizer.currentValue.equals(close)) {
			while (true) {
				elements.add(parse(tokenizer));
				final String op = tokenizer.currentValue;
				if (op.equals(close)) {
					break;
				}
				if (separator == null) {
					throw tokenizer.exception("Closing bracket expected: '" + close + "'.", null);
				}
				if (!separator.isEmpty()) {
					if (!op.equals(separator)) {
						throw tokenizer.exception(
								"List separator '" + separator + "' or closing paren '" + close + " expected.", null);
					}
					tokenizer.nextToken(); //  separator
				}
			}
		}
		tokenizer.nextToken(); //  closing paren
		return elements;
	}

	T parsePrimary(final Tokenizer tokenizer) {
		final String candidate = tokenizer.currentValue;
		if (groups.containsKey(candidate)) {
			tokenizer.nextToken();
			final String[] grouping = groups.get(candidate);
			return processor.group(tokenizer, candidate, parseList(tokenizer, grouping[0], grouping[1]));
		}

		if (primary.contains(candidate)) {
			tokenizer.nextToken();
			return processor.primary(tokenizer, candidate);
		}

		T result;
		switch (tokenizer.currentType) {
		case NUMBER:
			tokenizer.nextToken();
			result = processor.numberLiteral(tokenizer, candidate);
			break;
		case IDENTIFIER:
			tokenizer.nextToken();
			if (calls.containsKey(tokenizer.currentValue)) {
				final String openingBracket = tokenizer.currentValue;
				final String[] call = calls.get(openingBracket);
				tokenizer.nextToken();
				result = processor.call(tokenizer, candidate, openingBracket, parseList(tokenizer, call[0], call[1]));
			} else {
				result = processor.identifier(tokenizer, candidate);
			}
			break;
		case STRING:
			tokenizer.nextToken();
			result = processor.stringLiteral(tokenizer, candidate);
			break;
		default:
			throw tokenizer.exception("Unexpected token type.", null);
		}
		return result;
	}

}
