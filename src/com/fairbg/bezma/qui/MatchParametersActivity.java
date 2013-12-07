package com.fairbg.bezma.qui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView.BufferType;

import com.fairbg.bezma.R;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.MatchParameters.GameType;
import com.fairbg.bezma.core.MatchParameters.MatchWinConditions;
import com.fairbg.bezma.core.MatchParameters.RollTypes;

public class MatchParametersActivity extends Activity
{
	private EditText	edtFixedPoints;
	private EditText	edtFixedGames;
	private Spinner	 lstGameType;
	private ArrayAdapter<GameType> m_gameTypeAdapter;
	
	private EditText	edtMatchLimit;
	private EditText	edtMoveLimit;
	private EditText	edtPlayer1;
	private EditText	edtPlayer2;
	private CheckBox	cbxUseCrawford;
	private CheckBox	cbxCalculateRolls;
	private CheckBox	cbxUseTimer;
	private RadioButton rbxApproximately;
	private RadioButton rbxComputerRolls;
	private RadioButton rbxExactly;
	private RadioButton rbxFixedGames;
	private RadioButton rbxFixedPoints;
	private RadioButton rbxPlayersRools;
	private View		layoutCalculateDice;
	private View		layoutCalculateDicePrecision;
	private View		layoutTimerLimits;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match_parameters_view);
		((Button) findViewById(R.id.btn_start)).setOnClickListener(click_listener);
		((Button) findViewById(R.id.btn_cancel)).setOnClickListener(click_listener);

		edtFixedPoints = (EditText) findViewById(R.id.edtFixedPoints);
		edtFixedGames = (EditText) findViewById(R.id.edtFixedGames);
		edtMatchLimit = (EditText) findViewById(R.id.edtMatchLimit);
		edtMoveLimit = (EditText) findViewById(R.id.edtMoveLimit);
		edtPlayer1 = (EditText) findViewById(R.id.edtPlayer1);
		edtPlayer2 = (EditText) findViewById(R.id.edtPlayer2);
		cbxUseCrawford = (CheckBox) findViewById(R.id.cbxUseCrawford);
		cbxCalculateRolls = (CheckBox) findViewById(R.id.cbxCalculateRolls);
		cbxCalculateRolls.setOnCheckedChangeListener(check_listener);
		cbxUseTimer = (CheckBox) findViewById(R.id.cbxUseTimer);
		cbxUseTimer.setOnCheckedChangeListener(check_listener);

		lstGameType = (Spinner) findViewById(R.id.lstGameType);
		m_gameTypeAdapter = new ArrayAdapter<GameType>(this, android.R.layout.simple_spinner_item, GameType.values());
		lstGameType.setAdapter(m_gameTypeAdapter);
		
		rbxApproximately = ((RadioButton) findViewById(R.id.rbxApproximately));
		rbxApproximately.setOnCheckedChangeListener(check_listener);
		rbxComputerRolls = ((RadioButton) findViewById(R.id.rbxComputerRolls));
		rbxComputerRolls.setOnCheckedChangeListener(check_listener);
		rbxExactly = ((RadioButton) findViewById(R.id.rbxExactly));
		rbxExactly.setOnCheckedChangeListener(check_listener);
		rbxFixedGames = ((RadioButton) findViewById(R.id.rbxFixedGames));
		rbxFixedGames.setOnCheckedChangeListener(check_listener);
		rbxFixedPoints = ((RadioButton) findViewById(R.id.rbxFixedPoints));
		rbxFixedPoints.setOnCheckedChangeListener(check_listener);
		rbxPlayersRools = ((RadioButton) findViewById(R.id.rbxPlayersRools));
		rbxPlayersRools.setOnCheckedChangeListener(check_listener);

		layoutCalculateDice = findViewById(R.id.calculate_dice_box);
		layoutCalculateDicePrecision = findViewById(R.id.calcalute_dice_precision_box);
		layoutTimerLimits = findViewById(R.id.timer_limits_box);

		setData();

		updateVisibility();
	}

	private void setData()
	{
		Bundle extras = getIntent().getExtras();
		MatchParameters params = (MatchParameters) extras.getSerializable(MatchParameters.class.getCanonicalName());
		edtPlayer1.setText(params.bPlayerName, BufferType.EDITABLE);
		edtPlayer2.setText(params.wPlayerName, BufferType.EDITABLE);

		lstGameType.setSelection(m_gameTypeAdapter.getPosition(params.gameType), false);

		rbxFixedGames.setChecked(params.winConditions == MatchWinConditions.FixedGames);
		rbxFixedPoints.setChecked(!rbxFixedGames.isChecked());
		edtFixedGames.setText(Integer.toString(params.matchLength));
		edtFixedPoints.setText(Integer.toString(params.matchLength));

		cbxUseCrawford.setChecked(params.isCrawford);
		rbxComputerRolls.setChecked(params.rollType == RollTypes.Generate);
		rbxPlayersRools.setChecked(params.rollType == RollTypes.Manual);
		cbxCalculateRolls.setChecked(params.calculateRolls);
		rbxExactly.setChecked(params.exactRolls);
		rbxApproximately.setChecked(!params.exactRolls);

		cbxUseTimer.setChecked(params.useTimer);
		edtMatchLimit.setText(Integer.toString(params.matchTimeLimit));
		edtMoveLimit.setText(Integer.toString(params.moveTimeLimit));
	}
	
	private OnClickListener		 click_listener = new OnClickListener()
												   {
													   @Override
													   public void onClick(View v)
													   {
														   switch (v.getId())
														   {
														   case R.id.btn_cancel:
															   setResult(RESULT_CANCELED);
															   finish();
															   break;
														   case R.id.btn_start:
															   returnData();
															   break;
														   }
													   }
												   };

	private OnCheckedChangeListener check_listener = new OnCheckedChangeListener()
												   {

													   @Override
													   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
													   {
														   updateVisibility();
													   }
												   };

	private void updateVisibility()
	{
		cbxUseCrawford.setVisibility(rbxFixedPoints.isChecked() ? CheckBox.VISIBLE : CheckBox.GONE);
		edtFixedGames.setEnabled(rbxFixedGames.isChecked());
		edtFixedPoints.setEnabled(rbxFixedPoints.isChecked());

		layoutCalculateDice.setVisibility(rbxPlayersRools.isChecked() ? View.VISIBLE : View.GONE);
		layoutCalculateDicePrecision.setVisibility(cbxCalculateRolls.isChecked() ? View.VISIBLE : View.GONE);
		layoutTimerLimits.setVisibility(cbxUseTimer.isChecked() ? View.VISIBLE : View.GONE);
	}

	protected void returnData()
	{
		try
		{
			MatchParameters params = new MatchParameters();
			params.bPlayerName = edtPlayer1.getText().toString();
			params.wPlayerName = edtPlayer2.getText().toString();

			params.gameType = m_gameTypeAdapter.getItem(lstGameType.getSelectedItemPosition());
			if (rbxFixedGames.isChecked())
			{
				params.winConditions = MatchWinConditions.FixedGames;
				params.matchLength = Integer.parseInt(edtFixedGames.getText().toString());
			} else
			{
				params.winConditions = MatchWinConditions.Scores;
				params.matchLength = Integer.parseInt(edtFixedPoints.getText().toString());
			}
			params.isCrawford = cbxUseCrawford.isChecked();

			if (rbxComputerRolls.isChecked())
			{
				params.rollType = RollTypes.Generate;
			} else
			{
				params.rollType = RollTypes.Manual;
				params.calculateRolls = cbxCalculateRolls.isChecked();
				params.exactRolls = rbxExactly.isChecked();
			}

			params.useTimer = cbxUseTimer.isChecked();
			if (params.useTimer)
			{
				params.matchTimeLimit = Integer.parseInt(edtMatchLimit.getText().toString());
				params.moveTimeLimit = Integer.parseInt(edtMoveLimit.getText().toString());
			}
			getIntent().putExtra(MatchParameters.class.getCanonicalName(), params);
			setResult(RESULT_OK, getIntent());
			finish();
		} catch (NumberFormatException ex)
		{

		}

	}
}
