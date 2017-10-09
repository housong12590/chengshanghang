package com.jmm.csg.pro.presenter;

import android.text.TextUtils;

import com.jmm.csg.bean.BaseResp;
import com.jmm.csg.bean.UploadBean;
import com.jmm.csg.http.CommonSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.pro.contract.AfterSaleContract;
import com.jmm.csg.utils.OSSUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscription;

public class AfterSalePresenter extends XPresenter<AfterSaleContract.V> implements AfterSaleContract.P {
    @Override
    public void getRECountByOrderId(String orderId) {
        Subscription subscribe = HttpModule.getRECountByOrderId(HttpParams.getRECountByorderId(orderId))
                .subscribe(new CommonSubscriber<BaseResp>(getView()) {
                    @Override
                    public void onNext(BaseResp resp) {
                        getView().reQuantity(resp.getMessage());
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void insertREOrder(Map<String, String> params, List<UploadBean> list) {
        if (!checkParams(params)) {
            return;
        }
        getView().showDialog();
        Observable<BaseResp> observable;
        if (list.size() == 0) {
            observable = HttpModule.insertREorder(params);
        } else {
            observable = OSSUtils.uploadImages(list).last()
                    .flatMap(s -> HttpModule.insertREorder(params));
        }
        Subscription subscribe = observable.subscribe(new CommonSubscriber<BaseResp>(getView()) {
            @Override
            public void onNext(BaseResp resp) {
                if (resp.getStatus().equals("0")) {
                    getView().onSuccess();
                }
            }
        });
        addSubscribe(subscribe);

    }

    private boolean checkParams(Map<String, String> params) {
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (TextUtils.isEmpty(entry.getValue())) {
                return false;
            }
        }
        return true;
    }
}
