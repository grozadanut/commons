package ro.flexbiz.util.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ro.flexbiz.util.commons.PresentationUtils.EMPTY_STRING;

import org.junit.jupiter.api.Test;

public class StringUtilsTest {
	@Test
	public void sanitizePhoneNumber_Test() {
		// Normal cases
		assertEquals("+40755555555", StringUtils.sanitizePhoneNumber("0755555555"));
		assertEquals("+40755555555", StringUtils.sanitizePhoneNumber("+40755555555"));
		assertEquals("+40755555555", StringUtils.sanitizePhoneNumber("0040755555555"));

		// Formatting and special characters
		assertEquals("+40755555555", StringUtils.sanitizePhoneNumber("0755 555 555"));
		assertEquals("+40755555555", StringUtils.sanitizePhoneNumber("(0755) 555-555"));
		assertEquals("+40755555555", StringUtils.sanitizePhoneNumber("0755.555.555"));
		assertEquals("+40755555555", StringUtils.sanitizePhoneNumber("  0755555555  "));
		assertEquals("+40755555555", StringUtils.sanitizePhoneNumber("+40 (755) 555-555"));
		assertEquals("+40755555555", StringUtils.sanitizePhoneNumber("-(0040) 755 555 555"));
		assertEquals("+17022345678", StringUtils.sanitizePhoneNumber("+1 702 234-5678"));

		// Missing prefix - defaults to +40
		assertEquals("+40755555555", StringUtils.sanitizePhoneNumber("755555555"));
		assertEquals("+40755555555", StringUtils.sanitizePhoneNumber("755 555 555"));

		// Other country prefixes
		assertEquals("+340752150485", StringUtils.sanitizePhoneNumber("+34 0752 150 485"));
		assertEquals("+340752150485", StringUtils.sanitizePhoneNumber("0034 0752 150 485"));

		// Edge cases and empty strings
		assertEquals("", StringUtils.sanitizePhoneNumber(null));
		assertEquals("", StringUtils.sanitizePhoneNumber(""));
		assertEquals("   ", StringUtils.sanitizePhoneNumber("   "));

		// Values without digits (returns original input)
		assertEquals("abc", StringUtils.sanitizePhoneNumber("abc"));
		assertEquals("+", StringUtils.sanitizePhoneNumber("+"));
		assertEquals(" + ", StringUtils.sanitizePhoneNumber(" + "));
		assertEquals("no-digits here", StringUtils.sanitizePhoneNumber("no-digits here"));
	}

	@Test
	public void truncate_Test() {
		assertEquals(StringUtils.truncate("hey there", 100), "hey there");
		assertEquals(StringUtils.truncate("hey there", -5), "");
		assertEquals(StringUtils.truncate("hey there", 0), "");
		assertEquals(StringUtils.truncate("hey there", 1), "h");
		assertEquals(StringUtils.truncate("hey there", 2), "he");
		assertEquals(StringUtils.truncate("hey there", 3), "hey");
		assertEquals(StringUtils.truncate("hey there", 4), "hey ");
		assertEquals(StringUtils.truncate("hey there", 5), "hey t");
		assertEquals(StringUtils.truncate("hey there", 6), "hey th");
		assertEquals(StringUtils.truncate("hey there", 7), "hey ...");
		assertEquals(StringUtils.truncate("hey there mister john", 9), "hey th...");
		assertEquals(StringUtils.truncate("hey there mister john", 12), "hey there...");
		assertEquals(StringUtils.truncate(null, 0), EMPTY_STRING);
		assertEquals(StringUtils.truncate(null, 0), EMPTY_STRING);
		assertEquals(StringUtils.truncate(EMPTY_STRING, 0), EMPTY_STRING);
		assertEquals(StringUtils.truncate(null, 0), EMPTY_STRING);
		assertEquals(StringUtils.truncate(EMPTY_STRING, 10), EMPTY_STRING);
		assertEquals(StringUtils.truncate(null, 10), EMPTY_STRING);
		assertEquals(StringUtils.truncate(EMPTY_STRING, -10), EMPTY_STRING);
		assertEquals(StringUtils.truncate(null, -10), EMPTY_STRING);
	}
}
