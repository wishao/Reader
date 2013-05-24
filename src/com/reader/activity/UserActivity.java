package com.reader.activity;

import com.reader.R;
import com.reader.model.User;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class UserActivity extends Activity {
	private TextView nameTv;
	private TextView signatureTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		nameTv = (TextView) findViewById(R.id.name);
		signatureTv = (TextView) findViewById(R.id.signature);
		User user = (User) getIntent().getSerializableExtra("user");
		nameTv.setText(user.getName());
		signatureTv.setText(user.getSignature());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user, menu);
		return true;
	}

}
