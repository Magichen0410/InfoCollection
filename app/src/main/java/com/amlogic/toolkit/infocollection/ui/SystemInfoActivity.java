package com.amlogic.toolkit.infocollection.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.amlogic.toolkit.infocollection.R;
import com.amlogic.toolkit.infocollection.javabean.SystemInfoBean;
import com.amlogic.toolkit.infocollection.natives.SysUtils;
import com.amlogic.toolkit.infocollection.utils.DensityUtil;
import com.amlogic.toolkit.infocollection.utils.NetWorkUtil;
import com.amlogic.toolkit.infocollection.utils.SystemInfoUtil;
import com.amlogic.toolkit.infocollection.widgets.SystemInfoOptionFragment;

import java.io.Serializable;
import java.util.ArrayList;

public class SystemInfoActivity extends AppCompatActivity {

    private static final String TAG = "SystemInfoActivity";

    private ArrayList<SystemInfoBean> systemInfoBeanArrayList = null;
    private FragmentManager fragmentManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_info);

        systemInfoBeanArrayList = new ArrayList<SystemInfoBean>();
        initData();
        printData();

        fragmentManager = getFragmentManager();
        SystemInfoOptionFragment systemInfoOptionFragment = new SystemInfoOptionFragment();
        systemInfoOptionFragment.setFragmentManager(fragmentManager);
        Bundle bundle = new Bundle();
        bundle.putSerializable("systeminfobeanlist",systemInfoBeanArrayList);
        systemInfoOptionFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.systeminfo_option_layout, systemInfoOptionFragment);
        fragmentTransaction.commit();
    }

    private String getSystemInfo() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

        StringBuilder sb = new StringBuilder();

        sb.append("系统版本 : " + SystemInfoUtil.getVersion() + "\n");
        sb.append("CPU型号 : " + SystemInfoUtil.getCpuName() + "\n" );
        sb.append("CPU核心数 : " + SystemInfoUtil.getNumberOfCPUCores() + "\n" );
        sb.append("CPU频率 : " + SystemInfoUtil.getMinCpuFreq() + "-"+ SystemInfoUtil.getMaxCpuFreq() +" MHZ "+"\n" );
        sb.append("CPU当前温度 : " + SystemInfoUtil.getCpuTemp() + "\n");
        sb.append("GPU核心数 : " + SystemInfoUtil.getNumberOfGPUCores() + "\n");
        sb.append("GPU当前频率 : " + SystemInfoUtil.getCurGpuFreq() + " MHZ" + "\n");
        sb.append("屏幕dpi : " + DensityUtil.getDensity(this) + " dpi" + "\n");
        sb.append("屏幕尺寸 : " + metric.widthPixels + " * "+ metric.heightPixels+"\n"  );
        sb.append("运行内存 : " + SystemInfoUtil.getTotalMemory() + "\n");
        String totalSDMemory = String.valueOf(SystemInfoUtil.getSDCardMemory()[0] / (1024.0 * 1024.0 * 1024.0));
        totalSDMemory = totalSDMemory.substring(0, totalSDMemory.indexOf(".") + 3);
        String availableMemory = String.valueOf(SystemInfoUtil.getSDCardMemory()[1] / (1024.0 * 1024.0 * 1024.0));
        availableMemory = availableMemory.substring(0, totalSDMemory.indexOf(".") + 3);
        sb.append("机身存储 : " + totalSDMemory + "G"+  " ; 可用 : "+ availableMemory+"G"+"\n"  );
        sb.append("屏幕密度 : " + metric.density + "\n"  );

        return sb.toString();
    }

    private String getNetWorkInfo() {
        StringBuffer sbNetworkInfo = new StringBuffer();

        sbNetworkInfo.append("MAC地址: " + SysUtils.getMac() + "\n");
        sbNetworkInfo.append("IP地址: " + SysUtils.getIpAddr() + "\n");
        sbNetworkInfo.append("广播地址: " + SysUtils.getBroadcastAddr() + "\n");
        sbNetworkInfo.append("Mask地址: " + SysUtils.getMaskAddr() + "\n");
        sbNetworkInfo.append("DNS1地址: " + SysUtils.getDNS1() + "\n");
        sbNetworkInfo.append("DNS2地址: " + SysUtils.getDNS2() + "\n");

        return sbNetworkInfo.toString();
    }

    private void printData() {
        Log.i("build","BOARD:" + Build.BOARD);
        Log.i("build","BOOTLOADER:" + Build.BOOTLOADER);
        Log.i("build","BRAND:" + Build.BRAND);
        Log.i("build","CPU_ABI:" + Build.CPU_ABI);
        Log.i("build","CPU_ABI2:" + Build.CPU_ABI2);
        Log.i("build","DEVICE:" + Build.DEVICE);
        Log.i("build","DISPLAY:" + Build.DISPLAY);
        Log.i("build","FINGERPRINT:" + Build.FINGERPRINT);
        Log.i("build","HARDWARE:" + Build.HARDWARE);
        Log.i("build","HOST:" + Build.HOST);
        Log.i("build","ID:" + Build.ID);
        Log.i("build","MANUFACTURER:" + Build.MANUFACTURER);
        Log.i("build","MODEL:" + Build.MODEL);
        Log.i("build","PRODUCT:" + Build.PRODUCT);
        Log.i("build","RADIO:" + Build.RADIO);
        Log.i("build","TAGS:" + Build.TAGS);
        Log.i("build","TIME:" + Build.TIME);
        Log.i("build","TYPE:" + Build.TYPE);
        Log.i("build","UNKNOWN:" + Build.UNKNOWN);
        Log.i("build","USER:" + Build.USER);
        Log.i("build","VERSION.CODENAME:" + Build.VERSION.CODENAME);
        Log.i("build","VERSION.INCREMENTAL:" + Build.VERSION.INCREMENTAL);
        Log.i("build","VERSION.RELEASE:" + Build.VERSION.RELEASE);
        Log.i("build","VERSION.SDK:" + Build.VERSION.SDK);
        Log.i("build","VERSION.SDK_INT:" + Build.VERSION.SDK_INT);
    }



    private void initData() {
        systemInfoBeanArrayList.add(new SystemInfoBean("系统信息",getSystemInfo()));
        systemInfoBeanArrayList.add(new SystemInfoBean("网络信息",getNetWorkInfo()));
        //systemInfoBeanArrayList.add(new SystemInfoBean("播放信息","程序员正在加班处理中~~~~~~~~~~~"));
    }

}
