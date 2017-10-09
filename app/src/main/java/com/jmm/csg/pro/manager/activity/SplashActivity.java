package com.jmm.csg.pro.manager.activity;

import android.content.Intent;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.XActivity;
import com.jmm.csg.helper.AppDataHelper;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.pro.contract.SplashContract;
import com.jmm.csg.pro.presenter.SplashPresenter;
import com.jmm.csg.pro.setting.GestureUnlockActivity;


public class SplashActivity extends XActivity<SplashPresenter> implements SplashContract.V {

    private static final int SPLASH_TIME = 2;

    @Override
    public int getLayoutId() {
        transparentStatusBar();
        return R.layout.activity_splash;
    }


    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        getP().splashDelay(SPLASH_TIME);
    }

    @Override
    public boolean isOpenGesture() {
        return false;
    }

    @Override
    public SplashPresenter newPresenter() {
        return new SplashPresenter();
    }

    @Override
    public void jumpActivity() {
        if (AppDataHelper.isFirstUse()) { //用户第一次使用APP
            startActivity(new Intent(this, GuideActivity.class));
        } else if (AppDataHelper.isLoginOnline()) {//用户使用过并处于在线状态
            if (!UserDataHelper.getRealNameStatus().equals("2")) {//实名认证没有通过,跳转到登录页
                startActivity(new Intent(this, LoginActivity.class));
            } else if (UserDataHelper.isGestureUnlock()) {//用户设置了手势
                startActivity(new Intent(this, GestureUnlockActivity.class));
            } else { // 没有设置手势,直接到主页
//                startActivity(new Intent(this, MainActivity.class));
                startActivity(new Intent(this, LoginActivity.class));
            }
        } else { //用户使用过,但是退出登录了
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
