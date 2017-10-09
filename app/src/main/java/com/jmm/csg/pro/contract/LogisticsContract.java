package com.jmm.csg.pro.contract;

import com.jmm.csg.bean.LogisticsBean;

public interface LogisticsContract {

    interface V extends BaseContract.View {
        void onSuccess(LogisticsBean bean);
    }

    interface P extends BaseContract.Presenter<V> {

        void logisticLis(String orderId);
    }

}
