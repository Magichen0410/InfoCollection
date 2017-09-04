package com.amlogic.toolkit.infocollection.ui.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.amlogic.toolkit.infocollection.R;
import com.amlogic.toolkit.infocollection.widgets.MutliVideoView;

public class VideoPlayerActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener
{

    private int playNum = 0;
    private String[] filePaths;
    private int curVolume;
    private AudioManager mAudioManager;
    private MutliVideoView[] videoViews = new MutliVideoView[9];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playNum = getIntent().getIntExtra("count", 0);
        filePaths = getIntent().getStringArrayExtra("filePaths");
        mAudioManager = ((AudioManager)getSystemService(Context.AUDIO_SERVICE));
        curVolume = this.mAudioManager.getStreamVolume(1);
        mAudioManager.setStreamVolume(1, 0, 4);

        setContentView(getViewId());
        initView();
    }

    private int getViewId()
    {
        switch (playNum)
        {
            default:
                return -1;
            case 1:
                return R.layout.video_player_1;
            case 2:
                return R.layout.video_player_2;
            case 3:
                return R.layout.video_player_3;
            case 4:
                return R.layout.video_player_4;
            case 5:
                return R.layout.video_player_5;
            case 6:
                return R.layout.video_player_6;
            case 7:
                return R.layout.video_player_7;
            case 8:
                return R.layout.video_player_8;
            case 9:
                return R.layout.video_player_9;
        }
    }

    private void initView()
    {
        videoViews[0] = (MutliVideoView) findViewById(R.id.video_view_0);
        videoViews[1] = (MutliVideoView) findViewById(R.id.video_view_1);
        videoViews[2] = (MutliVideoView) findViewById(R.id.video_view_2);
        videoViews[3] = (MutliVideoView) findViewById(R.id.video_view_3);
        videoViews[4] = (MutliVideoView) findViewById(R.id.video_view_4);
        videoViews[5] = (MutliVideoView) findViewById(R.id.video_view_5);
        videoViews[6] = (MutliVideoView) findViewById(R.id.video_view_6);
        videoViews[7] = (MutliVideoView) findViewById(R.id.video_view_7);
        videoViews[8] = (MutliVideoView) findViewById(R.id.video_view_8);

        videoViews[0].setOnPreparedListener(new OnPreparedListener0());
        videoViews[0].setOnCompletionListener(this);
        videoViews[0].setOnErrorListener(this);
        if (!TextUtils.isEmpty(filePaths[0])) {
            videoViews[0].setVideoPath(VideoPlayerActivity.this.filePaths[0]);
        }
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        finish();
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    private class OnPreparedListener0 implements MediaPlayer.OnPreparedListener {

        public OnPreparedListener0() {
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            if (!TextUtils.isEmpty(filePaths[0])) {
                mediaPlayer.start();
            }
            if (playNum > 1) {
                videoViews[1].setOnPreparedListener(new OnPreparedListener1());
                videoViews[1].setOnCompletionListener(VideoPlayerActivity.this);
                videoViews[1].setOnErrorListener(VideoPlayerActivity.this);
                if (!TextUtils.isEmpty(filePaths[1])) {
                    videoViews[1].setVideoPath(VideoPlayerActivity.this.filePaths[1]);
                }
            }
        }
    }

    private class OnPreparedListener1 implements MediaPlayer.OnPreparedListener {

        public OnPreparedListener1() {
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            if (!TextUtils.isEmpty(filePaths[1])) {
                mediaPlayer.start();
            }
            if (playNum > 2) {
                videoViews[2].setOnPreparedListener(new OnPreparedListener2());
                videoViews[2].setOnCompletionListener(VideoPlayerActivity.this);
                videoViews[2].setOnErrorListener(VideoPlayerActivity.this);
                if (!TextUtils.isEmpty(filePaths[2])) {
                    videoViews[2].setVideoPath(VideoPlayerActivity.this.filePaths[2]);
                }
            }
        }
    }

    private class OnPreparedListener2 implements MediaPlayer.OnPreparedListener {

        public OnPreparedListener2() {
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            if (!TextUtils.isEmpty(filePaths[2])) {
                mediaPlayer.start();
            }
            if (playNum > 3) {
                videoViews[3].setOnPreparedListener(new OnPreparedListener3());
                videoViews[3].setOnCompletionListener(VideoPlayerActivity.this);
                videoViews[3].setOnErrorListener(VideoPlayerActivity.this);
                if (!TextUtils.isEmpty(filePaths[3])) {
                    videoViews[3].setVideoPath(VideoPlayerActivity.this.filePaths[3]);
                }
            }
        }
    }

    private class OnPreparedListener3 implements MediaPlayer.OnPreparedListener {

        public OnPreparedListener3() {
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            if (!TextUtils.isEmpty(filePaths[3])) {
                mediaPlayer.start();
            }
            if (playNum > 4) {
                videoViews[4].setOnPreparedListener(new OnPreparedListener4());
                videoViews[4].setOnCompletionListener(VideoPlayerActivity.this);
                videoViews[4].setOnErrorListener(VideoPlayerActivity.this);
                if (!TextUtils.isEmpty(filePaths[4])) {
                    videoViews[4].setVideoPath(VideoPlayerActivity.this.filePaths[4]);
                }
            }
        }
    }

    private class OnPreparedListener4 implements MediaPlayer.OnPreparedListener {

        public OnPreparedListener4() {
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            if (!TextUtils.isEmpty(filePaths[4])) {
                mediaPlayer.start();
            }
            if (playNum > 5) {
                videoViews[5].setOnPreparedListener(new OnPreparedListener5());
                videoViews[5].setOnCompletionListener(VideoPlayerActivity.this);
                videoViews[5].setOnErrorListener(VideoPlayerActivity.this);
                if (!TextUtils.isEmpty(filePaths[5])) {
                    videoViews[5].setVideoPath(VideoPlayerActivity.this.filePaths[5]);
                }
            }
        }
    }

    private class OnPreparedListener5 implements MediaPlayer.OnPreparedListener {

        public OnPreparedListener5() {
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            if (!TextUtils.isEmpty(filePaths[5])) {
                mediaPlayer.start();
            }
            if (playNum > 6) {
                videoViews[6].setOnPreparedListener(new OnPreparedListener6());
                videoViews[6].setOnCompletionListener(VideoPlayerActivity.this);
                videoViews[6].setOnErrorListener(VideoPlayerActivity.this);
                if (!TextUtils.isEmpty(filePaths[6])) {
                    videoViews[6].setVideoPath(VideoPlayerActivity.this.filePaths[6]);
                }
            }
        }
    }

    private class OnPreparedListener6 implements MediaPlayer.OnPreparedListener {

        public OnPreparedListener6() {
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            if (!TextUtils.isEmpty(filePaths[6])) {
                mediaPlayer.start();
            }
            if (playNum > 7) {
                videoViews[7].setOnPreparedListener(new OnPreparedListener7());
                videoViews[7].setOnCompletionListener(VideoPlayerActivity.this);
                videoViews[7].setOnErrorListener(VideoPlayerActivity.this);
                if (!TextUtils.isEmpty(filePaths[7])) {
                    videoViews[7].setVideoPath(VideoPlayerActivity.this.filePaths[7]);
                }
            }
        }
    }

    private class OnPreparedListener7 implements MediaPlayer.OnPreparedListener {

        public OnPreparedListener7() {
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            if (!TextUtils.isEmpty(filePaths[7])) {
                mediaPlayer.start();
            }
            if (playNum > 8) {
                videoViews[8].setOnPreparedListener(new OnPreparedListener8());
                videoViews[8].setOnCompletionListener(VideoPlayerActivity.this);
                videoViews[8].setOnErrorListener(VideoPlayerActivity.this);
                if (!TextUtils.isEmpty(filePaths[8])) {
                    videoViews[8].setVideoPath(VideoPlayerActivity.this.filePaths[8]);
                }
            }
        }
    }

    private class OnPreparedListener8 implements MediaPlayer.OnPreparedListener {

        public OnPreparedListener8() {
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            if (!TextUtils.isEmpty(filePaths[8])) {
                mediaPlayer.start();
            }
        }
    }
}
