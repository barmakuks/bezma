package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.PlayerColors;


public interface IBackgammonAutomat extends IGameWithCube
{
    public Position.Direction getStartPositionDirection(Position position);
    
    public void startGame(Position.Direction direction);
    public void nextGame();

    public boolean isGameFinished();
    public void finishGame();
    
    public boolean isMatchFinished();
    public void finishMatch();
    
    public boolean findAndAcceptMove(Position position);

    public boolean processCube(Position position);
    
    public IAutomatState getAutomatState();
    
    public boolean setAutomatState(IAutomatState.AutomatStates state);

    public boolean isCurrentPlayer(PlayerColors player);
}
