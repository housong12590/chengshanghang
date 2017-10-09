package com.jmm.csg.http;


import com.jmm.csg.bean.BaseResp;

import rx.Observable;
import rx.functions.Func1;

public class BaseRespFunc<T> implements Func1<BaseResp<T>, Observable<T>> {
    @Override
    public Observable<T> call(BaseResp<T> resp) {
        if (!resp.getStatus().equals("200")) {
            return Observable.error(new HttpApiException(resp.getStatus(),resp.getMessage()));
        } else {
            return Observable.just(resp.getData());
        }
    }
}

