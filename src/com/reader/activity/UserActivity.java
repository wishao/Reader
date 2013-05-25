package com.reader.activity;

import com.reader.R;
import com.reader.model.User;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UserActivity extends Activity {
	private TextView nameTv;
	private TextView signatureTv;
	private Button button;
	private Button button1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		nameTv = (TextView) findViewById(R.id.name);
		signatureTv = (TextView) findViewById(R.id.signature);
		button = (Button) findViewById(R.id.button1);
		button1 = (Button) findViewById(R.id.button2);
		final User user = (User) getIntent().getSerializableExtra("user");
		nameTv.setText(user.getName());
		signatureTv.setText(user.getSignature());
		final Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle bundle = new Bundle();
		bundle.putSerializable("id", user.getId());
		intent.putExtras(bundle);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent.setClass(getApplicationContext(), SayActivity.class);
				getApplicationContext().startActivity(intent);

			}
		});
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent.setClass(getApplicationContext(),
						OtherBookActivity.class);
				getApplicationContext().startActivity(intent);

			}
		});
	}
}
