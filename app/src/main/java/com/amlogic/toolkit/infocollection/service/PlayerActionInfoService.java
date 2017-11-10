package com.amlogic.toolkit.infocollection.service;

import android.Manifest;
import android.app.ActionBar;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.amlogic.toolkit.infocollection.R;
import com.amlogic.toolkit.infocollection.constant.PlayerActionInfoConstant;
import com.amlogic.toolkit.infocollection.global.PlayerInfoGlobal;
import com.amlogic.toolkit.infocollection.javabean.PlayerBaseInfoBean;
import com.amlogic.toolkit.infocollection.javabean.PlayingInfoBean;
import com.amlogic.toolkit.infocollection.receiver.PlayerActionInfoReceiver;
import com.amlogic.toolkit.infocollection.receiver.PlayerActionInfoUnicomReceiver;
import com.amlogic.toolkit.infocollection.utils.DensityUtil;
import com.amlogic.toolkit.infocollection.widgets.MsyhtjTextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.amlogic.toolkit.infocollection.utils.OtherUtil.secToTime;

public class PlayerActionInfoService extends Service {

    private static final String TAG = "PlayerActionInfoService";
    private static final String APTS_NODE = "/sys/class/tsync/pts_audio";
    private static final String VPTS_NODE = "/sys/class/tsync/pts_video";
    private static final String PCR_NODE = "/sys/class/tsync/pts_pcrscr";
    private static final long CACHE_TIME_INTERVAL = 1000;
    //定义浮动窗口布局
    private ConstraintLayout mFloatLayout;
    private LinearLayout mPtsFloatLayout;
    private TableLayout tablePtsLayout, tableUserLayout, tablePlayerInfoLayout;
    private TextView idText, urlText, durationText, startTimeText, firstBufferingTimeText;
    private TextView resolutionRatioText, bufferingPlaytimeText, bufferingTimesText;
    private TextView currentBufferingAvgTimeText, audioFormat, audioChannel, audioSampleRate;
    private TextView videoFormat, videoFrameRate, videoUnload, blurredScreenTimes, bitRateChangeTimes;
    private TextView videoProgressText, audioNumberText, currentAudioText, videoDecoderErrorsText;
    private TextView audioDecoderErrorsText;
    private ImageView closeImageView;
    private Button startPtsMonitor, stopPtsMonitor;
    private Timer timer;
    private long mlTimerUnit = 1000;
    private TimerTask task = null;
    private boolean ptsFloatflg = false;

    private WindowManager.LayoutParams wmParams, wmPtsParams;
    //创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager, mPtsWindowManager;
    private float mRawX, mRawY, mStartX, mStartY;
    private float mPtsRawX, mPtsRawY, mPtsStartX, mPtsStartY;

    private IntentFilter intentMobileFileter;
    private IntentFilter intentUnicomFileter;
    private PlayerActionInfoReceiver playerActionInfoReceiver;
    private PlayerActionInfoUnicomReceiver playerActionInfoUnicomReceiver;
    private Handler playerActionInfoHandler = new PlayerActionInfoHandler();
    private Handler playingInfoHandler = new PlayingInfoHandler();

    public PlayerActionInfoService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        startUnicomProbeDug();
        setCacheTimeInterval(CACHE_TIME_INTERVAL);
        intentMobileFileter = new IntentFilter();
        intentMobileFileter.addAction("MEDIA_PLAY_MONITOR_MESSAGE");
        intentUnicomFileter = new IntentFilter();
        intentUnicomFileter.addAction("MEDIA_PLAY_MONITOR_MESSAGE_UNICOM");
        playerActionInfoReceiver = new PlayerActionInfoReceiver(this, playerActionInfoHandler);
        playerActionInfoUnicomReceiver = new PlayerActionInfoUnicomReceiver(this, playerActionInfoHandler);
        registerReceiver(playerActionInfoReceiver,intentMobileFileter);
        registerReceiver(playerActionInfoUnicomReceiver,intentUnicomFileter);
        createFloatView();

        startPtsMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ptsFloatflg) {
                    Toast.makeText(getApplicationContext(), "pts监控已经启动", Toast.LENGTH_SHORT).show();
                } else {
                    createPtsFloatView();
                    createPtsTimer();
                }
            }
        });

        stopPtsMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ptsFloatflg) {
                    Toast.makeText(getApplicationContext(), "pts监控未启动", Toast.LENGTH_SHORT).show();
                } else {
                    destoryPtsMonitor();
                }
            }
        });
    }

    private void startUnicomProbeDug() {
        try {
            Runtime.getRuntime().exec("setprop media.player.report_enable 1");
            Runtime.getRuntime().exec("media.report.unicom true");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void stopUnicomProbeDug() {
        try {
            Runtime.getRuntime().exec("setprop media.player.report_enable 0");
            Runtime.getRuntime().exec("media.report.unicom false");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setCacheTimeInterval(long millisecond){
        try {
            Runtime.getRuntime().exec("media.amplayer.cachetime_interval " + millisecond);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updatePtsData(String aPts, String vPts, String pcr) {
        PlayerBaseInfoBean playerBaseInfoBean = new PlayerBaseInfoBean();
        Message msg = Message.obtain();
        msg.what = PlayerActionInfoConstant.PTS_INFO;
        playerBaseInfoBean.setApts(aPts);
        playerBaseInfoBean.setVpts(vPts);
        playerBaseInfoBean.setPcr(pcr);
        msg.obj = playerBaseInfoBean;
        playerActionInfoHandler.sendMessage(msg);
    }

    private void OpenPtsNode() {
        try {
            FileInputStream audioFis = new FileInputStream(APTS_NODE);
            InputStreamReader audioIsr = new InputStreamReader(audioFis);
            BufferedReader audioBr = new BufferedReader(audioIsr, 4096);
            StringBuffer audioBuffer = new StringBuffer();
            String audioCh = null;
            {
                while ((audioCh = audioBr.readLine()) != null)
                    audioBuffer.append(audioCh);
            }

            FileInputStream videoFis = new FileInputStream(VPTS_NODE);
            InputStreamReader videoIsr = new InputStreamReader(videoFis);
            BufferedReader videoBr = new BufferedReader(videoIsr, 4096);
            StringBuffer videoBuffer = new StringBuffer();
            String videoCh = null;
            {
                while ((videoCh = videoBr.readLine()) != null)
                    videoBuffer.append(videoCh);
            }

            FileInputStream pcrFis = new FileInputStream(PCR_NODE);
            InputStreamReader pcrIsr = new InputStreamReader(pcrFis);
            BufferedReader pcrBr = new BufferedReader(pcrIsr, 4096);
            StringBuffer pcrBuffer = new StringBuffer();
            String pcrCh = null;
            {
                while ((pcrCh = pcrBr.readLine()) != null)
                    pcrBuffer.append(pcrCh);
            }

            pcrBr.close();
            videoBr.close();
            audioBr.close();

            if (audioBuffer.toString().equals(null)) {
                //Log.i(TAG, "OpenDevName: buffer is null ");
                updatePtsData("0xffffffff","0x0","0x0");
            } else {
                //Log.i(TAG, "OpenDevName: buffer = " + buffer.toString());
                updatePtsData(audioBuffer.toString(), videoBuffer.toString(), pcrBuffer.toString());
            }
        } catch (FileNotFoundException e) {
            Log.i(TAG, "OpenDevName: No file found ");
        } catch (IOException e) {
            Log.i(TAG, "OpenDevName: Error reading ", e);
        }
    }

    private void createPtsTimer(){
        if (null == timer) {
            if (null == task) {
                task = new TimerTask() {
                    int ret = 0;
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        OpenPtsNode();
                        Log.e(TAG, "pts: get pts info");
                    }
                };
            }
        }

        timer = new Timer(true);
        timer.schedule(task, mlTimerUnit, mlTimerUnit); // set timer duration
    }

    private void destoryPtsMonitor() {
        ptsFloatflg = false;
        if(mPtsFloatLayout != null)
        {
            //移除悬浮窗口
            mPtsWindowManager.removeView(mPtsFloatLayout);
        }
        task.cancel();
        task = null;
        timer.cancel(); // Cancel timer
        timer.purge();
        timer = null;
    }

    private void createPtsFloatView()
    {
        ptsFloatflg = true;
        wmPtsParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mPtsWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        Log.i(TAG, "mWindowManager--->" + mPtsWindowManager);
        //设置window type
        wmPtsParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        //设置图片格式，效果为背景透明
        wmPtsParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmPtsParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmPtsParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmPtsParams.x = 0;
        wmPtsParams.y = DensityUtil.dip2px(this,416);

        //设置悬浮窗口长宽数据
        wmPtsParams.width = DensityUtil.dip2px(this,470); //WindowManager.LayoutParams.WRAP_CONTENT;
        wmPtsParams.height = DensityUtil.dip2px(this,192);//WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(this);
        //获取浮动窗口视图所在布局
        mPtsFloatLayout = (LinearLayout) inflater.inflate(R.layout.pts_monitor_layout, null);
        //添加mFloatLayout
        mPtsWindowManager.addView(mPtsFloatLayout, wmPtsParams);
        //浮动窗口按钮

        tablePtsLayout  = mPtsFloatLayout.findViewById(R.id.tablePtsLayout);
        addPtsInfo("APts", "VPts", "Pcr");

        //设置监听浮动窗口的触摸移动
        mPtsFloatLayout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                mPtsRawX = event.getRawX();
                mPtsRawY = event.getRawY();

                final int action = event.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // 以当前父视图左上角为原点
                        mPtsStartX = event.getX();
                        mPtsStartY = event.getY();
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
                wmPtsParams.x = (int)(mPtsRawX - mPtsStartX);
                wmPtsParams.y = (int)(mPtsRawY - mPtsStartY);

                // 使参数生效
                mPtsWindowManager.updateViewLayout(mPtsFloatLayout, wmPtsParams);
            }
        });
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
        wmParams.width = DensityUtil.dip2px(this,908); //WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = DensityUtil.dip2px(this,416);//WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(this);
        //获取浮动窗口视图所在布局
        mFloatLayout = (ConstraintLayout) inflater.inflate(R.layout.player_action_info_service_layout, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮

        startPtsMonitor = mFloatLayout.findViewById(R.id.pts_info);
        stopPtsMonitor = mFloatLayout.findViewById(R.id.close_pts_info);
        idText = mFloatLayout.findViewById(R.id.playerid);
        urlText = mFloatLayout.findViewById(R.id.playerurl);
        startTimeText = mFloatLayout.findViewById(R.id.playerstarttime);
        durationText = mFloatLayout.findViewById(R.id.playerduration);
        audioFormat = mFloatLayout.findViewById(R.id.audioformat);
        audioChannel = mFloatLayout.findViewById(R.id.audiochannel);
        audioNumberText = mFloatLayout.findViewById(R.id.audionumber);
        currentAudioText = mFloatLayout.findViewById(R.id.currentaudio);
        audioSampleRate = mFloatLayout.findViewById(R.id.audiosamplerate);
        videoFormat = mFloatLayout.findViewById(R.id.videoformat);
        videoFrameRate = mFloatLayout.findViewById(R.id.videoframerate);
        firstBufferingTimeText = mFloatLayout.findViewById(R.id.firstbuffering);
        resolutionRatioText = mFloatLayout.findViewById(R.id.resolutionratio);
        videoProgressText = mFloatLayout.findViewById(R.id.videoprogress);
        //bufferingPlaytimeText = mFloatLayout.findViewById(R.id.buffering_playtime);
        blurredScreenTimes = mFloatLayout.findViewById(R.id.blurredscreentimes);
        bufferingTimesText = mFloatLayout.findViewById(R.id.bufferingtimes);
        bitRateChangeTimes = mFloatLayout.findViewById(R.id.bitratechange);
        currentBufferingAvgTimeText = mFloatLayout.findViewById(R.id.current_buffering_average_time);
        videoUnload = mFloatLayout.findViewById(R.id.video_unload);
        videoDecoderErrorsText = mFloatLayout.findViewById(R.id.video_decoder_errors);
        audioDecoderErrorsText = mFloatLayout.findViewById(R.id.audio_decoder_errors);
        closeImageView = mFloatLayout.findViewById(R.id.imageView);
        tableUserLayout = mFloatLayout.findViewById(R.id.tableUserLayout);
        addUserTableRow("操作名称", "操作时间", "操作结果", "播放时间");
        tablePlayerInfoLayout = mFloatLayout.findViewById(R.id.tablePlayerInfoLayout);
        addPlayInfoTableRow("播放状态", "播放时间", "audio缓存", "video缓存", "可播时长");

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentName comp = new ComponentName("com.amlogic.toolkit.infocollection",
                        "com.amlogic.toolkit.infocollection.service.PlayerActionInfoService");
                Intent intent = new Intent();
                intent.setComponent(comp);
                stopService(intent);
            }
        });

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

    private void deInit(){
        idText.setText("0");
        urlText.setText("null");
        startTimeText.setText("null");
        durationText.setText("0");
        audioFormat.setText("null");
        audioChannel.setText("null");
        audioNumberText.setText("0");
        currentAudioText.setText("0");
        audioSampleRate.setText("0");
        videoFormat.setText("null");
        videoFrameRate.setText("0");
        firstBufferingTimeText.setText("0");
        resolutionRatioText.setText("null");
        videoProgressText.setText("null");
        //bufferingPlaytimeText.setText("0");
        blurredScreenTimes.setText("0");
        bitRateChangeTimes.setText("0");
        bufferingTimesText.setText("0");
        currentBufferingAvgTimeText.setText("0");
        videoUnload.setText("0");
        videoDecoderErrorsText.setText("0");
        audioDecoderErrorsText.setText("0");
        tableUserLayout.removeViews(1,(tableUserLayout.getChildCount() - 1));
        tablePlayerInfoLayout.removeViews(1,(tablePlayerInfoLayout.getChildCount() - 1));
    }

    private void setTextViewStyle(TextView textView, String str){
        setTextBold(textView);
        if ( str.equals("操作名称") || str.equals("操作时间") || str.equals("操作结果") ||
                str.equals("播放时间") || str.equals("播放状态") || str.equals("audio缓存") ||
                str.equals("video缓存") || str.equals("可播时长(s)") || str.equals("APts") ||
                str.equals("VPts") || str.equals("Pcr")) {
            textView.setTextColor(getBaseContext().getResources().getColorStateList(R.color.black));
        } else {
            textView.setTextColor(getBaseContext().getResources().getColorStateList(R.color.white));
        }
        textView.setTextSize(14);
        textView.setText(str);
        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
    }

    private void setTextBold(TextView textView){
        TextPaint tp = textView.getPaint();
        tp.setFakeBoldText(true);
    }

    private void addPtsInfo(String aPts, String vPts, String pcr) {
        TableRow tableRow = new TableRow(PlayerActionInfoService.this);
        TableRow.LayoutParams tableRowParam = new TableRow.LayoutParams();
        if ( aPts.equals("APts")) {
            tableRow.setBackgroundResource(R.color.powderblue);
        }
        tableRowParam.leftMargin = DensityUtil.dip2px(PlayerActionInfoService.this,6);
        tableRowParam.rightMargin = DensityUtil.dip2px(PlayerActionInfoService.this,6);
        tableRow.setLayoutParams(tableRowParam);

        TextView opAPts = new MsyhtjTextView(PlayerActionInfoService.this);
        setTextViewStyle(opAPts, aPts);
        opAPts.setGravity(Gravity.CENTER);
        tableRow.addView(opAPts);

        TextView opVPts = new MsyhtjTextView(PlayerActionInfoService.this);
        setTextViewStyle(opVPts, vPts);
        opVPts.setGravity(Gravity.CENTER);
        tableRow.addView(opVPts);

        TextView opPcr = new MsyhtjTextView(PlayerActionInfoService.this);
        setTextViewStyle(opPcr, pcr);
        opPcr.setGravity(Gravity.CENTER);
        tableRow.addView(opPcr);

        if ( tablePtsLayout.getChildCount() == 6){
            tablePtsLayout.removeViews(1,1);
        }
        tablePtsLayout.addView(tableRow);
    }

    private void addUserTableRow(String name, String time, String result, String playTime){
        TableRow tableRow = new TableRow(PlayerActionInfoService.this);
        TableRow.LayoutParams tableRowParam = new TableRow.LayoutParams();
        if ( name.equals("操作名称")) {
            tableRow.setBackgroundResource(R.color.powderblue);
        }
        tableRowParam.leftMargin = DensityUtil.dip2px(PlayerActionInfoService.this,6);
        tableRowParam.rightMargin = DensityUtil.dip2px(PlayerActionInfoService.this,6);
        tableRow.setLayoutParams(tableRowParam);

        TextView opName = new MsyhtjTextView(PlayerActionInfoService.this);
        setTextViewStyle(opName, name);
        tableRow.addView(opName);

        TextView opTime = new MsyhtjTextView(PlayerActionInfoService.this);
        setTextViewStyle(opTime, time);
        tableRow.addView(opTime);

        TextView opResult = new MsyhtjTextView(PlayerActionInfoService.this);
        setTextViewStyle(opResult, result);
        tableRow.addView(opResult);

        TextView opPlayTime = new MsyhtjTextView(PlayerActionInfoService.this);
        setTextViewStyle(opPlayTime, playTime);
        tableRow.addView(opPlayTime);

        if ( tableUserLayout.getChildCount() == 6){
            tableUserLayout.removeViews(1,1);
        }
        tableUserLayout.addView(tableRow);
    }

    private void addPlayInfoTableRow(String status, String playTime,
                                     String audioBuffering, String videoBuffering,
                                     String buffering){
        TableRow tableRow = new TableRow(PlayerActionInfoService.this);
        TableRow.LayoutParams tableRowParam = new TableRow.LayoutParams();
        if ( status.equals("播放状态")) {
            tableRow.setBackgroundResource(R.color.powderblue);
        }
        tableRowParam.leftMargin = DensityUtil.dip2px(PlayerActionInfoService.this,6);
        tableRowParam.rightMargin = DensityUtil.dip2px(PlayerActionInfoService.this,6);
        tableRow.setLayoutParams(tableRowParam);

        TextView playStatus = new MsyhtjTextView(PlayerActionInfoService.this);
        setTextViewStyle(playStatus, status);
        tableRow.addView(playStatus);

        TextView playSeconds = new MsyhtjTextView(PlayerActionInfoService.this);
        setTextViewStyle(playSeconds, playTime);
        tableRow.addView(playSeconds);

        TextView audioBuffer = new MsyhtjTextView(PlayerActionInfoService.this);
        setTextViewStyle(audioBuffer, audioBuffering);
        tableRow.addView(audioBuffer);

        TextView videoBuffer = new MsyhtjTextView(PlayerActionInfoService.this);
        setTextViewStyle(videoBuffer, videoBuffering);
        tableRow.addView(videoBuffer);

        /*TextView aPtsInfo = new MsyhtjTextView(PlayerActionInfoService.this);
        setTextViewStyle(aPtsInfo, audioPts);
        tableRow.addView(aPtsInfo);

        TextView vPtsInfo = new MsyhtjTextView(PlayerActionInfoService.this);
        setTextViewStyle(vPtsInfo, videoPts);
        tableRow.addView(vPtsInfo);

        TextView pcrInfo = new MsyhtjTextView(PlayerActionInfoService.this);
        setTextViewStyle(pcrInfo, pcr);
        tableRow.addView(pcrInfo);*/

        TextView bufferingTime = new MsyhtjTextView(PlayerActionInfoService.this);
        setTextViewStyle(bufferingTime, buffering + "(s)");
        tableRow.addView(bufferingTime);

        if ( tablePlayerInfoLayout.getChildCount() == 6){
            tablePlayerInfoLayout.removeViews(1,1);
        }

        tablePlayerInfoLayout.addView(tableRow);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(playerActionInfoReceiver);
        unregisterReceiver(playerActionInfoUnicomReceiver);
        super.onDestroy();
        if(mFloatLayout != null)
        {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
        if (ptsFloatflg) {
            destoryPtsMonitor();
        }
        int pid = android.os.Process.myPid();//获取当前应用程序的PID
        android.os.Process.killProcess(pid);//杀死当前进程
    }

    private class PlayerActionInfoHandler extends Handler {
        public PlayerActionInfoHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: msg in" );
            switch (msg.what){
                case PlayerActionInfoConstant.PLAY_START:
                    PlayerBaseInfoBean playerBaseInfoBean = (PlayerBaseInfoBean) msg.obj;
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateString = formatter.format(new Date(playerBaseInfoBean.getStartTime()));
                    idText.setText( String.valueOf(playerBaseInfoBean.getId()));
                    //urlText.setText( playerBaseInfoBean.getUrl());
                    //durationText.setText( String.valueOf(playerBaseInfoBean.getDuration()));
                    startTimeText.setText(dateString);
                    firstBufferingTimeText.setText(String.valueOf(playerBaseInfoBean.getFirstBufferingTime()));
                    resolutionRatioText.setText(playerBaseInfoBean.getResolutionRatio());
                    break;
                case PlayerActionInfoConstant.SEEK_START:
                    addUserTableRow("SeekStart", secToTime(msg.arg1), "Success", (String) msg.obj);
                    break;
                case PlayerActionInfoConstant.SEEK_END:
                    addUserTableRow("SeekEnd", secToTime(msg.arg1), "Success", (String) msg.obj);
                    break;
                case PlayerActionInfoConstant.PAUSE_MESSAGE:
                    addUserTableRow("Pause", secToTime(msg.arg1), "Success", (String) msg.obj);
                    break;
                case PlayerActionInfoConstant.RESUME_MESSAGE:
                    addUserTableRow("Resume", secToTime(msg.arg1), "Success", (String) msg.obj);
                    break;
                case PlayerActionInfoConstant.PLAYABE_REPORT:
                    String seconds = (String) msg.obj;
                    //bufferingPlaytimeText.setText(seconds);
                    break;
                case PlayerActionInfoConstant.BITRATE_CHANGE:
                    int bitRateChangeNum =  msg.arg1;
                    bitRateChangeTimes.setText(String.valueOf(bitRateChangeNum));
                    break;
                case PlayerActionInfoConstant.PLAY_QUIT:
                    PlayerInfoGlobal.first_flag = 0;
                    deInit();
                    break;
                case PlayerActionInfoConstant.BUFFER_END:
                    String bufferingTimes = String.valueOf(((ArrayList) msg.obj).size());
                    String bufferingAvgSeconds = String.valueOf(msg.arg1);
                    bufferingTimesText.setText(bufferingTimes);
                    currentBufferingAvgTimeText.setText(bufferingAvgSeconds);
                    break;
                case PlayerActionInfoConstant.BLURREDSCREEN_END:
                    int blurredNum =  msg.arg1;
                    blurredScreenTimes.setText(String.valueOf(blurredNum));
                    break;
                case 15:
                    PlayerBaseInfoBean playingInfo = (PlayerBaseInfoBean) msg.obj;
                    urlText.setText( playingInfo.getUrl());
                    durationText.setText(String.valueOf(playingInfo.getDuration()));
                    audioFormat.setText(playingInfo.getAudioFormat());
                    audioChannel.setText(playingInfo.getAudioChannel());
                    audioNumberText.setText(String.valueOf(playingInfo.getAudioNumber()));
                    currentAudioText.setText(String.valueOf(playingInfo.getCurrentAudioTrack()));
                    audioSampleRate.setText(String.valueOf(playingInfo.getAudioSampleRate()));
                    videoFormat.setText(playingInfo.getVideoFormat());
                    videoFrameRate.setText(String.valueOf(playingInfo.getVideoFrameRate()));
                    videoProgressText.setText(playingInfo.getProgress());
                    break;
                case 16:
                    PlayerBaseInfoBean playerBaseInfo = (PlayerBaseInfoBean) msg.obj;
                    if (PlayerInfoGlobal.first_flag != 0) { //修改多线程刷新不同步问题。
                        addPlayInfoTableRow(playerBaseInfo.getStatus(), playerBaseInfo.getPlayTime(),
                                playerBaseInfo.getAudioBuffering(), playerBaseInfo.getVideoBuffering(),
                                String.valueOf(playerBaseInfo.getBufferingTime()));
                        videoDecoderErrorsText.setText(String.valueOf(playerBaseInfo.getVideoDecoderError()));
                        audioDecoderErrorsText.setText(String.valueOf(playerBaseInfo.getAudioDecoderError()));
                    }

                    break;
                case PlayerActionInfoConstant.UNLOAD_START:
                    int num =  msg.arg1;
                    videoUnload.setText(String.valueOf(num));
                    break;
                case  PlayerActionInfoConstant.PTS_INFO:
                    PlayerBaseInfoBean playerPtsInfo = (PlayerBaseInfoBean) msg.obj;
                    addPtsInfo(playerPtsInfo.getApts(), playerPtsInfo.getVpts(), playerPtsInfo.getPcr());
                    break;
                default:
                    break;
            }
            /*String txt = null;
            txt = (String) msg.obj;
            if (!(txt.equals(null))) {
                mActionInfo.append((String) msg.obj);
            }*/
        }
    }

    private class PlayingInfoHandler extends Handler {
        public PlayingInfoHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "PlayingInfoHandler handleMessage: msg in" );
            switch (msg.what){
                case 15:
                    PlayerBaseInfoBean playerBaseInfoBean = (PlayerBaseInfoBean) msg.obj;
                    audioFormat.setText(playerBaseInfoBean.getAudioFormat());
                    audioChannel.setText(playerBaseInfoBean.getAudioChannel());
                    audioSampleRate.setText(String.valueOf(playerBaseInfoBean.getAudioSampleRate()));
                    videoFormat.setText(playerBaseInfoBean.getVideoFormat());
                    videoFrameRate.setText(String.valueOf(playerBaseInfoBean.getVideoFrameRate()));
                    break;
                case 16:
                    PlayerBaseInfoBean playerBaseInfo = (PlayerBaseInfoBean) msg.obj;
                    if (PlayerInfoGlobal.first_flag != 0) {
                        addPlayInfoTableRow(playerBaseInfo.getStatus(), playerBaseInfo.getPlayTime(),
                                playerBaseInfo.getAudioBuffering(), playerBaseInfo.getVideoBuffering(),
                                String.valueOf(playerBaseInfo.getBufferingTime()));
                    } else {
                        Log.e(TAG, "handleMessage: first_flag == 0");
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
