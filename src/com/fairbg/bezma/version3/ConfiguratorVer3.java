package com.fairbg.bezma.version3;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.fairbg.bezma.communication.IBroadcast;
import com.fairbg.bezma.communication.ICommunicator;
import com.fairbg.bezma.core.Configuration;
import com.fairbg.bezma.core.IConfigurator;
import com.fairbg.bezma.core.WrongConfigurationException;
import com.fairbg.bezma.store.IDatabase;

/**Реализация конфигуратора для 3- версии BEZMA
 * Создает комуникатор для связи с устройством 3-его поколения через Bluetooth
 * БД и Транслятор через интернет не создаются 
 * */
public class ConfiguratorVer3 implements IConfigurator{

	@Override
	public ICommunicator createCommunicator(Configuration configuration) throws WrongConfigurationException {
		if (configuration instanceof ConfigurationVer3)
		{
			ConfigurationVer3 c3 = (ConfigurationVer3) configuration;
			CommunicatorImpl communicator = new CommunicatorImpl();
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			
			UserSettings settings = c3.getUserSettings();
			
			BluetoothDevice device = adapter.getRemoteDevice(settings.boardMAC);
			communicator.addView(new DeviceImpl(device));

			return communicator;			
		}
		throw new WrongConfigurationException();
	}
	
	@Override
	public IDatabase createDatabase(Configuration configuration) throws WrongConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBroadcast createBroadcastModule(Configuration configuration) throws WrongConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}


	
}
