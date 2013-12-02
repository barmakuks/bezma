package com.fairbg.bezma.unit_tests;

import java.util.ArrayList;

import com.fairbg.bezma.communication.IModelView;
import com.fairbg.bezma.communication.commands.CommunicationCommand;
import com.fairbg.bezma.communication.commands.ICommandObserver;
import com.fairbg.bezma.core.backgammon.MovePrinter;
import com.fairbg.bezma.core.model.IMove;
import com.fairbg.bezma.core.model.ModelSituation;

public class TestModelOut implements IModelView
{
    /** Datagram listeners list */
    private ArrayList<ICommandObserver> m_Observers = new ArrayList<ICommandObserver>();

    @Override
    public void notifyObservers(CommunicationCommand userCommand)
    {
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
	return true;
    }

    @Override
    public void stop()
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void sendCommand(CommunicationCommand command)
    {
	System.out.println(command.toString());
    }

    @Override
    public void setModelState(ModelSituation aModelState)
    {
	if (aModelState != null)
	{
	    System.out.println(aModelState.getPosition());
	    System.out.println();
	} 
	else
	{
	    System.out.println("empty state");
	}
    }

    @Override
    public void appendMove(IMove move)
    {
	System.out.print(MovePrinter.printMove(move));
    }
}