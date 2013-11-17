package com.fairbg.bezma.log;

public class ConsoleLogger implements ILogger
{
    private boolean m_showTag;

    @Override
    public void i(String tag, String text)
    {
        System.out.println((m_showTag ? tag + " : " : "") + text);
    }

    @Override
    public void e(String tag, String text)
    {
        System.err.println((m_showTag ? tag + " : " : "") + text);
    }

    @Override
    public void setShowTag(boolean showTag)
    {
        m_showTag = showTag;
    }
}
