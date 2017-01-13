package com.xinghai.networkframelib.common.listener;

/**
 * Created on 17/1/3.
 * author: yuanbaoyu
 */

public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
