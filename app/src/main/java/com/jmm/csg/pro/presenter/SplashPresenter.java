package com.jmm.csg.pro.presenter;


import com.jmm.csg.pro.contract.SplashContract;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class SplashPresenter extends XPresenter<SplashContract.V> implements SplashContract.P {

    @Override
    public void splashDelay(long time) {
        addSubscribe(Observable.timer(time, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    getView().jumpActivity();
                }));
    }
}
