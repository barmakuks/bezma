package com.fairbg.core;

import android.util.Log;

import com.fairbg.core.commands.Command;
import com.fairbg.core.commands.CommanderImpl;
import com.fairbg.core.commands.ICommandListener;

public class UsbCommander implements com.fairbg.core.commands.ICommander {
	CommanderImpl _commander = null;	
	@Override
	public void addListener(ICommandListener listener) {
		if(_commander != null)
			_commander.addListener(listener);		
	}

	@Override
	public void sendCommand(Command command) {
		if(_commander != null)
			_commander.sendCommand(command);		
	}

	public UsbCommander(ICommandListener listener){
		_commander = new CommanderImpl(this);
		_commander.addListener(listener);
	}

	private Thread _thread = new Thread(new Runnable() {
		
		@Override
		public void run() {
			while(true){
				sendCommand(new Command());
				try {
					Log.i("USB_COMMANDER", "Command sent");
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
	});
	
	@Override
	public void start() {
		_thread.start();
	}

	@Override
	public void stop() {
		_thread.stop();		
	} 
}
