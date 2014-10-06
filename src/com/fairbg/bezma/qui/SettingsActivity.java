package com.fairbg.bezma.qui;

import com.fairbg.bezma.R;
import com.fairbg.bezma.version3.UserSettings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity
{
	private Spinner  lstBoardMAC = null;
	private EditText edtUserName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		registerButton(R.id.btn_ok);
		registerButton(R.id.btn_cancel);
		lstBoardMAC = (Spinner) findViewById(R.id.chbxBoardMac);
		edtUserName = (EditText) findViewById(R.id.edtOwnerName);

		setData();
	}

	private void setData()
	{
		Bundle extras = getIntent().getExtras();
		UserSettings settings = (UserSettings) extras.getSerializable(UserSettings.class.getCanonicalName());

		@SuppressWarnings("unchecked")
		ArrayAdapter<String> myAdap = (ArrayAdapter<String>) lstBoardMAC.getAdapter(); // cast to an ArrayAdapter
		int spinnerPosition = myAdap.getPosition(settings.boardMAC);
		lstBoardMAC.setSelection(spinnerPosition, false);

		edtUserName.setText(settings.userName);
	}

	/**
	 * Get button from mock up and set callback function OnClick
	 * @param id button identifier
	 */
	private void registerButton(int id)
	{
		// get button from mock up
		Button btn = (Button) findViewById(id);
		// set up call back for OnClick
		btn.setOnClickListener(mAddListener);
	}

	/**
	 * Create unnamed implementation of OnClickListener
	 */
	private OnClickListener mAddListener = new OnClickListener()
										 {
											 public void onClick(View v)
											 {
												 switch (v.getId())
												 {
												 case R.id.btn_ok:
													 returnData();
													 break;
												 case R.id.btn_cancel:
													 finish();
													 break;
												 }
											 }
										 };

	protected void returnData()
	{
		try
		{
			UserSettings settings = new UserSettings();

			settings.boardMAC = (lstBoardMAC.getSelectedItem().toString());
			settings.userName = edtUserName.getText().toString();
			getIntent().putExtra(UserSettings.class.getCanonicalName(), settings);
			setResult(RESULT_OK, getIntent());
			finish();
		} catch (NumberFormatException ex)
		{

		}

	}

}