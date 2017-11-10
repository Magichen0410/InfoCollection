package com.amlogic.toolkit.infocollection.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amlogic.toolkit.infocollection.constant.PlayerActionInfoConstant;
import com.amlogic.toolkit.infocollection.constant.PlayerActionInfoEnum;
import com.amlogic.toolkit.infocollection.javabean.PlayerBaseInfoBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayerActionInfoReceiver extends BroadcastReceiver {

    private Handler handler;
    private static final String TAG = "PlayerActionInfoReceiver";
    private PlayerBaseInfoBean playerBaseInfoBean = new PlayerBaseInfoBean();
    private long bufferStartTime = 0;
    private int unloadNum = 0;
    private int blurredScreenNum = 0;
    private int bitRateChangeNum = 0;
    List<Integer> bufferingtimelist = new ArrayList<Integer>();


    public PlayerActionInfoReceiver() {
        super();
    }

    public PlayerActionInfoReceiver(Context context, Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        int key = 0;
        String obj = null;
        Message message = Message.obtain();
        String intentFilter = intent.getAction();
        String Type = intent.getStringExtra("TYPE");
        int id = intent.getIntExtra("ID",0);
        playerBaseInfoBean.setId(id);

        key = PlayerActionInfoEnum.getKeyByPlayerActionInfoName(Type);

        switch (key) {
            case PlayerActionInfoConstant.PLAY_PREPARE:
                StringBuffer sbPrepare = new StringBuffer();
                String url = intent.getStringExtra("URL");
                long startTime = intent.getLongExtra("START_TIME", 0L);
                playerBaseInfoBean.setStartTime(startTime);
                playerBaseInfoBean.setUrl(url);
                break;
            case PlayerActionInfoConstant.PLAY_START:
                StringBuffer resolutionRatioBuffer = new StringBuffer();
                int playTime = intent.getIntExtra("PLAY_TIME", 0);
                long endTime = intent.getLongExtra("END_TIME", 0L);
                int bitRate = intent.getIntExtra("BITRATE", 0);
                String name =  intent.getStringExtra("NAME");
                String programType = intent.getStringExtra("PROGRAM_TYPE");
                int width = intent.getIntExtra("WIDTH", 0);
                int height = intent.getIntExtra("HEIGHT", 0);
                String videoCodec = intent.getStringExtra("VIDEO_CODEC");
                String audioCodec = intent.getStringExtra("AUDIO_CODEC");
                resolutionRatioBuffer.append("" + width + "x" + height);
                //playerBaseInfoBean.setDuration(playTime);
                playerBaseInfoBean.setFirstBufferingTime(endTime - playerBaseInfoBean.getStartTime());
                //playerBaseInfoBean.setAudioCodec(audioCodec);
                playerBaseInfoBean.setVideoCodec(videoCodec);
                playerBaseInfoBean.setResolutionRatio(resolutionRatioBuffer.toString());
                message.what = key;
                message.obj = playerBaseInfoBean;
                handler.sendMessage(message);
                break;
            case PlayerActionInfoConstant.SEEK_START:
                long seekStartTime = intent.getLongExtra("START_TIME", 0L);
                int seekPlayTime = intent.getIntExtra("PLAY_TIME", 0);
                SimpleDateFormat seekStartFormatter = new SimpleDateFormat("HH:mm:ss");
                String seekStartDateString = seekStartFormatter.format(new Date(seekStartTime));
                message.what = key;
                message.arg1 = seekPlayTime;
                message.obj = seekStartDateString;
                handler.sendMessage(message);
                break;
            case PlayerActionInfoConstant.SEEK_END:
                long seekEndTime = intent.getLongExtra("END_TIME", 0L);
                int seekEndPlayTime = intent.getIntExtra("PLAY_TIME", 0);
                SimpleDateFormat seekEndFormatter = new SimpleDateFormat("HH:mm:ss");
                String seekEndDateString = seekEndFormatter.format(new Date(seekEndTime));
                message.what = key;
                message.arg1 = seekEndPlayTime;
                message.obj = seekEndDateString;
                handler.sendMessage(message);
                break;
            case PlayerActionInfoConstant.PAUSE_MESSAGE:
                long time = intent.getLongExtra("TIME", 0L);
                int PausePlayTime = intent.getIntExtra("PLAY_TIME", 0);
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                String dateString = formatter.format(new Date(time));
                message.what = key;
                message.arg1 = PausePlayTime;
                message.obj = dateString;
                handler.sendMessage(message);
                break;
            case PlayerActionInfoConstant.RESUME_MESSAGE:
                long resumeTime = intent.getLongExtra("TIME", 0L);
                int resumePlayTime = intent.getIntExtra("PLAY_TIME", 0);
                String resumeUrl = intent.getStringExtra("URL");
                int resumeBitRate = intent.getIntExtra("BITRATE", 0);
                String resumeName =  intent.getStringExtra("NAME");
                String resumeProgramType = intent.getStringExtra("PROGRAM_TYPE");
                int resumeWidth = intent.getIntExtra("WIDTH", 0);
                int resumeHeight = intent.getIntExtra("HEIGHT", 0);
                String resumeVideoCodec = intent.getStringExtra("VIDEO_CODEC");
                String resumeAudioCodec = intent.getStringExtra("AUDIO_CODEC");
                SimpleDateFormat resumeFormatter = new SimpleDateFormat("HH:mm:ss");
                String resumeDateString = resumeFormatter.format(new Date(resumeTime));
                message.what = key;
                message.arg1 = resumePlayTime;
                message.obj = resumeDateString;
                handler.sendMessage(message);
                break;
            case PlayerActionInfoConstant.BUFFER_START:
                bufferStartTime = intent.getLongExtra("START_TIME", 0L);
                int bufferStartPlayTime = intent.getIntExtra("PLAY_TIME", 0);
                break;
            case PlayerActionInfoConstant.BUFFER_END:
                long bufferEndTime = intent.getLongExtra("END_TIME", 0L);
                bufferingtimelist.add((int) (bufferEndTime - bufferStartTime));
                int total = 0;
                for(int i=0; i<bufferingtimelist.size(); i++){
                    total += bufferingtimelist.get(i);
                }
                message.what = key;
                message.arg1 = total/bufferingtimelist.size();
                message.obj = bufferingtimelist;
                handler.sendMessage(message);
                break;
            case PlayerActionInfoConstant.PLAYABE_REPORT:
                long reportTime = intent.getLongExtra("TIME", 0L);
                long seconds = intent.getLongExtra("SECONDS", 0L);
                int bytes = intent.getIntExtra("BYTES", 0);
                int reportPlayTime = intent.getIntExtra("PLAY_TIME", 0);
                int preFec = intent.getIntExtra("PRE_FEC", 0);
                int afterFec = intent.getIntExtra("AFTER_FEC", 0);
                message.what = key;
                message.obj = String.valueOf(seconds);
                handler.sendMessage(message);
                break;
            case PlayerActionInfoConstant.BITRATE_CHANGE:
                bitRateChangeNum ++;
                long changeTime = intent.getLongExtra("TIME", 0L);
                int oldId = intent.getIntExtra("OLDID", 0);
                String toUrl = intent.getStringExtra("TO_URL");
                int toBitRate = intent.getIntExtra("TO_BITRATE", 0);
                message.what = key;
                message.arg1 = bitRateChangeNum;
                handler.sendMessage(message);
                break;
            case PlayerActionInfoConstant.PLAY_QUIT:
                long quitTime = intent.getLongExtra("TIME", 0L);
                int quitPlayTime = intent.getIntExtra("PLAY_TIME", 0);
                SimpleDateFormat quitFormatter = new SimpleDateFormat("HH:mm:ss");
                String quitDateString = quitFormatter.format(new Date(quitTime));
                bitRateChangeNum = 0;
                blurredScreenNum = 0;
                bufferingtimelist.clear();
                message.what = key;
                message.arg1 = quitPlayTime;
                message.obj = quitDateString;
                handler.sendMessage(message);
                break;
            case PlayerActionInfoConstant.BLURREDSCREEN_START:
                StringBuffer sbBlurredScreenStart = new StringBuffer();
                long blurredScreenStartTime = intent.getLongExtra("TIME", 0L);
                int blurredScreenStartPlayTime = intent.getIntExtra("PLAY_TIME", 0);
                sbBlurredScreenStart.append("Type : " + Type + "\n");
                sbBlurredScreenStart.append("Id : " + id + "\n");
                sbBlurredScreenStart.append("Time : " + blurredScreenStartTime + "\n");
                sbBlurredScreenStart.append("PlayTime : " + blurredScreenStartPlayTime + "\n");
                obj = sbBlurredScreenStart.toString();
                break;
            case PlayerActionInfoConstant.BLURREDSCREEN_END:
                blurredScreenNum ++;
                long blurredScreenEndTime = intent.getLongExtra("END_TIME", 0L);
                int ratio = intent.getIntExtra("RATIO", 0);
                message.what = key;
                message.arg1 = blurredScreenNum;
                handler.sendMessage(message);
                break;
            case PlayerActionInfoConstant.ERROR_MESSAGE:
                StringBuffer sbError = new StringBuffer();
                int errorCode = intent.getIntExtra("ERROR_CODE", 0);
                long errorTime = intent.getLongExtra("TIME", 0L);
                sbError.append("Type : " + Type + "\n");
                sbError.append("Id : " + id + "\n");
                sbError.append("ErrorCode : " + errorCode + "\n");
                sbError.append("PlayTime : " + errorTime + "\n");
                obj = sbError.toString();
                break;
            case PlayerActionInfoConstant.UNLOAD_START:
                /*unloadNum ++;
                message.what = key;
                message.arg1 = unloadNum;
                handler.sendMessage(message);*/
                break;
            case PlayerActionInfoConstant.UNLOAD_END:
                unloadNum ++;
                message.what = key;
                message.arg1 = unloadNum;
                handler.sendMessage(message);
                break;
            default:
                StringBuffer sbUnknown = new StringBuffer();
                sbUnknown.append("Unknown Type \n");
                obj = sbUnknown.toString();
                break;
        }

    }
}
