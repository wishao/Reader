package com.reader.activity;

import com.reader.R;
import com.reader.listener.LoginListener;
import com.reader.listener.RegisterListener;
import com.reader.model.User;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BaseActivity extends Activity {
	Button registerButton = null;
	Button loginButton = null;
	EditText nameText = null;
	EditText pwdText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		nameText=(EditText) findViewById(R.id.editText1);
		pwdText =(EditText) findViewById(R.id.editText2);
		String name = nameText.getText().toString();
		String password = pwdText.getText().toString();
		User user =new User();
		user.setName(name);
		user.setPassword(password);
		registerButton = (Button) findViewById(R.id.Button01);
		OnClickListener registerListener = new RegisterListener(
				this);
		registerButton.setOnClickListener(registerListener);

		loginButton = (Button) findViewById(R.id.button1);
		OnClickListener loginListener = new LoginListener(this,user);
		loginButton.setOnClickListener(loginListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.base, menu);
		return true;
	}

}
