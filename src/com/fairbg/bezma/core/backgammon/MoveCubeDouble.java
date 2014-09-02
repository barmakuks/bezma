package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.IMoveVisitor;
import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.PlayerId;

public class MoveCubeDouble extends MoveAbstract
{
    private int m_cubeValue; 
    
    public MoveCubeDouble(PlayerId player, int cubeValue)
    {
        this.m_Player = player;
        m_cubeValue = cubeValue;
    }
    
    public int getCubeValue()
    {
        return m_cubeValue;
    }
    
    public void accept(IMoveVisitor visitor)
    {
        visitor.visit(this);
    }
}
