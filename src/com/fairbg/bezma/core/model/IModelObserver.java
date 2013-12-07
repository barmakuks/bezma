package com.fairbg.bezma.core.model;

/**
 * Observer listenes notifies from Model
 * 
 * @author Vitalik
 * 
 */
public interface IModelObserver
{
    class Event{}
    class MoveEvent extends Event
    {
	public MoveEvent(IMove move)
	{
	    m_Move = move;
	}
	public IMove getMove()
	{
	    return m_Move;
	}
	private IMove m_Move;
    }
    
    void onModelEvent(Event event);
}