package ro.flexbiz.util.commons;

import static ro.flexbiz.util.commons.ListUtils.NONE_ARE_NULL;
import static ro.flexbiz.util.commons.ListUtils.compareNulls;

import java.util.Comparator;

/**
 * Comparator that compares objects based on class type.
 * <ol>
 * <li>If classes are different, compares by class name. Hierarchy is NOT 
 * taken in consideration, hence BigDecimal(1) != Integer(1)</li>
 * <li>If classes are the same, they are cast to Comparable and use the internal
 * compareTo. If one of them is not Comparable -1 is returned.</li>
 * </ol>
 */
public class HeterogeneousDataComparator implements Comparator<Object> {
	public static final HeterogeneousDataComparator INSTANCE = new HeterogeneousDataComparator();

	private HeterogeneousDataComparator() {
	}

	@Override
	public int compare(final Object arg0, final Object arg1) {
		final int nullComparison = compareNulls(arg0, arg1);
		if (nullComparison != NONE_ARE_NULL)
			return nullComparison;

		if (!arg0.getClass().equals(arg1.getClass()))
			return arg0.getClass().getName().compareTo(arg1.getClass().getName());
		else if (arg0 instanceof Comparable && arg1 instanceof Comparable)
			return ((Comparable) arg0).compareTo(arg1);
		return -1;
	}
}
