package com.fairbg.bezma.unit_tests;

import com.fairbg.bezma.communication.CommunicatorBase;

public class TestCommunicator extends CommunicatorBase
{
    @Override
    public void stop()
    {
        super.stop();
        System.out.println("Stop listening");
    }
}
