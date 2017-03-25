package com.xinghai.networkframelib.common.lifecycle;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Rx生命周期管理辅助类
 * Created on 17/3/24.
 * author: yuanbaoyu
 */
public class RxLifecycleHelper {

    public static <T>ObservableTransformer<T, T> bind(final BehaviorSubject<ActivityEvent> lifecycleSubject){
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.takeUntil(lifecycleSubject.filter(new Predicate<ActivityEvent>() {
                    @Override
                    public boolean test(ActivityEvent activityEvent) throws Exception {
                        return activityEvent.equals(ActivityEvent.DESTROY);
                    }
                }));
            }
        };
    }
}
