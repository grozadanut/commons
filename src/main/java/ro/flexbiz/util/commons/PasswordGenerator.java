package ro.flexbiz.util.commons;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Does not contain characters that are easy to mismatch with one another: 
 * eg: 1 and l(one and lower case L)
 * 
 * @author Groza Danut
 */
public class PasswordGenerator
{
	private static final String LOWER = "abcdefghijkmnopqrstuvwxyz";
	private static final String UPPER = "ABCDEFGHJKLMNOPQRSTUVWXYZ";
	private static final String DIGITS = "023456789";
	private static final String PUNCTUATION = "!@#$%^&*=+-/";
	private boolean useLower = true;
	private boolean useUpper = true;
	private boolean useDigits = true;
	private boolean usePunctuation = true;
	
	public static PasswordGenerator withDefaults() {
		return new PasswordGenerator();
	}

	private PasswordGenerator(){}

	private PasswordGenerator(final boolean useLower, final boolean useUpper, final boolean useDigits, final boolean usePunctuation) {
		super();
		this.useLower = useLower;
		this.useUpper = useUpper;
		this.useDigits = useDigits;
		this.usePunctuation = usePunctuation;
	}

	/**
	 * This method will generate a password depending the use* properties you
	 * define. It will use the categories with a probability. It is not sure that
	 * all of the defined categories will be used.
	 *
	 * @param length
	 *            the length of the password you would like to generate.
	 * @return a password that uses the categories you define when constructing the
	 *         object with a probability.
	 */
	public String generate(final int length) {
		// Argument Validation.
		if (length <= 0)
			throw new IllegalArgumentException("Length should be greater than zero: "+length);

		// Variables.
		final StringBuilder password = new StringBuilder(length);
		final SecureRandom random = new SecureRandom();

		// Collect the categories to use.
		final List<String> charCategories = new ArrayList<>(4);
		if (useLower)
			charCategories.add(LOWER);
		
		if (useUpper)
			charCategories.add(UPPER);
		
		if (useDigits)
			charCategories.add(DIGITS);
		
		if (usePunctuation)
			charCategories.add(PUNCTUATION);

		// Build the password.
		for (int i = 0; i < length; i++) {
			final String charCategory = charCategories.get(random.nextInt(charCategories.size()));
			final int position = random.nextInt(charCategory.length());
			password.append(charCategory.charAt(position));
		}
		return new String(password);
	}
	
	public static class Builder {
		private boolean useLower;
		private boolean useUpper;
		private boolean useDigits;
		private boolean usePunctuation;

		public Builder() {
			this.useLower = true;
			this.useUpper = true;
			this.useDigits = true;
			this.usePunctuation = true;
		}

		/**
		 * Set true in case you would like to include lower characters (abc...xyz).
		 * Default true.
		 *
		 * @param useLower
		 *            true in case you would like to include lower characters
		 *            (abc...xyz)
		 * @return the builder for chaining.
		 */
		public Builder useLower(final boolean useLower) {
			this.useLower = useLower;
			return this;
		}

		/**
		 * Set true in case you would like to include upper characters (ABC...XYZ).
		 * Default true.
		 *
		 * @param useUpper
		 *            true in case you would like to include upper characters
		 *            (ABC...XYZ)
		 * @return the builder for chaining.
		 */
		public Builder useUpper(final boolean useUpper) {
			this.useUpper = useUpper;
			return this;
		}

		/**
		 * Set true in case you would like to include digit characters (023..). Default
		 * true.
		 *
		 * @param useDigits
		 *            true in case you would like to include digit characters (023..)
		 * @return the builder for chaining.
		 */
		public Builder useDigits(final boolean useDigits) {
			this.useDigits = useDigits;
			return this;
		}

		/**
		 * Set true in case you would like to include punctuation characters (!@#..).
		 * Default true.
		 *
		 * @param usePunctuation
		 *            true in case you would like to include punctuation characters
		 *            (!@#..)
		 * @return the builder for chaining.
		 */
		public Builder usePunctuation(final boolean usePunctuation) {
			this.usePunctuation = usePunctuation;
			return this;
		}

		/**
		 * Get an object to use.
		 *
		 * @return the {@link PasswordGenerator} object.
		 */
		public PasswordGenerator build() {
			if (!useLower && !useUpper && !useDigits && !usePunctuation)
				throw new IllegalArgumentException("At least one category should be specified!");
			
			return new PasswordGenerator(useLower, useUpper, useDigits, usePunctuation);
		}
	}
}
