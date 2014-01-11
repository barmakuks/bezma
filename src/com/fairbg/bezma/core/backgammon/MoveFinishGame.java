package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.IMoveVisitor;
import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.PlayerId;

public class MoveFinishGame extends MoveAbstract
{
    private int m_winnerPoints;
    
    public MoveFinishGame(PlayerId winner, int winnerPoints)
    {
        m_Player = winner;
        m_winnerPoints = winnerPoints;
    }
    
    public int getPoints()
    {
        return m_winnerPoints;
    }
    
    @Override
    public void accept(IMoveVisitor visitor)
    {
        visitor.visit(this);
    }

}
