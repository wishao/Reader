package com.reader.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;

import com.reader.R;
import com.reader.view.NewBookListView;
import com.reader.view.NewBookListView.OnRefreshListener;

public class NewBookActivity extends Activity {
	String baseurl = "http://192.168.0.231:8080/musicserver/getmyrecorder?";
	private List<String> data;
	private BaseAdapter adapter;
	private List<Map<String, String>> list;
	NewBookListView listView;
	int curpage = 0;

	public void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
																		// 就包括了磁盘读写和网络I/O
				.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
				.penaltyLog() // 打印logcat
				.penaltyDeath().build());
		super.onCreate(savedInstanceState);

		setContentView(R.layout.newbook_main);
		list = new ArrayList<Map<String, String>>();
		listView = (NewBookListView) findViewById(R.id.listView);

		adapter = new SimpleAdapter(NewBookActivity.this, list,
				R.layout.item_newbook, new String[] { "title", "information" },
				new int[] { R.id.title, R.id.artist });
		listView.setAdapter(adapter);
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				String url = baseurl + "curpage=" + curpage + "&username=oh";
				list.clear();
				Log.i("TAG", url);
				PageTask task = new PageTask();
				task.execute(url);
			}
		});

	}

	class PageTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.i("TAG", "he" + params[0]);
			HttpClient client = new DefaultHttpClient();
			Log.i("TAG", "before");
			HttpGet get = new HttpGet(params[0]);
			Log.i("TAG", "after");
			try {
				Log.i("TAG", "befroe executee");
				HttpResponse resp = client.execute(get);
				Log.i("TAG", "heere");
				if (resp.getStatusLine().getStatusCode() == 200) {
					Log.i("TAG", params[0]);
					String result = EntityUtils.toString(resp.getEntity());

					JSONArray json = new JSONArray(result);
					for (int i = 0; i < json.length(); i++) {
						JSONObject j = json.getJSONObject(i);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", j.getString("title"));
						map.put("id", j.getString("id"));
						String information = "有" + j.getString("zan") + "赞，"
								+ j.getString("dao") + "倒，共有"
								+ j.getString("ping") + "条评论， 总评分是 "
								+ j.getString("score");

						map.put("information", information);
						list.add(map);
					}

				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
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