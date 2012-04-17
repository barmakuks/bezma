package com.fairbg.bezma.qui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.fairbg.bezma.core.from_net.enums.RollTypes;
import com.fairbg.bezma.core.from_net.enums.MatchWinConditions;

public class MatchParametersActivity extends Activity {

	private EditText edtFixedPoints;
	private EditText edtFixedGames;
	private Spinner lstGameType;
	private EditText edtMatchLimit;
	private EditText edtMoveLimit;
	private EditText edtPlayer1;
	private EditText edtPlayer2;
	private CheckBox cbxUseCrawford;
	private CheckBox cbxCalculateRolls;
	private CheckBox cbxUseTimer;
	private RadioButton rbxApproximately;
	private RadioButton rbxComputerRolls;
	private RadioButton rbxExactly;
	private RadioButton rbxFixedGames;
	private RadioButton rbxFixedPoints;
	private RadioButton rbxPlayersRools;
	private View layoutCalculateDice;
	private View layoutCalculateDicePrecision;
	private View layoutTimerLimits;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match_parameters_view);
		((Button) findViewById(R.id.btn_start))
				.setOnClickListener(click_listener);
		((Button) findViewById(R.id.btn_cancel))
				.setOnClickListener(click_listener);

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

		// Получаем переданные параметры
		Bundle extras = getIntent().getExtras();
		MatchParameters params = (MatchParameters) extras
				.getSerializable(MatchParameters.class.getCanonicalName());
		edtPlayer1.setText(params.Player1, BufferType.EDITABLE);
		edtPlayer2.setText(params.Player2, BufferType.EDITABLE);

		lstGameType.setSelection(params.GameType, false);

		rbxFixedGames
				.setChecked(params.MatchType == MatchWinConditions.FixedGames);
		rbxFixedPoints.setChecked(!rbxFixedGames.isChecked());
		edtFixedGames.setText(Integer.toString(params.MatchLength));
		edtFixedPoints.setText(Integer.toString(params.MatchLength));

		cbxUseCrawford.setChecked(params.Crawford);
		rbxComputerRolls.setChecked(params.RollType == RollTypes.Generate);
		rbxPlayersRools.setChecked(params.RollType == RollTypes.Manual);
		cbxCalculateRolls.setChecked(params.CalculateRolls);
		rbxExactly.setChecked(params.ExactRolls);
		rbxApproximately.setChecked(!params.ExactRolls);

		cbxUseTimer.setChecked(params.UseTimer);
		edtMatchLimit.setText(Integer.toString(params.MatchLimit));
		edtMoveLimit.setText(Integer.toString(params.MoveLimit));

		updateVisibility();
	}

	private OnClickListener click_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
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

	private OnCheckedChangeListener check_listener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			updateVisibility();
		}
	};

	private void updateVisibility() {
		cbxUseCrawford
				.setVisibility(rbxFixedPoints.isChecked() ? CheckBox.VISIBLE
						: CheckBox.GONE);
		edtFixedGames.setEnabled(rbxFixedGames.isChecked());
		edtFixedPoints.setEnabled(rbxFixedPoints.isChecked());

		layoutCalculateDice
				.setVisibility(rbxPlayersRools.isChecked() ? View.VISIBLE
						: View.GONE);
		layoutCalculateDicePrecision.setVisibility(cbxCalculateRolls
				.isChecked() ? View.VISIBLE : View.GONE);
		layoutTimerLimits.setVisibility(cbxUseTimer.isChecked() ? View.VISIBLE
				: View.GONE);
	}

	protected void returnData() {
		try {
			MatchParameters params = new MatchParameters();
			params.Player1 = edtPlayer1.getText().toString();
			params.Player2 = edtPlayer2.getText().toString();
			params.GameType = lstGameType.getSelectedItemPosition();
			if (rbxFixedGames.isChecked()) {
				params.MatchType = MatchWinConditions.FixedGames;
				params.MatchLength = Integer.parseInt(edtFixedGames.getText().toString());
			} else {
				params.MatchType = MatchWinConditions.Scores;
				params.MatchLength = Integer.parseInt(edtFixedPoints.getText().toString());
			}
			params.Crawford = cbxUseCrawford.isChecked();

			if (rbxComputerRolls.isChecked()) {
				params.RollType = RollTypes.Generate;
			} else {
				params.RollType = RollTypes.Manual;
				params.CalculateRolls = cbxCalculateRolls.isChecked();
				params.ExactRolls = rbxExactly.isChecked();
			}

			params.UseTimer = cbxUseTimer.isChecked();
			if (params.UseTimer) {
				params.MatchLimit = Integer.parseInt(edtMatchLimit.getText()
						.toString());
				params.MoveLimit = Integer.parseInt(edtMoveLimit.getText()
						.toString());
			}
			getIntent().putExtra(MatchParameters.class.getCanonicalName(),
					params);
			setResult(RESULT_OK, getIntent());
			finish();
		} catch (NumberFormatException ex) {

		}

	}
}
