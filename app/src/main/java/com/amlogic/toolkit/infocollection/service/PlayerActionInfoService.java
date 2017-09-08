package com.amlogic.toolkit.infocollection.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.amlogic.toolkit.infocollection.R;
import com.amlogic.toolkit.infocollection.receiver.PlayerActionInfoReceiver;
import com.amlogic.toolkit.infocollection.utils.DensityUtil;

public class PlayerActionInfoService extends Service {

    private static final String TAG = "PlayerActionInfoService";
    private GridView mGridView;
    //定义浮动窗口布局
    LinearLayout mFloatLayout;
    LinearLayout mFloatLayoutChild;

    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;

    private IntentFilter intentFileter;
    private PlayerActionInfoReceiver playerActionInfoReceiver;

    public PlayerActionInfoService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        intentFileter = new IntentFilter();
        intentFileter.addAction("MEDIA_PLAY_MONITOR_MESSAGE");
        playerActionInfoReceiver = new PlayerActionInfoReceiver();
        registerReceiver(playerActionInfoReceiver,intentFileter);
    }

    private void createFloatView()
    {
        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        Log.i(TAG, "mWindowManager--->" + mWindowManager);
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.RIGHT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = DensityUtil.dip2px(this,400); //WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = DensityUtil.dip2px(this,400);//WindowManager.LayoutParams.WRAP_CONTENT;

		 /*// 设置悬浮窗口长宽数据
        wmParams.width = 200;
        wmParams.height = 80;*/

        LayoutInflater inflater = LayoutInflater.from(this);
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮

        //mFloatLayoutChild = (LinearLayout) mFloatLayout.getChildAt(1);
        //mGridView = (GridView) mFloatLayoutChild.getChildAt(0);

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(playerActionInfoReceiver);
        super.onDestroy();
    }
}
