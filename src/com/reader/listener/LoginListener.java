package com.reader.listener;

import java.sql.Timestamp;
import org.json.JSONObject;

import com.reader.R;
import com.reader.activity.MenuActivity;
import com.reader.impl.UserHelper;
import com.reader.model.User;
import com.reader.util.Config;
import com.reader.util.Constant;
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

public class LoginListener implements OnClickListener {
	private Context context;

	public LoginListener(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		EditText nameText = (EditText) ((Activity) context)
				.findViewById(R.id.editText1);
		EditText pwdText = (EditText) ((Activity) context)
				.findViewById(R.id.editText2);
		String name = nameText.getText().toString();
		String password = pwdText.getText().toString();
		User user = new User();
		user.setName(name);
		user.setPassword(password);
		if (isNetworkAvailable()) {
			String path = Config.HTTP_LOGIN;

			String params = "loginName=" + user.getName() + "&loginPassword="
					+ user.getPassword();
			JSONObject result = HttpUtils.getJsonByPost(path, params);
			try {
				if (result.getBoolean("result")) {

					user.setId(result.getString("id"));
					user.setName(result.getString("name"));
					user.setCreateTime(new Timestamp((Constant.sf).parse(
							result.getString("create_time")).getTime()));
					user.setAddress(result.getString("address"));
					user.setSignature(result.getString("signature"));
					user.setUpdateTime(new Timestamp((Constant.sf).parse(
							result.getString("update_time")).getTime()));
					user.setStatus(new Byte(result.getString("status")));
					//更新用户
					UserHelper helper = new UserHelper(
							context.getApplicationContext());
					if (helper.findUser() != null) {
						helper.updateUser(user);
					} else {
						helper.insertUser(user);
					}
					Toast.makeText(context, result.getString("msg"),
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(context, MenuActivity.class);
					context.startActivity(intent);
				} else {
					Toast.makeText(context, result.getString("msg"),
							Toast.LENGTH_SHORT).show();
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(context,
					context.getString(R.string.network_service),
					Toast.LENGTH_SHORT).show();
			UserHelper helper = new UserHelper(context.getApplicationContext());
			if (helper.login(user) != null) {
				Toast.makeText(context, user.getName() + "登录成功",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(context, MenuActivity.class);
				context.startActivity(intent);
			} else {
				Toast.makeText(context, user.getName() + "登录失败",
						Toast.LENGTH_SHORT).show();
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
