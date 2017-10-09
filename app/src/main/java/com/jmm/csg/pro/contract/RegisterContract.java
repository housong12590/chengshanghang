package com.jmm.csg.pro.contract;


public interface RegisterContract {

    interface V extends BaseContract.View {

        void startTime();

        void onSuccess();
    }

    interface P extends BaseContract.Presenter<V> {

        void getVerificationCode(String phone, String smsFlag);

        void register(String phone, String password, String smsCode, boolean isChecked);

        void forgetPassword(String phone, String password, String smsCode);
    }
}
