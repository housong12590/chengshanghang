package com.jmm.csg.pro.setting;

import android.content.Intent;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jmm.csg.config.AppConfig;
import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.bean.Account;
import com.jmm.csg.helper.AppDataHelper;
import com.jmm.csg.helper.DialogHelper;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.BaseSubscriber;
import com.jmm.csg.pro.manager.activity.LoginActivity;
import com.jmm.csg.pro.manager.activity.UserCenterActivity;
import com.jmm.csg.utils.FileUtils;
import com.jmm.csg.utils.RxUtils;
import com.jmm.csg.utils.ToastUtils;
import com.jmm.csg.widget.TitleView;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

public class SettingActivity extends BaseActivity {

    @Bind(R.id.titleView) TitleView mTitleView;
    @Bind(R.id.tv_cache_size) TextView mTvCacheSize;
    @Bind(R.id.tv_gesture_status) TextView mTvGestureStatus;
    private Account account;
    private boolean isChangeAvatar;

    @Override
    public void parseIntent(Intent intent) {
        account = (Account) intent.getSerializableExtra("account");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        mTitleView.setOnLeftImgClickListener(v -> finish());
        String gesturePassword = UserDataHelper.getGesturePassword();
        mTvGestureStatus.setText(TextUtils.isEmpty(gesturePassword) ? "未设置" :
                UserDataHelper.isGestureUnlock() ? "已启用" : "未启用");
        getCacheSize();
    }

    public void gestureStatusChange() {
        String gesturePassword = UserDataHelper.getGesturePassword();
        boolean isOpen = UserDataHelper.isGestureUnlock();
        mTvGestureStatus.setText(TextUtils.isEmpty(gesturePassword) ? "未设置" :
                isOpen ? "已启用" : "未启用");
    }

    @OnClick({R.id.ll_cache, R.id.ll_gesture, R.id.tv_logout, R.id.tv_about, R.id.tv_userInfo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_cache://清除缓存
                DialogHelper.getMessageDialog(this, "是否清除缓存?",
                        (dialog, which) -> clearCache()).show();
                break;
            case R.id.ll_gesture: //手势密码
                if (TextUtils.isEmpty(UserDataHelper.getGesturePassword())) {
                    startActivityForResult(new Intent(this, CreateGestureActivity.class), 100);
                } else {
                    startActivityForResult(new Intent(this, SetupPwdLockActivity.class), 100);
                }
                break;
            case R.id.tv_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.tv_logout:
                AppDataHelper.setLoginOnline(false);
                startActivity(new Intent(this, LoginActivity.class));
                exit();
                break;
            case R.id.tv_userInfo:
                Intent intent = new Intent(this, UserCenterActivity.class);
                intent.putExtra("account", account);
                startActivityForResult(intent, 100);
                break;
        }
    }

    private void getCacheSize() {
        try {
            Observable.create(new Observable.OnSubscribe<Long>() {
                @Override
                public void call(Subscriber<? super Long> subscriber) {
                    Long fileDirSize = FileUtils.getFileDirSize(new File(AppConfig.DEFAULT_IMAGE_PATH));
                    Long fileDirSize1 = FileUtils.getFileDirSize(Glide.getPhotoCacheDir(SettingActivity.this));
                    long size = fileDirSize + fileDirSize1;
                    subscriber.onNext(size);
                    subscriber.onCompleted();
                }
            }).map(aLong -> Formatter.formatFileSize(SettingActivity.this, aLong))
                    .compose(RxUtils.rxSchedulerHelper())
                    .subscribe(s -> mTvCacheSize.setText(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearCache() {
        Glide.get(this).clearMemory();
        try {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    Glide.get(SettingActivity.this).clearDiskCache();
                    File file = new File(AppConfig.DEFAULT_IMAGE_PATH);
                    if (!file.exists()) {
                        ToastUtils.showMsg("清除缓存失败");
                        return;
                    }
                    for (File f : file.listFiles()) {
                        boolean b = f.delete();
                        System.out.println(b ? "删除成功" : "删除失败" + "----" + f.getAbsolutePath());
                    }
                    subscriber.onNext("");
                    subscriber.onCompleted();
                }
            }).delay(1, TimeUnit.SECONDS)
                    .compose(RxUtils.rxSchedulerHelper())
                    .subscribe(new BaseSubscriber<String>() {
                        @Override
                        public void onNext(String s) {
                            getCacheSize();
                        }

                        @Override
                        public void onStart() {
                            mTvCacheSize.setText("...");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            gestureStatusChange();
            if (requestCode == 100) {
                isChangeAvatar = true;
            }
        }
    }

    @Override
    public void finish() {
        if (isChangeAvatar) {
            setResult(RESULT_OK);
        }
        super.finish();
    }
}