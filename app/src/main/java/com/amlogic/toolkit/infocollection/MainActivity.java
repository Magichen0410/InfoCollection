package com.amlogic.toolkit.infocollection;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.amlogic.toolkit.infocollection.adapter.MainMenuAdapter;
import com.amlogic.toolkit.infocollection.javabean.MainMenuBean;
import com.amlogic.toolkit.infocollection.utils.DensityUtil;
import com.amlogic.toolkit.infocollection.utils.OtherUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private GridView menuGridView;
    private ArrayList<MainMenuBean> mainMenuBeanArrayList = null;
    MainMenuAdapter mainMenuAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DensityUtil.displaydpi(this);

        menuGridView = (GridView) findViewById(R.id.menu_list);
        mainMenuBeanArrayList = new ArrayList<MainMenuBean>();
        initMenu();
        mainMenuAdapter = new MainMenuAdapter(this, mainMenuBeanArrayList);
        menuGridView.setAdapter(mainMenuAdapter);
        menuGridView.setNumColumns(2);
        menuGridView.setOnItemClickListener(new MenuGridViewItemListener());
    }

    private void initMenu() {
        mainMenuBeanArrayList.add(new MainMenuBean("com.amlogic.toolkit.infocollection",
                "com.amlogic.toolkit.infocollection.ui.SystemInfoActivity", "系统信息", false));
        mainMenuBeanArrayList.add(new MainMenuBean("com.amlogic.toolkit.infocollection",
                "com.amlogic.toolkit.infocollection.service.SwitchChannelTimeService", "切台统计服务",
                true));
        mainMenuBeanArrayList.add(new MainMenuBean("com.amlogic.toolkit.infocollection",
                "com.amlogic.toolkit.infocollection.service.StopWatchService", "秒表计时器",
                true));
        mainMenuBeanArrayList.add(new MainMenuBean("com.amlogic.toolkit.infocollection",
                "com.amlogic.toolkit.infocollection.ui.mediaplayer.MutliMediaPlayerActivity",
                "多实例播放器", false));
    }

    private class MenuGridViewItemListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
            if (mainMenuBeanArrayList.get(postion).getIsService()) {
                String packageName = mainMenuBeanArrayList.get(postion).getPackageNmae();
                String className = mainMenuBeanArrayList.get(postion).getClassName();
                if (OtherUtil.isServiceRunning(getApplicationContext(),className)) {
                    Toast.makeText(getApplicationContext(), "关闭Service", Toast.LENGTH_SHORT).show();
                    ComponentName comp = new ComponentName(packageName, className);
                    Intent intent=new Intent();
                    intent.setComponent(comp);
                    stopService(intent);
                    mainMenuAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "启动Service", Toast.LENGTH_SHORT).show();
                    ComponentName comp = new ComponentName(packageName, className);
                    Intent intent=new Intent();
                    intent.setComponent(comp);
                    getApplication().startService(intent);
                    mainMenuAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getApplicationContext(), "启动Activity", Toast.LENGTH_SHORT).show();
                ComponentName comp = new ComponentName(mainMenuBeanArrayList.get(postion).getPackageNmae(),
                        mainMenuBeanArrayList.get(postion).getClassName());
                Intent intent=new Intent();
                intent.setComponent(comp);
                startActivity(intent);
            }
        }
    }
}
