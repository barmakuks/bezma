package com.fairbg.bezma.core.model;

import com.fairbg.bezma.core.backgammon.Position;

/**
 * Current model state
 * */
public class BoardContext
{
    /** Current checkers position */
    private Position m_Position = null;
    /** Last error message */
    private String m_ErrorMsg = "";
    
    public BoardContext(Position position, String errorMsg)
    {
        try
        {
            m_Position = (Position) position.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        m_ErrorMsg = errorMsg;
    }

    /** get current checkers position */
    public Position getPosition()
    {
        return m_Position;
    }

    /** get last error message */
    public String getErrorMessage()
    {
        return m_ErrorMsg;
    }

    /**
     * TRUE if last command was processed with error
     */
    public boolean isErrorState()
    {
        return m_ErrorMsg != "";
    }
}
