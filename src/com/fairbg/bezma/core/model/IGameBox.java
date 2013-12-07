package com.fairbg.bezma.core.model;

import com.fairbg.bezma.core.backgammon.Position;

public interface IGameBox
{
    void nextGame();
    void startGame(Position.Direction direction);
    void finishGame();

    void nextMatch();
    void finishMatch();
    boolean isMatchFinished();

    void appendMove(IMove move);
}
