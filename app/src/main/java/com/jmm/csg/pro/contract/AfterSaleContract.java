package com.jmm.csg.pro.contract;

import com.jmm.csg.bean.UploadBean;

import java.util.List;
import java.util.Map;

public interface AfterSaleContract {

    interface V extends BaseContract.View {

        void reQuantity(String quantity);

        void onSuccess();
    }

    interface P extends BaseContract.Presenter<V> {

        void getRECountByOrderId(String orderId);

        void insertREOrder(Map<String, String> params, List<UploadBean> list);
    }
}
