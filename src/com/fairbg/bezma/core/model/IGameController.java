package com.fairbg.bezma.core.model;

public interface IGameController
{
    void startGame();
    void finishGame(PlayerId winner, int cubeValue);

    void appendMove(MoveAbstract move);
    BoardContext getModelSituation();
    
    boolean processCommand(ModelCommand modelCommand);
    boolean cubeInGame();    
    
}
