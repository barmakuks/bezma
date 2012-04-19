package com.fairbg.bezma.version3;

import java.util.ArrayList;

import com.fairbg.bezma.communication.IModelView;
import com.fairbg.bezma.communication.Logger;
import com.fairbg.bezma.communication.commands.ICommandObserver;
import com.fairbg.bezma.communication.commands.UserCommand;
import com.fairbg.bezma.core.model.ModelState;

/**Устройство отображения данных на экране смартфона с android*/
public class DisplayImpl implements IModelView{

	private ArrayList<ICommandObserver> observers = new ArrayList<ICommandObserver>();
	
	@Override
	public void setModelState(ModelState modelState) {
		// set avaible buttons on display
		Logger.writeln(this, "setModelState");
	}

	@Override
	public void notifyObservers(UserCommand userCommand) {
		Logger.writeln(this, "notifyObservers ");		
		for(ICommandObserver observer : observers)
		{
			observer.handeEvent(userCommand);
		}
	}

	@Override
	public void addObserver(ICommandObserver aCommandObserver) {
		Logger.writeln(this, "addObserver: " + aCommandObserver.toString());
		observers.add(aCommandObserver);
	}

	@Override
	public void removeObserver(ICommandObserver aCommandObserver) {
		Logger.writeln(this, "removeObserver: " + aCommandObserver.toString());
		observers.remove(aCommandObserver);
	}

	@Override
	public boolean start() {		
		// start listen events from display		
		Logger.writeln(this, "start");
		return true;
	}

	@Override
	public void stop() {
		// stop listen events from display
		Logger.writeln(this, "stop");		
	}

	@Override
	public String toString() {
		return "DisplayImpl";
	}

	@Override
	public void sendCommand(UserCommand command) {
		// do nothing to send datagram		
	}
}
