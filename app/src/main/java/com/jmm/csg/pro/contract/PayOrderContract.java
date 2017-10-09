package com.jmm.csg.pro.contract;

import com.jmm.csg.bean.PayResult;
import com.jmm.csg.bean.UnionPayBean;
import com.jmm.csg.bean.WeChatBean;

public interface PayOrderContract {

    interface V extends BaseContract.View {

        void onWeChatRequestSuccess(WeChatBean bean);

        void onUnionPaySuccess(UnionPayBean bean);

        void queryResult(PayResult result);
    }

    interface P extends BaseContract.Presenter<V> {

        void payOrder(String orderId, String userId);

        void payWeChat(String orderId);

        void unionPay(String orderId);

        void orderQuery(String payid, String txnTime);
    }
}
