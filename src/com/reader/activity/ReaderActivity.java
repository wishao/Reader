package com.reader.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.reader.R;
import com.reader.impl.ReaderHelper;
import com.reader.model.Reader;
import com.reader.model.Record;
import com.reader.util.Config;
import com.reader.util.Constant;
import com.reader.util.HttpUtils;
import com.reader.view.LazyScrollView;
import com.reader.view.LazyScrollView.OnScrollListener;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

public class ReaderActivity extends Activity {
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filebrowser);
		LazyScrollView scrollView = (LazyScrollView) findViewById(R.id.scrollView);
		scrollView.getView();
		scrollView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onTop() {
				try {
					Record record = (Record) getIntent().getSerializableExtra(
							"record");
					String path = Config.HTTP_RECORD_UPDATE_READ;
					String params = "id=" + record.getId() + "&start="
							+ Constant.BOOK_LIMIT * (-1);
					JSONObject result = HttpUtils.getJsonByPost(path, params);

					refreshGUI(true);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onScroll() {
				// TODO Auto-generated method stub
				// Log.d(tag,"没有到最下方，也不是最上方");
			}

			@Override
			public void onBottom() {
				// TODO Auto-generated method stub
				// Log.d(tag,"------滚动到最下方------");
				try {
					Record record = (Record) getIntent().getSerializableExtra(
							"record");
					String path = Config.HTTP_RECORD_UPDATE_READ;
					String params = "id=" + record.getId() + "&start="
							+ Constant.BOOK_LIMIT;
					JSONObject result = HttpUtils.getJsonByPost(path, params);

					refreshGUI(false);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		try {
			refreshGUI(false);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	private void refreshGUI(boolean back) throws JSONException {

		tv = (TextView) findViewById(R.id.view_contents);
		ReaderHelper rhelper = new ReaderHelper(getApplicationContext());
		Reader reader = rhelper.findReader();
		if (reader != null) {
			int r = Integer.parseInt(reader.getBackgroundColor()
					.substring(1, 3), 16);
			int g = Integer.parseInt(reader.getBackgroundColor()
					.substring(3, 5), 16);
			int b = Integer.parseInt(reader.getBackgroundColor()
					.substring(5, 7), 16);
			tv.setBackgroundColor(Color.rgb(r, g, b));

			r = Integer.parseInt(reader.getFontColor().substring(1, 3), 16);
			g = Integer.parseInt(reader.getFontColor().substring(3, 5), 16);
			b = Integer.parseInt(reader.getFontColor().substring(5, 7), 16);
			tv.setTextColor(Color.rgb(r, g, b));
			tv.setTextSize(Float.parseFloat(reader.getFont()));
		}
		Record record = (Record) getIntent().getSerializableExtra("record");
		String path = Config.HTTP_RECORD_SELECT_ID;
		String params = "id=" + record.getId();
		JSONObject result = HttpUtils.getJsonByPost(path, params);
		record.setRecord(Integer.parseInt(result.getString("record")));
		path = Config.HTTP_RECORD_READ;
		params = "id=" + record.getId() + "&start=" + record.getRecord()
				+ "&book_id=" + record.getBook().getId();
		result = HttpUtils.getJsonByPost(path, params);
		if (result.getBoolean("success")) {
			String text = result.getString("result");
			if (back) {
				tv.setText(text + tv.getText());
			} else {
				tv.setText(tv.getText() + text);
			}
		} else {
			tv.setText(record.toString());
		}

	}
}
