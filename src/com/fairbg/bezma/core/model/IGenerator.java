/**
 * 
 */
package com.fairbg.bezma.core.model;

import com.fairbg.bezma.core.MatchParameters;

/**
 * Default interface for all generators that process match and all moves
 */
public interface IGenerator
{
    public void beginProccessing();
    public void processMatchParameters(MatchParameters matchParameters);
    public void processMoves(MovesList moves);
    public void finishProcessing();
}
