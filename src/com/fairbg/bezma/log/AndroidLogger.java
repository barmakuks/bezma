package com.fairbg.bezma.log;

public class AndroidLogger implements ILogger
{

    @Override
    public void i(String tag, String text)
    {
	android.util.Log.i(tag, text);
    }

    @Override
    public void e(String tag, String text)
    {
	android.util.Log.e(tag, text);
    }

    @Override
    public void setShowTag(boolean showTag)
    {
    }
}
