package com.reader.listener;

import org.json.JSONException;
import org.json.JSONObject;

import com.reader.R;
import com.reader.model.User;
import com.reader.util.Config;
import com.reader.util.HttpUtils;

import android.app.Activity;
import android.content.Context;
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
		String path = Config.HTTP_LOGIN;
		EditText nameText = (EditText) ((Activity) context)
				.findViewById(R.id.editText1);
		EditText pwdText = (EditText) ((Activity) context)
				.findViewById(R.id.editText2);
		String name = nameText.getText().toString();
		String password = pwdText.getText().toString();
		User user = new User();
		user.setName(name);
		user.setPassword(password);
		String params = "loginName=" + user.getName() + "&loginPassword="
				+ user.getPassword();
		JSONObject result = HttpUtils.getJsonByPost(path, params);
		try {
			Toast.makeText(context, result.getString("msg"), Toast.LENGTH_SHORT)
					.show();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}