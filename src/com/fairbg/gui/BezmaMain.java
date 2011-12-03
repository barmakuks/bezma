package com.fairbg.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fairbg.R;
import com.fairbg.core.Corel;
import com.fairbg.core.IBoardWindow;
import com.fairbg.core.MatchParameters;
import com.fairbg.store.DBAdapter;

public class BezmaMain extends Activity {
	static final int REQUEST_MATCH_PARAMETERS = 100; 

	/** Вызывается, когда оператор создается впервые. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
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
	private void registerButton(int id) {
		// Захватывает кнопку из макета
		Button btn = (Button) findViewById(id);
		// Регистрирует приемник OnClick
		btn.setOnClickListener(mAddListener);
	}

	// Создает анонимную реализацию OnClickListener
	private OnClickListener mAddListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_match_new:
				newMatch();
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

	protected void showSettings() {
		try{			
			Intent intent = new Intent(this, Settings.class);
			// intent.setData(Uri.parse("content://uri_to_my_object"));
			startActivity(intent);			
		}catch(Exception ex){
			Log.e("ERROR", ex.getMessage());
		}
	}

	protected void newMatch() {
		try{
			Intent intent = new Intent(this, MatchParametersActivity.class);
			com.fairbg.core.MatchParameters params = new com.fairbg.core.MatchParameters();
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
			params.RollType = adapter.getInteger("ROLL_TYPE", params.RollType);
			params.UseTimer= adapter.getBoolean("USE_TIMER", params.UseTimer);
			adapter.close();
			params.GameType = MatchParameters.GULBARA;
			intent.putExtra(MatchParameters.class.getCanonicalName(), params);
			// intent.setData(Uri.parse("content://uri_to_my_object"));
			startActivityForResult(intent, REQUEST_MATCH_PARAMETERS);			
		}catch(Exception ex){
			Log.e("ERROR", ex.getMessage());
		}
	}
	
	private Corel _corel = null;
	private BoardWindow _board_window = null;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("RESULT requestCode", Integer.toString(requestCode));
		Log.i("RESULT resultCode", Integer.toString(resultCode));
		if(resultCode == RESULT_OK){
			if(requestCode == REQUEST_MATCH_PARAMETERS){
				MatchParameters params = (MatchParameters) data.getSerializableExtra(MatchParameters.class.getCanonicalName());
				// здесь сохраняем в БД параметры и начинаем матч
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
				adapter.putValue("ROLL_TYPE", params.RollType);
				adapter.putValue("USE_TIMER", params.UseTimer);				
				adapter.close();
				Intent intent = new Intent(this, BoardWindow.class);
				startActivity(intent);
				Corel.corel.startListenUsb();
			}
			else{
				// неизвестно откуда пришли данные
			}
		}
		else{
			// операция отменена пользователем
		}
	}
}
