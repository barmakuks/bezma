package com.fairbg.bezma.core.backgammon;

import java.util.ArrayList;

import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.model.IMatchController;
import com.fairbg.bezma.core.model.IModelEventNotifier;
import com.fairbg.bezma.core.model.IModelObserver;
import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.PlayerId;
import com.fairbg.bezma.core.model.IScore;

public class BgMatchController implements IMatchController
{
    ArrayList<MoveAbstract> m_Moves = new ArrayList<MoveAbstract>();
    IModelEventNotifier     m_ModelNotifier;
    
    BgMatchController(IModelEventNotifier     aModelNotifier)
    {
        m_ModelNotifier = aModelNotifier;
    }

    public void appendMove(MoveAbstract move)
    {
        m_Moves.add(move);        
        m_ModelNotifier.notifyAll(new IModelObserver.MoveEvent(move));        
    }

    @Override
    public void finishGame(PlayerId winner, int points)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isMatchFinished()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public MatchParameters getMatchParameters()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IScore getScore()
    {
        // TODO Auto-generated method stub
        return null;
    }
}