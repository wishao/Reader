package com.reader.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
				R.layout.content, new String[] { "title", "image" }, new int[] {
						R.id.ContentTitle, R.id.image });
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
				String id = a.substring(b, c);
				int d = c + 14;
				int e = a.indexOf(", title=");
				String information = a.substring(d, e);
				int f = e + 8;
				String title = a.substring(f, a.length() - 1);

				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ReaderActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Bundle bundle = new Bundle();
				Record record = new Record();
				record.setId(id);
				Book book = new Book();
				book.setId(information);
				record.setBook(book);
				bundle.putSerializable("record", record);
				intent.putExtras(bundle);
				getApplicationContext().startActivity(intent);
				listView.invalidateViews();
			}
		});

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
					map.put("information",
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

	public Bitmap returnBitMap(String url) {

		URL myFileUrl = null;

		Bitmap bitmap = null;

		try {

			myFileUrl = new URL(url);

		} catch (MalformedURLException e) {

			e.printStackTrace();

		}

		try {

			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

}