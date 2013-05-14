package com.reader.listener;

import com.reader.R;
import com.reader.activity.RegisterActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class RegisterListener implements OnClickListener {
	private Context context;

	public RegisterListener(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		if (isNetworkAvailable()) {
			Intent intent = new Intent();
			intent.setClass(context, RegisterActivity.class);
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
