package com.jmm.csg.pro.manager.activity;

import android.content.Intent;
import android.widget.TextView;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;

public class IntegralDetailsActivity extends BaseActivity {

    @Bind(R.id.titleView) TitleView titleView;
    @Bind(R.id.tv_integral) TextView tvIntegral;
    private String integral;

    @Override
    public void parseIntent(Intent intent) {
        integral = intent.getStringExtra("integral");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_integral_details;
    }

    @Override
    public void initView() {
        titleView.setOnLeftImgClickListener(view -> finish());
        tvIntegral.setText(integral);
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();
//        transaction.add(R.id.fl_content, new IntegralDetailsFragment());
//        transaction.commit();
    }
}
