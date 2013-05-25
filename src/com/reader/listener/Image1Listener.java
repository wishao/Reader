package com.reader.listener;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import com.reader.R;
import com.reader.activity.IndividualActivity;
import com.reader.impl.UserHelper;
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
import android.widget.Toast;

public class Image1Listener implements OnClickListener {
	private Context context;

	public Image1Listener(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		if (isNetworkAvailable()) {
			UserHelper helper = new UserHelper(context.getApplicationContext());
			User user = helper.findUser();
			String path = Config.HTTP_USER_SELECT;

			String params = "id=" + user.getId();
			JSONObject result = HttpUtils.getJsonByPost(path, params);
			try {
				user.setId(result.getString("id"));
				user.setName(result.getString("name"));
				user.setCreateTime(new Timestamp((Constant.sf).parse(
						result.getString("create_time")).getTime()));
				user.setAddress(result.getString("address"));
				user.setSignature(result.getString("signature"));
				user.setUpdateTime(new Timestamp((Constant.sf).parse(
						result.getString("update_time")).getTime()));
				user.setStatus(new Byte(result.getString("status")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("user", user);
			intent.putExtras(bundle);
			intent.setClass(context, IndividualActivity.class);
			context.startActivity(intent);
		} else {
			Toast.makeText(context,
					context.getString(R.string.network_service),
					Toast.LENGTH_SHORT).show();
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
