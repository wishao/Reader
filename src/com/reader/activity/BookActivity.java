package com.reader.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.reader.R;
import com.reader.model.Book;
import com.reader.model.Reader;
import com.reader.util.Config;
import com.reader.util.HttpUtils;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class BookActivity extends Activity {

	private TextView titleTv;
	private TextView authorTv;
	private TextView recommendTv;
	private TextView catalogTv;
	private TextView readerTv;
	private TextView scoreTv;
	private ImageView coverIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book);
		titleTv = (TextView) findViewById(R.id.bookTitle);
		authorTv = (TextView) findViewById(R.id.author);
		recommendTv = (TextView) findViewById(R.id.recommend);
		catalogTv = (TextView) findViewById(R.id.catalog);
		readerTv = (TextView) findViewById(R.id.reader);
		scoreTv = (TextView) findViewById(R.id.score);
		coverIv = (ImageView) findViewById(R.id.cover);
		Book book = (Book) getIntent().getSerializableExtra("book");
		String path = Config.HTTP_BOOK_SELECT;
		String params = "id=" + book.getId();
		JSONObject result = HttpUtils.getJsonByPost(path, params);
		try {
			titleTv.setText(result.getString("name"));
			authorTv.setText(result.getString("author"));
			recommendTv.setText(result.getString("recommend"));
			catalogTv.setText(result.getString("catalog"));
			readerTv.setText(result.getString("reader"));
			scoreTv.setText(result.getString("score"));
			coverIv.setImageBitmap(returnBitMap(result.getString("cover")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.book, menu);
		return true;
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
