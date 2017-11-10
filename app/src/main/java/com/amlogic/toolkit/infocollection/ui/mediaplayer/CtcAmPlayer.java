package com.amlogic.toolkit.infocollection.ui.mediaplayer;

import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

/**
 * Created by Wenjie.Chen on 2017/9/25.
 */

public class CtcAmPlayer {

    private static final String TAG = "CtcAmPlayer";
    public static final int IDLE_STATE = 0;
    public static final int PLAY_STATE = 1;
    public static final int FAST_STATE = 2;
    public static final int PAUSE_STATE = 3;
    public static final int ERROR_STATE = 4;

    static {
        try {
            System.loadLibrary("SwcMediaProcessor");
        } catch (UnsatisfiedLinkError e) {
            Log.i(TAG, "Unable to load native support libraries.");
        }
    }
    private int mState ;
    public CtcAmPlayer() {
        mState =IDLE_STATE;
    }

    /**
     * Set the EPG Size from page html meta.
     * @param width
     * @param height
     */
    public void setEpgSize(int width, int height) {
        nativeCtcSetEpgSize(width, height);
    }

    public void setPlayWindow(int left, int top, int right, int bottom) {
        nativeCtcSetPlayWindow(left, top, right, bottom);
    }

    public void setDataSource(String url) {
        nativeCtcSetDataSource(url);
    }

    public void prepareAsync() {
        nativeCtcPrepareAsync();
    }

    public void play() {
        if(mState==PAUSE_STATE){
            nativeCtcResume();
        }else if(mState==FAST_STATE){
            nativeCtcStopFast();
        }else{
            nativeCtcStart();
        }
        mState =PLAY_STATE;
    }

    public void pause() {
        if(mState==PAUSE_STATE){
            //nativeResume();
        }else if(mState==FAST_STATE){
            nativeCtcStopFast();
        }else{
            nativeCtcPause();
        }

        mState =PAUSE_STATE;
    }
    public void resume() {
        if(mState==PAUSE_STATE){
            nativeCtcResume();
        }else if(mState==FAST_STATE){
            nativeCtcStopFast();
        }
        mState =PLAY_STATE;
    }
    public void stop() {
        nativeCtcStop();
    }
    public void stopFast() {
        nativeCtcStopFast();
        mState =PLAY_STATE;
    }
    public void begin() {
        nativeCtcBegin();
        mState =PLAY_STATE;
    }
    public void end() {
        nativeCtcEnd();
    }
    public void release() {
        nativeCtcRelease();
        mState =IDLE_STATE;
    }
    public void fast(int scale) {
        nativeCtcSetPlaySpeed(scale);
        mState =FAST_STATE;
    }
    public int getDuration() {
        return nativeCtcGetDuration();
    }

    public int getCurrentPosition() {
        return nativeCtcGetCurrentPosition();
    }

    public void setPlaySpeed(int speed) {
        nativeCtcSetPlaySpeed(speed);
    }

    public void onPlayerEvent(int event) {

    }

    public void setDisplaySurface(SurfaceHolder sh) {
        Surface surface;
        if (sh != null) {
            surface = sh.getSurface();
        } else {
            surface = null;
        }
        nativeCtcSetDisplaySurface(surface);
    }

    private void setDisplayMode(int mode) {
        nativeCtcSetDisplayMode(mode);
    }



    private native void nativeCtcSetEpgSize(int width, int height);
    private native void nativeCtcSetDataSource(String url);
    private native void nativeCtcSetDisplaySurface(Surface holder);
    private native void nativeCtcSetPlayWindow(int left, int top, int width, int height);
    private native void nativeCtcPrepareAsync();
    private native void nativeCtcStart();
    private native void nativeCtcBegin();
    private native void nativeCtcEnd();
    private native void nativeCtcStopFast();
    private native void nativeCtcPause();
    private native void nativeCtcResume();
    private native void nativeCtcStop();
    private native void nativeCtcRelease();
    private native void nativeCtcSetDisplayMode(int mode);
    private native void nativeCtcSetPlaySpeed(int speed);
    private native int nativeCtcGetDuration();
    private native int nativeCtcGetCurrentPosition();
}
