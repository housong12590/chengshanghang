package com.jmm.csg.pro.manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.bean.Integral;
import com.jmm.csg.callback.ConnectCallback;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.http.ProgressSubscriber;
import com.jmm.csg.pro.manager.fragment.ExchangeRecordFragment;
import com.jmm.csg.pro.manager.fragment.IntegralProductFragment;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;

public class IntegralProductActivity extends BaseActivity implements ConnectCallback {

    private String[] titles = {"商品种类", "兑换记录"};
    @Bind(R.id.titleView) TitleView titleView;
    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.tabLayout) TabLayout tabLayout;
    @Bind(R.id.tv_integral) TextView tvIntegral;
    private String type;
    private boolean isExchange;

    @Override
    public void parseIntent(Intent intent) {
        type = intent.getStringExtra("type");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_integral_product;
    }

    @Override
    public void initView() {
        titleView.setOnLeftImgClickListener(view -> finish());
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void initData() {
        refreshIntegral();
    }

    public void refreshIntegral() {
        HttpModule.getAvailable(HttpParams.getAvailable(UserDataHelper.getUserId()))
                .subscribe(new ProgressSubscriber<Integral>(this) {
                    @Override
                    public void onNext(Integral integral) {
                        tvIntegral.setText(integral.getData());
                    }
                });
    }

    @Override
    public void onRefresh() {
        isExchange = true;
        refreshIntegral();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle bundle = new Bundle();
            if (position == 0) {
                fragment = new IntegralProductFragment();
            } else if (position == 1) {
                fragment = new ExchangeRecordFragment();
            }
            bundle.putString("type", type);
            if (fragment != null) {
                fragment.setArguments(bundle);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Override
    public void finish() {
        if (isExchange) {
            setResult(RESULT_OK);
        }
        super.finish();
    }
}
