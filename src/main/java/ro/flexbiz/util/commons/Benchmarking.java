package ro.flexbiz.util.commons;

import java.util.logging.Logger;

public class Benchmarking
{
	private static final Logger log = Logger.getLogger(Benchmarking.class.getName());
	
	private long startTime;
	
	public static Benchmarking start()
	{
		return new Benchmarking();
	}
	
	private Benchmarking()
	{
		startTime = System.currentTimeMillis();
	}
	
	public Benchmarking printAndReset(final String name)
	{
		final long elapsed = System.currentTimeMillis() - startTime;
		log.info(name+": "+elapsed+" ms");
		startTime = System.currentTimeMillis();
		return this;
	}
	
	public Benchmarking printAndContinue(final String name)
	{
		final long elapsed = System.currentTimeMillis() - startTime;
		log.info(name+": "+elapsed+" ms");
		return this;
	}
	
	public long elapsedMillis()
	{
		return System.currentTimeMillis() - startTime;
	}
}
