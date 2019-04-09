package com.android.ninos.techmaaxx.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class CustomButtonLight extends AppCompatButton {

	public CustomButtonLight(Context context, AttributeSet attrs, int defStyle) {
	      super(context, attrs, defStyle);
	      init();
	  }

	 public CustomButtonLight(Context context, AttributeSet attrs) {
	      super(context, attrs);
	      init();
	  }

	 public CustomButtonLight(Context context) {
	      super(context);
	      init();
	 }


	public void init() {
	    Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
	    setTypeface(tf);
	}
}