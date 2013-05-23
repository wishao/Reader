package com.reader.listener;

import org.json.JSONException;
import org.json.JSONObject;

import com.reader.R;
import com.reader.activity.MenuActivity;
import com.reader.activity.MyBookListActivity;
import com.reader.activity.NewBookActivity;
import com.reader.model.User;
import com.reader.util.Config;
import com.reader.util.HttpUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class Image5Listener implements OnClickListener {
	private Context context;

	public Image5Listener(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(context, NewBookActivity.class);
		context.startActivity(intent);

	}

}
