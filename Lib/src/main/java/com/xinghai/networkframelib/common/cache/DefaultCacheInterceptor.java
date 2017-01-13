package com.xinghai.networkframelib.common.cache;

import com.xinghai.networkframelib.util.HttpNetUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created on 16/12/27.
 * author: yuanbaoyu
 *
 * description:
 * 有网络时,缓存时间1分钟
 * 无网络时,强制读取缓存,缓存保存时长为一周
 *
 */

public class DefaultCacheInterceptor implements Interceptor {
    private static final int MAX_AGE = 60;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (!HttpNetUtil.isConnected()) {
            request = request.newBuilder()
                    //强制使用缓存
                    .cacheControl(CacheControl.FORCE_CACHE).build();
        }

        Response response = chain.proceed(request);

        if (HttpNetUtil.isConnected()) {
            int maxAge = MAX_AGE; // 有网络时 设置缓存超时1分钟
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Pragma")
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 7; // 无网络时，设置超时为1周
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build();
        }
        return response;
    }
}
