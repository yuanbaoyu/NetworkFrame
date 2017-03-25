package com.xinghai.networkframelib.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.xinghai.networkframelib.BaseApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;


/**
 * 获取手机信息
 *
 * @author user
 * @since 1.0
 */
public class Device {
    private static final String FILE_MEMORY = "/proc/meminfo";
    private static final String FILE_CPU = "/proc/cpuinfo";
    public String mIMEI;
    public int mPhoneType;
    public int mSysVersion;
    public String mNetWorkCountryIso;
    public String mNetWorkOperator;
    public String mNetWorkOperatorName;
    public int mNetWorkType;
    public boolean mIsOnLine;
    public String mConnectTypeName;
    public long mFreeMem;
    public long mTotalMem;
    public String mCupInfo;
    public String mProductName;
    public String mModelName;
    public String mManufacturerName;

    /**
     * 获取屏幕信息
     *
     * @param context
     * @return
     * @author user
     * @since 1.0
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm;
    }

    /**
     * 获取手机IMEI号 需要权限
     *
     * @param context
     * @return
     * @author liaoww
     * @since 1.0
     */
    public static String getIMEI(Context context) {
        String imei = "";
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        // check if has the permission
        if (PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission(
                Manifest.permission.READ_PHONE_STATE, context.getPackageName())) {
            imei = manager.getDeviceId();
        }

        return imei == null || "".equals(imei) ? "isFake" : imei;
    }

    public static String getUdid() {

        return DeviceId.getDeviceID(BaseApplication.getInstance().getApplicationContext());
    }


    /**
     * 获取手机网络制式 获取通讯类型，GSM,CDMA...
     *
     * @param context
     * @return
     * @author liaoww
     * @since 1.0
     */
    public static String getPhoneType(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        return String.valueOf(manager.getPhoneType());
    }

    /**
     * 获取手机系统版本
     *
     * @return
     * @author liaoww
     * @since 1.0
     */
    public static String getSysVersion() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    /**
     * 获取移动通讯的国家注册码（移动区域码）
     *
     * @param context
     * @return
     * @author liaoww
     * @since 1.0
     */
    public static String getNetWorkCountryIso(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        return manager.getNetworkCountryIso();
    }

    /**
     * 获取手机Mac地址
     *
     * @param context
     * @return
     * @author user
     * @since 1.0
     */
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();

    }

    /**
     * 用IMSI号码获取手机运营商
     *
     * @param context
     * @return
     * @author user
     * @since 1.0
     */
    public static String getProvidersName(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String ProvidersName = null;
        String IMSI = null;
        // 返回唯一的用户ID;就是这张卡的编号
        IMSI = telephonyManager.getSubscriberId();
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        System.out.println(IMSI);
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        }
        return ProvidersName;
    }

    /**
     * 获取网络运营商名称
     *
     * @param context
     * @return
     * @author user
     * @since 1.0
     */
    public static String getNetWorkOperatorName(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        return manager.getNetworkOperatorName();
    }

    /**
     * 获取当前网络类别
     *
     * @param context
     * @return
     * @author user
     * @since 1.0
     */
    public static int getNetworkType(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        return manager.getNetworkType();
    }

    /**
     * 是否已联网
     *
     * @param context
     * @return
     * @author user
     * @since 1.0
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 获取手机ip地址
     *
     * @return
     * @author user
     * @since 1.0
     */
    public static String GetIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = ipAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获得当前数据连接类型的名称.比如：GPRS,WIFI
     *
     * @param context
     * @return
     * @author liaoww
     * @since 1.0
     */
    public static String getConnectTypeName(Context context) {
        if (!isOnline(context)) {
            return "OFFLINE";
        }
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null) {
            return info.getTypeName();
        } else {
            return "OFFLINE";
        }
    }

    /**
     * 获取手机剩余内存容量
     *
     * @param context
     * @return
     * @author user
     * @since 1.0
     */
    public static long getFreeMem(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        MemoryInfo info = new MemoryInfo();
        manager.getMemoryInfo(info);
        long free = info.availMem / 1024 / 1024;
        return free;
    }

    /**
     * 获取手机sd卡剩余容量
     *
     * @return
     * @author user
     * @since 1.0
     */
    public static long getAvailaleSize() {
        File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
        // (availableBlocks * blockSize)/1024 KIB 单位

        // (availableBlocks * blockSize)/1024 /1024 MIB单位

    }

    /**
     * 获取手机总内存
     *
     * @param context
     * @return
     * @author user
     * @since 1.0
     */
    public static long getTotalMem(Context context) {
        try {
            FileReader fr = new FileReader(FILE_MEMORY);
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split("\\s+");

            return Long.valueOf(array[1]) / 1024;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取手机状态栏高度
     *
     * @param context
     * @return
     * @author user
     * @since 1.0
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获取手机cpu信息
     *
     * @return
     * @author user
     * @since 1.0
     */
    public static String getCpuInfo() {
        try {
            FileReader fr = new FileReader(FILE_CPU);
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {

            }

            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取手机名称
     *
     * @return
     * @author user
     * @since 1.0
     */
    public static String getProductName() {
        return Build.PRODUCT;
    }

    /**
     * 获取手机型号
     *
     * @return
     * @author user
     * @since 1.0
     */
    public static String getModelName() {
        return Build.MODEL;
    }

    /**
     * 获取手机设备制造商信息
     *
     * @return
     * @author user
     * @since 1.0
     */
    public static String getManufacturerName() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取项目版本号
     *
     * @param ctx
     * @return
     * @author user
     * @since 1.0
     */
    public static String getSoftVersion(Context ctx) {
        PackageManager pm = ctx.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi == null ? null : pi.versionName;
    }

    /**
     * sd卡是否存在
     *
     * @return
     * @author liaoww
     * @since 1.0
     */
    public static boolean isSdExit() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    public static String getVersionName() {
        try {
            PackageInfo pi = BaseApplication.getInstance().getApplicationContext().getPackageManager()
                    .getPackageInfo(BaseApplication.getInstance().getApplicationContext().getPackageName(), 0);
            return pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "2.1";

    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static int getVersionCode() {
        try {
            PackageInfo pi = BaseApplication.getInstance().getApplicationContext().getPackageManager()
                    .getPackageInfo(BaseApplication.getInstance().getApplicationContext().getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取包名
     * @return
     */
    public static String getPackageName(){
        return BaseApplication.getInstance().getApplicationContext().getPackageName();
    }

    /**
     * 判断服务是否已经启动
     *
     * @param className
     * @return
     */
    public static boolean isServiceRunning(String className) {
        boolean isRunning = false;
        ActivityManager manager = (ActivityManager) BaseApplication.getInstance().getApplicationContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = manager.getRunningServices(50);
        if (serviceList != null) {
            for (int i = 0; i < serviceList.size(); i++) {
                if (serviceList.get(i).service.getClassName().equals(className) == true) {
                    isRunning = true;
                    break;
                }
            }
        }
        return isRunning;
    }

    /**
     * 获取屏幕的宽度
     * @param ctx
     * @return
     */
    public static int getScreenWidth(Context ctx)
    {
        WindowManager wm = ((Activity)ctx).getWindowManager();

        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕的宽度
     * @param ctx
     * @return
     */
    public static int getScreenHeight(Context ctx)
    {
        WindowManager wm = ((Activity)ctx).getWindowManager();

        return  wm.getDefaultDisplay().getHeight();
    }
}