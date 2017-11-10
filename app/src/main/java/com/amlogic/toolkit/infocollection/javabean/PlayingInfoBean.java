package com.amlogic.toolkit.infocollection.javabean;

/**
 * Created by Wenjie.Chen on 2017/9/30.
 */

public class PlayingInfoBean {
    private String status;
    private String playTime;
    private String audioBuffering;
    private String videoBuffering;
    private String apts;
    private String vpts;
    private String pcr;

    private String audioFormat;
    private String audioChannel;
    private int audioSampleRate;
    private String videoFormat;
    private int videoFrameRate;

    public PlayingInfoBean (){
        this.status = null;
        this.playTime = null;
        this.audioBuffering = null;
        this.videoBuffering = null;
        this.apts = null;
        this.vpts = null;
        this.pcr = null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    public String getAudioBuffering() {
        return audioBuffering;
    }

    public void setAudioBuffering(String audioBuffering) {
        this.audioBuffering = audioBuffering;
    }

    public String getVideoBuffering() {
        return videoBuffering;
    }

    public void setVideoBuffering(String videoBuffering) {
        this.videoBuffering = videoBuffering;
    }

    public String getApts() {
        return apts;
    }

    public void setApts(String apts) {
        this.apts = apts;
    }

    public String getVpts() {
        return vpts;
    }

    public void setVpts(String vpts) {
        this.vpts = vpts;
    }

    public String getPcr() {
        return pcr;
    }

    public void setPcr(String pcr) {
        this.pcr = pcr;
    }

    public String getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(String audioFormat) {
        this.audioFormat = audioFormat;
    }

    public String getAudioChannel() {
        return audioChannel;
    }

    public void setAudioChannel(String audioChannel) {
        this.audioChannel = audioChannel;
    }

    public int getAudioSampleRate() {
        return audioSampleRate;
    }

    public void setAudioSampleRate(int audioSampleRate) {
        this.audioSampleRate = audioSampleRate;
    }

    public String getVideoFormat() {
        return videoFormat;
    }

    public void setVideoFormat(String videoFormat) {
        this.videoFormat = videoFormat;
    }

    public int getVideoFrameRate() {
        return videoFrameRate;
    }

    public void setVideoFrameRate(int videoFrameRate) {
        this.videoFrameRate = videoFrameRate;
    }
}
