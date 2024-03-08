package ro.linic.util.commons;

import static ro.linic.util.commons.ListUtils.NONE_ARE_NULL;
import static ro.linic.util.commons.ListUtils.compareNulls;

import java.util.Comparator;

public class HeterogeneousDataComparator implements Comparator<Object>
{
	public static final HeterogeneousDataComparator INSTANCE = new HeterogeneousDataComparator();

	private HeterogeneousDataComparator()
	{
	}
	
	@Override
	public int compare(final Object arg0, final Object arg1)
	{
		final int nullComparison = compareNulls(arg0, arg1);
		if (nullComparison != NONE_ARE_NULL)
			return nullComparison;
		
		if (arg0.getClass() != arg1.getClass())
			return 0;
		else if (arg0 instanceof Comparable && arg1 instanceof Comparable)
			return ((Comparable) arg0).compareTo(arg1);
		return 0;
	}
}
