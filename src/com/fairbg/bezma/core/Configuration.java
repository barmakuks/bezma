package com.fairbg.bezma.core;

import android.text.TextUtils;

import java.io.Serializable;

/** Match configuration */
public abstract class Configuration implements Serializable
{

	private static final long serialVersionUID = 7086822977080560127L;

	/** Match parameters */
	private MatchParameters   m_MatchParameters;

	public void configureMatchParameters(MatchParameters matchParameters)
	{
		m_MatchParameters = matchParameters;
		
		if (TextUtils.isEmpty(m_MatchParameters.defaultDir))
		{
		    m_MatchParameters.defaultDir = getDefaultUserDirectory();
		}
	}

	public MatchParameters getMatchParameters()
	{
		return m_MatchParameters;
	}
	
	public abstract String getDefaultUserDirectory();
	
	public abstract String getUnfinishedMatchPath();
}