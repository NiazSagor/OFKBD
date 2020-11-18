package com.ofk.bd.Utility;

import android.util.Log;

import com.ofk.bd.BuildConfig;

public class LogUtils {
    public static void log(String x) {
        if (BuildConfig.DEBUG)
            Log.i("Sagor", x);
    }

    public static void log(int x) {
        if (BuildConfig.DEBUG)
            Log.i("Sagor", String.valueOf(x));
    }

}
