package com.reader.listener;

import org.json.JSONObject;

import com.reader.activity.SettingActivity;
import com.reader.impl.ReaderHelper;
import com.reader.impl.UserHelper;
import com.reader.model.Reader;
import com.reader.model.User;
import com.reader.util.Config;
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
		if(isNetworkAvailable()){
			
		}else{
			ReaderHelper rhelper = new ReaderHelper(
					context.getApplicationContext());
			Reader reader = rhelper.findReader();
			UserHelper uhelper = new UserHelper(
					context.getApplicationContext());
			User user = uhelper.findUser();
			
			if(reader!=null){
				String path = Config.HTTP_READER_SELECT;
				String params = "user_id=" + user.getId();
				JSONObject result = HttpUtils.getJsonByPost(path, params);
			}
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("user",user);
			intent.putExtras(bundle);
		}
		
		Intent intent = new Intent();
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
