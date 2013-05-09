package com.reader.activity;

import com.reader.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
		OnClickListener clickListener = new OnClickListener() {
			public void onClick(View v) {
				String pwd = pwdText.getText().toString();
				String pwd1 = pwdText1.getText().toString();
				if (!pwd.equals(pwd1)) {
					Toast.makeText(RegisterActivity.this, "两次密码不一致",
							Toast.LENGTH_SHORT).show();
				}

			}
		};
		registerButton.setOnClickListener(clickListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
