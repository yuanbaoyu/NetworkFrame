package com.xinghai.networkframelib.util;

import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * Created on 17/3/14.
 * author: yuanbaoyu
 */
public class StethoHelper {

    public static void init(Context context){
        Stetho.initialize(
                Stetho.newInitializerBuilder(context)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                        .build());
    }
}
