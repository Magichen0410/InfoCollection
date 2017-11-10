package com.amlogic.toolkit.infocollection.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amlogic.toolkit.infocollection.global.PlayerInfoGlobal;
import com.amlogic.toolkit.infocollection.javabean.PlayerBaseInfoBean;
import com.amlogic.toolkit.infocollection.javabean.PlayingInfoBean;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.amlogic.toolkit.infocollection.utils.OtherUtil.secToTime;

/**
 * Created by Wenjie.Chen on 2017/9/30.
 */

public class PlayerActionInfoUnicomReceiver extends BroadcastReceiver {

    private Handler handler;
    private static final String TAG = "PlayerActionInfoUnicomReceiver";
    private PlayerBaseInfoBean playerBaseInfoBean = new PlayerBaseInfoBean();
    int playTime = 1;

    public PlayerActionInfoUnicomReceiver() {
        super();
    }

    public PlayerActionInfoUnicomReceiver(Context context, Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Message msg = Message.obtain();
        String intentFilter = intent.getAction();
        Log.e(TAG, "onReceive: " + intentFilter);
        int audioPercent, videoPercent;
        if (PlayerInfoGlobal.first_flag == 0)
        {
            Message message = new Message();
            int audioChannel = 0;
            int progress = 0;
            playerBaseInfoBean.setUrl(intent.getStringExtra("PLAYER_URL"));
            playerBaseInfoBean.setDuration(secToTime(intent.getIntExtra("DURATION", 0)/1000));
            playerBaseInfoBean.setVideoFormat(intent.getStringExtra("VIDEO_FORMAT"));
            playerBaseInfoBean.setVideoFrameRate(intent.getIntExtra("FRAMERATE", 0));
            playerBaseInfoBean.setAudioFormat(intent.getStringExtra("AUDIO_FORMAT"));
            playerBaseInfoBean.setAudioSampleRate(intent.getIntExtra("AUDIO_SR", 0));
            playerBaseInfoBean.setAudioNumber(intent.getIntExtra("TOTAL_AUDIO_NUM", 0));
            playerBaseInfoBean.setCurrentAudioTrack(intent.getIntExtra("CUR_AUDIO_INDEX", 0));
            audioChannel = intent.getIntExtra("AUDIO_CHANNELS", 0);
            progress = intent.getIntExtra("VIDEO_SAMPLETYPE", -1);
            if (audioChannel == 2)
            {
                playerBaseInfoBean.setAudioChannel("立体声");
            } else {
                playerBaseInfoBean.setAudioChannel(String.valueOf(audioChannel));
            }

            if (progress == 0){
                playerBaseInfoBean.setProgress("逐行扫描");
            } else if (progress == 1){
                playerBaseInfoBean.setProgress("隔行扫描");
            } else {
                playerBaseInfoBean.setProgress("null");
            }
            PlayerInfoGlobal.first_flag = 1;
            message.what = 15;
            message.obj = playerBaseInfoBean;
            handler.sendMessage(message);
        }

        if ( intent.getStringExtra("PLAYER_STATE") != null ) {
            playerBaseInfoBean.setStatus(intent.getStringExtra("PLAYER_STATE"));
        } else {
            playerBaseInfoBean.setStatus("UnKnown");
        }
        playerBaseInfoBean.setPlayTime(secToTime(intent.getIntExtra("CURRENT_POSITION", 0)/1000));
        audioPercent = (int) (((float) intent.getIntExtra("AUDIO_USED_SIZE", 0)/(float) intent.getIntExtra("AUDIO_BUF_SIZE", 1))*100);
        playerBaseInfoBean.setAudioBuffering("" + audioPercent + "%");
        videoPercent = (int) (((float) intent.getIntExtra("VIDEO_USED_SIZE", 0)/(float) intent.getIntExtra("VIDEO_BUF_SIZE", 1))*100);
        playerBaseInfoBean.setVideoBuffering("" + videoPercent + "%");
        playerBaseInfoBean.setVideoDecoderError(intent.getIntExtra("VDEC_ERROR", 0));
        playerBaseInfoBean.setAudioDecoderError(intent.getIntExtra("ADEC_ERROR", 0));
        playerBaseInfoBean.setApts("0x"+Integer.toHexString(intent.getIntExtra("APTS", 0)));
        playerBaseInfoBean.setVpts("0x"+Integer.toHexString(intent.getIntExtra("VPTS", 0)));
        playerBaseInfoBean.setPcr("0x"+Integer.toHexString(intent.getIntExtra("PCR", 0)));
        playerBaseInfoBean.setBufferingTime(intent.getIntExtra("CACHE_TIME", 0));

        msg.what = 16;
        msg.obj = playerBaseInfoBean;
        handler.sendMessage(msg);

    }
}
