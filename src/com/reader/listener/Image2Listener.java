package com.reader.listener;

import java.sql.Timestamp;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.reader.activity.SettingActivity;
import com.reader.impl.ReaderHelper;
import com.reader.impl.UserHelper;
import com.reader.model.Reader;
import com.reader.model.User;
import com.reader.util.Config;
import com.reader.util.Constant;
import com.reader.util.HttpUtils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Image2Listener implements OnClickListener {
	private Context context;

	public Image2Listener(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		Reader reader = new Reader();
		ReaderHelper rhelper = new ReaderHelper(context.getApplicationContext());
		reader = rhelper.findReader();
		UserHelper uhelper = new UserHelper(context.getApplicationContext());
		User user = uhelper.findUser();
		if (isNetworkAvailable()) {
			if (reader != null) {
				try {
					String path = Config.HTTP_READER_SELECT;
					String params = "user_id=" + user.getId();
					JSONObject result = HttpUtils.getJsonByPost(path, params);
					Timestamp localTime = reader.getCreateTime();
					Timestamp netTime = new Timestamp((Constant.sf).parse(
							result.getString("create_time")).getTime());
					if (reader.getUser().getId().equals(user.getId())) {
						if (localTime.after(netTime)) {
							path = Config.HTTP_READER_UPDATE;
							params = "id=" + reader.getId() + "&user_id="
									+ reader.getUser().getId() + "&font="
									+ reader.getFont() + "&background_color="
									+ reader.getBackgroundColor()
									+ "&font_color=" + reader.getFontColor();
							result = HttpUtils.getJsonByPost(path, params);
						}
					} else {
						reader.setId(result.getString("id"));
						user.setId(result.getString("user_id"));
						reader.setUser(user);
						reader.setFont(result.getString("font"));
						reader.setBackgroundColor(result
								.getString("background_color"));
						reader.setCreateTime(new Timestamp((Constant.sf).parse(
								result.getString("create_time")).getTime()));
						reader.setFontColor(result.getString("font_color"));
						rhelper.updateReader(reader);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("reader", reader);
		intent.putExtras(bundle);
		intent.setClass(context, SettingActivity.class);
		context.startActivity(intent);
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
