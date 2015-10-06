package com.colombosoft.ednasalespad.libs.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by thahzan on 1/1/15.
 */
public class RobotoEditText extends EditText {

    Context context;

    public RobotoEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public RobotoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public RobotoEditText(Context context) {
        super(context);
        this.context = context;
    }

    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.NORMAL) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_regular.ttf"));
        } else if (style == Typeface.ITALIC) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_italic.ttf"));
        } else if (style == Typeface.BOLD) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_bold.ttf"));
        }
    }

}
