package com.reader.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.reader.R;
import com.reader.listener.ColorListener;
import com.reader.listener.SettingButtonListener;
import com.reader.model.Reader;

public class SettingActivity extends Activity {
	Button settingButton = null;
	EditText fontText = null;
	EditText fontColorText = null;
	EditText backgroundText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		settingButton = (Button) findViewById(R.id.button1);
		OnClickListener settingListener = new SettingButtonListener(this);
		settingButton.setOnClickListener(settingListener);
		fontText = (EditText) findViewById(R.id.editText2);
		fontColorText = (EditText) findViewById(R.id.editText1);
		backgroundText = (EditText) findViewById(R.id.EditText01);
		OnClickListener fontColorListener = new ColorListener(this,fontColorText);
		OnClickListener backgroundColorListener = new ColorListener(this,backgroundText);
		fontColorText.setOnClickListener(fontColorListener);
		backgroundText.setOnClickListener(backgroundColorListener);

		Reader reader = (Reader) getIntent().getSerializableExtra("reader");
		fontText.setText(reader.getFont());
		fontColorText.setText(reader.getFontColor());
		backgroundText.setText(reader.getBackgroundColor());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.individual, menu);
		return true;
	}

}
