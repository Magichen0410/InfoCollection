package com.amlogic.toolkit.infocollection.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amlogic.toolkit.infocollection.R;
import com.amlogic.toolkit.infocollection.adapter.SwitchChannelAdapter;
import com.amlogic.toolkit.infocollection.javabean.SwitchChannelInfoBean;
import com.amlogic.toolkit.infocollection.utils.DensityUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Wenjie.Chen on 2017/8/7.
 */

public class SwitchChannelTimeService extends Service {

    private static final String str = "total_time:1690 ms,total_time(no wait keyframe):830 ms,total_time(init->fistpic):1080 ms,total_time(key->firstpic):180 ms,total_time(no press key):1420 ms,press_close_time:270 ms,codec_close_time:10 ms,codec_init_time:330 ms,init_firstcheckin_time:20 ms,firstcheckin_cmd1_time:860 ms,cmd1_firstcheckout_time:0 ms,firstcheckout_decoded_time:80 ms,decoded_frame0_time:10 ms,frame0_frame1_time:0 ms,frame1_frame2_time:80 ms,di2_firstout_time:10 ms,di_firstpic_time:0 ms";
    private static final String initializedData = "total_time:0 ms,total_time(no wait keyframe):0 ms,total_time(init->fistpic):0 ms,total_time(key->firstpic):0 ms,total_time(no press key):0 ms,press_close_time:0 ms,codec_close_time:0 ms,codec_init_time:0 ms,init_firstcheckin_time:0 ms,firstcheckin_cmd1_time:0 ms,cmd1_firstcheckout_time:0 ms,firstcheckout_decoded_time:0 ms,decoded_frame0_time:0 ms,frame0_frame1_time:0 ms,frame1_frame2_time:0 ms,di2_firstout_time:0 ms,di_firstpic_time:0 ms";
    private static final String TAG = "SwitchChannelService";
    private static final String slDugDevName = "/sys/module/di/parameters/time_debug";
//    private static final String slDevName = "/storage/external_storage/sda1/first_frame_toggled";
    private static final String slDevName = "/sys/module/amvideo/parameters/first_frame_toggled";
    private static final String slDevSwitchChannel = "/sys/class/video/timedebug_info";
    private GridView mGridView;
    private ImageView mCloseImageView;
    private Timer timer;
    private long mlTimerUnit = 1000;
    private long mlSwitchTime;
    private TimerTask task = null;
    private SwitchChannelHandler switchChannelHandler;
    private Message msg = null;
    private List<SwitchChannelInfoBean> switchChannelInfoBeans;
    private SwitchChannelAdapter switchChannelAdapter;
    //定义浮动窗口布局
    RelativeLayout mFloatLayout;
    LinearLayout mFloatLayoutChild;

    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;
    float mRawX, mRawY, mStartX, mStartY;

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.i(TAG, "oncreat");
        switchChannelInfoBeans = new ArrayList<SwitchChannelInfoBean>();

        //初始化数据
        initData();
        switchChannelAdapter =  new SwitchChannelAdapter(this,switchChannelInfoBeans);
        createFloatView();

        mGridView.setAdapter(switchChannelAdapter);
        mGridView.setNumColumns(1);
        mGridView.setFocusable(false);

        mCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentName comp = new ComponentName("com.amlogic.toolkit.infocollection",
                        "com.amlogic.toolkit.infocollection.service.SwitchChannelTimeService");
                Intent intent = new Intent();
                intent.setComponent(comp);
                stopService(intent);
            }
        });

        switchChannelHandler = new SwitchChannelHandler();

        if (null == timer) {
            if (null == task) {
                task = new TimerTask() {
                    int ret = 0;
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        ret = OpenFirstFrameDev(slDevName);
                        msg = switchChannelHandler.obtainMessage();
                        if (1 == ret) {
                            msg.obj = OpenDevName(slDevSwitchChannel);
                        } else {
                            msg.obj = null;
                        }
//                        UpdateData(str);
                        switchChannelHandler.sendMessage(msg);
                    }
                };
            }
        }

        StartDug(slDugDevName);
        timer = new Timer(true);
        timer.schedule(task, mlTimerUnit, mlTimerUnit); // set timer duration
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
        wmParams.x = 401;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = DensityUtil.dip2px(this,400); //WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = DensityUtil.dip2px(this,500);//WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(this);
        //获取浮动窗口视图所在布局
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.float_layout, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        mGridView = mFloatLayout.findViewById(R.id.switch_channel_detailinfo);
        mCloseImageView = mFloatLayout.findViewById(R.id.image_view_close);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + mGridView.getMeasuredWidth()/2);
        Log.i(TAG, "Height/2--->" + mGridView.getMeasuredHeight()/2);

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
                    Log.e(TAG, "updateWindowPosition: wmParams.x = " + wmParams.x + " wmParams.y = " + wmParams.y);
                    // 使参数生效
                    mWindowManager.updateViewLayout(mFloatLayout, wmParams);
            }
        });
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(mFloatLayout != null)
        {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }

        int pid = android.os.Process.myPid();//获取当前应用程序的PID
        android.os.Process.killProcess(pid);//杀死当前进程
    }

    private class SwitchChannelHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switchChannelAdapter.notifyDataSetChanged();

        }
    }

    private void UpdateData(String str){

        switchChannelInfoBeans.clear();

        String[] sourceStrArray = str.split(",");
        for (int i = 0; i < sourceStrArray.length; i++) {

            if (i == 2 || i == 3 || i == 4) {
                continue;
            } else {
                SwitchChannelInfoBean switchChannelInfo;
                String[] strArray = (sourceStrArray[i]).split(":");
                //Log.i(TAG, "onCreate: sourceStrArray: " + sourceStrArray[i]);
                //new String()修改equals无法匹配的问题。
                switchChannelInfo = new SwitchChannelInfoBean(new String(strArray[0].trim()), new String (strArray[1].trim()));
                //Log.i(TAG, "onCreate: strArray[0] : " + strArray[0] + " strArray[1] :" + strArray[1]);
                switchChannelInfoBeans.add(switchChannelInfo);
            }
        }
    }

    /*private int OpenFirstFrameDev () {
        int first_frame_flag = 0;
        String strbuf = null;
        strbuf = SysUtils.getFirstFrame();
        //strbuf = "0";
        strbuf.length();
        Log.e(TAG, "OpenFirstFrameDev: strbuf = " + strbuf);
        if (strbuf.equals(null)) {
            first_frame_flag = 0;
        } else {
            //Log.i(TAG, "OpenFirstFrameDev: buffer " + buffer.toString());
            first_frame_flag = Integer.parseInt(strbuf.toString());
        }
        //Log.i(TAG, "OpenFirstFrameDev: first_frame_flag = " + first_frame_flag);
        return first_frame_flag;
    }*/

    private int OpenFirstFrameDev (String slDevName) {
        int first_frame_flag = 0;
        try {
            FileInputStream fis = new FileInputStream(slDevName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr, 4096);
            StringBuffer buffer = new StringBuffer();
            String ch = null;
            {
                while ((ch = br.readLine()) != null)
                    buffer.append(ch);
            }
            br.close();

            if (buffer.toString().equals(null)) {
                //Log.i(TAG, "OpenDevName: buffer is null ");
                first_frame_flag = 0;
            } else {
                //Log.i(TAG, "OpenDevName: buffer = " + buffer.toString());
                first_frame_flag = Integer.parseInt(buffer.toString());
            }
        } catch (FileNotFoundException e) {
            Log.i(TAG, "OpenFirstFrameDev: No file found ");
        } catch (IOException e) {
            Log.i(TAG, "OpenFirstFrameDev: Error reading ", e);
        }

        return first_frame_flag;
    }

    private void StartDug (String DevPath) {
        try {
            FileOutputStream fops = new FileOutputStream(DevPath);
            fops.write("1".getBytes());
            fops.flush();
            fops.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void StopDug (String DevPath) {
        try {
            FileOutputStream fops = new FileOutputStream(DevPath);
            fops.write("0".getBytes());
            fops.flush();
            fops.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int OpenDevName (String DevPath) {
        try {
            FileInputStream fis = new FileInputStream(DevPath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr, 4096);
            StringBuffer buffer = new StringBuffer();
            String ch = null;
            {
                while ((ch = br.readLine()) != null)
                    buffer.append(ch);
            }
            br.close();

            if (buffer.toString().equals(null)) {
                //Log.i(TAG, "OpenDevName: buffer is null ");
                UpdateData(initializedData);
            } else {
                //Log.i(TAG, "OpenDevName: buffer = " + buffer.toString());
                UpdateData(buffer.toString());
            }
        } catch (FileNotFoundException e) {
            Log.i(TAG, "OpenDevName: No file found ");
        } catch (IOException e) {
            Log.i(TAG, "OpenDevName: Error reading ", e);
        }

        return 1;
    }

    private void initData()
    {
        UpdateData(initializedData);
    }


}
