package com.amlogic.toolkit.infocollection.javabean;

/**
 * Created by Wenjie.Chen on 2017/9/28.
 */

public class PlayerBaseInfoBean {
    private int id;
    private String url;
    private String duration;
    private long startTime;
    private long firstBufferingTime;
    private String audioFormat;
    private String audioChannel;
    private int audioNumber;
    private int currentAudioTrack;
    private int audioSampleRate;
    private String videoFormat;
    private String videoCodec;
    private int videoFrameRate;
    private String resolutionRatio;

    private String status;
    private String playTime;
    private String audioBuffering;
    private String videoBuffering;
    private String apts;
    private String vpts;
    private String pcr;
    private int bufferingTime;
    private String progress;
    private int videoDecoderError;
    private int audioDecoderError;

    public PlayerBaseInfoBean(){
        this.id = 0;
        this.url = null;
        this.duration = "00:00:00";
        this.startTime = 0;
        this.firstBufferingTime = 0;
        this.audioFormat = null;
        this.audioChannel = null;
        this.audioSampleRate = 0;
        this.videoFormat = null;
        this.videoCodec = null;
        this.videoFrameRate = 0;
        this.resolutionRatio = null;
        this.status = null;
        this.playTime = null;
        this.audioBuffering = null;
        this.videoBuffering = null;
        this.apts = null;
        this.vpts = null;
        this.pcr = null;
        this.bufferingTime = 0;
        this.audioNumber = 0;
        this.currentAudioTrack = 0;
        this.videoDecoderError = 0;
        this.audioDecoderError = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getFirstBufferingTime() {
        return firstBufferingTime;
    }

    public void setFirstBufferingTime(long firstBufferingTime) {
        this.firstBufferingTime = firstBufferingTime;
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

    public String getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public int getVideoFrameRate() {
        return videoFrameRate;
    }

    public void setVideoFrameRate(int videoFrameRate) {
        this.videoFrameRate = videoFrameRate;
    }

    public String getResolutionRatio() {
        return resolutionRatio;
    }

    public void setResolutionRatio(String resolutionRatio) {
        this.resolutionRatio = resolutionRatio;
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

    public int getBufferingTime() {
        return bufferingTime;
    }

    public void setBufferingTime(int bufferingTime) {
        this.bufferingTime = bufferingTime;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public int getAudioNumber() {
        return audioNumber;
    }

    public void setAudioNumber(int audioNumber) {
        this.audioNumber = audioNumber;
    }

    public int getCurrentAudioTrack() {
        return currentAudioTrack;
    }

    public void setCurrentAudioTrack(int currentAudioTrack) {
        this.currentAudioTrack = currentAudioTrack;
    }

    public int getVideoDecoderError() {
        return videoDecoderError;
    }

    public void setVideoDecoderError(int videoDecoderError) {
        this.videoDecoderError = videoDecoderError;
    }

    public int getAudioDecoderError() {
        return audioDecoderError;
    }

    public void setAudioDecoderError(int audioDecoderError) {
        this.audioDecoderError = audioDecoderError;
    }
}
