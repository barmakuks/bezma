package com.fairbg.bezma.version3;

import java.io.IOException;
import java.util.ArrayList;

import android.bluetooth.BluetoothDevice;

import com.fairbg.bezma.bluetooth.Datagram;
import com.fairbg.bezma.bluetooth.DatagramConverter;
import com.fairbg.bezma.bluetooth.IDatagramObserver;
import com.fairbg.bezma.bluetooth.android.BluetoothBoardDevice;
import com.fairbg.bezma.communication.IModelView;
import com.fairbg.bezma.communication.Logger;
import com.fairbg.bezma.communication.commands.ICommandObserver;
import com.fairbg.bezma.communication.commands.UserCommand;
import com.fairbg.bezma.core.model.ModelState;

/**Имплементация устройства 3-го покоения со связью через bluetooth*/
public class DeviceImpl implements IModelView, IDatagramObserver {
	
	/**Связь через bluetooth*/
	private BluetoothBoardDevice m_BoardDevice = null;
	
	private ArrayList<ICommandObserver> observers = new ArrayList<ICommandObserver>(); 
	
	public DeviceImpl(BluetoothDevice bluetoothDevice)
	{
		m_BoardDevice = new BluetoothBoardDevice(bluetoothDevice);
		m_BoardDevice.addObserver(this);
	}
	
	@Override
	public void setModelState(ModelState modelState) {
		Logger.writeln(this, "setModelState");
		Datagram datagram = null;
		m_BoardDevice.sendDatagram(datagram);
	}

	@Override
	public void notifyObservers(UserCommand userCommand) {
		Logger.writeln(this, "notifyObservers");		
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
		Logger.writeln(this, "start");		
		try {
			// Start listen bluetooth port
			return m_BoardDevice.startListen();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void stop() {
		Logger.writeln(this, "stop");
		// Stop listen bluetooth
		m_BoardDevice.stopListen();
	}

	@Override
	public String toString() {		
		return "BoardImpl";
	}

	@Override
	public void handleEvent(Datagram datagram) {
		UserCommand command = DatagramConverter.datagramToCommand(datagram);
		notifyObservers(command);
	}

	@Override
	public void sendCommand(UserCommand command) {
		Datagram datagram = DatagramConverter.commandToDatagram(command);
		m_BoardDevice.sendDatagram(datagram);		
	}
}
