package com.reader.activity;

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

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabContentFactory;

public class MyBookListActivity extends TabActivity {
	private RecordAdapter ca;
	private ListView listView;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		final TabHost tabHost = getTabHost();
		try {
			tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("我的记录")
					.setContent(new TabContentFactory() {
						UserHelper uhelper = new UserHelper(
								getApplicationContext());
						User user = uhelper.findUser();
						List<Record> items;
						String path = Config.HTTP_RECORD_SELECT;
						String params = "user_id=" + user.getId();
						JSONObject result = HttpUtils.getJsonByPost(path,
								params);
						{
							items = new ArrayList<Record>();
							Record record = new Record();
							JSONArray rows = new JSONArray();
							rows = result.getJSONArray("rows");
							for (int i = 0; i < rows.length(); i++) {
								record = new Record();
								record.setId(((JSONObject) rows.get(i))
										.getString("id"));
								Book book = new Book();
								book.setId(((JSONObject) rows.get(i))
										.getString("book_id"));
								book.setName(((JSONObject) rows.get(i))
										.getString("book_name"));
								record.setBook(book);
								record.setRecord(Integer
										.parseInt(((JSONObject) rows.get(i))
												.getString("record")));
								items.add(record);
							}
						}

						public View createTabContent(String tag) {
							listView = new ListView(MyBookListActivity.this);
							ca = new RecordAdapter(getApplicationContext(), 0,
									items);
							listView.setAdapter(ca);

							return listView;
						}
					}));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private class RecordAdapter extends ArrayAdapter<Record> {

		private List<Record> items;

		public RecordAdapter(Context context, int textViewResourceId,
				List<Record> items) {
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

			final Record record = items.get(position);
			if (record != null) {
				TextView title = (TextView) view
						.findViewById(R.id.ContentTitle);

				title.setText(record.getBook().getName());
			}
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							ReaderActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Bundle bundle = new Bundle();
					bundle.putSerializable("record", record);
					intent.putExtras(bundle);
					getApplicationContext().startActivity(intent);
				}

			});
			return view;
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * menu.add(0, 1, 1, "上一页"); menu.add(0, 1, 2, "下一页");
		 */
		return true;
	}

}
