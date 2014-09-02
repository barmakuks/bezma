package com.fairbg.bezma.core.model;


/** Interface for current match score */
public interface MatchScore
{
    public int getPlayerScore(PlayerId playerId);
    public void setPlayerScore(PlayerId playerId, int score);
}
