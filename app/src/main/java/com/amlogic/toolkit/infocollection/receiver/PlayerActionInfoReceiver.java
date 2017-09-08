package com.amlogic.toolkit.infocollection.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import javax.security.auth.login.LoginException;

public class PlayerActionInfoReceiver extends BroadcastReceiver {

    private static final String TAG = "PlayerActionInfoReceive";


    @Override
    public void onReceive(Context context, Intent intent) {
        String intentFilter = intent.getAction();
        Log.e(TAG, "onReceive: " + intentFilter);
        String Type = intent.getStringExtra("TYPE");
        Log.e(TAG, "onReceive: " + Type);
    }
}
