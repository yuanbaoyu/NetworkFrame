package com.xinghai.networkframelib.common.result;

import com.xinghai.networkframelib.common.exception.ApiException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 预处理服务器返回数据
 *
 * Created on 17/3/15.
 * author: yuanbaoyu
 */
public class RxResultHelper {

    /**
     * 预处理结果
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<HttpResult<T>, T> handleResult() {
        return new ObservableTransformer<HttpResult<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<HttpResult<T>> upstream) {
                return upstream.flatMap(new Function<HttpResult<T>, Observable<T>>() {
                    @Override
                    public Observable<T> apply(HttpResult<T> tHttpResult) throws Exception {

                        if (tHttpResult.meta.ecode == 0) {
                            return createData(tHttpResult.data);
                        } else {
                            return Observable.error(new ApiException(tHttpResult.meta.ecode, tHttpResult.meta.emsg));
                        }
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io());
            }
        };
    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                try {
                    e.onNext(data);
                    e.onComplete();
                }catch (Exception exception){
                    e.onError(exception);
                }
            }
        });
    }

}
