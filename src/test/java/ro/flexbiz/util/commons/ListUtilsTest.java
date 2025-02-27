package ro.flexbiz.util.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.HashMap;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class ListUtilsTest {
	@Test
	public void toMapOfNullables_WhenHasNullKey() {
		final HashMap<String, String> expected = new HashMap<>();
		expected.put(null, "value");
		assertEquals(expected, Stream.of("ignored").collect(ListUtils.toMapOfNullables(i -> null, i -> "value")));
	}

	@Test
	public void toMapOfNullables_WhenHasNullValue() {
		final HashMap<String, String> expected = new HashMap<>();
		expected.put("key", null);
		assertEquals(expected, Stream.of("ignored").collect(ListUtils.toMapOfNullables(i -> "key", i -> null)));
	}

	@Test
	public void toMapOfNullables_WhenHasDuplicateNullKeys() {
		assertThrowsExactly(IllegalStateException.class,
				() -> Stream.of(1, 2, 3).collect(ListUtils.toMapOfNullables(i -> null, i -> i)), "Duplicate key null");
	}

	@Test
	public void toMapOfNullables_WhenHasDuplicateKeys_NoneHasNullValue() {
		assertThrows(IllegalStateException.class,
				() -> Stream.of(1, 2, 3).collect(ListUtils.toMapOfNullables(i -> "duplicated-key", i -> i)),
				"Duplicate key duplicated-key");
	}

	@Test
	public void toMapOfNullables_WhenHasDuplicateKeys_OneHasNullValue() {
		assertThrows(IllegalStateException.class,
				() -> Stream.of(1, null, 3).collect(ListUtils.toMapOfNullables(i -> "duplicated-key", i -> i)),
				"Duplicate key duplicated-key");
	}

	@Test
	public void toMapOfNullables_WhenHasDuplicateKeys_AllHasNullValue() {
		assertThrows(IllegalStateException.class,
				() -> Stream.of(null, null, null).collect(ListUtils.toMapOfNullables(i -> "duplicated-key", i -> i)),
				"Duplicate key duplicated-key");
	}
}
