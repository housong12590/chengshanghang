package com.jmm.csg.pro.presenter;

import com.jmm.csg.bean.BaseResp;
import com.jmm.csg.bean.PayResult;
import com.jmm.csg.bean.UnionPayBean;
import com.jmm.csg.bean.WeChatBean;
import com.jmm.csg.http.CommonSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.pro.contract.PayOrderContract;
import com.jmm.csg.utils.ToastUtils;

import rx.Subscription;

public class PayOrderPresenter extends XPresenter<PayOrderContract.V> implements PayOrderContract.P {
    @Override
    public void payOrder(String orderId, String userId) {
        Subscription subscribe = HttpModule.payOrder(HttpParams.payOrder(userId, orderId))
                .subscribe(new CommonSubscriber<BaseResp>(getView()) {
                    @Override
                    public void onNext(BaseResp resp) {
                        if (resp.getStatus().equals("0")) {
                        }
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void payWeChat(String orderId) {
        Subscription subscribe = HttpModule.payWx(HttpParams.payWx(orderId))
                .subscribe(new CommonSubscriber<WeChatBean>(getView(), false) {
                    @Override
                    public void onNext(WeChatBean resp) {
                        if (resp.getResult().equals("SUCCESS")) {
                            getView().onWeChatRequestSuccess(resp);
                            return;
                        } else if (resp.getResult().equals("001")) {
                            ToastUtils.showMsg("库存不足");
                        } else if (resp.getResult().equals("002")) {
                            ToastUtils.showMsg("产品已下架");
                        } else if (resp.getResult().equals("003")) {
                            ToastUtils.showMsg("商品不存在");
                        } else if (resp.getResult().equals("004")) {
                            ToastUtils.showMsg("订单错误");
                        } else if (resp.getResult().equals("005")) {
                            ToastUtils.showMsg("订单已支付");
                        } else {
                            ToastUtils.showMsg("订单异常");
                        }
                        getView().onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
//                        getView().onError();
                        getView().onCompleted();
                    }

                    @Override
                    public void onCompleted() {
//                        super.onCompleted();
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void unionPay(String orderId) {
        HttpModule.appPay(HttpParams.appPay(orderId))
                .subscribe(new CommonSubscriber<UnionPayBean>(getView(), false) {
                    @Override
                    public void onNext(UnionPayBean resp) {
                        if (resp.getResult().equals("SUCCESS")) {
                            getView().onUnionPaySuccess(resp);
                            return;
                        } else if (resp.getResult().equals("001")) {
                            ToastUtils.showMsg("库存不足");
                        } else if (resp.getResult().equals("002")) {
                            ToastUtils.showMsg("产品已下架");
                        } else if (resp.getResult().equals("003")) {
                            ToastUtils.showMsg("商品不存在");
                        } else if (resp.getResult().equals("004")) {
                            ToastUtils.showMsg("订单错误");
                        } else if (resp.getResult().equals("005")) {
                            ToastUtils.showMsg("订单已支付");
                        } else {
                            ToastUtils.showMsg("订单异常");
                        }
                        getView().onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
//                        getView().onError();
                        getView().onCompleted();
                    }

                    @Override
                    public void onCompleted() {
//                        super.onCompleted();
                    }
                });
    }

    @Override
    public void orderQuery(String payId, String txnTime) {
        HttpModule.orderQuery(HttpParams.orderQuery(txnTime, payId))
                .subscribe(new CommonSubscriber<PayResult>(getView()) {
                    @Override
                    public void onNext(PayResult resp) {
                        getView().queryResult(resp);
                    }
                });
    }
}
