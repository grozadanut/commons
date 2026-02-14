package ro.flexbiz.util.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ListUtils {
	public static final int NONE_ARE_NULL = 10;

	public static <T> Stream<T> toStream(final Iterator<T> iterator) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
	}

	public static <T> Collector<T, ?, ArrayList<T>> toArrayList() {
		return Collector.of(ArrayList::new, List::add, (left, right) -> {
			left.addAll(right);
			return left;
		});
	}

	public static <T> Collector<T, ?, HashSet<T>> toHashSet() {
		return Collector.of(HashSet::new, Set::add, (left, right) -> {
			left.addAll(right);
			return left;
		});
	}

	public static int compareNulls(final Object c1, final Object c2) {
		if (c1 != null && c2 == null)
			return -1;
		else if (c1 == null && c2 != null)
			return 1;
		else if (c1 == null && c2 == null)
			return 0;
		else
			return NONE_ARE_NULL;
	}
	
	public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }
	
	public static boolean notEmpty(Collection collection) {
        return !isEmpty(collection);
    }

	/**
	 * This version:
	 * 
	 * <ul>
	 * <li>Allows null keys</li>
	 * <li>Allows null values</li>
	 * <li>Detects duplicate keys (even if they are null) and throws
	 * IllegalStateException as in the original JDK implementation</li>
	 * <li>Detects duplicate keys also when the key already mapped to the null
	 * value. In other words, separates a mapping with null-value from
	 * no-mapping</li>
	 * </ul>
	 * 
	 * @param <T>
	 * @param <K>
	 * @param <U>
	 * @param keyMapper
	 * @param valueMapper
	 * @return
	 */
	public static <T, K, U> Collector<T, ?, Map<K, U>> toMapOfNullables(Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends U> valueMapper) {
		return Collectors.collectingAndThen(Collectors.toList(), list -> {
			final Map<K, U> map = new HashMap<>();
			list.forEach(item -> {
				final K key = keyMapper.apply(item);
				final U value = valueMapper.apply(item);
				if (map.containsKey(key)) {
					throw new IllegalStateException(String
							.format("Duplicate key %s (attempted merging values %s and %s)", key, map.get(key), value));
				}
				map.put(key, value);
			});
			return map;
		});
	}

	public static <T, K, U> Collector<T, ?, Map<K, U>> toMapOfNullables(Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends U> valueMapper, BinaryOperator<U> mergeFunction) {
		return Collectors.collectingAndThen(Collectors.toList(), list -> {
			final Map<K, U> map = new HashMap<>();
			for (final T item : list) {
				final K key = keyMapper.apply(item);
				final U newValue = valueMapper.apply(item);
				final U value = map.containsKey(key) ? mergeFunction.apply(map.get(key), newValue) : newValue;
				map.put(key, value);
			}
			return map;
		});
	}
	
	public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
		final Set<T> intersectSet = new HashSet<>(a);
		intersectSet.retainAll(b);
		return intersectSet;
	}
}
