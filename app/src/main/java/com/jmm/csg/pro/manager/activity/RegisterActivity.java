package com.jmm.csg.pro.manager.activity;

import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jmm.csg.config.AppConfig;
import com.jmm.csg.R;
import com.jmm.csg.base.activity.XActivity;
import com.jmm.csg.helper.AppDataHelper;
import com.jmm.csg.pro.contract.RegisterContract;
import com.jmm.csg.pro.presenter.RegisterPresenter;
import com.jmm.csg.pro.setting.WebViewActivity;
import com.jmm.csg.widget.DelayedTimeView;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;
import butterknife.OnClick;

public class RegisterActivity extends XActivity<RegisterPresenter> implements RegisterContract.V {

    @Bind(R.id.titleView)
    TitleView mTitleView;
    @Bind(R.id.et_password)
    EditText mEtPassword;
    @Bind(R.id.et_phone_num)
    EditText mEtPhoneNum;
    @Bind(R.id.cb_agreement)
    CheckBox mCbAgreement;
    @Bind(R.id.et_verify_code)
    EditText mEtVerifyCode;
    @Bind(R.id.tv_user_agreement)
    TextView mTvUserAgreement;
    @Bind(R.id.verificationCode)
    DelayedTimeView delayedTimeView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        mTitleView.setOnLeftImgClickListener(view -> finish());
        delayedTimeView.bindEditText(mEtPhoneNum);
        String agreement = mTvUserAgreement.getText().toString();
        SpannableString sp = new SpannableString(agreement);
        sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_set)),
                1, agreement.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvUserAgreement.setText(sp);
    }

    @Override
    public void startTime() {
        delayedTimeView.start();
    }

    @Override
    public void onSuccess() {
        startActivity(new Intent(RegisterActivity.this, RealNameAuthActivity.class));
        String phoneNum = mEtPhoneNum.getText().toString().trim();
        AppDataHelper.setPhoneNumber(phoneNum);
        finish();
    }


    @Override
    public RegisterPresenter newPresenter() {
        return new RegisterPresenter();
    }

    @OnClick({R.id.tv_register, R.id.verificationCode, R.id.tv_user_agreement})
    public void onClick(View view) {
        String phoneNum = mEtPhoneNum.getText().toString().trim();
        switch (view.getId()) {
            case R.id.tv_register:
                String password = mEtPassword.getText().toString().trim();
                String verifyCode = mEtVerifyCode.getText().toString().trim();
                boolean checked = mCbAgreement.isChecked();
                getP().register(phoneNum, password, verifyCode, checked);
                break;
            case R.id.verificationCode:
                getP().getVerificationCode(phoneNum, "reg");
                break;
            case R.id.tv_user_agreement:
//                startActivity(new Intent(this, WebViewActivity.class));
                WebViewActivity.start(this, "用户协议", AppConfig.REGISTER_URL);
                break;
        }
    }
}
