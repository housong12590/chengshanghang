package com.jmm.csg.pro.setting;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;
import butterknife.OnClick;

public class SetupPwdLockActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.cb_switch) CheckBox mCbSwitch;
    @Bind(R.id.titleView) TitleView mTitleView;
    @Bind(R.id.tv_change_pwd) TextView mTvChangePwd;
    @Bind(R.id.empty) View empty;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setup_pwd_lock;
    }

    @Override
    public void initView() {
        mTitleView.setOnLeftImgClickListener(v -> finish());
        mCbSwitch.setOnCheckedChangeListener(this);
        boolean open = UserDataHelper.isGestureUnlock();
        mCbSwitch.setChecked(open);
        mTvChangePwd.setVisibility(open ? View.VISIBLE : View.GONE);
        empty.setVisibility(open ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.tv_change_pwd)
    public void onClick() {
        startActivity(new Intent(this, PasswordLockActivity.class));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (TextUtils.isEmpty(UserDataHelper.getGesturePassword()) && isChecked) {
            startActivityForResult(new Intent(this, PasswordLockActivity.class), 100);
            return;
        }
        mTvChangePwd.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        empty.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        UserDataHelper.setGestureUnlock(isChecked);
//        RxBus.getDefault().post(Constant.SETUP_GESTURE_PWD, isChecked);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCbSwitch.setChecked(UserDataHelper.isGestureUnlock());
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }
}
