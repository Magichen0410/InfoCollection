package com.amlogic.toolkit.infocollection.constant;

import android.util.Log;

/**
 * Created by Wenjie.Chen on 2017/9/5.
 */

public enum PlayerActionInfoEnum {

    PlayPrepare("PLAY_PREPARE", 1), PlayStart("PLAY_START", 2), SeekStart("SEEK_START", 3),
    SeekEnd("SEEK_END", 4), PayseMessage("PAUSE_MESSAGE", 5), ResumeMessage("RESUME_MESSAGE", 6),
    BufferStart("BUFFER_START", 7), BufferEnd("BUFFER_END",8), PlayAbeReport("PLAYABE_REPORT", 9),
    BitRateChange("BITRATE_CHANGE", 10), PlayQuit("PLAY_QUIT", 11), BlurredScreenStart("BLURREDSCREEN_START", 12),
    BlurredScreenEnd("BLURREDSCREEN_END", 13), ErrorMessage("ERROR_MESSAGE", 14), UnloadStart("UNLOAD_START", 17),
    UnloadEnd("UNLOAD_End", 18);


    private String playerActionInfoName;
    private int key;
    private static final String TAG = "PlayerActionInfoEnum";

    private PlayerActionInfoEnum(String playerActionInfoName, int key) {
        this.playerActionInfoName = playerActionInfoName;
        this.key = key;
    }

    public String getPlayerActionInfoName() {
        return playerActionInfoName;
    }

    public void setPlayerActionInfoName(String playerActionInfoName) {
        this.playerActionInfoName = playerActionInfoName;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public static int getKeyByPlayerActionInfoName(String playerActionInfoName){

        int indexKey = 0;

        for (PlayerActionInfoEnum e : PlayerActionInfoEnum.values()) {
            //Log.i(TAG, "getKeyBySwitchChannelInfoName: getSwitchChannelInfoName = " + e.getSwitchChannelInfoName());
            String str = e.getPlayerActionInfoName();
            if (str.equals(playerActionInfoName)){
                indexKey = e.getKey();
                Log.i(TAG, "getKeyByPlayerActionInfoName: getKey = " + indexKey);
                return indexKey;
            }
        }

        return indexKey;
    }
}
