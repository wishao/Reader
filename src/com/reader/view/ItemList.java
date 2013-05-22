package com.reader.view;

import org.json.JSONObject;

import com.reader.impl.UserHelper;
import com.reader.model.User;
import com.reader.util.Config;
import com.reader.util.HttpUtils;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ItemList extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UserHelper uhelper = new UserHelper(getApplicationContext());
		User user = uhelper.findUser();
		String path = Config.HTTP_BOOK_UPDATE;
		String params = "user_id=" + user.getId();
		JSONObject result = HttpUtils.getJsonByPost(path, params);
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new String[] { "视频", "听听",
						"社区" }));
	}
}
