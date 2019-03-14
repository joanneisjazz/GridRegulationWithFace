package com.jstech.gridregulation.utils;

import android.util.Log;

public class LogUtils {
    private final static String TAG = "hesm";
    private final static boolean isDebug = true;

    private LogUtils(){
        throw new UnsupportedOperationException("can't be instantiated");
    }

    public static void v(String msg){
        if (isDebug){
            Log.v(TAG,msg);
        }
    }
    public static void d(String msg){
        if (isDebug){
            Log.d(TAG,msg);
        }
    }
    public static void w(String msg){
        if (isDebug){
            Log.w(TAG,msg);
        }
    }
    public static void i(String msg){
        if (isDebug){
            Log.i(TAG,msg);
        }
    }
    public static void e(String msg){
        if (isDebug){
            Log.e(TAG,msg);
        }
    }
}
