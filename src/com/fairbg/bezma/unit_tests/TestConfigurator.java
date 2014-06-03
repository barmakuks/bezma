package com.fairbg.bezma.unit_tests;

import com.fairbg.bezma.communication.IBroadcast;
import com.fairbg.bezma.communication.ICommunicator;
import com.fairbg.bezma.core.Configuration;
import com.fairbg.bezma.core.IConfigurator;
import com.fairbg.bezma.core.WrongConfigurationException;
import com.fairbg.bezma.core.backgammon.BgControllersFactory;
import com.fairbg.bezma.core.model.IControllersFactory;
import com.fairbg.bezma.store.IModelSerializer;
import com.fairbg.bezma.core.backgammon.generators.MatchSerializer;

public class TestConfigurator implements IConfigurator
{
    @Override
    public ICommunicator createCommunicator(Configuration configuration)
            throws WrongConfigurationException
    {
        return new TestCommunicator();
    }

    @Override
    public IModelSerializer createDatabase(Configuration configuration)
            throws WrongConfigurationException
    {
        return new MatchSerializer(configuration.getUnfinishedMatchPath());
    }

    @Override
    public IBroadcast createBroadcastModule(Configuration configuration)
            throws WrongConfigurationException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IControllersFactory createControllersFactory() throws WrongConfigurationException
    {
        return new BgControllersFactory();
    }

}
