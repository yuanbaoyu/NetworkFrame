package com.xinghai.networkframelib.common.result;

import com.google.gson.JsonObject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created on 17/3/15.
 * author: yuanbaoyu
 */
public abstract class BaseObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T value) {
        next(value);
    }

    @Override
    public void onError(Throwable e) {
//        if(e instanceof HttpException) {
//            LogUtils.e("========>" + ((HttpException) e).message());
//            error(createError(((HttpException) e).code(), ((HttpException) e).message()));
//        } else if(e instanceof TimeoutException){
//            LogUtils.e("========>网络连接超时");
//            error(createError(ErrorCodeConstant.TIMEOUT, e.getMessage()));
//        } else if(e instanceof UnknownHostException){
//            LogUtils.e("========>网络地址解析失败");
//            error(createError(ErrorCodeConstant.NO_NET, e.getMessage()));
//        } else if(e instanceof ApiException){
//            LogUtils.e("========>" + e.getMessage());
//            error(createError(((ApiException) e).getEcode(), e.getMessage()));
//        } else {
//            error(createError(ErrorCodeConstant.UNKNOWN, e.getMessage()));
//        }
    }

    @Override
    public void onComplete() {
        complete();
    }

    public abstract void next(T value);

    public abstract void error(String msg);

    public abstract void complete();

    private String createError(int code, String msg){
        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty(ParamConstant.ECODE, code);
//        jsonObject.addProperty(ParamConstant.EMSG, msg);
        return jsonObject.toString();
    }
}
