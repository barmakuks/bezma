package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.PlayerColors;


public interface IBackgammonAutomat
{
    public boolean isStartPosition(Position position);
    public void nextGame();
    
    public boolean canDouble(Position position);
    public void proposeDouble();

    public boolean isDoubleAccepted(Position position);
    public void acceptDouble();
    
    public boolean isGameFinished();
    public void finishGame();
    
    public boolean isMatchFinished();
    public void finishMatch();
    
    public boolean findAndAcceptMove(Position position);
    
    public IAutomatState getAutomatState();
    
    public boolean setAutomatState(IAutomatState.AutomatStates state);

    public boolean isCurrentPlayer(PlayerColors player);

    
}
