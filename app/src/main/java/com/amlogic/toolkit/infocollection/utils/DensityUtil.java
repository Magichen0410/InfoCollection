package com.amlogic.toolkit.infocollection.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by Wenjie.Chen on 2017/8/7.
 */

public class DensityUtil {

    private static final String TAG = "DensityUtil";
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getDensity(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.densityDpi;
    }

    public static void displaydpi(Context context){
        String str = "";
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        float density = dm.density;
        str += "屏幕分辨率为:" + dm.widthPixels + " * " + dm.heightPixels + "\n";
        str += "绝对宽度:" + String.valueOf(screenWidth) + "pixels\n";
        str += "绝对高度:" + String.valueOf(screenHeight)
                + "pixels\n";
        str += "逻辑密度:" + String.valueOf(density)
                + "\n";
        str += "屏幕长 :" + String.valueOf(dm.widthPixels/density) + "dp\n";
        str += "屏幕款:" + String.valueOf(dm.heightPixels/density) + "dp\n";
        Log.i(TAG, "displaydpi: " + str);
    }

}
