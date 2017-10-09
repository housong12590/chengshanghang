package com.jmm.csg.pro.manager.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.XActivity;
import com.jmm.csg.helper.AppDataHelper;
import com.jmm.csg.pro.contract.LoginContract;
import com.jmm.csg.pro.presenter.LoginPresenter;
import com.jmm.csg.utils.FileUtils;
import com.jmm.csg.utils.LogUtils;
import com.jmm.csg.utils.ToastUtils;
import com.jmm.csg.widget.TitleView;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.OnClick;

import static com.jmm.csg.config.AppConfig.DEFAULT_FILE_PATH;
import static com.jmm.csg.config.AppConfig.DEFAULT_IMAGE_PATH;

public class LoginActivity extends XActivity<LoginPresenter> implements LoginContract.V {

    @Bind(R.id.tv_login) TextView mTvLogin;
    @Bind(R.id.tv_register) TextView mTvRegister;
    @Bind(R.id.et_password) EditText mEtPassword;
    @Bind(R.id.et_phone_num) EditText mEtPhoneNum;
    @Bind(R.id.tv_forget_pwd) TextView mTvForgetPwd;
    @Bind(R.id.titleView) TitleView mTitleView;


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        mTvForgetPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mEtPhoneNum.setText(AppDataHelper.getPhoneNumber());
        RxPermissions.getInstance(this).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        FileUtils.createDirectory(DEFAULT_IMAGE_PATH, DEFAULT_FILE_PATH);
                    } else {
                        ToastUtils.showMsg("请去授权中心开启文件存储权限");
                    }
                });
    }


    @Override
    public void loginSuccess() {
        LogUtils.d(LoginActivity.class.getSimpleName(), "login success...");
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public LoginPresenter newPresenter() {
        return new LoginPresenter();
    }

    int count = 0;

    @OnClick({R.id.tv_login, R.id.tv_register, R.id.tv_forget_pwd, R.id.image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                String phoneNum = mEtPhoneNum.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();
                getP().login(phoneNum, password);
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_forget_pwd:
                startActivity(new Intent(this, ChangePwdActivity.class));
                break;
            case R.id.image: // 仅用来测试
//                count++;
//                if (count >= 8) {
//                    AppConfig.BASE_URL = AppConfig.TEST_BASE_URL;
//                    ToastUtils.showMsg("已切到测试环境");
//                    count = 0;
//                }
                break;
        }
    }
}
