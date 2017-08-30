package com.amlogic.toolkit.infocollection.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Wenjie.Chen on 2017/8/10.
 * 暂时不使用该类获取网络信息，现改为SysUtils类获取网络信息。
 */

public class NetWorkUtil {
    private static final String TAG = "NetWorkUtil";
    private static final String ETH0_MAC_ADDR = "/sys/class/net/eth0/address";


/*
* 获取mac号
* */
    public static String getWireMacAddr() {
        try {
            return readLine(ETH0_MAC_ADDR).toUpperCase();
        } catch (IOException e) {
            Log.e(TAG,"IO Exception when getting eth0 mac address",e);
            e.printStackTrace();
            return "unavailable";
        }
    }

    private static String readLine(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename), 256);
        try {
            return reader.readLine();
        } finally {
            reader.close();
        }
    }

    public static String getLocalIp(){
        return null;
    }

    public static String getBroadcast(){
        System.setProperty("java.net.preferIPv4Stack", "true");
        try {
            for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum.hasMoreElements();) {
                NetworkInterface ni = niEnum.nextElement();
                if (!ni.isLoopback()) {
                    for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
                        if (interfaceAddress.getBroadcast() != null) {
                            return interfaceAddress.getBroadcast().toString().substring(1);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}
