package com.jmm.csg.pro.product.fragment;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jmm.csg.R;
import com.jmm.csg.base.fragment.BaseFragment;
import com.jmm.csg.imgload.ImageLoaderUtils;

import butterknife.Bind;


/**
 * author：hs
 * date: 2017/6/23 0023 09:49
 */

public class ProductsFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.radioGroup) RadioGroup radioGroup;
    @Bind(R.id.rb_product) RadioButton rbProduct;
    @Bind(R.id.iv_logo) ImageView ivLogo;
    private Fragment[] fragments = new Fragment[2];

    private boolean isLoadLogo;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_product;
    }

    @Override
    protected void initView() {
        rbProduct.setChecked(true);
        radioGroup.setOnCheckedChangeListener(this);
        fragments[0] = new AllProductListFragment();
        fragments[1] = new MissionListFragment();
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i) {
            case R.id.rb_product:
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.rb_mission:
                viewPager.setCurrentItem(1, false);
                break;
        }
    }

    public void setBankLogo(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) { // 没有LOGO,隐藏布局
            ivLogo.setVisibility(View.GONE);
        } else {
            if(!isLoadLogo){
                ivLogo.setVisibility(View.VISIBLE);
                ImageLoaderUtils.getInstance().loadOSSImage(getActivity(), imageUrl, ivLogo);
                isLoadLogo = true;
            }
        }
    }
}
