package com.jmm.csg.pro.manager.activity;

import android.view.View;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;
import butterknife.OnClick;

public class IntegralRuleActivity extends BaseActivity {

    @Bind(R.id.titleView) TitleView titleView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_integral_rule;
    }

    @Override
    public void initView() {
        titleView.setOnLeftImgClickListener(view -> finish());
    }


    @OnClick(R.id.tv_ok)
    public void onClick(View view) {
        finish();
    }
}
