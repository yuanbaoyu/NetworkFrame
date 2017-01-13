package com.xinghai.networkframelib.common;

import com.google.gson.annotations.SerializedName;
import com.xinghai.networkframelib.constants.Constants;

/**
 * Created on 17/1/4.
 * author: yuanbaoyu
 */

public class HttpStatus {
    @SerializedName("code")
    private int mCode;
    @SerializedName("message")
    private String mMessage;

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    /**
     * API是否请求失败
     *
     * @return 失败返回true, 成功返回false
     */
    public boolean isCodeInvalid() {
        return mCode != Constants.WEB_RESP_CODE_SUCCESS;
    }
}
