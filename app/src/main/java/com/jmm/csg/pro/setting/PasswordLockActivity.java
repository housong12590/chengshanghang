package com.jmm.csg.pro.setting;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmm.csg.Constant;
import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.rxbus.RxBus;
import com.jmm.csg.widget.GesturePwdView;
import com.jmm.csg.widget.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class PasswordLockActivity extends BaseActivity implements View.OnClickListener {


    private int[] tipsIvIds = new int[]{R.id.gesture_pwd_iv0, R.id.gesture_pwd_iv1, R.id.gesture_pwd_iv2,
            R.id.gesture_pwd_iv3, R.id.gesture_pwd_iv4, R.id.gesture_pwd_iv5, R.id.gesture_pwd_iv6,
            R.id.gesture_pwd_iv7, R.id.gesture_pwd_iv8};
    private List<ImageView> tipsIvList = new ArrayList<>();
    private Animation shakeAnimation;
    @Bind(R.id.titleView) TitleView titleView;
    @Bind(R.id.gestureView) GesturePwdView mGesturePwdView;
    @Bind(R.id.ll_tip_layout) LinearLayout mLlTipLayout;
    @Bind(R.id.tv_tip) TextView mTvTip;


    @Override
    public int getLayoutId() {
        return R.layout.activity_gesture_setup;
    }


    @Override
    public void initView() {
        transparentStatusBar();
        titleView.setOnLeftImgClickListener(v -> finish());
        for (int tipsIvId : tipsIvIds) {
            ImageView iv = (ImageView) findViewById(tipsIvId);
            tipsIvList.add(iv);
        }
        // 左右移动动画
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_gesture_shake);
        // 手势完成后回调
        mGesturePwdView.setOnGestureFinishListener((status, key, linedCycles) -> {
            switch (status) {
                case GesturePwdView.GesturePwdStatus.NUMBER_ERROR:// 连接点数少于4个
                    mTvTip.setText("最少连接四个点,请重新输入");
                    mTvTip.startAnimation(shakeAnimation);
                    break;
                case GesturePwdView.GesturePwdStatus.VALIDATE_AGAIN:// 第一次手势绘制成功
                    // 二次验证状态
                    mTvTip.setText("请再次绘制解锁密码");
                    refreshTipsImg(linedCycles);
                    break;
                case GesturePwdView.GesturePwdStatus.VALIDATE_AGAIN_ERROR:// 手势设置失败
                    // 二次验证状态
                    mTvTip.setText("与上一次绘制不一致,请重新绘制");
                    mTvTip.startAnimation(shakeAnimation);
                    break;
                case GesturePwdView.GesturePwdStatus.VALIDATE_AGAIN_SUCCESS:// 手势设置成功
                    // 二次验证状态
                    UserDataHelper.setGesturePassword(key);
                    UserDataHelper.setGestureUnlock(true);
                    RxBus.getDefault().post(Constant.SETUP_GESTURE_PWD, true);
                    finish();
                    break;
            }
        });
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    /**
     * 刷新提示图片
     **/
    public void refreshTipsImg(List<Integer> linedCycles) {
        for (int i = 0; i < tipsIvList.size(); i++) {
            tipsIvList.get(i).setImageResource(R.drawable.shape_circle_nor);
            for (Integer integer : linedCycles) {
                if (i == integer) {
                    tipsIvList.get(i).setImageResource(R.drawable.shape_circle_set);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
