package com.reader.listener;

import org.json.JSONException;
import org.json.JSONObject;

import com.reader.R;
import com.reader.activity.MenuActivity;
import com.reader.impl.UserHelper;
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

public class RegisterButtonListener implements OnClickListener {
	private Context context;

	public RegisterButtonListener(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		String path = Config.HTTP_REGISTER;
		EditText nameText = (EditText) ((Activity) context)
				.findViewById(R.id.editText1);
		EditText pwdText = (EditText) ((Activity) context)
				.findViewById(R.id.editText2);
		EditText pwdText1 = (EditText) ((Activity) context)
				.findViewById(R.id.editText3);
		String name = nameText.getText().toString();
		String pwd = pwdText.getText().toString();
		String pwd1 = pwdText1.getText().toString();
		if (name.indexOf(" ") != -1 || pwd.indexOf(" ") != -1 || name == null
				|| pwd == null || name.equals("") || pwd.equals("")) {
			Toast.makeText(context, "输入有空格或为空值", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!pwd.equals(pwd1)) {
			Toast.makeText(context, "两次密码不一致", Toast.LENGTH_SHORT).show();
			return;
		}
		User user = new User();
		user.setName(name);
		user.setPassword(pwd);
		String params = "name=" + user.getName() + "&password="
				+ user.getPassword();
		JSONObject result = HttpUtils.getJsonByPost(path, params);
		try {
			if (result.getBoolean("success")) {
				UserHelper helper = new UserHelper(
						context.getApplicationContext());
				if (helper.findUser() != null) {
					helper.updateUser(user);
				} else {
					helper.insertUser(user);
				}
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
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
