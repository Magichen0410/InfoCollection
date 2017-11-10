package com.amlogic.toolkit.infocollection.widgets;

import android.content.Context;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Wenjie.Chen on 2017/11/9.
 */

public class StopWatchView extends RelativeLayout {

    private static final String TAG = "StopWatchView";

    private WindowManager.LayoutParams wmParams;
    private ImageView closeImageView;
    //创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;
    private float mRawX, mRawY, mStartX, mStartY;

    public StopWatchView(Context context) {
        super(context);
        mWindowManager = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);

    }

    public void setLayoutParams(WindowManager.LayoutParams layoutParams)
    {
        this.wmParams = layoutParams;
    }

    private void updateWindowPosition() {
        // 更新坐标
        wmParams.x = (int)(mRawX - mStartX);
        wmParams.y = (int)(mRawY - mStartY);

        // 使参数生效
        mWindowManager.updateViewLayout(this, wmParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 以当前父视图左上角为原点
                mStartX = event.getX();
                mStartY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                updateWindowPosition();
                break;

            case MotionEvent.ACTION_UP:
                updateWindowPosition();
                break;
        }
        return false;
    }
}
