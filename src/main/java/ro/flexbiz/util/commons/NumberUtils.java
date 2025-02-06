package ro.flexbiz.util.commons;

import static ro.flexbiz.util.commons.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class NumberUtils
{
	public static boolean greaterThan(final BigDecimal base, final BigDecimal comparedWith)
	{
		if (base == null)
			return false;
		
		if (comparedWith == null)
			return true;
		
		return base.compareTo(comparedWith) > 0;
	}
	
	public static boolean greaterThanOrEqual(final BigDecimal base, final BigDecimal comparedWith)
	{
		if (base == null)
			return false;
		
		if (comparedWith == null)
			return true;
		
		return base.compareTo(comparedWith) >= 0;
	}
	
	public static boolean smallerThan(final BigDecimal base, final BigDecimal comparedWith)
	{
		if (base == null)
			return true;
		
		if (comparedWith == null)
			return false;
		
		return base.compareTo(comparedWith) < 0;
	}
	
	public static boolean smallerThanOrEqual(final BigDecimal base, final BigDecimal comparedWith)
	{
		if (base == null)
			return true;
		
		if (comparedWith == null)
			return false;
		
		return base.compareTo(comparedWith) <= 0;
	}
	
	public static BigDecimal closerToZero(final BigDecimal num1, final BigDecimal num2)
	{
		if (num1 == null && num2 == null)
			return BigDecimal.ZERO;
		
		if (num1 == null)
			return num2;
		
		if (num2 == null)
			return num1;
		
		if (num1.compareTo(BigDecimal.ZERO) < 0 || num2.compareTo(BigDecimal.ZERO) < 0)
			return num1.max(num2);
		else
			return num1.min(num2);
	}
	
	public static BigDecimal furtherFromZero(final BigDecimal num1, final BigDecimal num2)
	{
		if (num1 == null && num2 == null)
			return BigDecimal.ZERO;
		
		if (num1 == null)
			return num2;
		
		if (num2 == null)
			return num1;
		
		if (num1.compareTo(BigDecimal.ZERO) < 0 || num2.compareTo(BigDecimal.ZERO) < 0)
			return num1.min(num2);
		else
			return num1.max(num2);
	}
	
	/**
	   * Determines whether two possibly-null BigDecimal are equal. Returns:
	   *
	   * <ul>
	   *   <li>{@code true} if {@code a} and {@code b} are both null.
	   *   <li>{@code true} if {@code a} and {@code b} are both non-null and they are equal according to
	   *       {@link BigDecimal#compareTo(BigDecimal)}.
	   *   <li>{@code false} in all other situations.
	   * </ul>
	   */
	public static boolean equal(final BigDecimal a, final BigDecimal b)
	{
		return a == b || (a != null && b != null && a.compareTo(b) == 0);
	}
	
	public static boolean nullOrZero(final BigDecimal num)
	{
		return num == null || num.compareTo(BigDecimal.ZERO) == 0;
	}
	
	public static boolean isNumeric(final String strNum)
	{
	    if (isEmpty(strNum))
	        return false;
	    
	    try
	    {
	        Double.parseDouble(strNum);
	    }
	    catch (final NumberFormatException nfe)
	    {
	        return false;
	    }
	    return true;
	}
	
	public static BigDecimal parse(final String strNum)
	{
	    if (isEmpty(strNum))
	        return BigDecimal.ZERO;
	    
	    try
	    {
	        return new BigDecimal(strNum.trim());
	    }
	    catch (final Exception ex)
	    {
	        return BigDecimal.ZERO;
	    }
	}
	
	public static int parseToInt(final String strNum)
	{
	    if (isEmpty(strNum))
	        return 0;
	    
	    try
	    {
	        return Integer.parseInt(strNum.trim());
	    }
	    catch (final Exception ex)
	    {
	        return 0;
	    }
	}
	
	public static long parseToLong(final String strNum)
	{
	    if (isEmpty(strNum))
	        return 0;
	    
	    try
	    {
	        return Long.parseLong(strNum.trim());
	    }
	    catch (final Exception ex)
	    {
	        return 0;
	    }
	}
	
	public static BigDecimal extractPercentage(final String readablePercentage)
	{
		final BigDecimal readable = parse(readablePercentage);
		if (readable.compareTo(BigDecimal.ZERO) != 0)
			return readable.divide(new BigDecimal("100"), 4, RoundingMode.HALF_EVEN);
		return BigDecimal.ZERO;
	}
	
	public static BigDecimal extractPercentage_EmptyAsNull(final String readablePercentage)
	{
		if (isEmpty(readablePercentage))
			return null;
		
		return extractPercentage(readablePercentage);
	}
	
	/**
	 * Converts the passed parameter to BigDecimal.ZERO if it's null.
	 * 
	 * @param number @nullable
	 * @return number or BigDecimal.ZERO if number is null
	 */
	public static BigDecimal nullsafe(final BigDecimal number)
	{
		return number == null ? BigDecimal.ZERO : number;
	}

	/**
	 * Adds the numbers together. Has the advantage that it supports
	 * null values.
	 * 
	 * @param num
	 * @return num1+num2+...+numX; if one number is null, it is considered 0; if no arguments present, it returns BigDecimal.ZERO
	 */
	public static BigDecimal add(final BigDecimal... num)
	{
		if (num == null || num.length == 0)
			return BigDecimal.ZERO;
		
		return Stream.of(num).filter(Objects::nonNull).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
	}
	
	/**
	 * Adds the objects together. Has the advantage that it supports null values.
	 * 
	 * @param mapper used to map the objects to BigDecimal; if null, returns BigDecimal.ZERO
	 * @param num
	 * @param funct 
	 * @return num1+num2+...+numX; if one number is null, it is considered 0; if no arguments present, it returns BigDecimal.ZERO
	 */
	public static <T> BigDecimal add(final Function<T, BigDecimal> mapper, final T... num)
	{
		if (num == null || num.length == 0 || mapper == null)
			return BigDecimal.ZERO;
		
		return Stream.of(num).filter(Objects::nonNull).map(mapper).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
	}
	
	/**
	 * Multiplies the two numbers. Has the advantage that it supports
	 * null values.
	 * 
	 * @param num1
	 * @param num2
	 * @return num1*num2; if one number is null, it returns BigDecimal.ZERO
	 */
	public static BigDecimal multiply(final BigDecimal num1, final BigDecimal num2)
	{
		if (num1 == null)
			return BigDecimal.ZERO;
		
		if (num2 == null)
			return BigDecimal.ZERO;
		
		return num1.multiply(num2);
	}
	
	/**
	 * Divides number 2 from number 1(num1/num2). Has the advantage that it supports
	 * null values.
	 * 
	 * @param num1
	 * @param num2
	 * @return num1/num2; if one number is null or zero, it returns BigDecimal.ZERO
	 */
	public static BigDecimal divide(final BigDecimal num1, final BigDecimal num2, final int scale, final RoundingMode roundingMode)
	{
		if (num1 == null)
			return BigDecimal.ZERO;

		if (num2 == null || equal(num2, BigDecimal.ZERO))
			return BigDecimal.ZERO;

		return num1.divide(num2, scale, roundingMode);
	}
	
	/**
	 * Subtracts the two numbers. Has the advantage that it supports
	 * null values.
	 * 
	 * @param num1
	 * @param num2
	 * @return num1-num2; either number as null is considered ZERO
	 */
	public static BigDecimal subtract(final BigDecimal num1, final BigDecimal num2)
	{
		if (num1 == null)
			return BigDecimal.ZERO.subtract(num2);
		
		if (num2 == null)
			return num1;
		
		return num1.subtract(num2);
	}
	
	/**
	 * Average between num1 and num2.
	 * if num1 is null, returns num2
	 * 
	 * @param num1 @Nullable 
	 * @param num2 @NotNull
	 * @return
	 */
	public static BigDecimal average(final BigDecimal num1, final BigDecimal num2)
	{
		if (num1 == null)
			return num2;
		
		return num1.add(num2).divide(new BigDecimal("2"), 6, RoundingMode.HALF_EVEN);
	}
	
	/**
	 * Rounds the <code>input</code> to at most <code>scale</code> decimal places
	 */
	public static BigDecimal truncate(final BigDecimal input, final int scale)
	{
		if (input == null)
			return null;
		
	    if(input.scale() > scale)
	        return input.setScale(scale, RoundingMode.HALF_EVEN);
	    else
	        return input;
	}
	
	/**
	 * If price*quantity is equal to a number which has the scale > 2,
	 * then it adjusts the price to the closest 2 decimal points value
	 * so that newPrice*quantity total can be rounded without ambiguity.
	 * 
	 * @return the adjusted price
	 */
	public static BigDecimal adjustPrice(final BigDecimal price, final BigDecimal quantity)
	{
		if (price == null || quantity == null)
			return null;
		
		final BigDecimal total = price.multiply(quantity);
		if (total.scale() > 2 && quantity.compareTo(BigDecimal.ZERO) != 0)
		{
			final BigDecimal newPrice = total.setScale(2, RoundingMode.HALF_EVEN).divide(quantity, 2, RoundingMode.HALF_EVEN);
			final BigDecimal newTotal = newPrice.multiply(quantity);
			if (newTotal.setScale(4, RoundingMode.HALF_EVEN).toString().endsWith("50"))
				return newPrice.subtract(new BigDecimal("0.01"));
			return newPrice;
		}
		return price;
	}
	
	/**
	 * Adaos Percentage = ((PVcuTVA � PUAcuTVA) / PUAcuTVA)
	 */
	public static BigDecimal calculateAdaosPercent(final BigDecimal lastBuyingPriceWithTva, final BigDecimal pvCuTVA)
	{
		if (pvCuTVA == null || equal(pvCuTVA, BigDecimal.ZERO))
			return BigDecimal.ZERO;
		
		if (lastBuyingPriceWithTva == null || equal(lastBuyingPriceWithTva, BigDecimal.ZERO))
			return BigDecimal.ONE;
		
		return pvCuTVA.subtract(lastBuyingPriceWithTva)
			.divide(lastBuyingPriceWithTva, 4, RoundingMode.HALF_EVEN);
	}
}
