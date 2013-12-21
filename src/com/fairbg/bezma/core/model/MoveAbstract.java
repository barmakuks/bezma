package com.fairbg.bezma.core.model;

public abstract class MoveAbstract
{
    protected PlayerColors m_Player;

    public PlayerColors getPlayer()
    {
        return m_Player;
    }

    public void setPlayer(PlayerColors player)
    {
        m_Player = player;
    }
    
    public abstract void accept(IMoveVisitor visitor);
}
