package com.jmm.csg.pro.manager.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.helper.AppDataHelper;

import butterknife.Bind;

public class GuideActivity extends BaseActivity {

    @Bind(R.id.viewPager) ViewPager viewPager;
    private int[] imgs = {R.drawable.icon_guide1, R.drawable.icon_guide2, R.drawable.icon_guide3};

    @Override
    public int getLayoutId() {
        transparentStatusBar();
        return R.layout.activity_guide;
    }

    @Override
    public void initView() {
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgs.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView iv = new ImageView(GuideActivity.this);
                iv.setImageResource(imgs[position]);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                if (position == imgs.length - 1) {
                    iv.setOnClickListener(v -> {
                        AppDataHelper.setFirstUse(false);
                        startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                        finish();
                    });
                }
                container.addView(iv);
                return iv;
            }
        });
    }
}
