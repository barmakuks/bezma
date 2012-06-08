package com.fairbg.bezma.qui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.fairbg.bezma.R;
import com.fairbg.bezma.log.BezmaLog;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SelectDeviceActivity extends Activity {
	private static final int REQUEST_ENABLE_BT = 1;

	private BluetoothAdapter mBluetoothAdapter = null;
	
	private ArrayAdapter<String> mArrayAdapter = null;
	private List<String> _device_mac_list = new ArrayList<String>();
	
	HashMap<String, BluetoothDevice> _devices = new HashMap<String, BluetoothDevice>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_device_view);
		initComponents();
		initBluetooth();
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	/**
	 * Recieves messages from bluetooth adapter when serach devises
	 */
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Add the name and address to an array adapter to show in a ListView
				addDeviceToList(device);
			}
		}
	};

	private void initBluetooth() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			setStatus("Device does not support Bluetooth");
		} else {
			if (!mBluetoothAdapter.isEnabled()) {
				// start intent to switch on bluetooth
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			} else {
				// start search active devices
				searchBtDevices();//updateBtDevicesList();
			}
			// Register the BroadcastReceiver
			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
		}
	}

	Button btnUpdateList = null;
	TextView txtStatus = null;

	private void initComponents() {
		btnUpdateList = (Button) findViewById(R.id.btn_update_devices);
		if(btnUpdateList != null){
			btnUpdateList.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					searchBtDevices();
				}
			});			
		}
		//txtStatus = (TextView) findViewById(R.id.txt_status);
		mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, _device_mac_list);
		ListView lstDevices = (ListView) findViewById(R.id.lst_devices);
		lstDevices.setAdapter(mArrayAdapter);
		lstDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				BezmaLog.i("ITEM SELECTED", Integer.toString(position));
				String mac = (String) adapter.getItemAtPosition(position);
				if(mac != null){
					BluetoothDevice device = _devices.get(mac);
					if(device != null){
						if(mBluetoothAdapter.isDiscovering())
							mBluetoothAdapter.cancelDiscovery();
						returnDeviceInfo(mac);
					}					
				}				
			}
		});
	}

	protected void returnDeviceInfo(String mac) {
			try {
				DeviceInfo info = new DeviceInfo();
				info.bluetoothDeviceMAC = mac;
				getIntent().putExtra(DeviceInfo.class.getCanonicalName(), info);
				setResult(RESULT_OK, getIntent());
				finish();
			} catch (NumberFormatException ex) {

			}

	}

	private void setStatus(String status) {
		if (txtStatus != null)
			txtStatus.setText(status);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == RESULT_OK) {
				searchBtDevices();//updateBtDevicesList();
			}
			else{
				setStatus("Bluetooth is not active");
			}
		} else
			super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Выполняет поиск устройств в радиусе действия
	 */
	protected void searchBtDevices() {
		clearDeviceList();
		mBluetoothAdapter.startDiscovery();
	}

	/**
	 * Получает список найденых устройств без активного поиска
	 */
	/*private void updateBtDevicesList() {
		if (mBluetoothAdapter == null)
			return;
		if (btnUpdateList != null)
			btnUpdateList.setEnabled(true);
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
				addDeviceToList(device);
			}
		}
	}*/

	private void addDeviceToList(BluetoothDevice device) {
		String mac = device.getAddress();
		if (!_devices.containsKey(mac)) {
			mArrayAdapter.add(mac);
			_devices.put(mac, device);
		}
	}

	private void clearDeviceList() {
		mArrayAdapter.clear();
		_devices.clear();
	}
}