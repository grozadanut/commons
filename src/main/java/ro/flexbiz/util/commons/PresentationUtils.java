package ro.flexbiz.util.commons;

import static ro.flexbiz.util.commons.NumberUtils.equal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public interface PresentationUtils
{
	public static final String EMPTY_STRING = "";
	public static final String NEWLINE = System.lineSeparator();
	public static final String BR_SEPARATOR = "<br>";
	public static final String LIST_SEPARATOR = ", ";
	public static final String SPACE = " ";
	public static final String EXPR_SEPARATOR = "&";
	public static final String ELLIPSES = "...";
	public static final char NBSP = '\u00a0';
	public static final String NBSP_S = "\u00a0";
	
	public static String safeString(final String string)
	{
		return safeString(string, EMPTY_STRING);
	}
	
	public static String safeString(final String string, final String defaultValue)
	{
		return Optional.ofNullable(string).orElse(defaultValue == null ? EMPTY_STRING : defaultValue);
	}
	
	public static <T> String safeString(final T model, final Function<T, String> stringMapper)
	{
		return safeString(model, stringMapper, EMPTY_STRING);
	}
	
	public static <T> String safeString(final T model, final Function<T, String> stringMapper, final String defaultValue)
	{
		return Optional.ofNullable(model).map(stringMapper).orElse(defaultValue);
	}
	
	public static <T, P> String safeString(final T model, final Function<T, P> mapper1, final Function<P, String> stringMapper)
	{
		return safeString(model, mapper1, stringMapper, EMPTY_STRING);
	}
	
	public static <T, P> String safeString(final T model, final Function<T, P> mapper1, final Function<P, String> stringMapper, final String defaultValue)
	{
		return Optional.ofNullable(model).map(mapper1).map(stringMapper).orElse(defaultValue);
	}
	
	public static <T, P, M> String safeString(final T model, final Function<T, P> mapper1, final Function<P, M> mapper2, final Function<M, String> stringMapper)
	{
		return safeString(model, mapper1, mapper2, stringMapper, EMPTY_STRING);
	}
	
	public static <T, P, M> String safeString(final T model, final Function<T, P> mapper1, final Function<P, M> mapper2, final Function<M, String> stringMapper, final String defaultValue)
	{
		return Optional.ofNullable(model).map(mapper1).map(mapper2).map(stringMapper).orElse(defaultValue);
	}
	
	public static <T, P, M, N> String safeString(final T model, final Function<T, P> mapper1, final Function<P, M> mapper2, final Function<M, N> mapper3, final Function<N, String> stringMapper)
	{
		return safeString(model, mapper1, mapper2, mapper3, stringMapper, EMPTY_STRING);
	}
	
	public static <T, P, M, N> String safeString(final T model, final Function<T, P> mapper1, final Function<P, M> mapper2, final Function<M, N> mapper3, final Function<N, String> stringMapper, final String defaultValue)
	{
		return Optional.ofNullable(model).map(mapper1).map(mapper2).map(mapper3).map(stringMapper).orElse(defaultValue);
	}
	
	public static String displayPercentage(final BigDecimal percent)
	{
		if (Objects.isNull(percent))
			return EMPTY_STRING;
		
		final DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
		final DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(' ');
		formatter.setDecimalFormatSymbols(symbols);
		
		return formatter.format(percent.multiply(new BigDecimal("100")))+"%";
	}
	
	public static String displayPercentageRaw(final BigDecimal percent)
	{
		if (Objects.isNull(percent))
			return EMPTY_STRING;
		
		final DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
		final DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(' ');
		formatter.setDecimalFormatSymbols(symbols);
		
		return formatter.format(percent.multiply(new BigDecimal("100")));
	}
	
	public static String displayBigDecimal(final BigDecimal decimal)
	{
		if (Objects.isNull(decimal))
			return EMPTY_STRING;
		
		final DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
		final DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(' ');
		formatter.setDecimalFormatSymbols(symbols);
		
		return formatter.format(decimal);
	}
	
	public static <T> String displayBigDecimal(final T model, final Function<T, BigDecimal> stringMapper)
	{
		return Optional.ofNullable(model)
				.map(stringMapper)
				.map(PresentationUtils::displayBigDecimal)
				.orElse(EMPTY_STRING);
	}
	
	public static String displayBigDecimal(final BigDecimal decimal, final int scale, final RoundingMode roundingMode)
	{
		if (Objects.isNull(decimal))
			return EMPTY_STRING;
		
		final DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
		final DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(' ');
		formatter.setDecimalFormatSymbols(symbols);
		formatter.setMinimumFractionDigits(scale);
		
		return formatter.format(decimal.setScale(scale, roundingMode));
	}
	
	public static String toRomanian(final Boolean value)
	{
		if (value == null)
			return EMPTY_STRING;
		
		return value ? "DA" : "NU";
	}
	
	/**
	 * Converts a decimal number to romanian words. Useful on receipts.
	 * Scales the number to 2 decimal places
	 * <br><br>
	 * Example: 123.34 = unasutadouazecisitreileisitreizecisipatrubani
	 */
	public static String toWords(final BigDecimal number)
	{
		if (number == null)
			return EMPTY_STRING;

		final String textNumber = number.setScale(2, RoundingMode.HALF_EVEN)
				.toPlainString().replace(".00", EMPTY_STRING);
		
		if (textNumber.contains("."))
		{
			final int radixLoc = textNumber.indexOf('.');
			final long lei = Long.parseLong(textNumber.substring(0, radixLoc));
			final long bani = Long.parseLong(textNumber.substring(radixLoc + 1, textNumber.length()));
			
			return MessageFormat.format("{0}leisi{1}bani", RomanianNumberToWords.convert(lei), RomanianNumberToWords.convert(bani));
		}
		else
			return MessageFormat.format("{0}lei", RomanianNumberToWords.convert(Long.parseLong(textNumber)));
	}
	
	public static String showInRed(final String text)
	{
		return "<span style=\"color:red;\">"+text+"</span>";
	}
	
	public static String pluralize(final BigDecimal quantity, final String singular, final String plural)
	{
		if (quantity == null)
			return plural;
		if (equal(quantity.abs(), BigDecimal.ONE))
			return singular;
		return plural;
	}
	
	public static String pluralize(final double quantity, final String singular, final String plural)
	{
		if (quantity == 0d)
			return plural;
		if (quantity == 1d || quantity == -1d)
			return singular;
		return plural;
	}
	
	public static String pluralizeTimeUom(final BigDecimal quantity, final String uom)
	{
		if (uom == null)
			return EMPTY_STRING;
		
		if ("MIN".equalsIgnoreCase(uom) ||
				"MINUT".equalsIgnoreCase(uom) ||
				"MINUTE".equalsIgnoreCase(uom))
			return pluralize(quantity, "minut", "minute");
		
		if ("H".equalsIgnoreCase(uom) ||
				"ORA".equalsIgnoreCase(uom) ||
				"HOUR".equalsIgnoreCase(uom))
			return pluralize(quantity, "ora", "ore");
		return uom;
	}
	
	public static String pluralizeTimeUom(final double quantity, final String uom)
	{
		return pluralizeTimeUom(BigDecimal.valueOf(quantity), uom);
	}
}
