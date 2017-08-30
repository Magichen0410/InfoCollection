package com.amlogic.toolkit.infocollection.natives;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Wenjie.Chen on 2017/8/22.
 */

public class SysUtils {
    private static final String TAG = "SysUtils";

    private String dns2Address = null;

    static {
        System.loadLibrary("SysUtils");
    }

    private static native String getIpAddress();

    private static native String getMask();

    private static native String getMacAddr();

    private static native String getBroadAddr();

    public static String getIpAddr()
    {
        return getIpAddress();
    }

    public static String getMaskAddr(){
        return getMask();
    }

    public static String getMac(){
        return getMacAddr().toUpperCase();
    }

    public static String getBroadcastAddr(){
        return getBroadAddr();
    }

    public static String getDNS1(){
        Process cmdProcess = null;
        BufferedReader reader = null;
        String dnsIP = "";
        try {
            cmdProcess = Runtime.getRuntime().exec("getprop net.dns1");
            reader = new BufferedReader(new InputStreamReader(cmdProcess.getInputStream()));
            dnsIP = reader.readLine();
            return dnsIP;
        } catch (IOException e) {
            return null;
        } finally{
            try {
                reader.close();
            } catch (IOException e) {
            }
            cmdProcess.destroy();
        }

    }

    public static String getDNS2(){
        Process cmdProcess = null;
        BufferedReader reader = null;
        String dnsIP = "";
        try {
            cmdProcess = Runtime.getRuntime().exec("getprop net.dns2");
            reader = new BufferedReader(new InputStreamReader(cmdProcess.getInputStream()));
            dnsIP = reader.readLine();
            return dnsIP;
        } catch (IOException e) {
            return null;
        } finally{
            try {
                reader.close();
            } catch (IOException e) {
            }
            cmdProcess.destroy();
        }

    }

}
