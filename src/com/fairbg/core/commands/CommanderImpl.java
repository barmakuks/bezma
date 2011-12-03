package com.fairbg.core.commands;

import java.util.ArrayList;


public class CommanderImpl implements ICommander{
	private ICommander _owner;
	private ArrayList<ICommandListener> _listeners = new ArrayList<ICommandListener>();
	
	@Override
	public void addListener(ICommandListener listener) {
		if(!_listeners.contains(listener))
			_listeners.add(listener);		
	}

	@Override
	public void sendCommand(UserCommand command) {
		for(ICommandListener listener : _listeners){
			if(listener instanceof ICommandListener){
				listener.onCommand(_owner, command);
			}
		}
	}
	public CommanderImpl(ICommander owner){
		_owner = owner;
	}

	/** ничего не делает */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	/** ничего не делает */
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	} 
}
