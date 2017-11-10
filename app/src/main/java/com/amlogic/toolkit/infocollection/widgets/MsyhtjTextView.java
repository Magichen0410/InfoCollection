package com.amlogic.toolkit.infocollection.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Wenjie.Chen on 2017/10/17.
 */

public class MsyhtjTextView extends AppCompatTextView {
    public MsyhtjTextView(Context context) {
        this(context, null);
    }

    public MsyhtjTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MsyhtjTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTypeface(FontManager.getTypeface(context));
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
