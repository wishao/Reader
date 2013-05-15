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

public class UpdateUserButtonListener implements OnClickListener {
	private Context context;

	public UpdateUserButtonListener(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		if (isNetworkAvailable()) {
			String loginPath = Config.HTTP_LOGIN;
			String updatePath = Config.HTTP_UPDATE_PASSWORD;
			EditText nameText = (EditText) ((Activity) context)
					.findViewById(R.id.editText2);
			EditText oldPwdText = (EditText) ((Activity) context)
					.findViewById(R.id.editText1);
			EditText pwdText = (EditText) ((Activity) context)
					.findViewById(R.id.EditText01);
			EditText pwdText1 = (EditText) ((Activity) context)
					.findViewById(R.id.EditText03);
			String name = nameText.getText().toString();
			String oldPwd = oldPwdText.getText().toString();
			String pwd = pwdText.getText().toString();
			String pwd1 = pwdText1.getText().toString();
			if (oldPwd.indexOf(" ") != -1 || pwd.indexOf(" ") != -1
					|| oldPwd == null || pwd == null || oldPwd.equals("")
					|| pwd.equals("")) {
				Toast.makeText(context, "输入有空格或为空值", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!pwd.equals(pwd1)) {
				Toast.makeText(context, "两次密码不一致", Toast.LENGTH_SHORT).show();
				return;
			}
			User user = new User();
			user.setName(name);
			user.setPassword(oldPwd);
			String params = "loginName=" + user.getName() + "&loginPassword="
					+ user.getPassword();
			JSONObject result = HttpUtils.getJsonByPost(loginPath, params);
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

					params = "id=" + user.getId() + "&password=" + pwd;
					result = HttpUtils.getJsonByPost(updatePath, params);
					if (result.getBoolean("success")) {
						UserHelper helper = new UserHelper(
								context.getApplicationContext());
						if (helper.findUser() != null) {
							helper.updateUser(user);
						} else {
							helper.insertUser(user);
						}
						Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT)
								.show();
						Intent intent = new Intent();
						intent.setClass(context, MenuActivity.class);
						context.startActivity(intent);
					} else {
						Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT)
								.show();
						return;
					}
				} else {
					Toast.makeText(context, "原密码错误", Toast.LENGTH_SHORT).show();
					return;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(context,
					context.getString(R.string.network_service),
					Toast.LENGTH_SHORT).show();
			return;
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
