package com.fairbg.bezma.qui;

import java.io.File;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fairbg.bezma.R;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.MatchParameters.RollTypes;
import com.fairbg.bezma.core.MatchParameters.GameType;
import com.fairbg.bezma.log.BezmaLog;
import com.fairbg.bezma.store.DBAdapter;
import com.fairbg.bezma.version3.ConfigurationVer3;
import com.fairbg.bezma.version3.UserSettings;

public class StartActivity extends Activity
{
	static final int REQUEST_MATCH_PARAMETERS = 100;
	static final int REQUEST_BLUETOOTH_DEVICE = 101;
	static final int REQUEST_SETTINGS		  = 102;
	static final int REQUEST_ENABLE_BT        = 103;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		BezmaLog.allowTag("BEZMA");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_view);
		registerButton(R.id.btn_match_new);
		registerButton(R.id.btn_match_continue);
		registerButton(R.id.btn_match_store);
		registerButton(R.id.btn_settings);
		registerButton(R.id.btn_exit);
		
	}
	
	@Override
	protected void onStart()
	{
	    super.onStart();

	    ConfigurationVer3 c3 = new ConfigurationVer3();
        File file = new File(c3.getUnfinishedMatchPath());
        // Get button from mock up
        Button btn = (Button) findViewById(R.id.btn_match_continue);
        // Set callback for OnClick
        btn.setEnabled(file.exists());
        file = null;
	}

	private void registerButton(int id)
	{
		// Get button from mock up
		Button btn = (Button) findViewById(id);
		// Set callback for OnClick
		btn.setOnClickListener(mAddListener);
	}

	// Create unnamed implementation of OnClickListener
	private OnClickListener mAddListener = new OnClickListener()
										 {
											 public void onClick(View v)
											 {
												 switch (v.getId())
												 {
												 case R.id.btn_match_new:
													 startMatchParamsActivity();
													 break;
												 case R.id.btn_match_continue:
												     continueMatch();
													 break;
												 case R.id.btn_match_store:
													 break;
												 case R.id.btn_settings:
													 showSettings();
													 break;
												 case R.id.btn_exit:
													 finish();
													 break;
												 }
											 }
										 };

	protected void showSettings()
	{
		try
		{
			Intent intent = new Intent(this, SettingsActivity.class);
			intent.putExtra(UserSettings.class.getCanonicalName(), getUserSettings());
			startActivityForResult(intent, REQUEST_SETTINGS);
		} catch (Exception ex)
		{
			Log.e("ERROR", ex.getMessage());
		}
	}

	protected void startMatchParamsActivity()
	{
		try
		{
			Intent intent = new Intent(this, MatchParametersActivity.class);
			intent.putExtra(MatchParameters.class.getCanonicalName(), getMatchParameters());
			startActivityForResult(intent, REQUEST_MATCH_PARAMETERS);
		} catch (Exception ex)
		{
			Log.e("ERROR", ex.getMessage());
		}
	}

	protected void continueMatch()
    {
        Intent intent = new Intent(this, PlayActivity.class);
        ConfigurationVer3 c3 = new ConfigurationVer3();
        
        c3.configureMatchParameters(getMatchParameters());
        c3.setUserSettings(getUserSettings());
        
        intent.putExtra(ConfigurationVer3.class.getCanonicalName(), c3);
        startActivity(intent);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("RESULT requestCode", Integer.toString(requestCode));
		Log.i("RESULT resultCode", Integer.toString(resultCode));
		if (resultCode == RESULT_OK)
		{
			switch (requestCode)
			{
                case REQUEST_MATCH_PARAMETERS:
                    saveMatchParameters((MatchParameters) data.getSerializableExtra(MatchParameters.class.getCanonicalName()));
                    enableBluetooth();
				    // startSelectDeviceActivity();
				    break;
//			case REQUEST_BLUETOOTH_DEVICE:
//				startPlayActivity((DeviceInfo) data.getSerializableExtra(DeviceInfo.class.getCanonicalName()));
//              break;
    			case REQUEST_SETTINGS:
	    			setUserSettings((UserSettings) data.getSerializableExtra(UserSettings.class.getCanonicalName()));
		    		break;
                case REQUEST_ENABLE_BT:
                    startPlayActivity(null);
                    break;

			default:
				// unknown data source
				break;
			}
		} else
		{
			// operation is canceled by user
		}
	}

	private void enableBluetooth()
	{
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter != null)
		{
            if (!mBluetoothAdapter.isEnabled())
            {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else
            {
                startPlayActivity(null);
            }
		}
        else
        {
            // Device does not support Bluetooth
        }

	}

	private void startPlayActivity(DeviceInfo deviceInfo)
	{
		Intent intent = new Intent(this, PlayActivity.class);
		ConfigurationVer3 c3 = new ConfigurationVer3();
		
		File file = new File(c3.getUnfinishedMatchPath());
		if (file.exists())
		{
	        file.delete();
		}
		file = null;
		
		c3.configureMatchParameters(getMatchParameters());
		c3.setUserSettings(getUserSettings());
		
		intent.putExtra(ConfigurationVer3.class.getCanonicalName(), c3);
		startActivity(intent);
	}

	@SuppressWarnings("unused")
	private void startSelectDeviceActivity()
	{
		Intent intent = new Intent(this, SelectDeviceActivity.class);
		startActivityForResult(intent, REQUEST_BLUETOOTH_DEVICE);
	}

	private UserSettings getUserSettings()
	{
		UserSettings settings = new UserSettings();

		DBAdapter adapter = new DBAdapter(this);
		adapter.open();
		settings.boardMAC = (adapter.getString("BOARD_MAC", settings.boardMAC));
		settings.userName = (adapter.getString("USER_NAME", settings.userName));
		settings.userEMail = (adapter.getString("USER_EMAIL", settings.userEMail));
		adapter.close();

		return settings;
	}

	private void setUserSettings(UserSettings settings)
	{
		DBAdapter adapter = new DBAdapter(this);
		adapter.open();
		adapter.putValue("BOARD_MAC", settings.boardMAC);
		adapter.putValue("USER_NAME", settings.userName);
		adapter.putValue("USER_EMAIL", settings.userEMail);
		adapter.close();
	}

	private MatchParameters getMatchParameters()
	{
		MatchParameters params = new MatchParameters();
		DBAdapter adapter = new DBAdapter(this);
		adapter.open();
		params.redPlayerName = adapter.getString("PLAYER1", params.redPlayerName);
		params.silverPlayerName = adapter.getString("PLAYER2", params.silverPlayerName);
		params.calculateRolls = adapter.getBoolean("CALC_ROLLS", params.calculateRolls);
		params.useCrawfordRule = adapter.getBoolean("CRAWFORD", params.useCrawfordRule);
		params.exactRolls = adapter.getBoolean("EXACT_ROLLS", params.exactRolls);
		params.gameType = GameType.valueOf(adapter.getString("GAME_TYPE_NAME", params.gameType.name()));
		params.matchLength = adapter.getInteger("MATCH_LEN", params.matchLength);
		params.matchTimeLimit = adapter.getInteger("MATCH_LIMIT", params.matchTimeLimit);
		params.moveTimeLimit = adapter.getInteger("MOVE_LIMIT", params.moveTimeLimit);
		params.rollType = RollTypes.valueOf(adapter.getString("ROLL_TYPE", params.rollType.name()));
		params.useTimer = adapter.getBoolean("USE_TIMER", params.useTimer);
		adapter.close();
		return params;
	}

	private void saveMatchParameters(MatchParameters params)
	{
		// write parameters into Database
		DBAdapter adapter = new DBAdapter(this);
		adapter.open();
		adapter.putValue("PLAYER1", params.redPlayerName);
		adapter.putValue("PLAYER2", params.silverPlayerName);
		adapter.putValue("CALC_ROLLS", params.calculateRolls);
		adapter.putValue("CRAWFORD", params.useCrawfordRule);
		adapter.putValue("EXACT_ROLLS", params.exactRolls);
		adapter.putValue("GAME_TYPE_NAME", params.gameType.name());
		adapter.putValue("MATCH_LEN", params.matchLength);
		adapter.putValue("MATCH_LIMIT", params.matchTimeLimit);
		adapter.putValue("MOVE_LIMIT", params.moveTimeLimit);
		adapter.putValue("ROLL_TYPE", params.rollType.name());
		adapter.putValue("USE_TIMER", params.useTimer);
		adapter.close();
	}
}
