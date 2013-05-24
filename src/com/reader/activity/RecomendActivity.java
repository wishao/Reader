package com.reader.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.reader.R;
import com.reader.listener.RecomendListener;
import com.reader.listener.SpinnerSelectedListener;
import com.reader.model.Record;
import com.reader.util.Config;
import com.reader.util.Constant;
import com.reader.util.HttpUtils;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class RecomendActivity extends Activity {

	private EditText editText;
	private Spinner spinner;
	private CheckBox shareBox;
	private CheckBox signBox;
	private Button button;
	private static final String[] m = { "0", "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "10" };
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recomend);

		editText = (EditText) findViewById(R.id.editText1);
		spinner = (Spinner) findViewById(R.id.spinner1);
		shareBox = (CheckBox) findViewById(R.id.checkBox1);
		signBox = (CheckBox) findViewById(R.id.checkBox2);
		button = (Button) findViewById(R.id.button1);
		Record record = (Record) getIntent().getSerializableExtra("record");
		String path = Config.HTTP_RECORD_SELECT_ID;
		String params = "id=" + record.getId();
		JSONObject result = HttpUtils.getJsonByPost(path, params);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, m);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setVisibility(View.VISIBLE);
		OnItemSelectedListener olistener = new SpinnerSelectedListener(this);
		spinner.setOnItemSelectedListener(olistener);
		OnClickListener buttonListener = new RecomendListener(this, record);
		button.setOnClickListener(buttonListener);
		try {
			editText.setText(result.getString("evaluation"));
			spinner.setSelection(result.getInt("score"));
			shareBox.setChecked(result.getInt("share") == Constant.STATUS_YES ? true
					: false); // 设置默认为勾选
			signBox.setChecked(true); // 设置默认为勾选
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

}
