package com.xinghai.networkframelib;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.xinghai.networkframelib.common.exception.ApiException;
import com.xinghai.networkframelib.common.exception.TokenInvalidException;
import com.xinghai.networkframelib.common.exception.TokenNotExistException;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created on 17/1/3.
 * author: yuanbaoyu
 */

public abstract class BaseObserver<T> implements Observer<T> {

    private Context mContext;

    public BaseObserver(Context context) {
        mContext = context;
    }

    @Override
    public void onSubscribe(Disposable d) {
        Log.e("===>", "onSubscribe");
    }

    @Override
    public void onNext(T value) {
        Log.e("===>", "onNext");
    }

    @Override
    public void onError(Throwable e) {
        Log.e("===>", "onError");

        if (e instanceof HttpException) {
            // We had non-2XX http error
            Toast.makeText(mContext, mContext.getString(R.string.server_internal_error), Toast.LENGTH_SHORT).show();
        } else if (e instanceof IOException) {
            // A network or conversion error happened
            Toast.makeText(mContext, mContext.getString(R.string.cannot_connected_server), Toast.LENGTH_SHORT).show();
        } else if (e instanceof TokenNotExistException) {
            //处理token不存在对应的逻辑
            Log.e("===>", "token不存在");
        } else if (e instanceof TokenInvalidException) {
            //处理token失效对应的逻辑
            Log.e("===>", "token失效");
        } else if (e instanceof ApiException) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onComplete() {
        Log.e("===>", "onComplete");
    }
}
