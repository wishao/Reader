package com.reader.activity;

import com.reader.R;
import com.reader.listener.UpdateUserButtonListener;
import com.reader.model.User;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class IndividualActivity extends Activity {
	Button updateUserButton = null;
	EditText nameText = null;
	EditText oldPwdText = null;
	EditText pwdText = null;
	EditText pwd1Text = null;
	EditText signText = null;
	EditText createTimeText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual);

		updateUserButton = (Button) findViewById(R.id.button1);
		OnClickListener updateListener = new UpdateUserButtonListener(
				this);
		updateUserButton.setOnClickListener(updateListener);
		nameText = (EditText) findViewById(R.id.editText2);
		oldPwdText = (EditText) findViewById(R.id.editText1);
		pwdText = (EditText) findViewById(R.id.EditText01);
		pwd1Text = (EditText) findViewById(R.id.EditText03);
		signText = (EditText) findViewById(R.id.editText3);
		createTimeText = (EditText) findViewById(R.id.EditText02);
		User user = (User) getIntent().getSerializableExtra("user");
		nameText.setText(user.getName());
		signText.setText(user.getSignature());
		createTimeText.setText(user.getCreateTime().toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.individual, menu);
		return true;
	}

}
