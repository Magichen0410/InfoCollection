package com.amlogic.toolkit.infocollection.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amlogic.toolkit.infocollection.R;
import com.amlogic.toolkit.infocollection.utils.DensityUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Wenjie.Chen on 2017/8/16.
 */

public class StopWatchService extends Service{

    private long mlCount = 0;
    private long mlTimerUnit = 1;
    private long mlValidationTime = 16; //160毫秒
    private long startMill = 0;
    private TextView tvTime;
    private ImageButton btnStartPause;
    private ImageButton btnStop;
    private Timer timer = null;
    private TimerTask task = null;
    private Timer validationTimer = null;
    private TimerTask validationTask = null;
    private Handler handler = null;
    private Message msg = null;
    private boolean bIsRunningFlg = false;
    private boolean bIsGetFirtSystemTimeFlg = false;
    //定义浮动窗口布局
    private RelativeLayout mFloatLayout;
    private WindowManager.LayoutParams wmParams;
    private ImageView closeImageView;
    //创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;
    private float mRawX, mRawY, mStartX, mStartY;

    private static final int SETTING_SECOND_ID = 101;
    private static final int SETTING_100MILLISECOND_ID = 102;
    private static final int SETTING_1MILLISECOND_ID = 103;
    private int settingTimerUnitFlg = SETTING_1MILLISECOND_ID;
    private static final String TAG = "StopWatchService";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sharedPreferences = getSharedPreferences("mytimer_unit", Context.MODE_PRIVATE);
        mlTimerUnit = sharedPreferences.getLong("time_unit", 1);
        Log.i(TAG, "mlTimerUnit = " + mlTimerUnit);
        //创建悬浮窗
        createFloatView();

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentName comp = new ComponentName("com.amlogic.toolkit.infocollection",
                        "com.amlogic.toolkit.infocollection.service.StopWatchService");
                Intent intent = new Intent();
                intent.setComponent(comp);
                stopService(intent);
            }
        });

        if (1000 == mlTimerUnit) {
            // second
            settingTimerUnitFlg = SETTING_SECOND_ID;
            tvTime.setText(R.string.init_time_second);
        } else if (100 == mlTimerUnit) {
            // 100 millisecond
            settingTimerUnitFlg = SETTING_100MILLISECOND_ID;
            tvTime.setText(R.string.init_time_100millisecond);
        }else if (1 == mlTimerUnit) {
            // 1 millisecond
            settingTimerUnitFlg = SETTING_1MILLISECOND_ID;
            tvTime.setText(R.string.init_time_1millisecond);
        }

        // Handle timer message
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch(msg.what) {
                    case 1:
                        mlCount++;
                        int totalSec = 0;
                        int yushu = 0;

                        if (SETTING_SECOND_ID == settingTimerUnitFlg) {
                            // second
                            totalSec = (int)(mlCount);
                        } else if (SETTING_100MILLISECOND_ID == settingTimerUnitFlg) {
                            // 100 millisecond
                            totalSec = (int)(mlCount / 10);
                            yushu = (int)(mlCount % 10);
                        } else if (SETTING_1MILLISECOND_ID == settingTimerUnitFlg) {
                            totalSec = (int)(mlCount / 1000);
                            yushu = (int)(mlCount % 1000);
                        }
                        // Set time display
                        int sec = ((totalSec % 3600)%60);
                        int min = ((totalSec %3600)/ 60);
                        int hour = (totalSec/3600);
                        try{
                            if (SETTING_SECOND_ID == settingTimerUnitFlg) {
                                // second(1000ms)
                                tvTime.setText(String.format("%1$02d:%2$02d:%3$02d",
                                        hour, min, sec));
                            } else if (SETTING_100MILLISECOND_ID == settingTimerUnitFlg) {
                                // 100 millisecond
                                tvTime.setText(String.format("%1$02d:%2$02d:%3$02d.%4$d",
                                        hour, min, sec, yushu));
                            } else if (SETTING_1MILLISECOND_ID == settingTimerUnitFlg) {
                                //1 millisecond
                                tvTime.setText(String.format("%1$02d:%2$02d:%3$02d.%4$03d",
                                        hour, min, sec, yushu));
                            }
                        } catch(Exception e) {
                            tvTime.setText("" + min + ":" + sec + ":" + yushu);
                            e.printStackTrace();
                            Log.e("MyTimer onCreate", "Format string error.");
                        }
                        break;

                    default:
                        break;
                }
                super.handleMessage(msg);
            }

        };

        if (!bIsGetFirtSystemTimeFlg) {
            startMill = System.currentTimeMillis();
        }

        Log.i(TAG, "startMill: " + startMill);

        if (null == timer) {
            if (null == task) {
                task = new TimerTask() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (null == msg) {
                            msg = new Message();
                        } else {
                            msg = Message.obtain();
                        }
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                };
            }

            if (null == validationTimer) {
                if (null == validationTask) {
                    validationTask = new TimerTask() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            long mill = System.currentTimeMillis();
                            long millsec = mill - startMill;
//                                Log.i(MYTIMER_TAG, "millsec: " + millsec);
//                                Log.i(MYTIMER_TAG, "mlCount: " + mlCount);
                            if (mlCount < millsec) {
                                mlCount = millsec;
                            }
                        }
                    };
                }
            }

            timer = new Timer(true);
            timer.schedule(task, mlTimerUnit, mlTimerUnit); // set timer duration

            validationTimer = new Timer(true);
            validationTimer.schedule(validationTask, mlValidationTime, mlValidationTime);
        }
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
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 401;

        //设置悬浮窗口长宽数据
        wmParams.width = DensityUtil.dip2px(this,512); //WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = DensityUtil.dip2px(this,152);//WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(this);
        //获取浮动窗口视图所在布局
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.stopwatch_float_view, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        tvTime = mFloatLayout.findViewById(R.id.stop_watch_timer);
        closeImageView = mFloatLayout.findViewById(R.id.stopwatch_close);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + tvTime.getMeasuredWidth()/2);
        Log.i(TAG, "Height/2--->" + tvTime.getMeasuredHeight()/2);

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

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: service");
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
        validationTask.cancel();
        validationTask = null;
        validationTimer.cancel();
        validationTimer.purge();
        validationTimer = null;
        handler.removeMessages(msg.what);
        int pid = android.os.Process.myPid();//获取当前应用程序的PID
        android.os.Process.killProcess(pid);//杀死当前进程
    }
}
