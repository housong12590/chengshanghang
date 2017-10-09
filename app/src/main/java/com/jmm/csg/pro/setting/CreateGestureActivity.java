package com.jmm.csg.pro.setting;

import android.content.Intent;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;
import butterknife.OnClick;

public class CreateGestureActivity extends BaseActivity {

    @Bind(R.id.titleView) TitleView titleView;
    private boolean isHasSkip;

    @Override
    public void parseIntent(Intent intent) {
        isHasSkip = intent.getBooleanExtra("isHasSkip", false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_gesture;
    }

    @Override
    public void initView() {
        titleView.setOnLeftImgClickListener(v -> finish());
        if (isHasSkip) {
            titleView.setRightText("跳过");
            titleView.setOnRightTextClickListener(v -> finish());
        }
    }


    @OnClick(R.id.tv_create)
    public void onClick() {
        startActivityForResult(new Intent(this, PasswordLockActivity.class), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(RESULT_OK);
        finish();
    }
}
