package com.jmm.csg.pro.contract;

import com.jmm.csg.bean.ReturnAddress;

import java.util.List;

/**
 * author：hs
 * date: 2017/6/13 0013 15:16
 */

public interface ReOrderContract {

    interface V extends BaseContract.View {

        void getReturnAddressSuccess(ReturnAddress bean);

        void submitSuccess();
    }

    interface P extends BaseContract.Presenter<V> {

        void getReturnAddress(String orderItemsId);

        void commitReturnDetail(String ordersId, String expressName, String expressNo, List<String> images);
    }
}
