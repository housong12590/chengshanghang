package com.jmm.csg.pro.presenter;

import com.jmm.csg.bean.BaseResp;
import com.jmm.csg.bean.ReturnAddress;
import com.jmm.csg.bean.UploadBean;
import com.jmm.csg.http.CommonSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.pro.contract.ReOrderContract;
import com.jmm.csg.utils.OSSUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscription;

/**
 * author：hs
 * date: 2017/6/13 0013 15:20
 */

public class ReOrderPresenter extends XPresenter<ReOrderContract.V> implements ReOrderContract.P {

    @Override
    public void getReturnAddress(String orderItemsId) {
        HttpModule.getReturnAddress(HttpParams.getReturnAddress(orderItemsId))
                .subscribe(new CommonSubscriber<ReturnAddress>(getView()) {
                    @Override
                    public void onNext(ReturnAddress resp) {
                        getView().getReturnAddressSuccess(resp);
                    }
                });
    }

    @Override
    public void commitReturnDetail(String ordersId, String expressName, String expressNo, List<String> images) {
        Observable<BaseResp> observable;
        getView().showDialog();

        List<UploadBean> tempList = new ArrayList<>();
        for (String s : images) {
            String path = OSSUtils.getCustomerService(s);
            tempList.add(new UploadBean(s, path));
        }
        Map<String, String> params = HttpParams.commitReturnDetail(ordersId, expressName, expressNo, tempList);
        if (tempList.size() == 0) {
            observable = HttpModule.commitReturnDetail(params);
        } else {
            observable = OSSUtils.uploadImages(tempList).last().flatMap(s -> HttpModule.commitReturnDetail(params));
        }
        Subscription subscribe = observable.subscribe(new CommonSubscriber<BaseResp>(getView()) {
            @Override
            public void onNext(BaseResp baseResp) {
                if(baseResp.getStatus().equals("0")){
                    getView().submitSuccess();
                }
            }
        });
        addSubscribe(subscribe);

    }
}
