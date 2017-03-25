package com.xinghai.networkframelib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public final class DeviceId {
    private static String mDeviceId = null;

    private DeviceId() {
    }

    public static String getDeviceID(Context context) {
        if (mDeviceId == null) {
            SharedPreferences sharedpreferences = context.getSharedPreferences("bids", 0);
            String s = sharedpreferences.getString("i", null);

            if (s == null) {
                s = getIMEI(context);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("i", s);
                editor.commit();
            }
            String s1 = sharedpreferences.getString("a", null);
            if (s1 == null) {
                s1 = getAndroidId(context);
                SharedPreferences.Editor editor1 = sharedpreferences.edit();
                editor1.putString("a", s1);
                editor1.commit();
            }

            mDeviceId = Md5Tools.toMd5((new StringBuilder()).append("com.okay").append(s).append(s1).toString().getBytes(), true);
        }

        return mDeviceId;
    }

    private static String getIMEI(Context context) {
        String s = "";
        TelephonyManager telephonymanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonymanager != null) {
            s = telephonymanager.getDeviceId();
            if (TextUtils.isEmpty(s))
                s = "";
        }
        return s;
    }

    private static String getAndroidId(Context context) {
        String s = "";
        s = android.provider.Settings.Secure.getString(context.getContentResolver(), "android_id");
        if (TextUtils.isEmpty(s))
            s = "";
        return s;
    }
}