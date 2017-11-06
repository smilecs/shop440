package com.shop440.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.shop440.R;
import com.shop440.Application;

/**
 * Created by mmumene on 11/07/2017.
 */

public class FontTextView extends AppCompatTextView{
    public FontTextView(Context context) {
        super(context);
        init(context, null);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /*public void setFont(String fontId) {
        Typeface font = Application.getFont(fontId);
        if (font != null) {
            setTypeface(font);
        }
    }*/

    private void init(Context context, AttributeSet attributeSet){
        if (isInEditMode()) {
            return;
        }
        if (attributeSet == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.FontTextView, 0, 0);
        try {
            if (a.hasValue(R.styleable.FontTextView_fontName)) {
                int fontId = a.getInt(R.styleable.FontTextView_fontName, 1);
                Typeface font = Application.Companion.getFont(fontId);
                if (font != null) {
                    setTypeface(font);
                }
            }
        } finally {
            a.recycle();
        }
    }
}
