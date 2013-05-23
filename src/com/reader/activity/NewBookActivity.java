package com.reader.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.reader.R;
import com.reader.impl.UserHelper;
import com.reader.model.Book;
import com.reader.model.Record;
import com.reader.model.User;
import com.reader.util.Config;
import com.reader.util.HttpUtils;
import com.reader.view.NewBookListView;
import com.reader.view.NewBookListView.OnRefreshListener;

public class NewBookActivity extends Activity {
	private BaseAdapter adapter;
	private List<Map<String, Object>> list;
	NewBookListView listView;
	private int pageSize = 2;
	private int page = 0;

	public void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		super.onCreate(savedInstanceState);
		PageTask task = new PageTask();
		task.execute();
		setContentView(R.layout.newbook_main);
		list = new ArrayList<Map<String, Object>>();
		listView = (NewBookListView) findViewById(R.id.listView);

		adapter = new SimpleAdapter(NewBookActivity.this, list,
				R.layout.content, new String[] { "title", "information",
						"image" }, new int[] { R.id.ContentTitle,
						R.id.ContentComment, R.id.image });
		listView.setAdapter(adapter);
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				PageTask task = new PageTask();
				task.execute();
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String a = (arg0.getItemAtPosition(arg2)).toString();
				int b = a.indexOf("id=") + 3;
				int c = a.indexOf(", information=");
				final String id = a.substring(b, c);
				int d = c + 14;
				int e = a.indexOf(", title=");
				String information = a.substring(d, e);
				int f = e + 8;
				String title = a.substring(f, a.length() - 1);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						NewBookActivity.this);
				builder.setTitle("订阅");
				builder.setMessage("《" + title + "》是否添加到我的记录？");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								Record record = new Record();
								UserHelper uhelper = new UserHelper(
										getApplicationContext());
								User user = uhelper.findUser();
								Book book = new Book();
								book.setId(id);
								record.setUser(user);
								record.setBook(book);
								record.setEvaluation("");
								record.setRecord(1);
								record.setScore(0);
								record.setShare(new Byte("1"));
								String path = Config.HTTP_RECORD_ADD;
								String params = "user_id="
										+ record.getUser().getId()
										+ "&book_id="
										+ record.getBook().getId() + "&record="
										+ record.getRecord() + "&evaluation="
										+ record.getEvaluation() + "&score="
										+ record.getScore() + "&share="
										+ record.getShare();
								JSONObject result = HttpUtils.getJsonByPost(
										path, params);
								try {
									Toast.makeText(getApplicationContext(),
											result.getString("message"),
											Toast.LENGTH_SHORT).show();
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}

						});
				builder.setNegativeButton("取消", null);
				builder.create().show();

				listView.invalidateViews();

			}
		});

	}

	class PageTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				String path = Config.HTTP_BOOK_UPDATE;
				String params1 = "start=" + page * pageSize + "&limit="
						+ pageSize;
				page++;
				JSONObject result = HttpUtils.getJsonByPost(path, params1);
				JSONArray rows = new JSONArray();
				rows = result.getJSONArray("rows");
				for (int i = 0; i < rows.length(); i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("title",
							((JSONObject) rows.get(i)).getString("name"));
					map.put("id", ((JSONObject) rows.get(i)).getString("id"));
					String information = ((JSONObject) rows.get(i))
							.getString("recommend");
					map.put("information", information);
					list.add(0, map);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			adapter.notifyDataSetChanged();
			listView.onRefreshComplete();
			super.onPostExecute(result);
		}

	}

}