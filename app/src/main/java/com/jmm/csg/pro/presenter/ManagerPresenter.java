package com.jmm.csg.pro.presenter;


import com.jmm.csg.bean.ManagerCenter;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.BaseSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.pro.contract.ManagerContract;

import rx.Subscription;

public class ManagerPresenter extends XPresenter<ManagerContract.V> implements ManagerContract.P {

    @Override
    public void managerCenter(String userId) {
        Subscription subscribe = HttpModule.managerCenter(HttpParams.managerCenter(UserDataHelper.getUserId()))
                .subscribe(new BaseSubscriber<ManagerCenter>() {
                    @Override
                    public void onNext(ManagerCenter managerCenter) {
                        getView().getManagerInfoSuccess(managerCenter);
                    }
                });
        addSubscribe(subscribe);
    }
}
