package com.fairbg.bezma.communication;

import com.fairbg.bezma.communication.commands.ICommandObservable;
import com.fairbg.bezma.communication.commands.ICommandReceiver;
import com.fairbg.bezma.core.model.MatchScore;
import com.fairbg.bezma.core.model.MoveAbstract;

/** Model state view interface*/
public interface IModelView extends ICommandObservable, ICommandReceiver, IModelStateListener
{
    /** Append move */
    void appendMove(MoveAbstract move);

    /** Change score */
    void changeScore(MatchScore score);
}
