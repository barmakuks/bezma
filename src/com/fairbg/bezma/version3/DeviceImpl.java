package com.fairbg.bezma.version3;

import java.io.IOException;
import java.util.ArrayList;

import android.bluetooth.BluetoothDevice;

import com.fairbg.bezma.bluetooth.Datagram;
import com.fairbg.bezma.bluetooth.DatagramConverter;
import com.fairbg.bezma.bluetooth.IDatagramObserver;
import com.fairbg.bezma.bluetooth.android.BluetoothBoardDevice;
import com.fairbg.bezma.communication.IModelView;
import com.fairbg.bezma.communication.commands.ICommandObserver;
import com.fairbg.bezma.communication.commands.CommunicationCommand;
import com.fairbg.bezma.core.model.IMove;
import com.fairbg.bezma.core.model.ModelSituation;

/** Имплементация устройства 3-го покоения со связью через bluetooth */
public class DeviceImpl implements IModelView, IDatagramObserver
{
    /** Связь через bluetooth */
    private BluetoothBoardDevice m_BoardDevice = null;

    private ArrayList<ICommandObserver> observers = new ArrayList<ICommandObserver>();

    public DeviceImpl(BluetoothDevice bluetoothDevice)
    {
	m_BoardDevice = new BluetoothBoardDevice(bluetoothDevice);
	m_BoardDevice.addObserver(this);
    }

    @Override
    public void setModelState(ModelSituation modelState)
    {
	Datagram datagram = null;
	m_BoardDevice.sendDatagram(datagram);
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
	try
	{
	    // Start listen bluetooth port
	    return m_BoardDevice.startListen();
	} catch (IOException e)
	{
	    e.printStackTrace();
	    return false;
	}
    }

    @Override
    public void stop()
    {
	// Stop listen bluetooth
	m_BoardDevice.stopListen();
    }

    @Override
    public String toString()
    {
	return "BoardImpl";
    }

    @Override
    public void handleEvent(Datagram datagram)
    {
	CommunicationCommand command = DatagramConverter
		.datagramToCommand(datagram);
	notifyObservers(command);
    }

    @Override
    public void sendCommand(CommunicationCommand command)
    {
	Datagram datagram = DatagramConverter.commandToDatagram(command);
	m_BoardDevice.sendDatagram(datagram);
    }

    @Override
    public void appendMove(IMove move)
    {
	// TODO Auto-generated method stub	
    }
}
