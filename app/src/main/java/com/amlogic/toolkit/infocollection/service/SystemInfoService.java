package com.amlogic.toolkit.infocollection.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.amlogic.toolkit.infocollection.R;
import com.amlogic.toolkit.infocollection.javabean.MemInfoBean;
import com.amlogic.toolkit.infocollection.utils.DensityUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import devlight.io.library.ArcProgressStackView;
import me.itangqi.waveloadingview.WaveLoadingView;

import static com.amlogic.toolkit.infocollection.utils.SystemInfoUtil.getTotalMemory;

/**
 * Created by Wenjie.Chen on 2017/10/31.
 */

public class SystemInfoService extends Service implements View.OnClickListener{

    private static final String TAG = "SystemInfoService";
    private final static int MODEL_COUNT = 4;

    private int[] mStartColors = new int[MODEL_COUNT];
    private int[] mEndColors = new int[MODEL_COUNT];
    //定义浮动窗口布局
    ConstraintLayout mFloatLayout;
    ArcProgressStackView mArcProgressStackView;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;
    private Timer timer = null;
    private SystemInfoServiceTimerTask task = null;
    private final static long mlTimerUnit = 1000;
    private SystemInfoServiceHandler systemInfoServiceHandler;
    float mRawX, mRawY, mStartX, mStartY;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        systemInfoServiceHandler = new SystemInfoServiceHandler();
        createFloatView();
        initArcProgressStackView();

        if(null == timer){
            if (null == task){
                task = new SystemInfoServiceTimerTask();
            }
        }

        timer = new Timer(true);
        timer.schedule(task, mlTimerUnit, mlTimerUnit); // set timer duration
    }

    void initArcProgressStackView() {
        final String[] startColors = getResources().getStringArray(R.array.devlight);
        final String[] endColors = getResources().getStringArray(R.array.default_preview);
        final String[] bgColors = getResources().getStringArray(R.array.medical_express);
        final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();

        for (int i = 0; i < MODEL_COUNT; i++) {
            mStartColors[i] = Color.parseColor(startColors[i]);
            mEndColors[i] = Color.parseColor(endColors[i]);
        }

        models.add(new ArcProgressStackView.Model("Strategy", 0, Color.parseColor(bgColors[0]), mStartColors[0]));
        models.add(new ArcProgressStackView.Model("Design", 0, Color.parseColor(bgColors[1]), mStartColors[1]));
        models.add(new ArcProgressStackView.Model("Development", 0, Color.parseColor(bgColors[2]), mStartColors[2]));
        models.add(new ArcProgressStackView.Model("CPU ALL", 0, Color.parseColor(bgColors[3]), mStartColors[3]));
        mArcProgressStackView.setModels(models);

        for (int i = 0; i < mArcProgressStackView.getModels().size(); i++) {
            final ArcProgressStackView.Model model = mArcProgressStackView.getModels().get(i);
            model.setColor(mStartColors[i]);
            model.setColors(new int[]{mStartColors[i], mEndColors[i]});
        }
        mArcProgressStackView.requestLayout();
        mArcProgressStackView.postInvalidate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mFloatLayout != null)
        {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
        task.cancel();
        task = null;
        timer.cancel(); // Cancel timer
        timer.purge();
        timer = null;
        int pid = android.os.Process.myPid();//获取当前应用程序的PID
        android.os.Process.killProcess(pid);//杀死当前进程
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
        wmParams.gravity = Gravity.START | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = DensityUtil.dip2px(this,300); //WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = DensityUtil.dip2px(this,300);//WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(this);
        //获取浮动窗口视图所在布局
        mFloatLayout = (ConstraintLayout) inflater.inflate(R.layout.system_info_service_layout, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮

        mArcProgressStackView = mFloatLayout.findViewById(R.id.apsv_systeminfo);

        //设置监听浮动窗口的触摸移动
        mFloatLayout.setOnTouchListener(new View.OnTouchListener()
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
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onClick(View view) {
        ComponentName comp = new ComponentName("com.amlogic.toolkit.infocollection",
                "com.amlogic.toolkit.infocollection.service.SystemInfoService");
        Intent intent = new Intent();
        intent.setComponent(comp);
        stopService(intent);
    }



    private class SystemInfoServiceHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            MemInfoBean memInfoBean = (MemInfoBean) msg.obj;
            int progress = memInfoBean.getAvailableProgress();
        }
    }

    private class SystemInfoServiceTimerTask extends TimerTask{

        MemInfoBean memInfoBean = new MemInfoBean();
        @Override
        public void run() {
            getTotalMemory(memInfoBean);
            Log.e(TAG, "run: memTotal = " + memInfoBean.getMemTotal() + " memAvailable = " + memInfoBean.getMemAvailable());
            Message msg = Message.obtain();
            msg.obj = memInfoBean;
            systemInfoServiceHandler.sendMessage(msg);
        }
    }
}
