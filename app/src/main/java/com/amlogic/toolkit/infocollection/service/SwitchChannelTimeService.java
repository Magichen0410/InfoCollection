package com.amlogic.toolkit.infocollection.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;

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
    private static final String slDevName = "/sys/module/amvideo/parameters/first_frame_toggled";
//    private static final String slDevName = "/storage/external_storage/sda1/first_frame_toggled";
    private static final String slDevSwitchChannel = "/sys/class/video/timedebug_info";
    private GridView mGridView;
    private Timer timer;
    private long mlTimerUnit = 1000;
    private long mlSwitchTime;
    private TimerTask task = null;
    private SwitchChannelHandler switchChannelHandler;
    private Message msg = null;
    private List<SwitchChannelInfoBean> switchChannelInfoBeans;
    private SwitchChannelAdapter switchChannelAdapter;
    //定义浮动窗口布局
    LinearLayout mFloatLayout;
    LinearLayout mFloatLayoutChild;

    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;



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

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
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

        mFloatLayoutChild = (LinearLayout) mFloatLayout.getChildAt(1);
        mGridView = (GridView) mFloatLayoutChild.getChildAt(0);

/*        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + mGridView.getMeasuredWidth()/2);
        Log.i(TAG, "Height/2--->" + mGridView.getMeasuredHeight()/2);*/

/*        //设置监听浮动窗口的触摸移动
        mGridView.setOnTouchListener(new View.OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                // TODO Auto-generated method stub
                //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                wmParams.x = (int) event.getRawX() - mGridView.getMeasuredWidth()/2;
                Log.i(TAG, "RawX" + event.getRawX());
                Log.i(TAG, "X" + event.getX());
                //减25为状态栏的高度
                wmParams.y = (int) event.getRawY() - mGridView.getMeasuredHeight()/2 - 25;
                Log.i(TAG, "RawY" + event.getRawY());
                Log.i(TAG, "Y" + event.getY());
                //刷新
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return false;  //此处必须返回false，否则OnClickListener获取不到监听
            }
        });*/

       /* mGridView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                Toast.makeText(SwitchChannelTimeService.this, "onClick", Toast.LENGTH_SHORT).show();
            }
        });*/
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

            if (i == 2 || i == 3 || i ==4) {
                continue;
            } else {
                SwitchChannelInfoBean switchChannelInfo;
                String[] strArray = sourceStrArray[i].split(":");
                //Log.i(TAG, "onCreate: sourceStrArray: " + sourceStrArray[i]);
                switchChannelInfo = new SwitchChannelInfoBean(strArray[0], strArray[1]);
                //Log.i(TAG, "onCreate: strArray[0] : " + strArray[0] + " strArray[1] :" + strArray[1]);
                switchChannelInfoBeans.add(switchChannelInfo);
            }
        }
    }

    private int OpenFirstFrameDev (String DevPath) {
        int first_frame_flag = 0;
        try {
            FileInputStream fis = new FileInputStream(DevPath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr, 4096);
            StringBuffer buffer = new StringBuffer();
            String ch = null;
            {
                while ((ch = br.readLine()) != null) {
                    buffer.append(ch);
                }
            }
            br.close();
            if (buffer.toString().equals(null)) {
                first_frame_flag = 0;
            } else {
                //Log.i(TAG, "OpenFirstFrameDev: buffer " + buffer.toString());
                first_frame_flag = Integer.parseInt(buffer.toString());
            }
        } catch (FileNotFoundException e) {
            Log.i(TAG, "OpenFirstFrameDev: No file found ");
        } catch (IOException e) {
            Log.i(TAG, "OpenFirstFrameDev: Error reading ", e);
        }
        //Log.i(TAG, "OpenFirstFrameDev: first_frame_flag = " + first_frame_flag);
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
