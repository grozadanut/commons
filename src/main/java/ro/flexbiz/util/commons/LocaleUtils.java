package ro.flexbiz.util.commons;

import static ro.flexbiz.util.commons.PresentationUtils.EMPTY_STRING;

import java.util.Locale;

public class LocaleUtils {
	/**
     * <p>Converts a String to a Locale.</p>
     *
     * <p>This method takes the string format of a locale and creates the
     * locale object from it.</p>
     *
     * <pre>
     *   LocaleUtils.toLocale("")           = new Locale("", "")
     *   LocaleUtils.toLocale("en")         = new Locale("en", "")
     *   LocaleUtils.toLocale("en_GB")      = new Locale("en", "GB")
     *   LocaleUtils.toLocale("en_001")     = new Locale("en", "001")
     *   LocaleUtils.toLocale("en_GB_xxx")  = new Locale("en", "GB", "xxx")   (#)
     * </pre>
     *
     * <p>(#) The behavior of the JDK variant constructor changed between JDK1.3 and JDK1.4.
     * In JDK1.3, the constructor upper cases the variant, in JDK1.4, it doesn't.
     * Thus, the result from getVariant() may vary depending on your JDK.</p>
     *
     * <p>This method validates the input strictly.
     * The language code must be lowercase.
     * The country code must be uppercase.
     * The separator must be an underscore.
     * The length must be correct.
     * </p>
     *
     * @param str  the locale String to convert, null returns null
     * @return a Locale, null if null input
     * @throws IllegalArgumentException if the string is an invalid format
     * @see Locale#forLanguageTag(String)
     */
    public static Locale toLocale(final String str) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty()) { // LANG-941 - JDK 8 introduced an empty locale where all fields are blank
            return new Locale(EMPTY_STRING, EMPTY_STRING);
        }
        if (str.contains("#")) { // LANG-879 - Cannot handle Java 7 script & extensions
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final int len = str.length();
        if (len < 2) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final char ch0 = str.charAt(0);
        if (ch0 == '_') {
            if (len < 3) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            final char ch1 = str.charAt(1);
            final char ch2 = str.charAt(2);
            if (!Character.isUpperCase(ch1) || !Character.isUpperCase(ch2)) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            if (len == 3) {
                return new Locale(EMPTY_STRING, str.substring(1, 3));
            }
            if (len < 5) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            if (str.charAt(3) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            return new Locale(EMPTY_STRING, str.substring(1, 3), str.substring(4));
        }

        return parseLocale(str);
    }
    
    /**
     * Tries to parse a locale from the given String.
     *
     * @param str the String to parse a locale from.
     * @return a Locale instance parsed from the given String.
     * @throws IllegalArgumentException if the given String can not be parsed.
     */
    private static Locale parseLocale(final String str) {
        if (isISO639LanguageCode(str)) {
            return new Locale(str);
        }

        final String[] segments = str.split("_", -1);
        final String language = segments[0];
        if (segments.length == 2) {
            final String country = segments[1];
            if (isISO639LanguageCode(language) && isISO3166CountryCode(country) ||
                    isNumericAreaCode(country)) {
                return new Locale(language, country);
            }
        } else if (segments.length == 3) {
            final String country = segments[1];
            final String variant = segments[2];
            if (isISO639LanguageCode(language) &&
                    (country.isEmpty() || isISO3166CountryCode(country) || isNumericAreaCode(country)) &&
                    !variant.isEmpty()) {
                return new Locale(language, country, variant);
            }
        }
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    
    /**
     * Checks whether the given String is a ISO 639 compliant language code.
     *
     * @param str the String to check.
     * @return true, if the given String is a ISO 639 compliant language code.
     */
    private static boolean isISO639LanguageCode(final String str) {
        return StringUtils.isAllLowerCase(str) && (str.length() == 2 || str.length() == 3);
    }
    
    /**
     * Checks whether the given String is a ISO 3166 alpha-2 country code.
     *
     * @param str the String to check
     * @return true, is the given String is a ISO 3166 compliant country code.
     */
    private static boolean isISO3166CountryCode(final String str) {
        return StringUtils.isAllUpperCase(str) && str.length() == 2;
    }
    
    /**
     * Checks whether the given String is a UN M.49 numeric area code.
     *
     * @param str the String to check
     * @return true, is the given String is a UN M.49 numeric area code.
     */
    private static boolean isNumericAreaCode(final String str) {
        return NumberUtils.isNumeric(str) && str.length() == 3;
    }
}
