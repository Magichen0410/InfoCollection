package com.amlogic.toolkit.infocollection.constant;

import android.util.Log;

/**
 * Created by Wenjie.Chen on 2017/8/15.
 */

public enum SwitchChannelInfoEnum {

    TotalTime("total_time", 1), TotalTimeNoWaitKeyframe("total_time(no wait keyframe)", 2),
    TotalTimeInitFirstPic("total_time(init->fistpic)", 3), TotalTimeKeyFirstPic("total_time(key->firstpic)", 4),
    TotalTimeNoPressKey("total_time(no press key)", 5), PressCloseTime("press_close_time", 6),
    CodecClosetime("codec_close_time", 7), CloseInitTime("close_init_time", 8), CodecInitTIme("codec_init_time", 9),
    InitFirstCheckinTime("init_firstcheckin_time", 10), FirstCheckinCmdTime("firstcheckin_cmd1_time", 11),
    FirstCheckoutCmdTime("cmd1_firstcheckout_time", 12), FirstCheckoutDecodedTime("firstcheckout_decoded_time", 13),
    DecodedFrame0Time("decoded_frame0_time", 14), Di0FirstOutTime("di0_firstout_time", 15),
    Frame0Frame1Time("frame0_frame1_time", 16), Di1FirstOutTime("di1_firstout_time", 17), Di2FirstOutTime("di2_firstout_time", 18),
    Frame1Frame2Time("frame1_frame2_time", 19), DiFirstPicTime("di_firstpic_time", 20);

    private String switchChannelInfoName;
    private int key;
    private static final String TAG = "SwitchChannelInfoEnum";

    private SwitchChannelInfoEnum(String switchChannelInfoName, int key) {
        this.switchChannelInfoName = switchChannelInfoName;
        this.key = key;
    }

    public String getSwitchChannelInfoName() {
        return switchChannelInfoName;
    }

    public void setSwitchChannelInfoName(String switchChannelInfoName) {
        this.switchChannelInfoName = switchChannelInfoName;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public static int getKeyBySwitchChannelInfoName(String switchChannelInfoName){

        int indexKey = 0;

        for (SwitchChannelInfoEnum e : SwitchChannelInfoEnum.values()) {
            //Log.i(TAG, "getKeyBySwitchChannelInfoName: getSwitchChannelInfoName = " + e.getSwitchChannelInfoName());
            if (e.getSwitchChannelInfoName().equals(switchChannelInfoName)){
                indexKey = e.getKey();
                //Log.i(TAG, "getKeyBySwitchChannelInfoName: getKey = " + indexKey);
                return indexKey;
            }
        }

        return indexKey;
    }
}
