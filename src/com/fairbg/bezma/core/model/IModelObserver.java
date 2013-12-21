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
	public MoveEvent(MoveAbstract move)
	{
	    m_Move = move;
	}
	public MoveAbstract getMove()
	{
	    return m_Move;
	}
	private MoveAbstract m_Move;
    }
    
    void onModelEvent(Event event);
}