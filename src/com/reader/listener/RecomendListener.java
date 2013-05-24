package com.reader.listener;

import org.json.JSONException;
import org.json.JSONObject;

import com.reader.R;
import com.reader.impl.UserHelper;
import com.reader.model.Book;
import com.reader.model.Record;
import com.reader.model.User;
import com.reader.util.Config;
import com.reader.util.Constant;
import com.reader.util.HttpUtils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RecomendListener implements OnClickListener {
	private Context context;
	private Record record;

	public RecomendListener(Context context, Record record) {
		this.context = context;
		this.record = record;
	}

	@Override
	public void onClick(View v) {
		EditText editText = (EditText) ((Activity) context)
				.findViewById(R.id.editText1);
		Spinner spinner = (Spinner) ((Activity) context)
				.findViewById(R.id.spinner1);
		CheckBox shareBox = (CheckBox) ((Activity) context)
				.findViewById(R.id.checkBox1);
		CheckBox signBox = (CheckBox) ((Activity) context)
				.findViewById(R.id.checkBox2);
		String recomend = editText.getText().toString();
		String score = spinner.getSelectedItem().toString();
		Byte share = shareBox.isChecked() ? Constant.STATUS_YES
				: Constant.STATUS_NO;
		boolean sign = signBox.isChecked();

		String path = Config.HTTP_RECORD_SELECT_ID;
		String params = "id=" + record.getId();
		JSONObject result = HttpUtils.getJsonByPost(path, params);

		try {
			Book book = new Book();
			book.setId(result.getString("book_id"));
			book.setName(result.getString("book_name"));
			record.setBook(book);
			UserHelper helper = new UserHelper(context.getApplicationContext());
			User user = helper.findUser();
			record.setUser(user);
			record.setRecord(result.getInt("record"));
			record.setEvaluation(recomend);
			record.setScore(Integer.parseInt(score));
			record.setShare(share);
			path = Config.HTTP_RECORD_UPDATE;
			params = "id=" + record.getId() + "&user_id="
					+ record.getUser().getId() + "&book_id="
					+ record.getBook().getId() + "&record="
					+ record.getRecord() + "&evaluation="
					+ record.getEvaluation() + "&score=" + record.getScore()
					+ "&share=" + record.getShare();
			result = HttpUtils.getJsonByPost(path, params);
			if (result.getBoolean("success")) {
				if (sign) {
					path = Config.HTTP_USER_UPDATE;
					params = "id=" + user.getId() + "&name=" + user.getName()
							+ "&signature=" + recomend + "--《"
							+ record.getBook().getName() + "》" + "&status=1";
					result = HttpUtils.getJsonByPost(path, params);
					Toast.makeText(context, result.getString("message"),
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(context, result.getString("message"),
						Toast.LENGTH_SHORT).show();
				((Activity) context).finish();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
