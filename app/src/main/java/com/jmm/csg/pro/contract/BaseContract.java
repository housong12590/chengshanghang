package com.jmm.csg.pro.contract;

public interface BaseContract {

    interface View {

        void showDialog();

        void dismissDialog();

        void onLoading();

        void onError();

        void onCompleted();
    }

    interface Presenter<V> {

        void attachView(V view);

        void detachView();
    }
}
