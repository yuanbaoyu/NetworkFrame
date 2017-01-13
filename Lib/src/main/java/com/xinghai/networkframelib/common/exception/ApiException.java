package com.xinghai.networkframelib.common.exception;

import com.xinghai.networkframelib.constants.Constants;

/**
 * Created on 17/1/4.
 * author: yuanbaoyu
 */

public class ApiException extends RuntimeException {
    private int mErrorCode;

    public ApiException(int errorCode, String errorMessage) {
        super(errorMessage);
        mErrorCode = errorCode;
    }

    /**
     * 判断是否是token失效
     *
     * @return 失效返回true, 否则返回false;
     */
    public boolean isTokenExpried() {
        return mErrorCode == Constants.TOKEN_EXPRIED;
    }

    /**
     * 判断是否存在token
     *
     * @return 不存在返回true，否则返回false
     */
    public boolean isTokenNotExist(){
        return mErrorCode == Constants.TOKEN_NOT_EXIST;
    }
}
