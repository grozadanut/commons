package ro.flexbiz.util.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ro.flexbiz.util.commons.PresentationUtils.EMPTY_STRING;

import org.junit.jupiter.api.Test;

public class StringUtilsTest
{
	@Test
	public void truncate_Test()
	{
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
