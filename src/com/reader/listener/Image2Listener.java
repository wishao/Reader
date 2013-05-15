package com.reader.listener;


import com.reader.activity.SettingActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class Image2Listener implements OnClickListener {
	private Context context;

	public Image2Listener(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(context, SettingActivity.class);
		context.startActivity(intent);
	}

}
