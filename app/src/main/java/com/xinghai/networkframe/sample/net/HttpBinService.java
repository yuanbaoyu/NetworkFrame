package com.xinghai.networkframe.sample.net;

import com.xinghai.networkframe.sample.entity.Ip;
import com.xinghai.networkframelib.common.annotations.Json;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created on 16/12/12.
 * author: yuanbaoyu
 */

public interface HttpBinService {
    @GET("http://httpbin.org/ip") @Json
    Call<Ip> getIp();
}
