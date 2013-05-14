package com.reader.activity;

import com.reader.R;
import com.reader.listener.LoginListener;
import com.reader.listener.RegisterListener;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class BaseActivity extends Activity {
	Button registerButton = null;
	Button loginButton = null;
	EditText nameText = null;
	EditText pwdText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		
		registerButton = (Button) findViewById(R.id.Button01);
		OnClickListener registerListener = new RegisterListener(
				this);
		registerButton.setOnClickListener(registerListener);

		loginButton = (Button) findViewById(R.id.button1);
		OnClickListener loginListener = new LoginListener(this);
		loginButton.setOnClickListener(loginListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.base, menu);
		return true;
	}

}
