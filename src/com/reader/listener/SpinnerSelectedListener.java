package com.reader.listener;

import com.reader.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class SpinnerSelectedListener implements OnItemSelectedListener {
	private Context context;
	private static final String[] m = { "0", "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "10" };

	public SpinnerSelectedListener(Context context) {
		this.context = context;
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		Spinner nameText = (Spinner) ((Activity) context)
				.findViewById(R.id.spinner1);
		nameText.setSelection(Integer.parseInt(m[arg2]));
	}

	public void onNothingSelected(AdapterView<?> arg0) {
	}
}