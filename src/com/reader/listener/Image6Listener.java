package com.reader.listener;

import org.json.JSONException;
import org.json.JSONObject;

import com.reader.R;
import com.reader.activity.BMapApiDemoMain;
import com.reader.activity.BaseMapDemo;
import com.reader.activity.DialogListActivity;
import com.reader.activity.ItemizedOverlayDemo;
import com.reader.activity.LocationOverlayDemo;
import com.reader.activity.MenuActivity;
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
