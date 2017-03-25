package com.xinghai.networkframelib;

import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xinghai.networkframelib.common.interceptor.GzipRequestInterceptor;
import com.xinghai.networkframelib.common.logger.HttpLoggingInterceptor;
import com.xinghai.networkframelib.common.parse.MultiplyConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created on 16/12/12.
 * author: yuanbaoyu
 */

public class RetrofitClient {

    private Retrofit mRetrofit;
    private String baseUrl = "https://raw.github.com/";

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private RetrofitClient() {
        mRetrofit = new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MultiplyConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getDefaultOkHttpClient())
                .build();
    }

    @NonNull
    public OkHttpClient getDefaultOkHttpClient(){

        return new OkHttpClient().newBuilder()
//                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                //gzip压缩上传
                .addInterceptor(new GzipRequestInterceptor())
                //连接超时
                .connectTimeout(10, TimeUnit.SECONDS)
                //读取超时
                .readTimeout(30, TimeUnit.SECONDS)
                //写入超时
                .writeTimeout(10, TimeUnit.SECONDS)
                //stetho,可以在chrome中查看请求
                .addNetworkInterceptor(new StethoInterceptor())
                //失败重连
                .retryOnConnectionFailure(true)
                .build();
    }

    public  <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return mRetrofit.create(service);
    }
}
