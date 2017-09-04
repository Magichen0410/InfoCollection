package com.amlogic.toolkit.infocollection.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by Wenjie.Chen on 2017/8/31.
 */

public class MutliVideoView extends VideoView {
    public MutliVideoView(Context context) {
        super(context);
    }

    public MutliVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MutliVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
    }
}
