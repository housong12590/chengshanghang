package com.jmm.csg.pro.presenter;


import android.text.TextUtils;

import com.jmm.csg.bean.Bank;
import com.jmm.csg.bean.BaseResp;
import com.jmm.csg.bean.RealNameBean;
import com.jmm.csg.bean.UploadBean;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.CommonSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.pro.contract.UserCenterContract;
import com.jmm.csg.utils.OSSUtils;
import com.jmm.csg.utils.ToastUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscription;

public class UserCenterPresenter extends XPresenter<UserCenterContract.V> implements UserCenterContract.P {

    @Override
    public void changeAvatar() {

    }

    @Override
    public Observable<Bank> getBanks() {
        return HttpModule.getBanks(HttpParams.signature());
    }

    @Override
    public Observable<List<Bank.BranchBank>> getBranchBanks(String bankId, String province, String city, String district) {
        return HttpModule.getBranchBanks(HttpParams.getBranchBanks(bankId, province, city, district));
    }

    @Override
    public void commitRealName(Map<String, String> params, List<UploadBean> list) {
//        if (!checkParams(params)) {
//            return;
//        }
        getView().showDialog();
        Subscription subscribe = OSSUtils.uploadImages(list) // 上传图片
                .last()
                .flatMap(s -> HttpModule.realNameRequest(params)) // 实名认证请求
                .subscribe(new CommonSubscriber<RealNameBean>(getView()) {
                    @Override
                    public void onNext(RealNameBean resp) {
                        if (resp.getRespCode().equals("0000")) {
                            getView().onSuccess();
                            UserDataHelper.setRealNameStatus("1");
                        } else {
                            ToastUtils.showMsg(resp.getRespMsg());
                        }
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void modifyMgrPhone(String id, String phone) {
        Subscription subscribe = HttpModule.modifyMgrPhone(HttpParams.modifyMgrPhone(id, phone))
                .subscribe(new CommonSubscriber<BaseResp>() {
                    @Override
                    public void onNext(BaseResp baseResp) {
                        if (baseResp.getMessage().equals("success")) {
                            ToastUtils.showMsg("修改成功");
                        }
                    }
                });
        addSubscribe(subscribe);
    }

    private boolean checkParams(Map<String, String> params) {
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (TextUtils.isEmpty(entry.getValue())) {
                ToastUtils.showMsg("请填写完整信息");
                return false;
            }
        }
        return true;
    }
}
