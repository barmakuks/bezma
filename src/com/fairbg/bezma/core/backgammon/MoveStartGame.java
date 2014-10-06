package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.IMoveVisitor;
import com.fairbg.bezma.core.model.MoveAbstract;

public class MoveStartGame extends MoveAbstract
{

    private Position.Direction m_direction;
    
    public MoveStartGame(Position.Direction direction)
    {
        m_direction = direction;
    }
    
    public Position.Direction getDirection()
    {
        return m_direction;
    }
    
    @Override
    public void accept(IMoveVisitor visitor)
    {
        visitor.visit(this);
    }

}
