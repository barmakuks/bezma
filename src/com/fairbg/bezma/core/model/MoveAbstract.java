package com.fairbg.bezma.core.model;

public abstract class MoveAbstract
{
    protected PlayerId m_Player;

    public PlayerId getPlayer()
    {
        return m_Player;
    }

    public void setPlayer(PlayerId player)
    {
        m_Player = player;
    }
    
    public abstract void accept(IMoveVisitor visitor);
}
