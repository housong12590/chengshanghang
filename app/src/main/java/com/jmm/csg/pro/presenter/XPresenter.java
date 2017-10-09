package com.jmm.csg.pro.presenter;


import com.jmm.csg.pro.contract.BaseContract;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class XPresenter<V extends BaseContract.View> implements BaseContract.Presenter<V> {

    protected V view;
    protected CompositeSubscription mCompositeSubscription;

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        unSubscribe();
    }

    public V getView() {
        if (view == null) {
            throw new IllegalStateException("v can not be null");
        }
        return view;
    }

    protected void addSubscribe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }
}