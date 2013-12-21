package com.fairbg.bezma.version3;

import java.util.ArrayList;

import com.fairbg.bezma.communication.IModelView;
import com.fairbg.bezma.communication.commands.ICommandObserver;
import com.fairbg.bezma.communication.commands.CommunicationCommand;
import com.fairbg.bezma.core.errors.Error;
import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.ModelSituation;

/** Устройство отображения данных на экране смартфона с android */
public class DisplayImpl implements IModelView
{

	private ArrayList<ICommandObserver> observers = new ArrayList<ICommandObserver>();

	@Override
	public void setModelState(ModelSituation modelState)
	{
	}

	@Override
	public void notifyObservers(CommunicationCommand userCommand)
	{
		for (ICommandObserver observer : observers)
		{
			observer.handeEvent(userCommand);
		}
	}

	@Override
	public void addObserver(ICommandObserver aCommandObserver)
	{
		observers.add(aCommandObserver);
	}

	@Override
	public void removeObserver(ICommandObserver aCommandObserver)
	{
		observers.remove(aCommandObserver);
	}

	@Override
	public boolean start()
	{
		// start listen events from display
		return true;
	}

	@Override
	public void stop()
	{
		// stop listen events from display
	}

	@Override
	public String toString()
	{
		return "DisplayImpl";
	}

	@Override
	public void sendCommand(CommunicationCommand command)
	{
		// do nothing to send datagram
	}

	@Override
	public void appendMove(MoveAbstract move)
	{
		// TODO Auto-generated method stub
		throw new NoSuchMethodError();
	}
	
	@Override
	public void displayError(Error error)
	{
		// TODO Auto-generated method stub
		throw new NoSuchMethodError();
	}
}
