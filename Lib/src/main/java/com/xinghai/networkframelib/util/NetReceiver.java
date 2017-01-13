package com.xinghai.networkframelib.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created on 16/12/27.
 * author: yuanbaoyu
 *
 * 监听网络状态变化
 */

public class NetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

       if(HttpNetUtil.setConnected(context)){

       }

    }
}
