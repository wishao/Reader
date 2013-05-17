package com.reader.listener;

import com.reader.util.Picker;
import com.reader.view.PickerDialog;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ColorListener implements OnClickListener {
	private Context context;
	private EditText text;

	public ColorListener(Context context, EditText text) {
		this.context = context;
		this.text = text;
	}

	@Override
	public void onClick(View v) {
		Paint mPaint = new Paint();
		new PickerDialog(context, new Picker.OnColorChangedListener() {
			@Override
			public void colorChanged(Paint color) {
			}
		}, mPaint, text).show();
	}

}
