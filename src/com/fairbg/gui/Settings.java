package com.fairbg.gui;

import com.fairbg.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Settings extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		registerButton(R.id.btn_ok);
		registerButton(R.id.btn_cancel);
	}
    /** Захватывает кнопку из макета и регистрирует приемник OnClick
     * @param id идентификатор кнопки
     */
    private void registerButton(int id){
        // Захватывает кнопку из макета
        Button btn = (Button)findViewById(id);
        // Регистрирует приемник OnClick
        btn.setOnClickListener(mAddListener);
    }

    // Создает анонимную реализацию OnClickListener
    private OnClickListener mAddListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		switch(v.getId())
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
