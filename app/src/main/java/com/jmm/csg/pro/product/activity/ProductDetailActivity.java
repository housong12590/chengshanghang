package com.jmm.csg.pro.product.activity;

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
import com.jmm.csg.bean.ProductDetail;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.imgload.ImageLoaderUtils;
import com.jmm.csg.pro.product.fragment.EvaluateCategoryFragment;
import com.jmm.csg.pro.product.fragment.DetailFragment;
import com.jmm.csg.pro.product.fragment.ProductDetailFragment;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;

/**
 * 商品详情
 */
public class ProductDetailActivity extends BaseActivity {

    @Bind(R.id.tabLayout) TabLayout mTabLayout;
    @Bind(R.id.viewPager) ViewPager mViewPager;
    @Bind(R.id.titleView) TitleView mTitleView;
    @Bind(R.id.iv_logo) ImageView mIvLogo;
    private DetailAdapter adapter;
    private String[] titleArray;

    private SparseArray<Fragment> fragments = new SparseArray<>();
    private String productId;
    private String proPic;
    private String missionId;
    private ProductDetail productDetail;
    private String missionNum;
    private String finishNum;
    private String productnum;
    private String productName;
    private String flag;

    @Override
    public void parseIntent(Intent intent) {
        productId = intent.getStringExtra("productId");
        proPic = intent.getStringExtra("proPic");
        missionId = intent.getStringExtra("missionId");
        missionNum = intent.getStringExtra("missionNum");
        finishNum = intent.getStringExtra("finishNum");
        productnum = intent.getStringExtra("productnum");
        productName = intent.getStringExtra("productName");
        flag = intent.getStringExtra("flag");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_product_detail;
    }


    @Override
    public void initView() {
        mTitleView.setOnLeftImgClickListener(view -> finish());
        mTitleView.setTitle(productName);
        titleArray = getResources().getStringArray(R.array.detailTitleArray);
        adapter = new DetailAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        if (TextUtils.isEmpty(UserDataHelper.getBankLogo())) {
            mIvLogo.setVisibility(View.GONE);
        }
        ImageLoaderUtils.getInstance().loadOSSImage(this, UserDataHelper.getBankLogo(), mIvLogo);
    }

    @Override
    public void initData() {
//        HttpModule.proDetail(HttpParams.proDetail(productId, SpHelper.getUserId()))
//                .subscribe(new BaseSubscriber<ProductDetail>() {
//                    @Override
//                    public void onNext(ProductDetail detail) {
//                        productDetail = detail;
//                    }
//                });
    }


    private class DetailAdapter extends FragmentPagerAdapter {

        public DetailAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return getFragment(position);
        }

        @Override
        public int getCount() {
            return titleArray.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleArray[position];
        }
    }

    private Fragment getFragment(int position) {
        Fragment fragment = fragments.get(position);
        Bundle bundle = new Bundle();
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new ProductDetailFragment();
//                    bundle.putSerializable("de",productDetail.getProduct().get(0));
                    break;
                case 1:
                    fragment = new DetailFragment();
                    break;
                case 2:
                    fragment = new EvaluateCategoryFragment();
                    break;
            }
            bundle.putString("productId", productId);
            if (position == 0) {
                bundle.putString("missionId", missionId);
                bundle.putString("proPic", proPic);
                bundle.putString("missionNum", missionNum);
                bundle.putString("finishNum", finishNum);
                bundle.putString("productnum", productnum);
                bundle.putString("flag", flag);
            }
            fragment.setArguments(bundle);
            fragments.put(position, fragment);
        }
        return fragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
