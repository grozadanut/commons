package ro.flexbiz.util.commons;

import static ro.flexbiz.util.commons.PresentationUtils.ELLIPSES;
import static ro.flexbiz.util.commons.PresentationUtils.EMPTY_STRING;
import static ro.flexbiz.util.commons.PresentationUtils.SPACE;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StringUtils
{
	final public static Pattern REMOVE_ACCENTS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+"); //$NON-NLS-1$
	
	public enum TextFilterMethod
	{
		CONTAINS("*=*", "Contains", "Find all items that contain this value. Use ''and'' and ''or'' keywords to search for several values at the same time."),
		NOT_CONTAINS("!*=*", "Not Contains", "Find all items without the containing value. Use ''and'' and ''or'' keywords to exclude several values."),
		BEGINS_WITH("=*", "Begins With", "Find all items that begin with this value"),
		NOT_BEGINS_WITH("!=*", "Not Begins With", "Find all items that do not have this value at the beginning"),
		ENDS_WITH("*=", "Ends With", "Find all items that end in this value"),
		NOT_ENDS_WITH("!*=", "Not Ends With", "Find all items that do not have this value at the end"),
		EQUALS("=", "Equals", "Find all items equal to this value. Use ''or'' keyword to search for several values"),
		NOT_EQUALS("!=", "Not Equals", "Find all items not equal to this value");
		
		public static List<TextFilterMethod> negativeSearch()
		{
			return List.of(TextFilterMethod.NOT_EQUALS, TextFilterMethod.NOT_CONTAINS, TextFilterMethod.NOT_BEGINS_WITH,
					TextFilterMethod.NOT_ENDS_WITH);
		}
		
		private final String presentationText;
		private final String name;
		private final String description;

		private TextFilterMethod(final String presentationText, final String name, final String description)
		{
			this.presentationText = presentationText;
			this.name = name;
			this.description = description;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getDescription()
		{
			return description;
		}
		
		public String code()
		{
			return presentationText;
		}
	}
	
	
	public static String randomUniqueId()
	{
		return UUID.randomUUID().toString().replaceAll("-", EMPTY_STRING);
	}
	
	/**
     * <p>Checks if a CharSequence is empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the CharSequence.
     * That functionality is available in isBlank().</p>
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is empty or null
     * @since 3.0 Changed signature from isEmpty(String) to isEmpty(CharSequence)
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.isEmpty();
    }
	
	public static boolean notEmpty(final String value)
	{
		return !isEmpty(value);
	}
	
	public static String replaceIfEmpty(final String value, final String newValue)
	{
		return isEmpty(value) ? newValue : value;
	}
	
	/**
	 * Truncates the <code>input</code> to at most <code>maxLength</code> chars.
	 * <br><br>
	 * Note: if the string is truncated, adds ELLIPSES at the end, 
	 * so the actual string will be truncated to maxLength - ELLIPSES.length<br>
	 * Special case: if maxLength <= 6 does not add ELLIPSES anymore
	 */
	public static String truncate(final String input, final int maxLength)
	{
		if (input == null)
			return EMPTY_STRING;
		
		String suffix = ELLIPSES;
		if (maxLength <= 6)
			suffix = EMPTY_STRING;
		
		if(input.length() > maxLength)
	        return input.substring(0, Math.max(maxLength-suffix.length(), 0))+suffix;
	    else
	        return input;
	}
	
	/**
	 * processForFiltering is applied to text and searchString, meaning accents are removed
	 * 
	 * @param text the text in which you want to search; if this one is empty, and searchString is NOT
	 * empty, then we return true on negative searched, or false otherwise
	 * @param searchString the string that is looked for in the text; if this one is empty,
	 * that means filtering is disabled and true is returned
	 * @param filterMethod
	 * @return
	 */
	public static boolean globalIsMatch(final String text, final String searchString,
			final TextFilterMethod filterMethod)
	{
		// If the filter text is empty, that means the filtering is disabled:
		if (isEmpty(searchString))
			return true;
		
		// By this time we know the search string is not empty, so we only have a match if the search is negative
		if (isEmpty(text))
			return TextFilterMethod.negativeSearch().contains(filterMethod);

		// Transform values:
		final String textL = processForFiltering(text);
		final String processedSearchString = processForFiltering(searchString);

		boolean result;
		switch (filterMethod)
		{
			case EQUALS:
				result = textL.equals(processedSearchString);
				break;
				
			case NOT_EQUALS:
				result = !textL.equals(processedSearchString);
				break;

			case BEGINS_WITH:
				result = textL.startsWith(processedSearchString);
				break;
				
			case NOT_BEGINS_WITH:
				result = !textL.startsWith(processedSearchString);
				break;

			case ENDS_WITH:
				result = textL.endsWith(processedSearchString);
				break;
				
			case NOT_ENDS_WITH:
				result = !textL.endsWith(processedSearchString);
				break;

			case NOT_CONTAINS:
				result = !textL.contains(processedSearchString);
				break;
				
			case CONTAINS:
			default:
				result = textL.contains(processedSearchString);
				break;
		}
		
		return result;
	}
	
	private static boolean smartSearch(final String text, final String searchPattern)
	{
		// By this time we know the search string is not empty, so an empty text is for sure no match:
		if (isEmpty(text))
			return false;

		// Transform values:
		return processForFiltering(text).matches(searchPattern);
	}
	
	/**
	 * Converts the string passed to a string ready to be searched.
	 * Normalizes the string passed and converts it to lower case
	 * and removes blanks.
	 * 
	 * <p>For 10 million iterations it takes 15 seconds</p>
	 * <p>For 1 million iterations it takes 2 seconds</p>
	 * <p>For 10 million iterations without accent striping it takes 1 second</p>
	 */
	public static String processForFiltering(final String text)
	{
		if(text == null) 
			return null;
		
		return stripAccents_internal(text.trim()).toLowerCase();
	}
	
	
	/**
	 * Strips every type of blank character (space, tab, new line character)
	 * 
	 * <p>For 1 milion iterations it takes 1.5 seconds</p>
	 */
	public static String stripBlankChars(final String origString)
	{
		return origString.replaceAll("\\s", EMPTY_STRING); //$NON-NLS-1$
	}
	
	/**
	 *<b>This is a very "expensive" method. For 1 million iterations it takes 20 seconds. Be careful when you used it.</b>
	 * 
	 * <p>ggrec, 2014.12.12: Taken from Apache Commons<p> 
	 * <p>Removes diacritics (~= accents) from a string. The case will not be altered.</p>
	 * <p>For instance, '&agrave;' will be replaced by 'a'.</p>
	 * <p>Note that ligatures will be left as is.</p>
	 *
	 * <pre>
	 * StringUtils.stripAccents(null)                = null
	 * StringUtils.stripAccents("")                  = ""
	 * StringUtils.stripAccents("control")           = "control"
	 * StringUtils.stripAccents("&eacute;clair")     = "eclair"
	 * </pre>
	 *
	 * @param input String to be stripped
	 * @return input text with diacritics removed
	 * @see http://commons.apache.org/proper/commons-lang/apidocs/src-html/org/apache/commons/lang3/StringUtils.html
	 * @see https://stackoverflow.com/questions/3322152/is-there-a-way-to-get-rid-of-accents-and-convert-a-whole-string-to-regular-lette
	 * @see https://stackoverflow.com/questions/3707977/converting-java-string-to-ascii
	 */
	public static String stripAccents(final String input)
	{
		if(input == null) 
			return null;
		
		return stripAccents_internal(input);
	}

	private static String stripAccents_internal(final String input)
	{
		final String decomposed = Normalizer.normalize(input, Normalizer.Form.NFD);
		
		// Note that this doesn't correctly remove ligatures...
		return REMOVE_ACCENTS_PATTERN.matcher(decomposed).replaceAll(EMPTY_STRING);
	}
	
	/**
	 * Strips strings accents and trims the string
	 */
	public static String processForStoring(final String value)
	{
		return Optional.ofNullable(value).map(String::trim).orElse(null);
	}
	
	/**
     * <p>Checks if the CharSequence contains only uppercase characters.</p>
     *
     * <p>{@code null} will return {@code false}.
     * An empty String (length()=0) will return {@code false}.</p>
     *
     * <pre>
     * StringUtils.isAllUpperCase(null)   = false
     * StringUtils.isAllUpperCase("")     = false
     * StringUtils.isAllUpperCase("  ")   = false
     * StringUtils.isAllUpperCase("ABC")  = true
     * StringUtils.isAllUpperCase("aBC")  = false
     * StringUtils.isAllUpperCase("A C")  = false
     * StringUtils.isAllUpperCase("A1C")  = false
     * StringUtils.isAllUpperCase("A/C")  = false
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if only contains uppercase characters, and is non-null
     * @since 2.5
     * @since 3.0 Changed signature from isAllUpperCase(String) to isAllUpperCase(CharSequence)
     */
    public static boolean isAllUpperCase(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isUpperCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * <p>Checks if the CharSequence contains only lowercase characters.</p>
     *
     * <p>{@code null} will return {@code false}.
     * An empty CharSequence (length()=0) will return {@code false}.</p>
     *
     * <pre>
     * StringUtils.isAllLowerCase(null)   = false
     * StringUtils.isAllLowerCase("")     = false
     * StringUtils.isAllLowerCase("  ")   = false
     * StringUtils.isAllLowerCase("abc")  = true
     * StringUtils.isAllLowerCase("abC")  = false
     * StringUtils.isAllLowerCase("ab c") = false
     * StringUtils.isAllLowerCase("ab1c") = false
     * StringUtils.isAllLowerCase("ab/c") = false
     * </pre>
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if only contains lowercase characters, and is non-null
     * @since 2.5
     * @since 3.0 Changed signature from isAllLowerCase(String) to isAllLowerCase(CharSequence)
     */
    public static boolean isAllLowerCase(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLowerCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Parses the string argument as a boolean.  The {@code boolean}
     * returned represents the value {@code true} if the string argument
     * is not {@code null} and is equal, ignoring case, to the string
     * {@code "true"} or {@code "Y"}.
     * Otherwise, a false value is returned, including for a null
     * argument.<p>
     * Example: {@code parseBoolean("True")} returns {@code true}.<br>
     * Example: {@code parseBoolean("yes")} returns {@code false}.<br>
     * Example: {@code parseBoolean("y")} returns {@code true}.
     *
     * @param      s   the {@code String} containing the boolean
     *                 representation to be parsed
     * @return     the boolean represented by the string argument
     */
	public static boolean parseBoolean(final String s) {
		if (isEmpty(s))
			return false;

		try {
			return "true".equalsIgnoreCase(s.trim()) || "Y".equalsIgnoreCase(s.trim());
		} catch (final Exception ex) {
			return false;
		}
	}
	
	/**
	 * This search gives the following functionalities: <br>
	 * 		- Close letters: i -> j, c -> k, i -> e, search claus <=> klaus<br>
	 *		- Duplicate letters: search iohanis <=> iohannis<br>
	 *		- Also the order should not matter so  Groza Danut <=> Danut Groza.<br><br>
     *
     * Regex symbols:<br>
	 *		.                  Any character (may or may not match line terminators)<br>
	 *		X*                 X zero or more times<br>
	 *		[abc]              a, b, or c<br>
	 *		X+                 X, one or more times<br><br>
	 *		
	 * .*[jie]+o+h+a+n+[jie]+s+.* would match johannes, johanes, iohanes and iohanis<br>
	 * Similar letters: [kc], [jie].
	 */
	public static abstract class SmartNameMatcher
	{
		final protected List<SmartNameMatcher> tokens;

		private SmartNameMatcher(final List<SmartNameMatcher> tokens)
		{
			this.tokens = tokens;
		}
		
		public abstract boolean isMatch(final String text);
		
		public static SmartNameMatcher create(final String searchString)
		{
			final String matchToken = isEmpty(searchString) ? EMPTY_STRING : removeConsecutiveChars(processForFiltering(searchString))
						.chars()
						.flatMap(SmartNameMatcher::transformCharToMatcher)
						.mapToObj(charInt -> String.valueOf((char) charInt))
						.collect(Collectors.joining(EMPTY_STRING, ".*", ".*"));
			return new SearchToken_AND(matchToken);
		}

		private static IntStream transformCharToMatcher(final int simpleChar)
		{
			switch (simpleChar)
			{
			case 'k':
			case 'c':
				return "[kc]+".chars();
				
			case 'j':
			case 'i':
			case 'e':
				return "[jie]+".chars();
				
			case ' ':
				return IntStream.of( '.', '*', simpleChar, '.', '*');
				
			default:
				return IntStream.of(simpleChar, '+');
			}
		}
		
		/**
		 * Pattern explanation:<br>
		 *		"(.)\\1{1,}" 	means any character (added to group 1) followed by itself at least once<br>
		 *		"$1" 			references contents of group 1<br><br>
		 *
		 * Input: ddooooonnneeeeee<br>
		 * Output: done
		 */
		private static String removeConsecutiveChars(final String duplicateCharsString)
		{
			return duplicateCharsString.replaceAll("(.)\\1{1,}", "$1");
		}
	}

	private static class SearchToken_OR extends SmartNameMatcher
	{
		private SearchToken_OR(final String searchString)
		{
			super(searchString == null ? List.of(new SearchToken_Single(EMPTY_STRING)) : Arrays.stream(searchString.split(SPACE)).map(SearchToken_AND::new).collect(Collectors.toUnmodifiableList())); //$NON-NLS-1$
		}
		
		@Override
		public boolean isMatch(final String text)
		{
			return tokens.stream().anyMatch(token -> token.isMatch(text));
		}
	}

	private static class SearchToken_AND extends SmartNameMatcher
	{
		private SearchToken_AND(final String searchString)
		{
			super(searchString == null ? List.of() : Arrays.stream(searchString.split(SPACE)).map(SearchToken_Single::new).collect(Collectors.toUnmodifiableList())); //$NON-NLS-1$
		}
		
		@Override
		public boolean isMatch(final String text)
		{
			return tokens.stream().allMatch(token -> token.isMatch(text));
		}
	}

	private static class SearchToken_Single extends SmartNameMatcher
	{
		final private String searchString;
		
		private SearchToken_Single(final String searchPattern)
		{
			super(List.of());
			this.searchString = searchPattern;
		}
		
		@Override
		public boolean isMatch(final String text)
		{
			if (isEmpty(searchString))
				return true;
			
			return smartSearch(text, searchString);
		}
	}
}
