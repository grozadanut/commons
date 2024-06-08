package ro.linic.util.commons.calculator;

public class ParsingException extends RuntimeException {
	final public int start;
	final public int end;

	public ParsingException(final int start, final int end, final String text, final Exception base) {
		super(text, base);
		this.start = start;
		this.end = end;
	}
}
