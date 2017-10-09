package com.jmm.csg.pro.presenter;


import com.jmm.csg.bean.BaseResp;
import com.jmm.csg.http.CommonSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.pro.contract.EvaluateContract;

import java.util.Map;

import rx.Subscription;

public class EvaluatePresenter extends XPresenter<EvaluateContract.V> implements EvaluateContract.P {
    @Override
    public void commitComment(Map<String, String> params) {
        Subscription subscribe = HttpModule.createComment(params)
                .subscribe(new CommonSubscriber<BaseResp>(getView()) {
                    @Override
                    public void onNext(BaseResp resp) {
                        if (resp.getStatus().equals("0")) {
                            getView().onSuccess();
                        }
                    }
                });
        addSubscribe(subscribe);
    }
}
