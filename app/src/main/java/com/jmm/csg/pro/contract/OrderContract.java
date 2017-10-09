package com.jmm.csg.pro.contract;

public interface OrderContract {

    interface V extends BaseContract.View {

        void onSuccess();
    }

    interface P extends BaseContract.Presenter<V> {

        void changeOrderState(String orderId, String userId, String status);
    }
}
