package com.android.ninos.techmaaxx.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class CustomEditTextviewBold extends AppCompatEditText {

	public CustomEditTextviewBold(Context context, AttributeSet attrs, int defStyle) {
	      super(context, attrs, defStyle);
	      init();
	  }

	 public CustomEditTextviewBold(Context context, AttributeSet attrs) {
	      super(context, attrs);
	      init();
	  }

	 public CustomEditTextviewBold(Context context) {
	      super(context);
	      init();
	 }


	public void init() {
	    Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceSansPro-Bold.ttf");
	    setTypeface(tf);
	}
}