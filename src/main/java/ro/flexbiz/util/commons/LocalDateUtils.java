package ro.flexbiz.util.commons;

import static ro.flexbiz.util.commons.PresentationUtils.EMPTY_STRING;
import static ro.flexbiz.util.commons.StringUtils.isEmpty;

import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalDateUtils
{
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
	
	public static final Locale RO_LOCALE = new Locale("ro", "RO");
	
	public static final LocalDateTime POSTGRES_MIN = LocalDateTime.of(1900, 1, 1, 0, 0, 0, 0);
	public static final LocalDateTime POSTGRES_MAX = LocalDateTime.of(5002, 1, 1, 0, 0, 0, 0);
	
	public static final LocalTime POSTGRES_MAX_TIME = LocalTime.of(23, 59, 59);
	
	/**
	 * @param date
	 * @return true if date is in future or is null
	 */
	public static boolean inFutureOrNull(final LocalDate date)
	{
		return Objects.isNull(date) || date.isAfter(LocalDate.now());
	}

	/**
	 * Verifies whether the date is after start and before end.
	 * 
	 * @param date the date to compare with the period
	 * @param start date should be after this date inclusive, or start should be null
	 * @param end date should be before this date inclusive, or end should be null
	 */
	public static boolean between(final LocalDate date, final LocalDate start, final LocalDate end)
	{
		return isAfter(date, start) && isBefore(date, end);
	}
	
	/**
	 * Verifies whether the date is after start and before end.
	 * 
	 * @param date the date to compare with the period
	 * @param start date should be after this date inclusive, or start should be null
	 * @param end date should be before this date inclusive, or end should be null
	 */
	public static boolean between(final LocalDateTime date, final LocalDateTime start, final LocalDateTime end)
	{
		return isAfter(date, start) && isBefore(date, end);
	}
	
	/**
	 * Verifies whether the time is after start and before end.
	 * 
	 * @param time the time to compare with the period
	 * @param start should be after this time inclusive, or start should be null
	 * @param end should be before this time inclusive, or end should be null
	 */
	public static boolean between(final LocalTime time, final LocalTime start, final LocalTime end)
	{
		return isAfter(time, start) && isBefore(time, end);
	}
	
	public static boolean overlap(final LocalDateTime startA, final LocalDateTime endA, final LocalDateTime startB, final LocalDateTime endB)
	{
		return isBeforeExclusive(startA, endB) && isAfterExclusive(endA, startB);
	}
	
	/**
	 * Verifies whether the date is after the after date inclusive.
	 * @return true if date is after after date or after date is null
	 */
	public static boolean isAfter(final LocalDate date, final LocalDate after)
	{
		return Objects.isNull(after) || (date != null && date.compareTo(after) >= 0);
	}
	
	/**
	 * Verifies whether the date is after the after date exclusive.
	 * @return true if date is after after date or after date is null
	 */
	public static boolean isAfterExclusive(final LocalDate date, final LocalDate after)
	{
		return Objects.isNull(after) || (date != null && date.compareTo(after) > 0);
	}
	
	/**
	 * Verifies whether the dateTime is after the after dateTime inclusive.
	 * @return true if date is after after date or after date is null
	 */
	public static boolean isAfter(final LocalDateTime date, final LocalDateTime after)
	{
		return Objects.isNull(after) || (date != null && date.compareTo(after) >= 0);
	}
	
	/**
	 * Verifies whether the dateTime is after the after dateTime exclusive.
	 * @return true if date is after after date or after date is null
	 */
	public static boolean isAfterExclusive(final LocalDateTime date, final LocalDateTime after)
	{
		return Objects.isNull(after) || (date != null && date.compareTo(after) > 0);
	}
	
	/**
	 * Verifies whether the time is after the after time inclusive.
	 * @return true if time is after after time or after time is null
	 */
	public static boolean isAfter(final LocalTime time, final LocalTime after)
	{
		return Objects.isNull(after) || (time != null && time.compareTo(after) >= 0);
	}
	
	/**
	 * Verifies whether the time is after the after time exclusive.
	 * @return true if time is after after time or after time is null
	 */
	public static boolean isAfterExclusive(final LocalTime time, final LocalTime after)
	{
		return Objects.isNull(after) || (time != null && time.compareTo(after) > 0);
	}
	
	/**
	 * Verifies whether the date is before the before date inclusive.
	 * @return true if date is before the before date or before date is null
	 */
	public static boolean isBefore(final LocalDate date, final LocalDate before)
	{
		return Objects.isNull(before) || (date != null && date.compareTo(before) <= 0);
	}
	
	/**
	 * Verifies whether the date is before the before date exclusive.
	 * @return true if date is before the before date or before date is null
	 */
	public static boolean isBeforeExclusive(final LocalDate date, final LocalDate before)
	{
		return Objects.isNull(before) || (date != null && date.compareTo(before) < 0);
	}
	
	/**
	 * Verifies whether the date is before the before date inclusive.
	 * @return true if date is before the before date or before date is null
	 */
	public static boolean isBefore(final LocalDateTime date, final LocalDateTime before)
	{
		return Objects.isNull(before) || (date != null && date.compareTo(before) <= 0);
	}
	
	/**
	 * Verifies whether the date is before the before date exclusive.
	 * @return true if date is before the before date or before date is null
	 */
	public static boolean isBeforeExclusive(final LocalDateTime date, final LocalDateTime before)
	{
		return Objects.isNull(before) || (date != null && date.compareTo(before) < 0);
	}
	
	/**
	 * Verifies whether the time is before the before time inclusive.
	 * @return true if time is before the before time or before time is null
	 */
	public static boolean isBefore(final LocalTime time, final LocalTime before)
	{
		return Objects.isNull(before) || (time != null && time.compareTo(before) <= 0);
	}
	
	/**
	 * Verifies whether the time is before the before time exclusive.
	 * @return true if time is before the before time or before time is null
	 */
	public static boolean isBeforeExclusive(final LocalTime time, final LocalTime before)
	{
		return Objects.isNull(before) || (time != null && time.compareTo(before) < 0);
	}
	
	/**
	   * Determines whether two possibly-null LocalDate are equal. Returns:
	   *
	   * <ul>
	   *   <li>{@code true} if {@code a} and {@code b} are both null.
	   *   <li>{@code true} if {@code a} and {@code b} are both non-null and they are equal according to
	   *       {@link LocalDate#compareTo(LocalDate)}.
	   *   <li>{@code false} in all other situations.
	   * </ul>
	   */
	public static boolean equal(final LocalDate a, final LocalDate b)
	{
		return a == b || (a != null && b != null && a.compareTo(b) == 0);
	}
	
	public static int compare(final LocalDate d1, final LocalDate d2)
	{
		if (d1 == null)
			return d2 == null ? 0 : 1;
		
		if (d2 == null)
			return -1;
		
		return d1.compareTo(d2);
	}
	
	public static int compareNullsFirst(final LocalDate d1, final LocalDate d2)
	{
		if (d1 == null)
			return d2 == null ? 0 : -1;
		
		if (d2 == null)
			return 1;
		
		return d1.compareTo(d2);
	}
	
	public static int compare(final LocalDateTime d1, final LocalDateTime d2)
	{
		if (d1 == null)
			return d2 == null ? 0 : 1;
		
		if (d2 == null)
			return -1;
		
		return d1.compareTo(d2);
	}
	
	public static int compareNullsFirst(final LocalDateTime d1, final LocalDateTime d2)
	{
		if (d1 == null)
			return d2 == null ? 0 : -1;
		
		if (d2 == null)
			return 1;
		
		return d1.compareTo(d2);
	}
	
	public static String displayLocalDateTimeReadable(final LocalDateTime time)
	{
		if (time == null)
			return EMPTY_STRING;
		
		if (time.toLocalDate().equals(LocalDate.now().minusDays(1)))
			return MessageFormat.format("Ieri {0}", time.format(TIME_FORMATTER));
		else if (time.toLocalDate().equals(LocalDate.now()))
			return MessageFormat.format("Azi {0}", time.format(TIME_FORMATTER));
		else if (time.toLocalDate().equals(LocalDate.now().plusDays(1)))
			return MessageFormat.format("Maine {0}", time.format(TIME_FORMATTER));
		
		if (time.getYear() == LocalDate.now().getYear())
		{
			if (time.getMonthValue() == LocalDate.now().getMonthValue())
				return time.format(DateTimeFormatter.ofPattern("dd E HH:mm"));
			return time.format(DateTimeFormatter.ofPattern("MMM-dd E HH:mm"));
		}
		
		return time.format(DateTimeFormatter.ofPattern("uuuu-MMM-dd E HH:mm"));
	}
	
	public static String displayLocalDateTime(final LocalDateTime time)
	{
		if (Objects.isNull(time))
			return EMPTY_STRING;
		
		return time.format(DATE_TIME_FORMATTER);
	}
	
	public static String displayLocalDateTime(final LocalDateTime time, final String pattern)
	{
		if (Objects.isNull(time))
			return EMPTY_STRING;
		
		return time.format(DateTimeFormatter.ofPattern(pattern));
	}
	
	public static String displayLocalDateTime(final LocalDateTime time, final DateTimeFormatter formatter)
	{
		if (Objects.isNull(time))
			return EMPTY_STRING;
		
		return time.format(formatter);
	}
	
	public static String displayLocalDate(final LocalDate localDate)
	{
		if (Objects.isNull(localDate))
			return EMPTY_STRING;
		
		return localDate.format(DATE_FORMATTER);
	}
	
	public static String displayLocalDate(final LocalDate localDate, final String pattern)
	{
		if (Objects.isNull(localDate))
			return EMPTY_STRING;
		
		return localDate.format(DateTimeFormatter.ofPattern(pattern));
	}
	
	public static String displayLocalDate(final LocalDate localDate, final DateTimeFormatter formatter)
	{
		if (Objects.isNull(localDate))
			return EMPTY_STRING;
		
		return localDate.format(formatter);
	}
	
	public static String displayLocalTime(final LocalTime time)
	{
		if (Objects.isNull(time))
			return EMPTY_STRING;
		
		return time.format(TIME_FORMATTER);
	}
	
	public static LocalDate fromDate(final Date date)
	{
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public static LocalDateTime toLocalDateTime(final Date date)
	{
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
	
	public static Date asDate(final LocalDateTime localDateTime, final String zoneId)
	{
        return Date.from(localDateTime.atZone(ZoneId.of(zoneId)).toInstant());
    }
	
	public static boolean isInDst(final LocalDateTime localDateTime, final String zoneId)
	{
		final TimeZone tz = TimeZone.getTimeZone(zoneId);
	    return tz.inDaylightTime(asDate(localDateTime, zoneId));
	}
	
	public static LocalDate parse(final String text)
	{
		if (isEmpty(text))
			return null;
		
		try
		{
			return LocalDate.parse(text);
		}
		catch (final Exception e)
		{
			return null;
		}
	}
	
	public static LocalTime parseTime(final String text)
	{
		if (isEmpty(text))
			return null;
		
		try
		{
			return LocalTime.parse(text);
		}
		catch (final Exception e)
		{
			return null;
		}
	}
	
	public static long toEpochMillis(final LocalDateTime time)
	{
		return time.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();
	}
	
	public static List<LocalDate> businessDaysBetween(final LocalDate startDate, final LocalDate endDate,
			final Optional<List<LocalDate>> holidays) 
	{
		// Validate method arguments
		if (startDate == null || endDate == null)
			throw new IllegalArgumentException("Invalid method argument(s) to countBusinessDaysBetween (" + startDate
					+ "," + endDate + "," + holidays + ")");

		// Predicate 1: If a given date is a holiday
		final Predicate<LocalDate> isHoliday = date -> holidays.isPresent() && holidays.get().contains(date);

		// Predicate 2: If a given date is a weekday
		final Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
				|| date.getDayOfWeek() == DayOfWeek.SUNDAY;

		// Get all days between two dates
		final long daysBetween = Math.abs(ChronoUnit.DAYS.between(startDate, endDate));

		// Iterate over stream of all dates and check each day against any weekday or
		// holiday
		return Stream.iterate(startDate, date -> date.plusDays(1))
				.limit(daysBetween)
				.filter(isHoliday.or(isWeekend).negate())
				.collect(Collectors.toUnmodifiableList());
	}
}
