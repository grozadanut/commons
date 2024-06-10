package ro.linic.util.commons.calculator;

import static ro.linic.util.commons.PresentationUtils.EMPTY_STRING;
import static ro.linic.util.commons.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Calculator {
	private static final Logger log = Logger.getLogger(Calculator.class.getName());
	private static final ExpressionParser<Object> parser = BigDecimalProcessor.createParser();

	/**
	 * Parses the expression and returns the result of the calculation.
	 * java.util.Math functions are supported.
	 * <br><br>
	 * Note 1: implicit operator doesn't work<br>
	 * eg: 2(3) - invalid, 2*(3) - valid
	 * <br><br>
	 * Special thanks to Stefan Haustein for the parser:
	 * https://github.com/stefanhaustein/expressionparser/blob/master/demo/calculator/src/main/java/org/kobjects/expressionparser/demo/calculator/Calculator.java
	 */
	public static synchronized BigDecimal parse(final String expression) {
		if (isEmpty(expression))
			return BigDecimal.ZERO;

		try {
			// might contain % in case of a single suffix operator, eg: 10%
			final Object result = parser.parse(expression.replaceAll(" |\t", EMPTY_STRING));
			return BigDecimalProcessor.removePercentage(result);
		} catch (final Exception ex) {
			log.log(Level.WARNING, "error parsing: "+expression, ex);
			return BigDecimal.ZERO;
		}
	}
}
