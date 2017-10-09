package com.jmm.csg.pro.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.imgload.ImageLoaderUtils;
import com.jmm.csg.pro.order.fragment.OrderListFragment;
import com.jmm.csg.pro.order.fragment.RCOrderListFragment;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;

/**
 * 订单列表
 */
public class OrderListActivity extends BaseActivity {

    @Bind(R.id.titleView) TitleView titleView;
    @Bind(R.id.tabLayout) TabLayout tabLayout;
    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.iv_logo) ImageView mIvLogo;
    private int page;
    private String[] orderTitles;
    private SparseArray<Fragment> fragments = new SparseArray<>();
    private boolean isPer;

    @Override
    public void parseIntent(Intent intent) {
        page = intent.getIntExtra("page", 0);
        isPer = intent.getBooleanExtra("isPer", false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_list;
    }

    @Override
    public void initView() {
        titleView.setOnLeftImgClickListener(view -> finish());
        if (TextUtils.isEmpty(UserDataHelper.getBankLogo())) {
            mIvLogo.setVisibility(View.GONE);
        } else {
            ImageLoaderUtils.getInstance().loadOSSImage(this, UserDataHelper.getBankLogo(), mIvLogo);
        }
        orderTitles = getResources().getStringArray(R.array.orderTitleArray);
        OrderPagerAdapter adapter = new OrderPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(page, false);
//        smoothScroll
    }

    private class OrderPagerAdapter extends FragmentPagerAdapter {

        public OrderPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return getFragment(position);
        }

        @Override
        public int getCount() {
            return orderTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return orderTitles[position];
        }
    }

    private Fragment getFragment(int position) {
        Fragment fragment = fragments.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                    fragment = new OrderListFragment();
                    break;
                case 5:
                    fragment = new RCOrderListFragment();
                    break;
            }
            Bundle bundle = new Bundle();
//            bundle.putInt("position", position);
//            bundle.putSerializable("status", getStatus(position));
            bundle.putString("status", getStatus(position));
            bundle.putBoolean("isPer", isPer);
            fragment.setArguments(bundle);
            fragments.put(position, fragment);
        }
        return fragment;
    }

    public String getStatus(int position) {
        switch (position) {
            case 0:
                return null;
            case 1:
                return "M";
            case 2:
                return "C";
            case 3:
                return "S";
            case 4:
                return "F";
            case 5:
                return null;
        }
        return null;
    }

//    public OrderStatus1 getStatus(int position) {
//        switch (position) {
//            case 0:
//                return OrderStatus1.A;
//            case 1:
//                return OrderStatus1.M;
//            case 2:
//                return OrderStatus1.C;
//            case 3:
//                return OrderStatus1.S;
//            case 4:
//                return OrderStatus1.F;
//            case 5:
//                return OrderStatus1.T;
//        }
//        return null;
//    }

}
