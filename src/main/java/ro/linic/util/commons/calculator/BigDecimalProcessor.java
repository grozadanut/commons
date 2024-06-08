package ro.linic.util.commons.calculator;

import static ro.linic.util.commons.NumberUtils.add;
import static ro.linic.util.commons.NumberUtils.divide;
import static ro.linic.util.commons.NumberUtils.multiply;
import static ro.linic.util.commons.NumberUtils.parse;
import static ro.linic.util.commons.NumberUtils.subtract;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

class BigDecimalProcessor extends Processor<BigDecimal> {
	private static final Class[] DOUBLE_TYPE_ARRAY_1 = {Double.TYPE};
	private static final Map<String, Double> variables = Map.of("tau", 2 * Math.PI,
			"pi", Math.PI,
			"e", Math.E);
	 
	@Override
	public BigDecimal infixOperator(final Tokenizer tokenizer, final String name, final BigDecimal left, final BigDecimal right) {
		switch (name.charAt(0)) {
		case '+':
			return add(left, right);
		case '-':
			return subtract(left, right);
		case '*':
			return multiply(left, right);
		case '/':
			return divide(left, right, 64, RoundingMode.HALF_EVEN);
		case '^':
			return new BigDecimal(Math.pow(left.doubleValue(), right.doubleValue()));
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public BigDecimal implicitOperator(final Tokenizer tokenizer, final boolean strong, final BigDecimal left,
			final BigDecimal right) {
		return multiply(left, right);
	}

	@Override
	public BigDecimal prefixOperator(final Tokenizer tokenizer, final String name, final BigDecimal argument) {
		return name.equals("-") ? argument.negate() : argument;
	}

	@Override
	public BigDecimal numberLiteral(final Tokenizer tokenizer, final String value) {
		return parse(value);
	}

	@Override
	public BigDecimal identifier(final Tokenizer tokenizer, final String name) {
		final Double value = variables.get(name);
		if (value == null) {
			throw new IllegalArgumentException("Undeclared variable: " + name);
		}
		return new BigDecimal(value);
	}

	@Override
	public BigDecimal group(final Tokenizer tokenizer, final String paren, final List<BigDecimal> elements) {
		return elements.get(0);
	}

	/**
	 *   Delegates function calls to Math via reflection.
	 */
	@Override
	public BigDecimal call(final Tokenizer tokenizer, final String identifier, final String bracket,
			final List<BigDecimal> arguments) {
		if (arguments.size() == 1) {
			try {
				return (BigDecimal) Math.class.getMethod(identifier, DOUBLE_TYPE_ARRAY_1).invoke(null, arguments.get(0).doubleValue());
			} catch (final Exception e) {
				//  Fall through
			}
		}
		return super.call(tokenizer, identifier, bracket, arguments);
	}

	/**
	 * Creates a parser for this processor with matching operations and precedences
	 * set up.
	 */
	static ExpressionParser<BigDecimal> createParser() {
		final ExpressionParser<BigDecimal> parser = new ExpressionParser<BigDecimal>(new BigDecimalProcessor());
		parser.addCallBrackets("(", ",", ")");
		parser.addGroupBrackets("(", null, ")");
		parser.addOperators(OperatorType.INFIX_RTL, 4, "^");
		parser.addOperators(OperatorType.PREFIX, 3, "+", "-");
		parser.setImplicitOperatorPrecedence(true, 2);
		parser.setImplicitOperatorPrecedence(false, 2);
		parser.addOperators(OperatorType.INFIX, 1, "*", "/");
		parser.addOperators(OperatorType.INFIX, 0, "+", "-");
		return parser;
	}
}
