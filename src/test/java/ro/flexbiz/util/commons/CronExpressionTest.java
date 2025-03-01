package ro.flexbiz.util.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ro.flexbiz.util.commons.CronExpression.CronFieldType;
import ro.flexbiz.util.commons.CronExpression.DayOfMonthField;
import ro.flexbiz.util.commons.CronExpression.DayOfWeekField;
import ro.flexbiz.util.commons.CronExpression.SimpleField;

public class CronExpressionTest
{
	TimeZone original;
	ZoneId zoneId;

	@BeforeEach
	public void setUp()
	{
		original = TimeZone.getDefault();
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Oslo"));
		zoneId = TimeZone.getDefault().toZoneId();
	}

	@AfterEach
	public void tearDown()
	{
		TimeZone.setDefault(original);
	}

	@Test
	public void shall_parse_number() throws Exception
	{
		final SimpleField field = new SimpleField(CronFieldType.MINUTE, "5");
		assertPossibleValues(field, 5);
	}

	private void assertPossibleValues(final SimpleField field, final Integer... values)
	{
		final Set<Integer> valid = values == null ? new HashSet<Integer>() : new HashSet<>(Arrays.asList(values));
		for (int i = field.fieldType.from; i <= field.fieldType.to; i++)
		{
			final String errorText = i + ":" + valid;
			if (valid.contains(i))
			{
				assertTrue(field.matches(i), errorText);
			} else
			{
				assertFalse(field.matches(i), errorText);
			}
		}
	}

	@Test
	public void shall_parse_number_with_increment() throws Exception
	{
		final SimpleField field = new SimpleField(CronFieldType.MINUTE, "0/15");
		assertPossibleValues(field, 0, 15, 30, 45);
	}

	@Test
	public void shall_parse_range() throws Exception
	{
		final SimpleField field = new SimpleField(CronFieldType.MINUTE, "5-10");
		assertPossibleValues(field, 5, 6, 7, 8, 9, 10);
	}

	@Test
	public void shall_parse_range_with_increment() throws Exception
	{
		final SimpleField field = new SimpleField(CronFieldType.MINUTE, "20-30/2");
		assertPossibleValues(field, 20, 22, 24, 26, 28, 30);
	}

	@Test
	public void shall_parse_asterix() throws Exception
	{
		final SimpleField field = new SimpleField(CronFieldType.DAY_OF_WEEK, "*");
		assertPossibleValues(field, 1, 2, 3, 4, 5, 6, 7);
	}

	@Test
	public void shall_parse_asterix_with_increment() throws Exception
	{
		final SimpleField field = new SimpleField(CronFieldType.DAY_OF_WEEK, "*/2");
		assertPossibleValues(field, 1, 3, 5, 7);
	}

	@Test
	public void shall_ignore_field_in_day_of_week() throws Exception
	{
		final DayOfWeekField field = new DayOfWeekField("?");
		assertTrue(field.matches(ZonedDateTime.now().toLocalDate()), "day of week is ?");
	}

	@Test
	public void shall_ignore_field_in_day_of_month() throws Exception
	{
		final DayOfMonthField field = new DayOfMonthField("?");
		assertTrue(field.matches(ZonedDateTime.now().toLocalDate()), "day of month is ?");
	}

