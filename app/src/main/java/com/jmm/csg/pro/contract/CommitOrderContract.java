package com.jmm.csg.pro.contract;

import com.jmm.csg.bean.AddressInfo;

public interface CommitOrderContract {

    interface V extends BaseContract.View {
        void defaultAddress(AddressInfo.Entity entity);

        void commitOrderSuccess(String orderId);

        void productListSuccess(String bankNum);

        void missionListSuccess(String bankNum);
    }

    interface P extends BaseContract.Presenter<V> {
        void getDefaultAddress(String userId);

        void commitOrder(String accountManagerId, String productId,
                         String addressInfo, String pickWay, String count,
                         String userId, String remark, String InvoiceInfo,
                         String invoiceType, String titleType, String missionId,
                         String companyName, String taxIdentNum);

        void productList(String id, String coding);

        void missionList(String id, String coding);
    }
}
