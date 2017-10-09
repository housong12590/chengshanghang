package com.jmm.csg.pro.presenter;


import com.jmm.csg.bean.AddressInfo;
import com.jmm.csg.bean.Product;
import com.jmm.csg.bean.SubmitOrderBean;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.BaseSubscriber;
import com.jmm.csg.http.CommonSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.pro.contract.CommitOrderContract;
import com.jmm.csg.utils.ToastUtils;

import java.util.List;

import rx.Subscription;

public class CommitOrderPresenter extends XPresenter<CommitOrderContract.V> implements CommitOrderContract.P {
    @Override
    public void getDefaultAddress(String userId) {
        Subscription subscribe = HttpModule.getAddressList(HttpParams.getAddressList(UserDataHelper.getUserId()))
                .subscribe(new BaseSubscriber<AddressInfo>() {
                    @Override
                    public void onNext(AddressInfo addressInfo) {
                        List<AddressInfo.Entity> address = addressInfo.getAddress();
                        for (AddressInfo.Entity s : address) {
                            if (s.getIsPrimary().equals("1")) {
                                getView().defaultAddress(s);
                                return;
                            }
                        }
                        AddressInfo.Entity entity = null;
                        if (address.size() != 0) {
                            entity = address.get(0);
                        }
                        getView().defaultAddress(entity);
                    }
                });
//                .flatMap(addressInfo -> Observable.from(addressInfo.getAddress()))
//                .filter(entity -> entity.getIsPrimary().equals("1"))
//                .first().subscribe(new BaseSubscriber<AddressInfo.Entity>() {
//                    @Override
//                    public void onNext(AddressInfo.Entity entity) {
//                        getView().defaultAddress(entity);
//                    }
//                });
        addSubscribe(subscribe);
    }

    @Override
    public void commitOrder(String accountManagerId, String productId, String addressInfo,
                            String pickWay, String count, String userId, String remark,
                            String InvoiceInfo, String invoiceType, String titleType, String missionId,
                            String companyName, String taxIdentNum) {
        Subscription subscribe = HttpModule.submitOrder(HttpParams.submitOrder(accountManagerId, productId, addressInfo, pickWay,
                count, userId, remark, InvoiceInfo, invoiceType, titleType, missionId, companyName, taxIdentNum))
                .subscribe(new CommonSubscriber<SubmitOrderBean>(getView()) {
                    @Override
                    public void onNext(SubmitOrderBean resp) {
                        if (resp.getCode().equals("1")) {
                            getView().commitOrderSuccess(resp.getData());
                        } else {
                            ToastUtils.showMsg(resp.getMessage());
                        }
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void productList(String id, String coding) {
        HttpModule.productList(HttpParams.productList(id, coding))
                .subscribe(new BaseSubscriber<List<Product>>() {
                    @Override
                    public void onNext(List<Product> products) {
                        if (products != null && products.size() > 1) {
                            Product product = products.get(1);
                            String bankNum = product.getBankNum();
                            getView().productListSuccess(bankNum);
                        }
                    }
                });
    }

    @Override
    public void missionList(String id, String coding) {
        HttpModule.missionList(HttpParams.missionList(id, coding))
                .subscribe(new BaseSubscriber<List<Product>>() {
                    @Override
                    public void onNext(List<Product> products) {
                        if (products != null && products.size() > 1) {
                            Product product = products.get(1);
                            String bankNum = product.getBankNum();
                            getView().missionListSuccess(bankNum);
                        }
                    }
                });
    }
}
