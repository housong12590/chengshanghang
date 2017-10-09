package com.jmm.csg.pro.manager.activity;

import android.view.View;
import android.widget.EditText;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.XActivity;
import com.jmm.csg.pro.contract.RegisterContract;
import com.jmm.csg.pro.presenter.RegisterPresenter;
import com.jmm.csg.widget.DelayedTimeView;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;
import butterknife.OnClick;

public class ChangePwdActivity extends XActivity<RegisterPresenter> implements RegisterContract.V {

    @Bind(R.id.titleView) TitleView mTitleView;
    @Bind(R.id.et_phone_num) EditText mEtPhoneNum;
    @Bind(R.id.et_password) EditText mEtPassword;
    @Bind(R.id.et_verify_code) EditText mEtVerifyCode;
    @Bind(R.id.delayedTimeView) DelayedTimeView delayedTimeView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_pwd;
    }

    @Override
    public void initView() {
        mTitleView.setOnLeftImgClickListener(view -> finish());
        delayedTimeView.bindEditText(mEtPhoneNum);
    }

    @Override
    public void startTime() {
        delayedTimeView.start();
    }

    @Override
    public void onSuccess() {
        finish();
    }

    @Override
    public RegisterPresenter newPresenter() {
        return new RegisterPresenter();
    }

    @OnClick({R.id.tv_commit, R.id.delayedTimeView})
    public void onClick(View view) {
        String phoneNum = mEtPhoneNum.getText().toString().trim();
        switch (view.getId()) {
            case R.id.tv_commit:
                String verifyCode = mEtVerifyCode.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();
                getP().forgetPassword(phoneNum, password, verifyCode);
                break;
            case R.id.delayedTimeView:
                getP().getVerificationCode(phoneNum, "fwd");
                break;
        }
    }
}
