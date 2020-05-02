package com.gnepux.wsgo.util;

/**
 * @author gnepux
 */
public class WsGoLog {

    private static final String TAG = "[WsGo]";

    public static boolean DEBUG = false;

    public static void d(String message) {
        if (DEBUG) {
            System.out.println(TAG + message);
        }
    }

}
