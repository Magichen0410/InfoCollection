package com.amlogic.toolkit.infocollection.javabean;

import android.media.MediaPlayer;

/**
 * Created by Wenjie.Chen on 2017/8/28.
 */

public class MutliMediaPlayerBean {
    private int mediaPlayerId;
    private MediaPlayer mediaPlayer;

    public MutliMediaPlayerBean(int mediaPlayerId, MediaPlayer mediaPlayer) {
        this.mediaPlayerId = mediaPlayerId;
        this.mediaPlayer = mediaPlayer;
    }

    public int getMediaPlayerId() {
        return mediaPlayerId;
    }

    public void setMediaPlayerId(int mediaPlayerId) {
        this.mediaPlayerId = mediaPlayerId;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
}
