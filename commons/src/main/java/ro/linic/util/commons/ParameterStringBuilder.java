package ro.linic.util.commons;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class ParameterStringBuilder
{
	public static String getParamsString(final Map<String, String> params) throws UnsupportedEncodingException
	{
		final StringBuilder result = new StringBuilder();

		for (final Map.Entry<String, String> entry : params.entrySet())
		{
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			result.append("&");
		}

		final String resultString = result.toString();
		return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
	}
}