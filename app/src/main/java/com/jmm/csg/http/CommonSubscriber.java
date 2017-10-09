package com.jmm.csg.http;


import com.jmm.csg.pro.contract.BaseContract;

public abstract class CommonSubscriber<T> extends BaseSubscriber<T> {

    private BaseContract.View view;
    private boolean isShowDialog = true;

    public CommonSubscriber() {

    }

    public CommonSubscriber(BaseContract.View view) {
        this.view = view;
    }

    public CommonSubscriber(BaseContract.View view, boolean isShowDialog) {
        this.view = view;
        this.isShowDialog = isShowDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (view == null) {
            return;
        }
        if (isShowDialog) {
            view.showDialog();
        } else {
            view.onLoading();
        }
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        if (view == null) {
            return;
        }
        if (isShowDialog) {
            view.dismissDialog();
        } else {
            view.onCompleted();
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        if (view == null) {
            return;
        }
        if (isShowDialog) {
            view.dismissDialog();
        } else {
            view.onError();
        }
    }
}
