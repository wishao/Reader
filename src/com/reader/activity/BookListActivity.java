package com.reader.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.reader.R;
import com.reader.util.Config;
import com.reader.util.HttpUtils;
import com.reader.view.Content;
import com.reader.view.ItemList;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabContentFactory;

public class BookListActivity extends TabActivity {
	private int page = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		final TabHost tabHost = getTabHost();

		try {
			tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("新书上架")
					.setContent(new TabContentFactory() {

						List<Content> items;
						String path = Config.HTTP_BOOK_UPDATE;
						String params = "start=" + page * 20 + "&limit="
								+ (page * 20 + 20);
						JSONObject result = HttpUtils.getJsonByPost(path,
								params);
						{
							items = new ArrayList<Content>();
							Content content = new Content();
							int resultSize = Integer.parseInt(result
									.getString("total"));
							JSONArray rows = new JSONArray();
							rows = result.getJSONArray("rows");
							for (int i = 0; i < resultSize; i++) {
								content = new Content();
								content.setTitle(((JSONObject) rows.get(i))
										.getString("name"));
								content.setComment(((JSONObject) rows.get(i))
										.getString("recommend"));
								content.setImageUrl(((JSONObject) rows.get(i))
										.getString("cover"));
								items.add(content);
							}
						}

						public View createTabContent(String tag) {
							ListView listView = new ListView(
									BookListActivity.this);
							listView.setAdapter(new ContentAdapter(
									BookListActivity.this, 0, items));

							return listView;
						}
					}));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("我的记录")
				.setContent(new Intent(this, ItemList.class)));
	}

	private class ContentAdapter extends ArrayAdapter<Content> {

		private List<Content> items;

		public ContentAdapter(Context context, int textViewResourceId,
				List<Content> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;

			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.content, null);
			}

			Content content = items.get(position);
			if (content != null) {
				TextView title = (TextView) view
						.findViewById(R.id.ContentTitle);
				TextView comment = (TextView) view
						.findViewById(R.id.ContentComment);
				ImageView imageView = (ImageView) view.findViewById(R.id.image);

				title.setText(content.getTitle());
				comment.setText(content.getComment());
				imageView.setImageBitmap(returnBitMap(content.getImageUrl()));
			}

			return view;
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

	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, "上一页");
		menu.add(0, 1, 2, "下一页");
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		if (page == 0) {
			switch (item.getOrder()) {
			case 1:
				intent = new Intent(BookListActivity.this,
						BookListActivity.class);
				break;
			case 2:
				page = page + 1;
				intent = new Intent(BookListActivity.this,
						BookListActivity.class);
				break;

			}
		} else {
			switch (item.getOrder()) {
			case 1:
				page = page - 1;
				intent = new Intent(BookListActivity.this,
						BookListActivity.class);
				break;
			case 2:
				page = page + 1;
				intent = new Intent(BookListActivity.this,
						BookListActivity.class);
				break;

			}
		}
		return false;

	}
}
