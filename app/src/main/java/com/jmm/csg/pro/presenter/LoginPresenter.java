package com.jmm.csg.pro.presenter;


import android.text.TextUtils;

import com.jmm.csg.Constant;
import com.jmm.csg.bean.UserInfo;
import com.jmm.csg.helper.AppDataHelper;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.CommonSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.pro.contract.LoginContract;
import com.jmm.csg.utils.ToastUtils;

import rx.Subscription;

public class LoginPresenter extends XPresenter<LoginContract.V> implements LoginContract.P {


    @Override
    public void login(String phone, String password) {
        if (!checkParams(phone, password)) {
            return;
        }
        Subscription subscribe = HttpModule.login(HttpParams.login(phone, password))
                .subscribe(new CommonSubscriber<UserInfo>(getView()) {
                    @Override
                    public void onNext(UserInfo userInfo) {
                        if (userInfo.getStatus().equals("0")) {
//                            AppDataHelper.setFirstUse(false);
                            AppDataHelper.setPhoneNumber(phone);
                            AppDataHelper.setLoginOnline(true);
                            UserDataHelper.setUserId(userInfo.getUserId());
                            UserDataHelper.setRealNameStatus(userInfo.getRealNameStatus());
                            getView().loginSuccess();
                        } else {
                            ToastUtils.showMsg(userInfo.getMessage());
                        }
                    }
                });
        addSubscribe(subscribe);
    }

    private boolean checkParams(String phone, String password) {
        if (!phone.matches(Constant.PHONE_CHECK)) {
            ToastUtils.showMsg("请输入正确的手机号");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showMsg("请输入登录密码");
            return false;
        }
        return true;
    }
}
