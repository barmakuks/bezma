package com.fairbg.bezma.version3;

import com.fairbg.bezma.communication.ICommunicator;
import com.fairbg.bezma.communication.Logger;
import com.fairbg.bezma.communication.CommunicatorBase;

/**Имплементация коммуникатора для 3-й версии BEZMA*/
public class CommunicatorImpl extends CommunicatorBase implements ICommunicator {

	public CommunicatorImpl()
	{
		Logger.writeln(this, "create communicator impl ver 3");
	}
	

	@Override
	public String toString() {
		return "CommunicatorImpl";
	}
	
	
}