package com.fairbg.bezma.core.model;

/**
 * Observer listens notifies from Model
 */
public interface IModelObserver
{
    class Event
    {
    }

    class MoveEvent extends Event
    {
        public MoveEvent(MoveAbstract move)
        {
            m_move = move;
        }

        public MoveAbstract getMove()
        {
            return m_move;
        }

        private MoveAbstract m_move;
    }

    class ScoreChangedEvent extends Event
    {
        public ScoreChangedEvent(MatchScore score)
        {
            m_score = score;
        }
        
        public MatchScore getScore()
        {
            return m_score;
        }
        
        private MatchScore m_score;
    }

    class MatchFinishEvent extends Event
    {
        
    }
    
    void onModelEvent(Event event);
}