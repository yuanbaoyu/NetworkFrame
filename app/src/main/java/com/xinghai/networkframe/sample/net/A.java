package com.xinghai.networkframe.sample.net;

import com.xinghai.networkframelib.common.annotations.Json;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Created on 16/12/28.
 * author: yuanbaoyu
 */

public interface A {
    @GET("/square/okhttp/master/README.md") @Json
    Observable<ResponseBody> get();
}
