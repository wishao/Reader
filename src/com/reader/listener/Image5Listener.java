package com.reader.listener;

import com.reader.activity.DialogListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class Image5Listener implements OnClickListener {
	private Context context;

	public Image5Listener(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(context, DialogListActivity.class);
		context.startActivity(intent);

	}

}
