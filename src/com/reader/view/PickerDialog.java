package com.reader.view;

import com.reader.R;
import com.reader.util.Picker;
import com.reader.util.PreviewView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PickerDialog extends Dialog {
	private Picker.OnColorChangedListener mListener;
	private final Paint mPaint;
	private Context context;
	private int color;
	private EditText text;

	public PickerDialog(Context context,
			Picker.OnColorChangedListener listener, Paint initialPaint,
			EditText text) {
		super(context);
		this.context = context;
		this.text = text;
		mListener = listener;
		mPaint = new Paint(initialPaint);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.pick_a_color);
		setContentView(R.layout.color_picker);

		final PreviewView previewView = (PreviewView) findViewById(R.id.preview_new);
		previewView.setPaint(mPaint);

		final Picker satValPicker = (Picker) findViewById(R.id.satval_picker);
		Picker.OnColorChangedListener satValLstr = new Picker.OnColorChangedListener() {
			public void colorChanged(Paint paint) {
				previewView.setColor(paint.getColor());
				mPaint.setColor(paint.getColor());
				color = paint.getColor();
				System.out.println(color);
			}
		};
		satValPicker.setOnColorChangedListener(satValLstr);
		satValPicker.setColor(mPaint.getColor());

		Picker huePicker = (Picker) findViewById(R.id.hue_picker);
		Picker.OnColorChangedListener hueLstr = new Picker.OnColorChangedListener() {
			@Override
			public void colorChanged(Paint paint) {
				satValPicker.setColor(paint.getColor());
				previewView.setColor(paint.getColor());
				mPaint.setColor(paint.getColor());
			}
		};
		huePicker.setOnColorChangedListener(hueLstr);
		huePicker.setColor(mPaint.getColor());

		Button acceptButton = (Button) findViewById(R.id.picker_button_accept);
		acceptButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.colorChanged(mPaint);
				if (color == 0) {
					text.setText(("#000000").toUpperCase());
				}
				int r = (color >> 16) & 0xFF;
				int g = (color >> 8) & 0xFF;
				int b = (color >> 0) & 0xFF;
				text.setText(("#" + Integer.toHexString(r)
						+ Integer.toHexString(g) + Integer.toHexString(b))
						.toUpperCase());
				dismiss();
			}
		});

		Button cancelButton = (Button) findViewById(R.id.picker_button_cancel);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}