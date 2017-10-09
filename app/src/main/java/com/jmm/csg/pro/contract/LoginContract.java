package com.jmm.csg.pro.contract;


public interface LoginContract {

    interface V extends BaseContract.View {
        void loginSuccess();
    }

    interface P extends BaseContract.Presenter<V> {

//        boolean checkParams(String phone, String password);

        void login(String phone, String password);
    }

}
