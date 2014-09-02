package com.fairbg.bezma.communication.commands;

/** Abstract class for communication command */
public abstract class CommunicationCommand
{
    public Object getRawData()
    {
        return m_rawData;
    }
    
    public void setRawData(Object rawData)
    {
        m_rawData = rawData;
    }
    
    private Object m_rawData;
}