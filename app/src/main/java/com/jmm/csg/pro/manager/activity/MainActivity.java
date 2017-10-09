package com.jmm.csg.pro.manager.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jmm.csg.config.AppConfig;
import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.pro.manager.fragment.MineFragment;
import com.jmm.csg.pro.manager.fragment.MsgFragment;
import com.jmm.csg.pro.product.fragment.ProductsFragment;

import butterknife.Bind;
import rx.Subscription;

/**
 * 主页
 */

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    //    @Bind(R.id.titleView) TitleView mTitleView;
    @Bind(R.id.radioGroup) RadioGroup mRadioGroup;
    @Bind(R.id.rb_message) RadioButton mRbMessage;
    @Bind(R.id.viewPager) ViewPager mViewPager;
    @Bind(R.id.rb_mine) RadioButton mRbMine;
    @Bind(R.id.rb_commodity) RadioButton mRbCommodity;

    private SparseArray<Fragment> fragments = new SparseArray<>();
    private long mExitTime;
    private Subscription subscribe;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        UserPageAdapter adapter = new UserPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        mRbMine.setChecked(true);
    }

    @Override
    public void initData() {
        subscribe = AppConfig.appCheckUpdate(this, false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_commodity:
                mViewPager.setCurrentItem(0, false);
//                mTitleView.setTitle("商品");
                break;
            case R.id.rb_message:
//                mViewPager.setCurrentItem(1, false);
//                mTitleView.setTitle("消息");
                break;
            case R.id.rb_mine:
                mViewPager.setCurrentItem(2, false);
//                mTitleView.setTitle("个人中心");
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mRbCommodity.setChecked(true);
                break;
            case 1:
                mRbMessage.setChecked(true);
                break;
            case 2:
                mRbMine.setChecked(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class UserPageAdapter extends FragmentPagerAdapter {
        public UserPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return getFragment(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private Fragment getFragment(int position) {
        Fragment fragment = fragments.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
//                    fragment = new MissionListFragment();
                    fragment = new ProductsFragment();
                    break;
                case 1:
                    fragment = new MsgFragment();
                    break;
                case 2:
                    fragment = new MineFragment();
                    break;
            }
            fragments.put(position, fragment);
        }
        return fragment;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setViewPagerCurrentItem(int page) {
        mViewPager.setCurrentItem(page, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }
}
