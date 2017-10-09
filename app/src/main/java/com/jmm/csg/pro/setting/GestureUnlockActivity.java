package com.jmm.csg.pro.setting;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.helper.AppDataHelper;
import com.jmm.csg.helper.DialogHelper;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.imgload.ImageLoaderUtils;
import com.jmm.csg.pro.manager.activity.LoginActivity;
import com.jmm.csg.pro.manager.activity.MainActivity;
import com.jmm.csg.widget.GesturePwdView;

import butterknife.Bind;
import butterknife.OnClick;


public class GestureUnlockActivity extends BaseActivity {

    @Bind(R.id.tv_hint) TextView mTvHint;
    @Bind(R.id.tv_text) TextView mTvText;
    @Bind(R.id.iv_avatar) ImageView ivAvatar;
    @Bind(R.id.gesturePwdView) GesturePwdView mGesturePwdView;

    private int count = 5;
    private Animation shakeAnimation;


    @Override
    public int getLayoutId() {
        return R.layout.activity_gesture_unlock;
    }


    @Override
    public boolean isOpenGesture() {
        return false;
    }

    @Override
    public void initView() {
        transparentStatusBar();
        ImageLoaderUtils.getInstance().loadOSSAvatar(this, UserDataHelper.getUserAvatar(), ivAvatar);
        mGesturePwdView.validationStatus = GesturePwdView.GesturePwdStatus.VALIDATE;
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_gesture_shake);
        mGesturePwdView.setOnGestureFinishListener((status, key, linedCycles) -> {
            if (!TextUtils.isEmpty(key) && key.equals(UserDataHelper.getGesturePassword())) { //验证成功
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else { //验证失败
                count--;
                if (count <= 0) {
                    DialogHelper.getSingleButtonDialog(GestureUnlockActivity.this,
                            "解锁失败", "连续5次解锁失败,请重新登录。", "确定", v -> {
                                UserDataHelper.setGestureUnlock(false);
                                UserDataHelper.setGesturePassword("");
                                AppDataHelper.setLoginOnline(false);
                                startActivity(new Intent(this, LoginActivity.class));
                                finish();
                            }).show();
                    return;
                }
                mTvText.setText("密码错误，还可以再试" + count + "次。");
                mTvText.setTextColor(Color.RED);
                mTvHint.setVisibility(View.VISIBLE);
                mTvText.startAnimation(shakeAnimation);
            }
        });
    }

    @OnClick(R.id.tv_forget)
    public void onClick() {
        AppDataHelper.setLoginOnline(false);
        UserDataHelper.setGestureUnlock(false);
        UserDataHelper.setGesturePassword("");
        startActivity(new Intent(this, LoginActivity.class));
        exit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
