package ro.linic.util.commons;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CsvParserSimple
{
	private static final char DEFAULT_SEPARATOR = ',';
	private static final char DOUBLE_QUOTES = '"';
	private static final char DEFAULT_QUOTE_CHAR = DOUBLE_QUOTES;
	private static final String NEW_LINE = "\n";

	private boolean isMultiLine = false;
	private String pendingField = "";
	private String[] pendingFieldLine = new String[] {};

	public List<String[]> readFile(final InputStream csvFile) throws Exception
	{
		return readFile(csvFile, 0);
	}

	public List<String[]> readFile(final InputStream csvFile, final int skipLine) throws Exception
	{
		final List<String[]> result = new ArrayList<>();
		int indexLine = 1;

		try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFile)))
		{
			String line;
			while ((line = br.readLine()) != null)
			{
				if (indexLine++ <= skipLine)
				{
					continue;
				}

				final String[] csvLineInArray = parseLine(line);

				if (isMultiLine)
				{
					pendingFieldLine = joinArrays(pendingFieldLine, csvLineInArray);
				} else
				{

					if (pendingFieldLine != null && pendingFieldLine.length > 0)
					{
						// joins all fields and add to list
						result.add(joinArrays(pendingFieldLine, csvLineInArray));
						pendingFieldLine = new String[] {};
					} else
					{
						// if dun want to support multiline, only this line is required.
						result.add(csvLineInArray);
					}
				}
			}
		}

		return result;
	}

	public String[] parseLine(final String line) throws Exception
	{
		return parseLine(line, DEFAULT_SEPARATOR);
	}

	public String[] parseLine(final String line, final char separator) throws Exception
	{
		return parse(line, separator, DEFAULT_QUOTE_CHAR).toArray(String[]::new);
	}

	private List<String> parse(final String line, final char separator, final char quoteChar) throws Exception
	{
		final List<String> result = new ArrayList<>();

		boolean inQuotes = false;
		boolean isFieldWithEmbeddedDoubleQuotes = false;

		final StringBuilder field = new StringBuilder();

		for (final char c : line.toCharArray())
		{

			if (c == DOUBLE_QUOTES)
			{ // handle embedded double quotes ""
				if (isFieldWithEmbeddedDoubleQuotes)
				{

					if (field.length() > 0)
					{ // handle for empty field like "",""
						field.append(DOUBLE_QUOTES);
						isFieldWithEmbeddedDoubleQuotes = false;
					}
				} else
				{
					isFieldWithEmbeddedDoubleQuotes = true;
				}
			} else
			{
				isFieldWithEmbeddedDoubleQuotes = false;
			}

			if (isMultiLine)
			{ // multiline, add pending from the previous field
				field.append(pendingField).append(NEW_LINE);
				pendingField = "";
				inQuotes = true;
				isMultiLine = false;
			}

			if (c == quoteChar)
			{
				inQuotes = !inQuotes;
			} else
			{
				if (c == separator && !inQuotes)
				{ // if find separator and not in quotes, add field to the list
					result.add(field.toString());
					field.setLength(0); // empty the field and ready for the next
				} else
				{
					field.append(c); // else append the char into a field
				}
			}
		}

		// line done, what to do next?
		if (inQuotes)
		{
			pendingField = field.toString(); // multiline
			isMultiLine = true;
		} else
		{
			result.add(field.toString()); // this is the last field
		}

		return result;
	}

	private String[] joinArrays(final String[] array1, final String[] array2)
	{
		return Stream.concat(Arrays.stream(array1), Arrays.stream(array2)).toArray(String[]::new);
	}
}