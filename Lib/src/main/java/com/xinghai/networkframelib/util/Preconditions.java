package com.xinghai.networkframelib.util;

/**
 * 验证类
 *
 * Created on 17/3/23.
 * author: yuanbaoyu
 */
public class Preconditions {

    private Preconditions() {
        throw new AssertionError("No instances.");
    }

    public static <T> T checkNotNull(T value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
        return value;
    }
}
