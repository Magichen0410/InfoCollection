package com.amlogic.toolkit.infocollection.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amlogic.toolkit.infocollection.R;
import com.amlogic.toolkit.infocollection.javabean.MainMenuBean;
import com.amlogic.toolkit.infocollection.utils.DensityUtil;
import com.amlogic.toolkit.infocollection.utils.OtherUtil;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

/**
 * Created by Wenjie.Chen on 2017/10/25.
 */

public class FloatMenuService extends Service implements View.OnClickListener{


    private static final String TAG = "FloatMenuService";
    private ArrayList<MainMenuBean> mainMenuBeanArrayList = null;
    //定义浮动窗口布局
    RelativeLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;
    float mRawX, mRawY, mStartX, mStartY;
    FloatingActionsMenu mFabMenu;
    FloatingActionButton mFabStopWatchButton, mFabPlayerInfoButton, mFabSystemInfoButton;
    FloatingActionButton mFabChangeChannel, mFabMutliPlayerButton, mFabCloseButton;
    private int serviceId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: startId " + startId );
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createFloatView();
        mainMenuBeanArrayList = new ArrayList<MainMenuBean>();
        initMenu();

        mFabStopWatchButton.setOnClickListener(this);
        mFabPlayerInfoButton.setOnClickListener(this);
        mFabSystemInfoButton.setOnClickListener(this);
        mFabChangeChannel.setOnClickListener(this);
        mFabMutliPlayerButton.setOnClickListener(this);
        mFabCloseButton.setOnClickListener(this);
    }

    private void initMenu() {
        mainMenuBeanArrayList.add(new MainMenuBean("com.amlogic.toolkit.infocollection",
                "com.amlogic.toolkit.infocollection.service.StopWatchService", "秒表计时器",
                true));
        mainMenuBeanArrayList.add(new MainMenuBean("com.amlogic.toolkit.infocollection",
                "com.amlogic.toolkit.infocollection.service.PlayerActionInfoService",
                "播放信息监听", true));
        mainMenuBeanArrayList.add(new MainMenuBean("com.amlogic.toolkit.infocollection",
                "com.amlogic.toolkit.infocollection.service.SystemInfoService",
                "系统信息", true));
        mainMenuBeanArrayList.add(new MainMenuBean("com.amlogic.toolkit.infocollection",
                "com.amlogic.toolkit.infocollection.service.SwitchChannelTimeService", "切台统计服务",
                true));
        mainMenuBeanArrayList.add(new MainMenuBean("com.amlogic.toolkit.infocollection",
                "com.amlogic.toolkit.infocollection.ui.mediaplayer.MutliMediaPlayerActivity",
                "多实例播放器", false));
        mainMenuBeanArrayList.add(new MainMenuBean("com.amlogic.toolkit.infocollection",
                "com.amlogic.toolkit.infocollection.service.FloatMenuService",
                "退出", true));
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
        wmParams.gravity = Gravity.END | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = DensityUtil.dip2px(this,200); //WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = DensityUtil.dip2px(this,WindowManager.LayoutParams.WRAP_CONTENT);//WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(this);
        //获取浮动窗口视图所在布局
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.float_menu_service_layout, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮

        mFabMenu = mFloatLayout.findViewById(R.id.button_tool_menu);
        mFabStopWatchButton = mFloatLayout.findViewById(R.id.button_stopwatch);
        mFabPlayerInfoButton = mFloatLayout.findViewById(R.id.button_playerinfo);
        mFabSystemInfoButton = mFloatLayout.findViewById(R.id.button_systeminfo);
        mFabChangeChannel = mFloatLayout.findViewById(R.id.button_changechannel);
        mFabMutliPlayerButton = mFloatLayout.findViewById(R.id.button_mutliplayer);
        mFabCloseButton = mFloatLayout.findViewById(R.id.button_quit);

        //设置监听浮动窗口的触摸移动
        /*mFloatLayout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                mRawX = event.getRawX();
                mRawY = event.getRawY();

                final int action = event.getAction();

                switch (action) {
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
                return false;  //此处必须返回false，否则OnClickListener获取不到监听
            }

            private void updateWindowPosition() {
                // 更新坐标
                wmParams.x = (int)(mRawX - mStartX);
                wmParams.y = (int)(mRawY - mStartY);

                // 使参数生效
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
            }
        });*/
    }

    private void startOrCloseFunction(String packageName, String className, boolean isService) {
        ComponentName comp = new ComponentName(packageName, className);
        Intent intent=new Intent();
        if (isService) {
            if (OtherUtil.isServiceRunning(getApplicationContext(), className)) {
                Toast.makeText(getApplicationContext(), "关闭Service", Toast.LENGTH_SHORT).show();
                intent.setComponent(comp);
                stopService(intent);
            } else {
                Toast.makeText(getApplicationContext(), "启动Service", Toast.LENGTH_SHORT).show();
                intent.setComponent(comp);
                startService(intent);
            }
        } else {
            //Toast.makeText(getApplicationContext(), "启动Activity", Toast.LENGTH_SHORT).show();
            intent.setComponent(comp);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        int position = 0;
        position = mFabMenu.indexOfChild(view);
        String packageName = mainMenuBeanArrayList.get(position).getPackageNmae();
        String className = mainMenuBeanArrayList.get(position).getClassName();
        boolean isService = mainMenuBeanArrayList.get(position).getIsService();
        Log.e(TAG, "onClick: postion = " + position + " isService: " + isService);
        if (position == 5)
        {
            //Log.e(TAG, "onClick: serviceId = " + serviceId );
            ComponentName comp = new ComponentName("com.amlogic.toolkit.infocollection",
                    "com.amlogic.toolkit.infocollection.service.FloatMenuService");
            Intent intent = new Intent();
            intent.setComponent(comp);
            stopService(intent);
        } else {

            startOrCloseFunction(packageName, className, isService);
        }

    }

    @Override
    public void onDestroy() {
        //mFabMenu.removeViews(0,6);
        //mWindowManager.removeView(mFloatLayout);
        //mFabMenu.destroyDrawingCache();
        super.onDestroy();
        if(mFloatLayout != null)
        {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
        int pid = android.os.Process.myPid();//获取当前应用程序的PID
        android.os.Process.killProcess(pid);//杀死当前进程
    }
}
