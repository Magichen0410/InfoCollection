package com.amlogic.toolkit.infocollection;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by Wenjie.Chen on 2017/10/26.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            boolean flag = Settings.canDrawOverlays(this);

            //Toast.makeText(getApplicationContext(), "悬浮窗权限：" + flag, Toast.LENGTH_SHORT).show();

            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
            } else {
                //TODO do something you need
            }
        }

        ComponentName comp = new ComponentName("com.amlogic.toolkit.infocollection",
                "com.amlogic.toolkit.infocollection.service.FloatMenuService");
        Intent intent = new Intent();
        intent.setComponent(comp);
        startService(intent);

        this.finish();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy !");
        super.onDestroy();
        Log.e(TAG, "onDestroy kill process ");
        //int pid = android.os.Process.myPid();//获取当前应用程序的PID
        //android.os.Process.killProcess(pid);//杀死当前进程
    }
}
