package com.fairbg.bezma.core.model;

import com.fairbg.bezma.core.backgammon.Position;

/**
 * Класс описывающий текущее состояние модели
 * */
public class ModelSituation
{

    /** Текущая позиция на доске */
    private Position m_Position = null;
    /** Последнее сообщение об ошибке */
    private String m_ErrorMsg = "";

    public ModelSituation(Position position, String errorMsg)
    {
        try
        {
            m_Position = (Position) position.clone();
        }
        catch (CloneNotSupportedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        m_ErrorMsg = errorMsg;
    }

    /** Возвращает текущую позицию на доске */
    public Position getPosition()
    {
        return m_Position;
    }

    /** Возвращает последнее сообщение об ошибке */
    public String getErrorMessage()
    {
        return m_ErrorMsg;
    }

    /**
     * Возвращает TRUE, если в процессе обработки пользовательской команды
     * возникла ошибка
     */
    public boolean isErrorState()
    {
        return m_ErrorMsg != "";
    }
}
