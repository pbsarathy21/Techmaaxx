package com.android.ninos.techmaaxx.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by l on 26-09-2018.
 */

public class CustomTextviewTitle extends AppCompatTextView {

    public CustomTextviewTitle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextviewTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextviewTitle(Context context) {
        super(context);
        init();
    }


    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceSansPro-Black.ttf");
        setTypeface(tf);
    }
}
