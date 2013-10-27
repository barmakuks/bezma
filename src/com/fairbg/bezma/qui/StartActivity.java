package com.fairbg.bezma.qui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fairbg.bezma.R;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.from_net.enums.RollTypes;
import com.fairbg.bezma.log.BezmaLog;
import com.fairbg.bezma.store.DBAdapter;
import com.fairbg.bezma.version3.ConfigurationVer3;

public class StartActivity extends Activity
{
    static final int REQUEST_MATCH_PARAMETERS = 100;
    static final int REQUEST_BLUETOOTH_DEVICE = 101;

    /** Вызывается, когда оператор создается впервые. */
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

    /**
     * Захватывает кнопку из макета и регистрирует приемник OnClick
     * 
     * @param id
     *            идентификатор кнопки
     */
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
            // intent.setData(Uri.parse("content://uri_to_my_object"));
            startActivity(intent);
        }
        catch (Exception ex)
        {
            Log.e("ERROR", ex.getMessage());
        }
    }

    MatchParameters getMatchParameters()
    {
        MatchParameters params = new MatchParameters();
        DBAdapter adapter = new DBAdapter(this);
        adapter.open();
        params.Player1 = adapter.getString("PLAYER1", params.Player1);
        params.Player2 = adapter.getString("PLAYER2", params.Player2);
        params.CalculateRolls = adapter.getBoolean("CALC_ROLLS", params.CalculateRolls);
        params.Crawford = adapter.getBoolean("CRAWFORD", params.Crawford);
        params.ExactRolls = adapter.getBoolean("EXACT_ROLLS", params.ExactRolls);
        params.GameType = adapter.getInteger("GAME_TYPE", params.GameType);
        params.MatchLength = adapter.getInteger("MATCH_LEN", params.MatchLength);
        params.MatchLimit = adapter.getInteger("MATCH_LIMIT", params.MatchLimit);
        params.MoveLimit = adapter.getInteger("MOVE_LIMIT", params.MoveLimit);
        params.RollType = RollTypes.valueOf(adapter.getString("ROLL_TYPE", params.RollType.name()));
        params.UseTimer = adapter.getBoolean("USE_TIMER", params.UseTimer);
        adapter.close();
        return params;
    }

    protected void startMatchParamsActivity()
    {
        try
        {
            Intent intent = new Intent(this, MatchParametersActivity.class);
            intent.putExtra(MatchParameters.class.getCanonicalName(), getMatchParameters());
            startActivityForResult(intent, REQUEST_MATCH_PARAMETERS);
        }
        catch (Exception ex)
        {
            Log.e("ERROR", ex.getMessage());
        }
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
                startPlayActivity(null);
                // startSelectDeviceActivity();
                break;
            case REQUEST_BLUETOOTH_DEVICE:
                startPlayActivity((DeviceInfo) data.getSerializableExtra(DeviceInfo.class.getCanonicalName()));
            default:
                // unknown data source
                break;
            }
        }
        else
        {
            // operation is canceled by user
        }
    }

    private void startPlayActivity(DeviceInfo deviceInfo)
    {
        Intent intent = new Intent(this, PlayActivity.class);
        ConfigurationVer3 c3 = new ConfigurationVer3();
        c3.setMatchParameters(getMatchParameters());
        // c3.deviceMAC = deviceInfo.bluetoothDeviceMAC;
        intent.putExtra(ConfigurationVer3.class.getCanonicalName(), c3);
        startActivity(intent);
    }

    private void startSelectDeviceActivity()
    {
        Intent intent = new Intent(this, SelectDeviceActivity.class);
        startActivityForResult(intent, REQUEST_BLUETOOTH_DEVICE);
    }

    private void saveMatchParameters(MatchParameters params)
    {
        // write parameters into Database
        DBAdapter adapter = new DBAdapter(this);
        adapter.open();
        adapter.putValue("PLAYER1", params.Player1);
        adapter.putValue("PLAYER2", params.Player2);
        adapter.putValue("CALC_ROLLS", params.CalculateRolls);
        adapter.putValue("CRAWFORD", params.Crawford);
        adapter.putValue("EXACT_ROLLS", params.ExactRolls);
        adapter.putValue("GAME_TYPE", params.GameType);
        adapter.putValue("MATCH_LEN", params.MatchLength);
        adapter.putValue("MATCH_LIMIT", params.MatchLimit);
        adapter.putValue("MOVE_LIMIT", params.MoveLimit);
        adapter.putValue("ROLL_TYPE", params.RollType.name());
        adapter.putValue("USE_TIMER", params.UseTimer);
        adapter.close();
    }
}
