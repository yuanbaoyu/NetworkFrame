package com.xinghai.networkframelib.base;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xinghai.networkframelib.util.NetReceiver;

/**
 * Created on 16/12/27.
 * author: yuanbaoyu
 */

public class BaseActivity extends AppCompatActivity {

    private NetReceiver mNetReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBroadCast();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mNetReceiver);
        super.onDestroy();
    }

    private void initBroadCast() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mNetReceiver = new NetReceiver();
        registerReceiver(mNetReceiver, filter);
    }



}
