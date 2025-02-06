package ro.flexbiz.util.commons;

import static ro.flexbiz.util.commons.PresentationUtils.EMPTY_STRING;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ParameterStringBuilder
{
	private static final Logger log = Logger.getLogger(ParameterStringBuilder.class.getName());
	
	public static String getParamsString(final Map<String, String> params)
	{
		if (params == null || params.isEmpty())
			return EMPTY_STRING;
		
		return params.entrySet().stream()
				.map(entry -> {
					try {
						return URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
					} catch (final UnsupportedEncodingException e) {
						log.log(Level.SEVERE, "Error url encoding: "+entry, e);
						return EMPTY_STRING;
					}
				})
				.collect(Collectors.joining("&", "?", EMPTY_STRING));
	}
}