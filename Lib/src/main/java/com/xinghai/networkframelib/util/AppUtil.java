package com.xinghai.networkframelib.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xinghai.networkframelib.BaseApplication;

import java.util.List;

public class AppUtil {

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground(Context ctx) {
        if (null == ctx) {
            return false;
        }

        ActivityManager activityManager = (ActivityManager) ctx.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = ctx.getApplicationContext().getPackageName();

        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
//            LogUtils.e("liaoww", "isAppOnForeground_process_null");
            return false;
        }

        for (RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)) {
//                LogUtils.e("liaoww", "appProcess.importance:" + appProcess.importance);
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        || appProcess.importance == RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 网络是否已连接
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (null != info) {
            isConnected = info.isAvailable();
        }

        return isConnected;
    }


    /**
     * activity 是否在 top
     * 需要 android.permission.GET_TASKS 权限
     *
     * @param activity
     * @return
     */
    public static boolean isTopActivity(Activity activity) {
        boolean isTop = true;
        try {
            ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            isTop = cn.getClassName().contains(activity.getLocalClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTop;
    }

}
