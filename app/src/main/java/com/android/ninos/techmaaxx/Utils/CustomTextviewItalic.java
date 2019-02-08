package com.android.ninos.techmaaxx.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class CustomTextviewItalic extends AppCompatTextView {

	public CustomTextviewItalic(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomTextviewItalic(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomTextviewItalic(Context context) {
		super(context);
		init();
	}


	public void init() {
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceSansPro-Regular.ttf");
		setTypeface(tf);
	}
}