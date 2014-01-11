package com.fairbg.bezma.core.model;

import com.fairbg.bezma.core.MatchParameters;

public interface IMatchController
{
    void appendMove(MoveAbstract move);
    void finishGame(PlayerId winner, int points);
    boolean isMatchFinished();
    MatchParameters getMatchParameters();
    MatchScore   getScore();
}
