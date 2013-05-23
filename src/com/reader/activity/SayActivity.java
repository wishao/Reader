package com.reader.activity;

import com.reader.R;
import com.reader.listener.RegisterListener;
import com.reader.listener.SayListener;
import com.reader.model.Reader;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SayActivity extends Activity {
	Button button = null;
	EditText text = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_say);
		button = (Button) findViewById(R.id.button1);
		String id = (String) getIntent().getSerializableExtra("id");
		OnClickListener sayListener = new SayListener(
				this,id);
		button.setOnClickListener(sayListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.say, menu);
		return true;
	}

}
