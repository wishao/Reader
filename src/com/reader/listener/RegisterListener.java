package com.reader.listener;

import com.reader.activity.RegisterActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class RegisterListener implements OnClickListener {
	private Context context;

	public RegisterListener(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(context, RegisterActivity.class);
		context.startActivity(intent);

	}
}
