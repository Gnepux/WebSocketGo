package com.gnepux.wsgo.util;

import android.util.Log;

/**
 * @author gnepux
 */
public class WsGoLog {

    private static final String TAG = "WsGo";

    public static boolean DEBUG = false;

    public static void i(String message) {
        if (DEBUG) {
            Log.i(TAG, message);
        }
    }

    public static void d(String message) {
        if (DEBUG) {
            Log.d(TAG, message);
        }
    }

    public static void e(String message) {
        if (DEBUG) {
            Log.e(TAG, message);
        }
    }

}
