package com.reader.activity;

import com.reader.R;
import com.reader.listener.Image1Listener;
import com.reader.listener.Image2Listener;
import com.reader.listener.Image3Listener;
import com.reader.listener.Image4Listener;
import com.reader.listener.Image5Listener;
import com.reader.listener.Image6Listener;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageView;

public class MenuActivity extends Activity {

	ImageView Image1 = null;
	ImageView Image2 = null;
	ImageView Image3 = null;
	ImageView Image4 = null;
	ImageView Image5 = null;
	ImageView Image6 = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		Image1 = (ImageView) findViewById(R.id.imageView1);
		Image2 = (ImageView) findViewById(R.id.imageView2);
		Image3 = (ImageView) findViewById(R.id.imageView3);
		Image4 = (ImageView) findViewById(R.id.imageView4);
		Image5 = (ImageView) findViewById(R.id.imageView5);
		Image6 = (ImageView) findViewById(R.id.imageView6);
		
		Image1Listener Image1Listener = new Image1Listener(this);
		Image1.setOnClickListener(Image1Listener);
		Image2Listener Image2Listener = new Image2Listener(this);
		Image2.setOnClickListener(Image2Listener);
		Image3Listener Image3Listener = new Image3Listener(this);
		Image3.setOnClickListener(Image3Listener);
		Image4Listener Image4Listener = new Image4Listener(this);
		Image4.setOnClickListener(Image4Listener);
		Image5Listener Image5Listener = new Image5Listener(this);
		Image5.setOnClickListener(Image5Listener);
		Image6Listener Image6Listener = new Image6Listener(this);
		Image6.setOnClickListener(Image6Listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

}
