package ro.linic.util.commons;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ListUtils
{
	public static final int NONE_ARE_NULL = 10;
	
	public static <T> Stream<T> toStream(final Iterator<T> iterator)
	{
		return StreamSupport.stream(
		          Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),
		          false);
	}
	
	public static <T> Collector<T, ?, ArrayList<T>> toArrayList()
	{
        return Collector.of(ArrayList::new, List::add, (left, right) -> { left.addAll(right); return left; });
    }
	
	public static <T> Collector<T, ?, HashSet<T>> toHashSet()
	{
	    return Collector.of(HashSet::new, Set::add, (left, right) -> { left.addAll(right); return left; });
	}
	
	public static int compareNulls(final Object c1, final Object c2)
	{
		if (c1 != null && c2 == null)
			return -1;
		else if (c1 == null && c2 != null)
			return 1;
		else if(c1 == null && c2 == null)
			return 0;
		else
			return NONE_ARE_NULL;
	}
}
