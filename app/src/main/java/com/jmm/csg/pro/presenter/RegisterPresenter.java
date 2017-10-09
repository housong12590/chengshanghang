package com.jmm.csg.pro.presenter;

import com.jmm.csg.Constant;
import com.jmm.csg.bean.BaseResp;
import com.jmm.csg.helper.AppDataHelper;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.CommonSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.pro.contract.RegisterContract;
import com.jmm.csg.utils.RxUtils;
import com.jmm.csg.utils.ToastUtils;

import rx.Subscription;

public class RegisterPresenter extends XPresenter<RegisterContract.V> implements RegisterContract.P {

    @Override
    public void getVerificationCode(String phone, String smsFlag) {
//        checkLoginId(phone);
        Subscription subscribe = HttpModule.checkLoginId(HttpParams.checkLoginId(phone))
                .filter(baseResp -> {
                    boolean isFilter = false;
                    if (smsFlag.equals("fwd")) {
                        isFilter = baseResp.getStatus().equals("0");//忘记密码需要账号已经存在
                    } else if (smsFlag.equals("reg")) {
                        isFilter = baseResp.getStatus().equals("1");//注册需要账号不存在
                    }
                    if (!isFilter) { // 不符合获取验证码条件, 提示用户原因
                        ToastUtils.showMsg(baseResp.getMessage());
                    }
                    return isFilter;
                })
                .flatMap(baseResp -> HttpModule.getVerificationCode(HttpParams.getVerificationCode(phone, smsFlag)))
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseResp>(getView()) {
                    @Override
                    public void onNext(BaseResp baseResp) {
                        ToastUtils.showMsg(baseResp.getMessage());
                        if (baseResp.getStatus().equals("0")) {
                            getView().startTime();
                        }
                    }
                });
//        Subscription subscribe = HttpModule.getVerificationCode(HttpParams.getVerificationCode(phone, smsFlag))
//                .subscribe(new CommonSubscriber<BaseResp>(getView()) {
//                    @Override
//                    public void onNext(BaseResp resp) {
//                        ToastUtils.showMsg(resp.getMessage());
//                        if (resp.getStatus().equals("0")) {
//                            getView().startTime();
//                        }
//                    }
//                });
        addSubscribe(subscribe);
    }

    @Override
    public void register(String phone, String password, String smsCode, boolean isChecked) {
        if (!checkParams(phone, password, smsCode, isChecked)) {
            return;
        }
        Subscription subscribe = HttpModule.register(HttpParams.register(phone, password, smsCode))
                .subscribe(new CommonSubscriber<BaseResp>() {
                    @Override
                    public void onNext(BaseResp resp) {

//                        UserInfo userInfo = SpHelper.getUserInfo();
//                        if (userInfo == null) {
//                            userInfo = new UserInfo();
//                        }
//                        userInfo.setUserId(resp.getMessage());
//                        SpHelper.setUserInfo(userInfo);
                        if (resp.getStatus().equals("0")) {
                            AppDataHelper.setPhoneNumber(phone);
                            UserDataHelper.setUserId(resp.getMessage());
                            getView().onSuccess();
                        } else {
                            ToastUtils.showMsg(resp.getMessage());
                        }
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void forgetPassword(String phone, String password, String smsCode) {
        if (!checkParams(phone, password, smsCode, true)) {
            return;
        }
        Subscription subscribe = HttpModule.forgetpwd(HttpParams.forgetpwd(phone, password, smsCode))
                .subscribe(new CommonSubscriber<BaseResp>(getView()) {
                    @Override
                    public void onNext(BaseResp resp) {
                        if (resp.getStatus().equals("0")) {
                            getView().onSuccess();
                        }
                        ToastUtils.showMsg(resp.getMessage());
                    }
                });
        addSubscribe(subscribe);
    }


    private boolean checkParams(String phone, String password, String smsCode, boolean isChecked) {
        if (!phone.matches(Constant.PHONE_CHECK)) {
            ToastUtils.showMsg("请输入正确的手机号");
            return false;
        }
        if (!password.matches(Constant.PWD_CHECK)) {
            ToastUtils.showMsg("密码格式不正确，请重新设置");
            return false;
        }
        if (!smsCode.matches(Constant.VERIFY_CODE_CHECK)) {
            ToastUtils.showMsg("请输入6位验证码");
            return false;
        }
        if (!isChecked) {
            ToastUtils.showMsg("请同意注册协议");
            return false;
        }
        return true;
    }
}
