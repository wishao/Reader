package com.reader.listener;

import org.json.JSONObject;

import com.reader.R;
import com.reader.activity.DialogListActivity;
import com.reader.impl.UserHelper;
import com.reader.model.Contact;
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

public class SayListener implements OnClickListener {
	private Context context;
	private String id;

	public SayListener(Context context, String id) {
		this.context = context;
		this.id = id;
	}

	@Override
	public void onClick(View v) {
		EditText contentText = (EditText) ((Activity) context)
				.findViewById(R.id.editText1);
		String content = contentText.getText().toString();
		Contact contact = new Contact();
		contact.setContent(content);
		User receiveUser = new User();
		receiveUser.setId(id);
		UserHelper uhelper = new UserHelper(context.getApplicationContext());
		User sendUser = uhelper.findUser();
		contact.setReceiveUser(receiveUser);
		contact.setSendUser(sendUser);
		if (isNetworkAvailable()) {
			String path = Config.HTTP_CONTACT_ADD;

			String params = "send_user_id=" + contact.getSendUser().getId()
					+ "&receive_user_id=" + contact.getReceiveUser().getId()
					+ "&content=" + contact.getContent();
			JSONObject result = HttpUtils.getJsonByPost(path, params);
			try {
				if (result.getBoolean("success")) {

					Toast.makeText(context, result.getString("message"),
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(context, DialogListActivity.class);
					context.startActivity(intent);
				} else {
					Toast.makeText(context, result.getString("message"),
							Toast.LENGTH_SHORT).show();
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();

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
