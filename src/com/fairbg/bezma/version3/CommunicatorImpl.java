package com.fairbg.bezma.version3;

import com.fairbg.bezma.communication.CommunicatorBase;
import com.fairbg.bezma.core.errors.Error;
import com.fairbg.bezma.log.BezmaLog;

/** BEZMA 3rd version communicator implementation*/
public class CommunicatorImpl extends CommunicatorBase
{
    public CommunicatorImpl()
    {
        BezmaLog.i("COMMUNICATOR", "create communicator impl ver 3");
    }

    @Override
    public String toString()
    {
        return "CommunicatorImpl";
    }

	@Override
	public void displayError(Error error)
	{
		// TODO Auto-generated method stub		
	}
}