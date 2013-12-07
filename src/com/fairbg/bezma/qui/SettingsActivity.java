package com.fairbg.bezma.qui;

import com.fairbg.bezma.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingsActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        registerButton(R.id.btn_ok);
        registerButton(R.id.btn_cancel);
    }

    /**
     * Get button from mock up and set callback function OnClick
     * 
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
                break;
            case R.id.btn_cancel:
                finish();
                break;
            }
        }
    };

}