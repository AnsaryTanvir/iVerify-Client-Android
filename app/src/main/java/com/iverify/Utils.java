package com.iverify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

public class Utils {

    public static boolean isInternetAvailable(Context ctx) {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) ctx.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getDeviceID(Context context){
        String device_id =  Build.MODEL+Build.MANUFACTURER+Build.BRAND + new StatFs(Environment.getDataDirectory().getPath()).getTotalBytes() + Build.BOARD+Build.DISPLAY;
        return device_id;
    }

}
