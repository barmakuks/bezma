package com.fairbg.bezma.core.model;

import com.fairbg.bezma.core.MatchIdentifier;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.store.IModelSerializer;

public interface IMatchController
{
    void appendMove(MoveAbstract move);
    void finishGame(PlayerId winner, int points);
    boolean isMatchFinished();
    MatchParameters getMatchParameters();
    MatchScore   getScore();
    boolean cubeInGame();
    
    void serialize(IModelSerializer serializer);
    void deserialize(IModelSerializer serializer, MatchIdentifier matchId);
}
