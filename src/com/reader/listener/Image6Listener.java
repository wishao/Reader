package com.reader.listener;

import com.reader.activity.ItemizedOverlayDemo;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class Image6Listener implements OnClickListener {
	private Context context;

	public Image6Listener(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(context, ItemizedOverlayDemo.class);
		context.startActivity(intent);

	}

}
