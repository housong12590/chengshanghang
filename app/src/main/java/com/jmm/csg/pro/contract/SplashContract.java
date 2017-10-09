package com.jmm.csg.pro.contract;


public interface SplashContract {

    interface V extends BaseContract.View {

        void jumpActivity();
    }

    interface P extends BaseContract.Presenter<V> {

        void splashDelay(long time);
    }


}
