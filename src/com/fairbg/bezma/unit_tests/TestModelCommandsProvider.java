package com.fairbg.bezma.unit_tests;

import java.util.ArrayList;

import com.fairbg.bezma.communication.IModelView;
import com.fairbg.bezma.communication.commands.CommunicationCommand;
import com.fairbg.bezma.communication.commands.CommunicationCommandState;
import com.fairbg.bezma.communication.commands.ICommandObserver;
import com.fairbg.bezma.core.model.ModelSituation;
import com.fairbg.bezma.core.model.PlayerColors;

public class TestModelCommandsProvider implements IModelView
{
    /** Datagram listeners list */
    private ArrayList<ICommandObserver> m_Observers = new ArrayList<ICommandObserver>();
    
//    short [][] datagrams = {
//	    new short[] { 0,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 2, 0, 0, 0, 0,-5, 0,-3, 0, 0, 0, 5, 0, 0, 0}, 
//	    new short[] { 0,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 2, 0, 0, 0,-2,-4, 0,-2, 0, 0, 0, 5, 0, 0, 0}, 
//	    new short[] { 0,-5, 0, 0, 0, 2, 0, 4, 2, 0, 0, 0,-2, 2, 0, 0, 0,-2,-4, 0,-2, 0, 0, 0, 5, 0, 0, 0}, 
//	    new short[] { 0,-5, 0, 0, 0, 2, 0, 4, 2, 0,-1,-1, 0, 2, 0, 0, 0,-2,-4, 0,-2, 0, 0, 0, 5, 0, 0, 0}, 
//	    new short[] { 0,-5, 0, 0, 0, 2, 0, 2, 2, 0,-1, 2, 0, 2, 0, 0, 0,-2,-4, 0,-2, 0, 0, 0, 5,-1, 0, 0}, 
//	    new short[] { 0,-5, 0, 0, 0, 2, 0, 2, 2, 0,-2, 2, 0, 2, 0, 0, 0,-2,-4, 0,-2, 0, 0, 0, 5, 0, 0, 0}, 
//	    };
    
    short [][] datagrams = {
	    new short[] { 0, 2, 0, 0, 0, 0,-5, 0,-3, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0}, 
	    new short[] { 0, 0, 1, 1, 0, 0,-5, 0,-3, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0}, 
	    new short[] { 0, 0, 1, 1, 0, 0,-5, 0,-3, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0,-1,-1, 0, 0, 0, 0}, 
	    new short[] { 0, 0, 1, 1, 0, 0,-5, 0,-3, 0, 0, 0, 5,-5, 0, 0, 0, 2, 0, 4, 2, 0,-1,-1, 0, 0, 0, 0}, 
	    new short[] { 1, 0, 1,-2, 0, 0,-4, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 2, 0, 4, 2, 0,-1,-1, 0, 0, 0, 0}, 
	    new short[] { 0, 0, 0,-2, 2, 0,-4, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 2, 0, 4, 2, 0,-1,-1, 0, 0, 0, 0}, 
	    new short[] { 0, 0, 0,-2, 2,-1,-3, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 2,-1, 4, 2, 0, 0,-1, 0, 0, 0, 0}, 
	    new short[] { 0, 0, 0,-2, 2,-1,-3, 0,-2, 0, 0, 0, 4,-5, 0, 0, 0, 1, 2, 4, 2, 0, 0,-1, 0,-1, 0, 0}, 
	    new short[] { 0, 0, 0,-2, 2,-1,-3, 0,-2, 0, 0, 0, 4,-5, 0, 0, 0, 1, 2, 4, 2, 0, 0,-1, 0,-1, 0, 0}, // skip move 
	    new short[] { 0, 0, 0,-2, 2,-1,-3, 0,-2, 0, 0, 0, 3,-5, 0, 0, 0, 2, 2, 4, 2, 0, 0,-1, 0,-1, 0, 0}, 
	    new short[] { 0, 0, 0,-2, 2,-1,-3, 0,-2, 0, 0, 0, 3,-5, 0, 0, 0, 2, 2, 4, 2, 0, 0,-1, 0,-1, 0, 0}, 
	    new short[] { 0, 0, 0,-2, 2,-1,-3, 0,-2, 0, 0, 0, 3,-5, 0, 0, 0, 2, 2, 2, 2, 2, 0,-1, 0,-1, 0, 0}, 
	    new short[] { 0, 0, 0,-2, 2,-1,-3, 0,-2, 0, 0, 0, 3,-5, 0, 0, 0, 2, 2, 2, 2, 2,-2, 0, 0, 0, 0, 0}, 
	    };
    
    @Override
    public void notifyObservers(CommunicationCommand userCommand)
    {
	System.out.println("View model sends command");
	for (ICommandObserver observer : m_Observers)
	{
	    observer.handeEvent(userCommand);
	}
    }

    @Override
    public void addObserver(ICommandObserver aCommandObserver)
    {
	m_Observers.add(aCommandObserver);
    }

    @Override
    public void removeObserver(ICommandObserver aCommandObserver)
    {
	m_Observers.remove(aCommandObserver);
    }

    @Override
    public boolean start()
    {
	for (int i = 0; i < datagrams.length; i++)
	{
	    CommunicationCommandState command = new CommunicationCommandState();
	    command.checkers = datagrams[i];
	    command.cubePosition = -1;
	    command.playerId = -1;
	    notifyObservers(command);
	}
	return false;
    }

    @Override
    public void stop()
    {
    }

    @Override
    public void sendCommand(CommunicationCommand command)
    {
    }

    @Override
    public void setModelState(ModelSituation aModelState)
    {
    }
}
