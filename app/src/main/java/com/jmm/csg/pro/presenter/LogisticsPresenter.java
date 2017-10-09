package com.jmm.csg.pro.presenter;

import com.jmm.csg.bean.LogisticsBean;
import com.jmm.csg.http.BaseSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.pro.contract.LogisticsContract;

import rx.Subscription;

public class LogisticsPresenter extends XPresenter<LogisticsContract.V> implements LogisticsContract.P {
    @Override
    public void logisticLis(String orderId) {
        Subscription subscribe = HttpModule.logisticLis(HttpParams.logisticLis(orderId))
                .subscribe(new BaseSubscriber<LogisticsBean>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        getView().onLoading();
                    }

                    @Override
                    public void onNext(LogisticsBean resp) {
                        getView().onSuccess(resp);
                    }
                });
        addSubscribe(subscribe);
    }
}
