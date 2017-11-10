package com.amlogic.toolkit.infocollection.widgets;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import com.amlogic.toolkit.infocollection.utils.DensityUtil;

/**
 * Created by Wenjie.Chen on 2017/11/10.
 */

public class FloatWindowManager {

    private static final String TAG = "FloatWindowManager";
    private static StopWatchView mStopWatchView;
    private static WindowManager mWindowManager;
    public static WindowManager.LayoutParams mStopWatchWindowParams;

    private static WindowManager getWindowManager(Context context)
    {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    public static void createStopWatchWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        if (mStopWatchView == null) {
            mStopWatchWindowParams = new WindowManager.LayoutParams();
            //设置window type
            mStopWatchWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
            //设置图片格式
            mStopWatchWindowParams.format = PixelFormat.RGBA_8888;
            //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
            mStopWatchWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            //调整悬浮窗显示的停靠位置为左侧置顶
            mStopWatchWindowParams.gravity = Gravity.START | Gravity.TOP;
            mStopWatchWindowParams.width = DensityUtil.dip2px(context,512);//WindowManager.LayoutParams.WRAP_CONTENT;
            mStopWatchWindowParams.height = DensityUtil.dip2px(context,152);
            mStopWatchWindowParams.x = 0;
            mStopWatchWindowParams.y = 401;
        }
        mStopWatchView.setLayoutParams(mStopWatchWindowParams);
        windowManager.addView(mStopWatchView, mStopWatchWindowParams);
    }
}