	@Test
	public void shall_give_error_if_invalid_count_field() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			new CronExpression("* 3 *");
		});
	}

	@Test
	public void shall_give_error_if_minute_field_ignored() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			final SimpleField field = new SimpleField(CronFieldType.MINUTE, "?");
			field.matches(1);
		});
	}

	@Test
	public void shall_give_error_if_hour_field_ignored() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			final SimpleField field = new SimpleField(CronFieldType.HOUR, "?");
			field.matches(1);
		});
	}

	@Test
	public void shall_give_error_if_month_field_ignored() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			final SimpleField field = new SimpleField(CronFieldType.MONTH, "?");
			field.matches(1);
		});
	}

	@Test
	public void shall_give_last_day_of_month_in_leapyear() throws Exception
	{
		final CronExpression.DayOfMonthField field = new DayOfMonthField("L");
		assertTrue(field.matches(LocalDate.of(2012, 02, 29)), "day of month is L");
	}

	@Test
	public void shall_give_last_day_of_month() throws Exception
	{
		final CronExpression.DayOfMonthField field = new DayOfMonthField("L");
		final YearMonth now = YearMonth.now();
		assertTrue(field.matches(LocalDate.of(now.getYear(), now.getMonthValue(), now.lengthOfMonth())),
				"L matches to the last day of month");
	}

	@Test
	public void check_all() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("* * * * * *");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 10, 13, 0, 1, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 2, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 2, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 2, 1, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 59, 59, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 14, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_invalid_input() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			new CronExpression(null);
		});
	}

	@Test
	public void check_second_number() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("3 * * * * *");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 10, 13, 1, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 10, 13, 1, 3, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 1, 3, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 2, 3, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 59, 3, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 14, 0, 3, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 23, 59, 3, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 11, 0, 0, 3, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 30, 23, 59, 3, 0, zoneId);
		expected = ZonedDateTime.of(2012, 5, 1, 0, 0, 3, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_second_increment() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("5/15 * * * * *");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 5, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 5, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 20, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 20, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 35, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 35, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 50, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 50, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 1, 5, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		// if rolling over minute then reset second (cron rules - increment affects only
		// values in own field)
		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 50, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 1, 10, 0, zoneId);
		assertTrue(new CronExpression("10/100 * * * * *").nextTimeAfter(after).equals(expected));

		after = ZonedDateTime.of(2012, 4, 10, 13, 1, 10, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 2, 10, 0, zoneId);
		assertTrue(new CronExpression("10/100 * * * * *").nextTimeAfter(after).equals(expected));
	}

	@Test
	public void check_second_list() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("7,19 * * * * *");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 7, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 7, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 19, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 19, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 1, 7, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_second_range() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("42-45 * * * * *");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 42, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 42, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 43, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 43, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 44, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 44, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 45, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 45, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 1, 42, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_second_invalid_range() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			new CronExpression("42-63 * * * * *");
		});
	}

	@Test
	public void check_second_invalid_increment_modifier() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			new CronExpression("42#3 * * * * *");
		});
	}

	@Test
	public void check_minute_number() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 3 * * * *");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 10, 13, 1, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 10, 13, 3, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 3, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 14, 3, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_minute_increment() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 0/15 * * * *");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 10, 13, 15, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 15, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 30, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 30, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 45, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 45, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 14, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_minute_list() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 7,19 * * * *");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 10, 13, 7, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 13, 7, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 13, 19, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_hour_number() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 * 3 * * *");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 10, 13, 1, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 11, 3, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 11, 3, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 11, 3, 1, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 11, 3, 59, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 12, 3, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_hour_increment() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 * 0/15 * * *");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 10, 15, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 15, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 15, 1, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 15, 59, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 11, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 11, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 11, 0, 1, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 11, 15, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 11, 15, 1, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_hour_list() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 * 7,19 * * *");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 10, 19, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 19, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 10, 19, 1, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 10, 19, 59, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 11, 7, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_hour_shall_run_25_times_in_DST_change_to_wintertime() throws Exception
	{
		final CronExpression cron = new CronExpression("0 1 * * * *");
		final ZonedDateTime start = ZonedDateTime.of(2011, 10, 30, 0, 0, 0, 0, zoneId);
		final ZonedDateTime slutt = start.plusDays(1);
		ZonedDateTime tid = start;

		// throws: Unsupported unit: Seconds
		// assertEquals(25, Duration.between(start.toLocalDate(),
		// slutt.toLocalDate()).toHours());

		int count = 0;
		ZonedDateTime lastTime = tid;
		while (tid.isBefore(slutt))
		{
			final ZonedDateTime nextTime = cron.nextTimeAfter(tid);
			assertTrue(nextTime.isAfter(lastTime));
			lastTime = nextTime;
			tid = tid.plusHours(1);
			count++;
		}
		assertEquals(25, count);
	}

	@Test
	public void check_hour_shall_run_23_times_in_DST_change_to_summertime() throws Exception
	{
		final CronExpression cron = new CronExpression("0 0 * * * *");
		final ZonedDateTime start = ZonedDateTime.of(2011, 03, 27, 1, 0, 0, 0, zoneId);
		final ZonedDateTime slutt = start.plusDays(1);
		ZonedDateTime tid = start;

		// throws: Unsupported unit: Seconds
		// assertEquals(23, Duration.between(start.toLocalDate(),
		// slutt.toLocalDate()).toHours());

		int count = 0;
		ZonedDateTime lastTime = tid;
		while (tid.isBefore(slutt))
		{
			final ZonedDateTime nextTime = cron.nextTimeAfter(tid);
			assertTrue(nextTime.isAfter(lastTime));
			lastTime = nextTime;
			tid = tid.plusHours(1);
			count++;
		}
		assertEquals(23, count);
	}

	@Test
	public void check_dayOfMonth_number() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 * * 3 * *");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 5, 3, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 5, 3, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 5, 3, 0, 1, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 5, 3, 0, 59, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 5, 3, 1, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 5, 3, 23, 59, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 6, 3, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_dayOfMonth_increment() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 0 0 1/15 * *");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 16, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 16, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 5, 1, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 30, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 5, 1, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 5, 16, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_dayOfMonth_list() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 0 0 7,19 * *");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 19, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 19, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 5, 7, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 5, 7, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 5, 19, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 5, 30, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 6, 7, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_dayOfMonth_last() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 0 0 L * *");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 30, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 2, 12, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 2, 29, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_dayOfMonth_number_last_L() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 0 0 3L * *");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 30 - 3, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 2, 12, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 2, 29 - 3, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_dayOfMonth_closest_weekday_W() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 0 0 9W * *");

		// 9 - is weekday in may
		ZonedDateTime after = ZonedDateTime.of(2012, 5, 2, 0, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 5, 9, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		// 9 - is weekday in may
		after = ZonedDateTime.of(2012, 5, 8, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		// 9 - saturday, friday closest weekday in june
		after = ZonedDateTime.of(2012, 5, 9, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 6, 8, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		// 9 - sunday, monday closest weekday in september
		after = ZonedDateTime.of(2012, 9, 1, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 9, 10, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_dayOfMonth_invalid_modifier() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			new CronExpression("0 0 0 9X * *");
		});
	}

	@Test
	public void check_dayOfMonth_invalid_increment_modifier() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			new CronExpression("0 0 0 9#2 * *");
		});
	}

	@Test
	public void check_month_number() throws Exception
	{
		final ZonedDateTime after = ZonedDateTime.of(2012, 2, 12, 0, 0, 0, 0, zoneId);
		final ZonedDateTime expected = ZonedDateTime.of(2012, 5, 1, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 1 5 *").nextTimeAfter(after).equals(expected));
	}

	@Test
	public void check_month_increment() throws Exception
	{
		ZonedDateTime after = ZonedDateTime.of(2012, 2, 12, 0, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 5, 1, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 1 5/2 *").nextTimeAfter(after).equals(expected));

		after = ZonedDateTime.of(2012, 5, 1, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 7, 1, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 1 5/2 *").nextTimeAfter(after).equals(expected));

		// if rolling over year then reset month field (cron rules - increments only
		// affect own field)
		after = ZonedDateTime.of(2012, 5, 1, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2013, 5, 1, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 1 5/10 *").nextTimeAfter(after).equals(expected));
	}

	@Test
	public void check_month_list() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 0 0 1 3,7,12 *");

		ZonedDateTime after = ZonedDateTime.of(2012, 2, 12, 0, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 3, 1, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 3, 1, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 7, 1, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 7, 1, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 12, 1, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_month_list_by_name() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 0 0 1 MAR,JUL,DEC *");

		ZonedDateTime after = ZonedDateTime.of(2012, 2, 12, 0, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 3, 1, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 3, 1, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 7, 1, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 7, 1, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 12, 1, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_month_invalid_modifier() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			new CronExpression("0 0 0 1 ? *");
		});
	}

	@Test
	public void check_dayOfWeek_number() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 0 0 * * 3");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 4, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 4, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 11, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 12, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 18, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 18, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 25, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_dayOfWeek_increment() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 0 0 * * 3/2");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 4, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 4, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 6, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 6, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 8, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 8, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 11, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_dayOfWeek_list() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 0 0 * * 1,5,7");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 2, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 2, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 6, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 6, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 8, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_dayOfWeek_list_by_name() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 0 0 * * MON,FRI,SUN");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 2, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 2, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 6, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 6, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 8, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void check_dayOfWeek_last_friday_in_month() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("0 0 0 * * 5L");

		ZonedDateTime after = ZonedDateTime.of(2012, 4, 1, 1, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 27, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 4, 27, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 5, 25, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 2, 6, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 2, 24, 0, 0, 0, 0, zoneId);
		assertEquals(expected, cronExpr.nextTimeAfter(after));

		after = ZonedDateTime.of(2012, 2, 6, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 2, 24, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 * * FRIL").nextTimeAfter(after).equals(expected));
	}

	@Test
	public void check_dayOfWeek_invalid_modifier() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			new CronExpression("0 0 0 * * 5W");
		});
	}

	@Test
	public void check_dayOfWeek_invalid_increment_modifier() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			new CronExpression("0 0 0 * * 5?3");
		});
	}

	@Test
	public void check_dayOfWeek_shall_interpret_0_as_sunday() throws Exception
	{
		final ZonedDateTime after = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 8, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 * * 0").nextTimeAfter(after).equals(expected));

		expected = ZonedDateTime.of(2012, 4, 29, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 * * 0L").nextTimeAfter(after).equals(expected));

		expected = ZonedDateTime.of(2012, 4, 8, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 * * 0#2").nextTimeAfter(after).equals(expected));
	}

	@Test
	public void check_dayOfWeek_shall_interpret_7_as_sunday() throws Exception
	{
		final ZonedDateTime after = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 8, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 * * 7").nextTimeAfter(after).equals(expected));

		expected = ZonedDateTime.of(2012, 4, 29, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 * * 7L").nextTimeAfter(after).equals(expected));

		expected = ZonedDateTime.of(2012, 4, 8, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 * * 7#2").nextTimeAfter(after).equals(expected));
	}

	@Test
	public void check_dayOfWeek_nth_day_in_month() throws Exception
	{
		ZonedDateTime after = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId);
		ZonedDateTime expected = ZonedDateTime.of(2012, 4, 20, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 * * 5#3").nextTimeAfter(after).equals(expected));

		after = ZonedDateTime.of(2012, 4, 20, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 5, 18, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 * * 5#3").nextTimeAfter(after).equals(expected));

		after = ZonedDateTime.of(2012, 3, 30, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 * * 7#1").nextTimeAfter(after).equals(expected));

		after = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 5, 6, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 * * 7#1").nextTimeAfter(after).equals(expected));

		after = ZonedDateTime.of(2012, 2, 6, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 2, 29, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 * * 3#5").nextTimeAfter(after).equals(expected)); // leapday

		after = ZonedDateTime.of(2012, 2, 6, 0, 0, 0, 0, zoneId);
		expected = ZonedDateTime.of(2012, 2, 29, 0, 0, 0, 0, zoneId);
		assertTrue(new CronExpression("0 0 0 * * WED#5").nextTimeAfter(after).equals(expected)); // leapday
	}

	@Test
	public void shall_not_not_support_rolling_period() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			new CronExpression("* * 5-1 * * *");
		});
	}

	@Test
	public void non_existing_date_throws_exception() throws Exception
	{
		// Will check for the next 4 years - no 30th of February is found so a IAE is
		// thrown.
		assertThrows(IllegalArgumentException.class, () ->
		{
			new CronExpression("* * * 30 2 *").nextTimeAfter(ZonedDateTime.now());
		});
	}

	@Test
	public void test_default_barrier() throws Exception
	{
		final CronExpression cronExpr = new CronExpression("* * * 29 2 *");

		final ZonedDateTime after = ZonedDateTime.of(2012, 3, 1, 0, 0, 0, 0, zoneId);
		final ZonedDateTime expected = ZonedDateTime.of(2016, 2, 29, 0, 0, 0, 0, zoneId);
		// the default barrier is 4 years - so leap years are considered.
		assertEquals(expected, cronExpr.nextTimeAfter(after));
	}

	@Test
	public void test_one_year_barrier() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			final ZonedDateTime after = ZonedDateTime.of(2012, 3, 1, 0, 0, 0, 0, zoneId);
			final ZonedDateTime barrier = ZonedDateTime.of(2013, 3, 1, 0, 0, 0, 0, zoneId);
			// The next leap year is 2016, so an IllegalArgumentException is expected.
			new CronExpression("* * * 29 2 *").nextTimeAfter(after, barrier);
		});
	}

	@Test
	public void test_two_year_barrier() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			final ZonedDateTime after = ZonedDateTime.of(2012, 3, 1, 0, 0, 0, 0, zoneId);
			// The next leap year is 2016, so an IllegalArgumentException is expected.
			new CronExpression("* * * 29 2 *").nextTimeAfter(after, 1000 * 60 * 60 * 24 * 356 * 2);
		});
	}

	@Test
	public void test_seconds_specified_but_should_be_omitted() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			CronExpression.createWithoutSeconds("* * * 29 2 *");
		});
	}

	@Test
	public void test_without_seconds() throws Exception
	{
		final ZonedDateTime after = ZonedDateTime.of(2012, 3, 1, 0, 0, 0, 0, zoneId);
		final ZonedDateTime expected = ZonedDateTime.of(2016, 2, 29, 0, 0, 0, 0, zoneId);
		assertTrue(CronExpression.createWithoutSeconds("* * 29 2 *").nextTimeAfter(after).equals(expected));
	}

	@Test
	public void testTriggerProblemSameMonth()
	{
		assertEquals(ZonedDateTime.parse("2020-01-02T00:50:00Z"),
				new CronExpression("00 50 * 1-8 1 *").nextTimeAfter(ZonedDateTime.parse("2020-01-01T23:50:00Z")));
	}

	@Test
	public void testTriggerProblemNextMonth()
	{
		assertEquals(ZonedDateTime.parse("2020-02-01T00:50:00Z"),
				new CronExpression("00 50 * 1-8 2 *").nextTimeAfter(ZonedDateTime.parse("2020-01-31T23:50:00Z")));
	}

	@Test
	public void testTriggerProblemNextYear()
	{
		assertEquals(ZonedDateTime.parse("2020-01-01T00:50:00Z"),
				new CronExpression("00 50 * 1-8 1 *").nextTimeAfter(ZonedDateTime.parse("2019-12-31T23:50:00Z")));
	}

	@Test
	public void testTriggerProblemNextMonthMonthAst()
	{
		assertEquals(ZonedDateTime.parse("2020-02-01T00:50:00Z"),
				new CronExpression("00 50 * 1-8 * *").nextTimeAfter(ZonedDateTime.parse("2020-01-31T23:50:00Z")));
	}

	@Test
	public void testTriggerProblemNextYearMonthAst()
	{
		assertEquals(ZonedDateTime.parse("2020-01-01T00:50:00Z"),
				new CronExpression("00 50 * 1-8 * *").nextTimeAfter(ZonedDateTime.parse("2019-12-31T23:50:00Z")));
	}

	@Test
	public void testTriggerProblemNextMonthDayAst()
	{
		assertEquals(ZonedDateTime.parse("2020-02-01T00:50:00Z"),
				new CronExpression("00 50 * * 2 *").nextTimeAfter(ZonedDateTime.parse("2020-01-31T23:50:00Z")));
	}

	@Test
	public void testTriggerProblemNextYearDayAst()
	{
		assertEquals(ZonedDateTime.parse("2020-01-01T00:50:00Z"),
				new CronExpression("00 50 * * 1 *").nextTimeAfter(ZonedDateTime.parse("2019-12-31T22:50:00Z")));
	}

	@Test
	public void testTriggerProblemNextMonthAllAst()
	{
		assertEquals(ZonedDateTime.parse("2020-02-01T00:50:00Z"),
				new CronExpression("00 50 * * * *").nextTimeAfter(ZonedDateTime.parse("2020-01-31T23:50:00Z")));
	}

	@Test
	public void testTriggerProblemNextYearAllAst()
	{
		assertEquals(ZonedDateTime.parse("2020-01-01T00:50:00Z"),
				new CronExpression("00 50 * * * *").nextTimeAfter(ZonedDateTime.parse("2019-12-31T23:50:00Z")));
	}
}