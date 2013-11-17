package com.fairbg.bezma.unit_tests;

import com.fairbg.bezma.communication.IBroadcast;
import com.fairbg.bezma.communication.ICommunicator;
import com.fairbg.bezma.core.Configuration;
import com.fairbg.bezma.core.IConfigurator;
import com.fairbg.bezma.core.WrongConfigurationException;
import com.fairbg.bezma.store.IDatabase;

public class TestConfigurator implements IConfigurator
{
    @Override
    public ICommunicator createCommunicator(Configuration configuration)
	    throws WrongConfigurationException
    {
	return new TestCommunicator();
    }

    @Override
    public IDatabase createDatabase(Configuration configuration)
	    throws WrongConfigurationException
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public IBroadcast createBroadcastModule(Configuration configuration)
	    throws WrongConfigurationException
    {
	// TODO Auto-generated method stub
	return null;
    }

}
