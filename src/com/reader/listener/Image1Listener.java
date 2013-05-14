package com.reader.listener;

import com.reader.activity.IndividualActivity;
import com.reader.impl.UserHelper;
import com.reader.model.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Image1Listener implements OnClickListener {
	private Context context;

	public Image1Listener(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		UserHelper helper = new UserHelper(
				context.getApplicationContext());
		User user = helper.findUser();
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("user",user);
		intent.putExtras(bundle);
		intent.setClass(context, IndividualActivity.class);
		context.startActivity(intent);

	}

}
