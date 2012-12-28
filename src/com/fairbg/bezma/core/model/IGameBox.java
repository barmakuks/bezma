package com.fairbg.bezma.core.model;

public interface IGameBox
{

    void nextGame();
    void finishGame();

    void nextMatch();
    void finishMatch();
    boolean isMatchFinished();

    void appendMove(IMove move);
}
