package com.fairbg.bezma.store;

import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.model.MovesList;

/**
 * Interface for model storage
 * contains all functions for store/restore model
 */
public interface IModelSerializer
{
    /** Check if last match was finished
     * @return true if last match was finished  
     * */
    boolean isLastMatchFinished();
    
    /** Finishes current match*/
    void finishMatch();
    /**
     * Writes Match Parameters and moves into storage
     * @param matchParameters match parameters
     * @param moves match list of moves
     */
    void serialize(MatchParameters matchParameters, MovesList moves);
    
    /**
     * Reads match parameters and moves from storage
     * @param matchId match identifier
     * @param matchParameters match parameters
     * @param moves match list of moves
     */
    void deserialize(MatchParameters matchParameters, MovesList moves);
}