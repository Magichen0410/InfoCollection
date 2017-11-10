package com.amlogic.toolkit.infocollection.utils;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.amlogic.toolkit.infocollection.javabean.MemInfoBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Wenjie.Chen on 2017/8/9.
 */

public class SystemInfoUtil {

    private static final String TAG = "SystemInfoUtil";

    public static String getCpuTemp() {
        String cpu_temp = null;
        try {
            FileInputStream fis = new FileInputStream("/sys/class/thermal/thermal_zone0/temp");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr, 4096);
            StringBuffer buffer = new StringBuffer();
            String ch = null;
            {
                while ((ch = br.readLine()) != null) {
                    buffer.append(ch);
                }
            }
            br.close();
            if (buffer.toString().equals(null)) {
                cpu_temp = "0";
            } else {
                cpu_temp = buffer.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cpu_temp;
    }

    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getNumberOfCPUCores() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            // Gingerbread doesn't support giving a single application access to both cores, but a
            // handful of devices (Atrix 4G and Droid X2 for example) were released with a dual-core
            // chipset and Gingerbread; that can let an app in the background run without impacting
            // the foreground application. But for our purposes, it makes them single core.
            return 1;
        }
        int cores;
        try {
            cores = new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).length;
        } catch (SecurityException e) {
            cores = -1;
        } catch (NullPointerException e) {
            cores = -1;
        }
        return cores;
    }

    public static int getNumberOfGPUCores() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            // Gingerbread doesn't support giving a single application access to both cores, but a
            // handful of devices (Atrix 4G and Droid X2 for example) were released with a dual-core
            // chipset and Gingerbread; that can let an app in the background run without impacting
            // the foreground application. But for our purposes, it makes them single core.
            return 1;
        }
        int cores = 0;
        try {
            FileInputStream fis = new FileInputStream("/sys/class/mpgpu/max_pp");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr, 4096);
            StringBuffer buffer = new StringBuffer();
            String ch = null;
            {
                while ((ch = br.readLine()) != null) {
                    buffer.append(ch);
                }
            }
            br.close();
            if (buffer.toString().equals(null)) {
                cores = 0;
            } else {
                cores = Integer.parseInt(buffer.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cores;
    }

    public static String getCurGpuFreq() {
        String curFreq = null;
        try {
            FileInputStream fis = new FileInputStream("/sys/class/mpgpu/cur_freq");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr, 4096);
            StringBuffer buffer = new StringBuffer();
            String ch = null;
            {
                while ((ch = br.readLine()) != null) {
                    buffer.append(ch);
                }
            }
            br.close();
            if (buffer.toString().equals(null)) {
                curFreq = "0";
            } else {
                curFreq = buffer.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return curFreq;
    }

    private static final FileFilter CPU_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getName();
            //regex is slow, so checking char by char.
            if (path.startsWith("cpu")) {
                for (int i = 3; i < path.length(); i++) {
                    if (path.charAt(i) < '0' || path.charAt(i) > '9') {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    };


    private String isSupport(boolean pValue){
        return (pValue ? "支持" : "不支持");
    }

    public static long[] getSDCardMemory() {
        long[] sdCardInfo=new long[2];
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long bSize = sf.getBlockSize();
            long bCount = sf.getBlockCount();
            long availBlocks = sf.getAvailableBlocks();

            sdCardInfo[0] = bSize * bCount;//总大小
            sdCardInfo[1] = bSize * availBlocks;//可用大小
        }
        return sdCardInfo;
    }

    /**
     * 获取手机内存大小
     *
     * @return
     */
    public static String getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString = null;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (IOException e) {
        }
//        return Formatter.formatFileSize(getBaseContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化
        return String.valueOf(Integer.valueOf(arrayOfString[1]).intValue() / 1024) + " M ";
    }

    public static void getTotalMemory(MemInfoBean memInfoBean) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString = null;
        double memAvailable , memTotal, memProgress;
        long initial_memory = 0;
        int i = 0;
        
        if (memInfoBean == null) {
            Log.e(TAG, "getTotalMemory: param memInfoBean is null");
            return;
        }
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            //str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            while ((str2 = localBufferedReader.readLine()) != null) {

                if (i == 0){
                    arrayOfString = str2.split("\\s+");
                    /*for (String num : arrayOfString) {
                        Log.i(str2, num + "\t");
                    }*/
                    memInfoBean.setMemTotal(arrayOfString[1]);
                } else if (i == 1) {
                    arrayOfString = str2.split("\\s+");
                    /*for (String num : arrayOfString) {
                        Log.i(str2, num + "\t");
                    }*/
                    memInfoBean.setMemFree(arrayOfString[1]);
                } else if (i == 2) {
                    arrayOfString = str2.split("\\s+");
                    /*for (String num : arrayOfString) {
                        Log.i(str2, num + "\t");
                    }*/
                    memInfoBean.setMemAvailable(arrayOfString[1]);
                } else {
                    break;
                }
                i++;
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        memAvailable = Integer.valueOf(memInfoBean.getMemAvailable()).intValue();
        memTotal = Integer.valueOf(memInfoBean.getMemTotal()).intValue();
        memProgress = (memAvailable/memTotal)*100;
        Log.e(TAG, "getTotalMemory: memAvailable = "+ memAvailable + ", memTotal = "+ memTotal +", memProgress = " + memProgress);
        memInfoBean.setAvailableProgress((int) memProgress);
    }

    /**
     * 获取当前可用内存大小 n
     *
     * @return
     */
    /*public static String getAvailMemory() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return Formatter.formatFileSize(getBaseContext(), mi.availMem);
    }*/

    public static String getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        int maxFreq = Integer.parseInt(result.trim())/(1024);
        return String.valueOf(maxFreq);
    }

    // 获取CPU最小频率（单位KHZ）
    public static String getMinCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        int minFreq = Integer.parseInt(result.trim())/(1024);
        return String.valueOf(minFreq);
    }

    // 实时获取CPU当前频率（单位KHZ）
    public static String getCurCpuFreq() {
        String result = "N/A";
        try {
            FileReader fr = new FileReader(
                    "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            result = text.trim() + "Hz";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getVersion() {
        String version = null;
        try {
            FileInputStream fis = new FileInputStream("/proc/version");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr, 4096);
            StringBuffer buffer = new StringBuffer();
            String ch = null;
            {
                while ((ch = br.readLine()) != null) {
                    buffer.append(ch);
                }
            }
            br.close();
            if (buffer.toString().equals(null)) {
                version = null;
            } else {
                version = buffer.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return version;
    }
}
