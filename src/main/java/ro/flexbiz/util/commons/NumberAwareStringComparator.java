package ro.flexbiz.util.commons;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberAwareStringComparator implements Comparator<String>
{
	public static final NumberAwareStringComparator INSTANCE = new NumberAwareStringComparator();

	private static final Pattern splitter = Pattern.compile("([-]?[\\d]+(?:\\.[\\d]+)?|[\\D]+)");
	private static final Pattern matchNumber = Pattern.compile("[-]?\\d+(?:\\.[\\d]+)?");
	
	public static <T> Comparator<T> comparing(final Function<T, ? extends String> keyExtractor)
	{
		Objects.requireNonNull(keyExtractor);
		final NumberAwareStringComparator comp = INSTANCE;
        return (Comparator<T> & Serializable)
        		(c1, c2) -> comp.compare(keyExtractor.apply(c1), keyExtractor.apply(c2));
	}

	private NumberAwareStringComparator()
	{
	}

	@Override
	public int compare(final String s1, final String s2)
	{
		if (s1.equals(s2))
			return 0;

		final Matcher m1 = splitter.matcher(s1);
		final Matcher m2 = splitter.matcher(s2);

		while (m1.find() && m2.find())
		{
			if (matchNumber.matcher(m1.group()).matches() && matchNumber.matcher(m2.group()).matches())
			{
				final int numberCompareResult = Double.compare(Double.parseDouble(m1.group()),
						Double.parseDouble(m2.group()));
				if (numberCompareResult != 0)
					return numberCompareResult;
			} else
			{
				if (m1.group().compareTo(m2.group()) != 0)
					return m1.group().compareTo(m2.group());
			}
		}
		return m1.matches() ? 1 : (m2.matches() ? -1 : 0);
	}
}
