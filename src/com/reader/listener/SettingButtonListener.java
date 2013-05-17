package com.reader.listener;


import org.json.JSONObject;

import com.reader.R;
import com.reader.activity.MenuActivity;
import com.reader.impl.ReaderHelper;
import com.reader.impl.UserHelper;
import com.reader.model.Reader;
import com.reader.model.User;
import com.reader.util.Config;
import com.reader.util.HttpUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class SettingButtonListener implements OnClickListener {
	private Context context;

	public SettingButtonListener(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		EditText fontText = (EditText) ((Activity) context)
				.findViewById(R.id.editText2);
		EditText fontColorText = (EditText) ((Activity) context)
				.findViewById(R.id.editText1);
		EditText backgroundText = (EditText) ((Activity) context)
				.findViewById(R.id.EditText01);
		String font = fontText.getText().toString();
		String fontColor = fontColorText.getText().toString();
		String background = backgroundText.getText().toString();
		UserHelper uhelper = new UserHelper(context.getApplicationContext());
		User user = uhelper.findUser();
		Reader reader = new Reader();
		ReaderHelper rhelper = new ReaderHelper(context.getApplicationContext());
		reader = rhelper.findReader();
		reader.setBackgroundColor(background);
		reader.setFont(font);
		reader.setFontColor(fontColor);
		rhelper.updateReader(reader);
		if (isNetworkAvailable()) {
			String updatePath = Config.HTTP_READER_UPDATE;
			String params = "id=" + reader.getId() + "&user_id=" + user.getId()
					+ "&font=" + reader.getFont() + "&background_color="
					+ reader.getBackgroundColor() + "&font_color="
					+ reader.getFontColor();
			JSONObject result = HttpUtils.getJsonByPost(updatePath, params);
			try {
				if (result.getBoolean("success")) {
					Toast.makeText(context, result.getString("message"),
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(context, MenuActivity.class);
					context.startActivity(intent);
				} else {
					Toast.makeText(context, result.getString("message"),
							Toast.LENGTH_SHORT).show();
					return;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
		} else {// 获取所有网络连接信息
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {// 逐一查找状态为已连接的网络
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
