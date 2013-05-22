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
import com.reader.impl.UserHelper;
import com.reader.model.Book;
import com.reader.model.Record;
import com.reader.model.User;
import com.reader.util.Config;
import com.reader.util.HttpUtils;
import com.reader.view.Content;
import com.reader.view.ItemList;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabContentFactory;

public class BookListActivity extends TabActivity {
	private int page = 0;
	private ContentAdapter ca;
	private ListView listView;
	private int pageSize = 20;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		final TabHost tabHost = getTabHost();
		try {
			tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("我的记录")
					.setContent(new Intent(this, ItemList.class)));
			tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("新书上架")
					.setContent(new TabContentFactory() {

						List<Content> items;
						String path = Config.HTTP_BOOK_UPDATE;
						String params = "start=" + page * pageSize + "&limit="
								+ pageSize;
						JSONObject result = HttpUtils.getJsonByPost(path,
								params);
						{
							items = new ArrayList<Content>();
							Content content = new Content();
							JSONArray rows = new JSONArray();
							rows = result.getJSONArray("rows");
							for (int i = 0; i < rows.length(); i++) {
								content = new Content();
								content.setId(((JSONObject) rows.get(i))
										.getString("id"));
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
							listView = new ListView(BookListActivity.this);
							ca = new ContentAdapter(getApplicationContext(), 0,
									items);
							listView.setAdapter(ca);

							return listView;
						}
					}));
		} catch (JSONException e) {
			e.printStackTrace();
		}

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

			final Content content = items.get(position);
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
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							BookListActivity.this);
					builder.setTitle("订阅");
					builder.setMessage("《" + content.getTitle() + "》是否添加到我的记录？");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									Record record = new Record();
									UserHelper uhelper = new UserHelper(
											getApplicationContext());
									User user = uhelper.findUser();
									Book book = new Book();
									book.setId(content.getId());
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
											+ record.getBook().getId()
											+ "&record=" + record.getRecord()
											+ "&evaluation="
											+ record.getEvaluation()
											+ "&score=" + record.getScore()
											+ "&share=" + record.getShare();
									JSONObject result = HttpUtils
											.getJsonByPost(path, params);
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
				}

			});
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
		/*
		 * menu.add(0, 1, 1, "上一页"); menu.add(0, 1, 2, "下一页");
		 */
		return true;
	}

}
