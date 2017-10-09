package com.jmm.csg.base.activity;

import android.view.View;

import com.jmm.csg.pro.contract.BaseContract;
import com.jmm.csg.widget.VisibilityLayout;


public abstract class XActivity<P extends BaseContract.Presenter> extends BaseActivity
        implements BaseContract.View {

    protected VisibilityLayout vml;
    private P presenter;


    @Override
    public void onCreate() {
        View visibilityView = getVisibilityView();
        if (visibilityView != null) {
            vml = new VisibilityLayout(visibilityView);
        }
        presenter = newPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
    }

    public View getVisibilityView() {
        return null;
    }

    public abstract P newPresenter();


    public P getP() {
        return presenter;
    }

    @Override
    public void onLoading() {
        if (vml != null) {
            vml.showLoadingView();
        }
    }

    @Override
    public void onError() {
        if (vml != null) {
            vml.showErrorView();
        }
    }

    @Override
    public void onCompleted() {
        if (vml != null) {
            vml.showContentView();
        }
    }

    @Override
    public void dismissDialog() {
        super.dismissDialog();
    }

    @Override
    public void showDialog() {
        super.showDialog();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }

}
