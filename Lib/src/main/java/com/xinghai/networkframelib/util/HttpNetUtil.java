package com.xinghai.networkframelib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created on 16/12/27.
 * author: yuanbaoyu
 */

public class HttpNetUtil {
    private static boolean isConnected = true;

    /**
     * 获取是否连接
     */
    public static boolean isConnected() {
        return isConnected;
    }

    private static void setConnected(boolean connected) {
        isConnected = connected;
    }

    /**
     * 判断网络连接是否存在
     */
    public static boolean setConnected(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            setConnected(false);
            return false;
        }

        NetworkInfo info = manager.getActiveNetworkInfo();

        boolean connected = info != null && info.isConnected();
        setConnected(connected);

        return connected;
    }
}
