package com.reader.activity;

import com.reader.R;
import com.reader.listener.RegisterButtonListener;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {
	Button registerButton = null;
	EditText nameText = null;
	EditText pwdText = null;
	EditText pwdText1 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		registerButton = (Button) findViewById(R.id.Button01);
		nameText = (EditText) findViewById(R.id.editText1);
		pwdText = (EditText) findViewById(R.id.editText2);
		pwdText1 = (EditText) findViewById(R.id.editText3);
		OnClickListener clickListener = new RegisterButtonListener(this);
		registerButton.setOnClickListener(clickListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
