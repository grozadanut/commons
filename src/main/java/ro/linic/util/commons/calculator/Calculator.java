package ro.linic.util.commons.calculator;

import static ro.linic.util.commons.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Calculator {
	private static final Logger log = Logger.getLogger(Calculator.class.getName());
	private static final ExpressionParser<BigDecimal> parser = BigDecimalProcessor.createParser();

	/**
	 * Parses the expression and returns the result of the calculation.
	 * java.util.Math functions are supported.
	 * <br><br>
	 * Note 1: spaces are NOT supported as thousand separator, 
	 * the implicit operator is applied when space is found<br>
	 * Note 2: implicit operator is multiplication<br>
	 * Note 3: implicit operator does NOT work before brackets<br>
	 * eg: 2(3) - invalid, 2*(3) - valid
	 * <br><br>
	 * Special thanks to Stefan Haustein for the parser:
	 * https://github.com/stefanhaustein/expressionparser/blob/master/demo/calculator/src/main/java/org/kobjects/expressionparser/demo/calculator/Calculator.java
	 */
	public static synchronized BigDecimal parse(final String expression) {
		if (isEmpty(expression))
			return BigDecimal.ZERO;

		try {
			return parser.parse(expression);
		} catch (final Exception ex) {
			log.log(Level.WARNING, "error parsing: "+expression, ex);
			return BigDecimal.ZERO;
		}
	}
}
