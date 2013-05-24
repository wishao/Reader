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
import com.reader.util.HttpUtils;
import com.reader.view.NewBookListView;
import com.reader.view.NewBookListView.OnRefreshListener;

public class MyBookActivity extends Activity {
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

		adapter = new SimpleAdapter(MyBookActivity.this, list,
				R.layout.content, new String[] { "id", "title", "book_id" },
				new int[] { R.id.id, R.id.ContentTitle, R.id.book_id });
		listView.setAdapter(adapter);
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				PageTask task = new PageTask();
				task.execute();
			}
		});
		registerForContextMenu(listView);

	}

	class PageTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				String path = Config.HTTP_RECORD_SELECT;
				UserHelper uhelper = new UserHelper(getApplicationContext());
				User user = uhelper.findUser();
				String params1 = "user_id=" + user.getId() + "&start=" + page
						* pageSize + "&limit=" + pageSize;
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
			TextView tv = (TextView) menuInfo.targetView.findViewById(R.id.id);
			final String id = tv.getText().toString();
			tv = (TextView) menuInfo.targetView.findViewById(R.id.ContentTitle);
			String title = tv.getText().toString();
			tv = (TextView) menuInfo.targetView.findViewById(R.id.book_id);
			final String bookId = tv.getText().toString();
			// 处理菜单的点击事件
			Record record = new Record();
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			record.setId(id);
			switch (item.getItemId()) {
			case R.id.read:
				intent.setClass(getApplicationContext(), ReaderActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				record = new Record();
				record.setId(id);
				Book book = new Book();
				book.setId(bookId);
				record.setBook(book);
				bundle.putSerializable("record", record);
				intent.putExtras(bundle);
				getApplicationContext().startActivity(intent);
				break;
			case R.id.recomend:
				record = new Record();
				record.setId(id);
				intent = new Intent();
				intent.setClass(getApplicationContext(), RecomendActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				bundle.putSerializable("record", record);
				intent.putExtras(bundle);
				getApplicationContext().startActivity(intent);
				break;
			case R.id.delete:
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MyBookActivity.this);
				builder.setTitle("取消订阅");
				builder.setMessage("《" + title + "》是否从我的记录删除？");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								Record record = new Record();
								record.setId(id);
								String path = Config.HTTP_RECORD_DELETE;
								String params = "id=" + record.getId();
								JSONObject result = HttpUtils.getJsonByPost(
										path, params);
								try {
									Toast.makeText(getApplicationContext(),
											result.getString("message"),
											Toast.LENGTH_SHORT).show();
									Intent intent = new Intent();
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									intent.setClass(getApplicationContext(),
											MenuActivity.class);
									getApplicationContext().startActivity(
											intent);
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
			inflater.inflate(R.menu.my_book_menu, menu);
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}
}