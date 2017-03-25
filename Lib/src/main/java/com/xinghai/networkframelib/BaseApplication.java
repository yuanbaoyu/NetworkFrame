package com.xinghai.networkframelib;

import android.app.Application;

import com.xinghai.networkframelib.util.StethoHelper;

/**
 * Created on 17/1/16.
 * author: yuanbaoyu
 */

public class BaseApplication extends Application {
    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        StethoHelper.init(this);
    }

    public static BaseApplication getInstance(){
        return instance;
    }

}
