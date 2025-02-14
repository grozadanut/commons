package ro.flexbiz.util.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ro.flexbiz.util.commons.PresentationUtils.EMPTY_STRING;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class ParameterStringBuilderTest {
	
	@Test
	public void getParamsStringTest() {
		assertEquals(ParameterStringBuilder.getParamsString(null), EMPTY_STRING);
		assertEquals(ParameterStringBuilder.getParamsString(Map.of()), EMPTY_STRING);
		assertEquals(ParameterStringBuilder.getParamsString(Map.of("key", "value")), "?key=value");
		
		assertTrue(List.of("?key1=value1&key2=value2", "?key2=value2&key1=value1")
				.contains(ParameterStringBuilder.getParamsString(Map.of("key1", "value1", "key2", "value2"))));
		assertTrue(List.of("?key1=value1&key2=value2&key3=value3", "?key1=value1&key3=value3&key2=value2", "?key3=value3&key2=value2&key1=value1", "?key3=value3&key1=value1&key2=value2", "?key2=value2&key1=value1&key3=value3", "?key2=value2&key3=value3&key1=value1")
				.contains(ParameterStringBuilder.getParamsString(Map.of("key1", "value1", "key2", "value2", "key3", "value3"))));
		
		assertEquals(ParameterStringBuilder.getParamsString(Map.of("key&", "value?:&")), "?key%26=value%3F%3A%26");
	}
}
