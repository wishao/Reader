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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.reader.R;
import com.reader.impl.UserHelper;
import com.reader.model.Book;
import com.reader.model.Record;
import com.reader.model.User;
import com.reader.util.Config;
import com.reader.util.Constant;
import com.reader.util.HttpUtils;
import com.reader.view.NewBookListView;
import com.reader.view.NewBookListView.OnRefreshListener;

public class OtherBookActivity extends Activity {
	private BaseAdapter adapter;
	private List<Map<String, Object>> list;
	NewBookListView listView;
	private int pageSize = 2;
	private int page = 0;

	public void onCreate(Bundle savedInstanceState) {
		final String id = (String) getIntent().getSerializableExtra("id");
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		super.onCreate(savedInstanceState);
		PageTask task = new PageTask(id);
		task.execute();
		setContentView(R.layout.newbook_main);
		list = new ArrayList<Map<String, Object>>();
		listView = (NewBookListView) findViewById(R.id.listView);

		adapter = new SimpleAdapter(OtherBookActivity.this, list,
				R.layout.content, new String[] { "id", "title", "information",
						"book_id" }, new int[] { R.id.id, R.id.ContentTitle,
						R.id.ContentComment, R.id.book_id });
		listView.setAdapter(adapter);
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				PageTask task = new PageTask(id);
				task.execute();
			}
		});
		registerForContextMenu(listView);

	}

	class PageTask extends AsyncTask<String, Integer, String> {
		private String id;

		public PageTask(String id) {
			this.id = id;
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				String path = Config.HTTP_RECORD_SELECT;
				String params1 = "user_id=" + id + "&start=" + page * pageSize
						+ "&limit=" + pageSize + "&share="
						+ Constant.STATUS_YES;
				page++;
				JSONObject result = HttpUtils.getJsonByPost(path, params1);
				JSONArray rows = new JSONArray();
				rows = result.getJSONArray("rows");
				for (int i = 0; i < rows.length(); i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("title",
							((JSONObject) rows.get(i)).getString("book_name"));
					map.put("id", ((JSONObject) rows.get(i)).getString("id"));
					map.put("book_id",
							((JSONObject) rows.get(i)).getString("book_id"));
					String information = ((JSONObject) rows.get(i))
							.getString("evaluation");
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

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getMenuInfo() instanceof AdapterContextMenuInfo) {
			AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
					.getMenuInfo();
			TextView tv = (TextView) menuInfo.targetView
					.findViewById(R.id.book_id);
			final String id = tv.getText().toString();
			tv = (TextView) menuInfo.targetView.findViewById(R.id.ContentTitle);
			String title = tv.getText().toString();
			// 处理菜单的点击事件
			switch (item.getItemId()) {
			case R.id.more:
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), BookActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Bundle bundle = new Bundle();
				Book book = new Book();
				book.setId(id);
				bundle.putSerializable("book", book);
				intent.putExtras(bundle);
				getApplicationContext().startActivity(intent);
				break;
			case R.id.read:
				AlertDialog.Builder builder = new AlertDialog.Builder(
						OtherBookActivity.this);
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
				break;

			}

		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.listView) {
			MenuInflater inflater = getMenuInflater();
			menu.setHeaderTitle("请选择");
			inflater.inflate(R.menu.new_book_menu, menu);
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}
}