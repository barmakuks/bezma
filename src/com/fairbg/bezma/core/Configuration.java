package com.fairbg.bezma.core;

import java.io.Serializable;

/** Match configuration */
public class Configuration implements Serializable
{

    private static final long serialVersionUID = 7086822977080560127L;

    /** Match parameters */
    private MatchParameters m_MatchParameters;

    public void setMatchParameters(MatchParameters matchParameters)
    {
	m_MatchParameters = matchParameters;
    }

    public MatchParameters getMatchParameters()
    {
	return m_MatchParameters;
    }
}