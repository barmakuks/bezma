package com.fairbg.bezma.log;

import java.util.HashSet;
import java.util.Set;

public class BezmaLog
{

    private static ILogger m_Logger = new ConsoleLogger();// new AndroidLogger();
    private static Set<String> m_Tags = new HashSet<String>();

    public static void setLogger(ILogger logger)
    {
	m_Logger = logger;
    }

    public static void i(String tag, String text)
    {
	if (tagAllowed(tag))
	{
	    m_Logger.i(tag, text);
	}
    }

    private static boolean tagAllowed(String tag)
    {
	return m_Tags.contains(tag.toUpperCase());
    }

    public static void setShowTag(boolean showTag)
    {
	m_Logger.setShowTag(showTag);
    }

    public static void allowTag(String tag)
    {
	if (tag != null && !tag.isEmpty())
	{
	    m_Tags.add(tag.toUpperCase());
	}
    }

    public static void disableTag(String tag)
    {
	if (tag != null && !tag.isEmpty())
	{
	    m_Tags.remove(tag.toUpperCase());
	}
    }

    public static void e(String tag, String text)
    {
	if (tagAllowed(tag))
	{
	    m_Logger.e(tag, text);
	}
    }

}
