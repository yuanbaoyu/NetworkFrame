package com.xinghai.networkframelib;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created on 17/1/16.
 * author: yuanbaoyu
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
