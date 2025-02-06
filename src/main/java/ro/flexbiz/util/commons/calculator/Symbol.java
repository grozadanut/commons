package ro.flexbiz.util.commons.calculator;

class Symbol {
	final int precedence;
	final OperatorType type;
	final String separator;
	final String close;

	Symbol(final int precedence, final OperatorType type) {
		this.precedence = precedence;
		this.type = type;
		this.separator = null;
		this.close = null;
	}

	Symbol(final int precedence, final String separator, final String close) {
		this.precedence = precedence;
		this.type = null;
		this.separator = separator;
		this.close = close;
	}

	@Override
	public String toString() {
		return "Symbol [precedence=" + precedence + ", type=" + type + ", separator=" + separator + ", close=" + close
				+ "]";
	}
}
