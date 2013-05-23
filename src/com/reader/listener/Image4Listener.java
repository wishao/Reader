package com.reader.listener;

import com.reader.activity.MyBookActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class Image4Listener implements OnClickListener {
	private Context context;

	public Image4Listener(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(context, MyBookActivity.class);
		context.startActivity(intent);

	}

}
