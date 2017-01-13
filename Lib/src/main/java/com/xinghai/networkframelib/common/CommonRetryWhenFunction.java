package com.xinghai.networkframelib.common;

import com.xinghai.networkframelib.common.exception.ApiException;
import com.xinghai.networkframelib.common.exception.TokenInvalidException;
import com.xinghai.networkframelib.common.exception.TokenNotExistException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * 通用重试机制
 *
 * Created on 17/1/5.
 * author: yuanbaoyu
 */

public class CommonRetryWhenFunction implements Function<Observable<Throwable>, ObservableSource<?>> {

    private int count = 5;
    private long delay = 5000;
    private long increaseDelay = 5000;

    public CommonRetryWhenFunction() {
    }

    public CommonRetryWhenFunction(int count, long delay) {
        this.count = count;
        this.delay = delay;
    }

    public CommonRetryWhenFunction(int count, long delay, long increaseDelay) {
        this.count = count;
        this.delay = delay;
        this.increaseDelay = increaseDelay;
    }

    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable.zipWith(Observable.range(1, count + 1), new BiFunction<Throwable, Integer, Wrapper>() {
            @Override
            public Wrapper apply(Throwable throwable, Integer integer) throws Exception {
                return new Wrapper(throwable, integer);
            }
        }).flatMap(new Function<Wrapper, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Wrapper wrapper) throws Exception {

                if ((wrapper.throwable instanceof ConnectException
                        || wrapper.throwable instanceof SocketTimeoutException
                        || wrapper.throwable instanceof TimeoutException)
                        && wrapper.index < count + 1) { //如果超出重试次数也抛出错误，否则默认是会进入onCompleted
                    return Observable.timer(delay + (wrapper.index - 1) * increaseDelay, TimeUnit.MILLISECONDS);
                }

                if(wrapper.throwable instanceof ApiException){

                    ApiException apiException = (ApiException) wrapper.throwable;

                    if(apiException.isTokenNotExist()){//token不存在，返回TokenNotExistException
                        return Observable.error(new TokenNotExistException());
                    } else if(apiException.isTokenExpried()){//token失效，返回TokenInvalidException
                        return Observable.error(new TokenInvalidException());
                    }
                }

                return Observable.error(wrapper.throwable);
            }
        });
    }

    private class Wrapper {
        private int index;
        private Throwable throwable;

        public Wrapper(Throwable throwable, int index) {
            this.index = index;
            this.throwable = throwable;
        }
    }
}
