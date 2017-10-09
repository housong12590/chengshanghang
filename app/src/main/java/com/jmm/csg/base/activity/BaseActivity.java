package com.jmm.csg.base.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jmm.csg.BaseApplication;
import com.jmm.csg.R;
import com.jmm.csg.helper.DialogHelper;
import com.jmm.csg.rxbus.RxBus;
import com.jmm.csg.utils.ActivityUtils;
import com.jmm.csg.utils.StatusBarUtils;
import com.jmm.csg.widget.LoadingDialog;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    public LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            parseIntent(intent);
        }
        ActivityUtils.addActivity(this);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        RxBus.getDefault().register(this);
        onCreate();
        initView();
        initData();
    }


    public void parseIntent(Intent intent) {

    }

    public abstract int getLayoutId();

    public void onCreate() {

    }

    public abstract void initView();

    public void initData() {

    }

    public boolean isOpenGesture() {
        return true;
    }

    public void showDialog() {
        if (loadingDialog == null) {
            loadingDialog = DialogHelper.getLoadingDialog(this);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public void dismissDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        String gesturePassword = SpHelper.getGesturePassword();
//        boolean isOpen = SpHelper.isGestureOpen();
//        if (isAppBack() && !TextUtils.isEmpty(gesturePassword) && isOpenGesture() && isOpen) {
//            startActivity(new Intent(this, GestureUnlockActivity.class));
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        BaseApplication.app_status_code++;
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        BaseApplication.app_status_code--;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 判断应用是否在后台
     */
    private boolean isAppBack() {
        return BaseApplication.app_status_code == 0;
    }

    public static void exit() {
        ActivityUtils.removeAllActivity();
    }

    public void setStatusBarBackgroundAndColor() {
        if (StatusBarUtils.StatusBarLightMode(this) != 0) {
            StatusBarUtils.setStatusBarColor(this, R.color.white);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.removeActivity(this);
        RxBus.getDefault().unRegister(this);
        ButterKnife.unbind(this);
    }
}
