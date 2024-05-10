package ro.linic.util.commons;

import static ro.linic.util.commons.PresentationUtils.EMPTY_STRING;

import java.text.DecimalFormat;

public class RomanianNumberToWords
{

	private static final String[] tensNames = { "", "zece", "douazeci", "treizeci", "patruzeci", "cincizeci", "saizeci",
			"saptezeci", "optzeci", "nouazeci" };

	private static final String[] numNames = { "", "una", "doua", "trei", "patru", "cinci", "sase", "sapte",
			"opt", "noua", "zece", "unsprezece", "douasprezece", "treisprezece", "patrusprezece", "cincisprezece", "saisprezece",
			"saptesprezece", "optsprezece", "nouasprezece" };

	private RomanianNumberToWords()
	{
	}

	private static String convertLessThanOneThousand(int number)
	{
		String soFar = EMPTY_STRING;

		if (number % 100 < 20)
		{
			soFar = numNames[number % 100];
			number /= 100;
		} else
		{
			if (number % 10 != 0)
				soFar = "si";

			soFar = soFar+numNames[number % 10];
			number /= 10;

			soFar = tensNames[number % 10] + soFar;
			number /= 10;
		}
		if (number == 0)
			return soFar;
		
		switch (number)
		{
		case 1:
			return numNames[number] + "suta" + soFar;
		default:
			return numNames[number] + "sute" + soFar;
		}
	}

	public static String convert(final long number)
	{
		// 0 to 999 999 999 999
		if (number == 0)
			return "zero";

		final String minus = number < 0 ? "minus" : EMPTY_STRING;
		final long numberAbs = Math.abs(number);
		String snumber = Long.toString(numberAbs);

		// pad with "0"
		final String mask = "000000000000";
		final DecimalFormat df = new DecimalFormat(mask);
		snumber = df.format(numberAbs);

		// XXXnnnnnnnnn
		final int billions = Integer.parseInt(snumber.substring(0, 3));
		// nnnXXXnnnnnn
		final int millions = Integer.parseInt(snumber.substring(3, 6));
		// nnnnnnXXXnnn
		final int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
		// nnnnnnnnnXXX
		final int thousands = Integer.parseInt(snumber.substring(9, 12));

		String tradBillions;
		switch (billions)
		{
		case 0:
			tradBillions = "";
			break;
		case 1:
			tradBillions = convertLessThanOneThousand(billions) + "miliard";
			break;
		default:
			tradBillions = convertLessThanOneThousand(billions) + "miliarde";
		}
		String result = tradBillions;

		String tradMillions;
		switch (millions)
		{
		case 0:
			tradMillions = "";
			break;
		case 1:
			tradMillions = convertLessThanOneThousand(millions) + "milion";
			break;
		default:
			tradMillions = convertLessThanOneThousand(millions) + "milioane";
		}
		result = result + tradMillions;

		String tradHundredThousands;
		switch (hundredThousands)
		{
		case 0:
			tradHundredThousands = "";
			break;
		case 1:
			tradHundredThousands = "unamie";
			break;
		default:
			tradHundredThousands = convertLessThanOneThousand(hundredThousands) + "mii";
		}
		result = result + tradHundredThousands;

		String tradThousand;
		tradThousand = convertLessThanOneThousand(thousands);
		result = result + tradThousand;

		// remove extra spaces!
		return minus+result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
	}
}
