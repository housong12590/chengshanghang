package com.jmm.csg.widget;


import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.jmm.csg.R;

import java.util.ArrayList;
import java.util.List;

public class AutoViewPager extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private LinearLayout indicatorLayout;
    private ViewPager viewPager;
    private ArrayList<ImageView> indicators;
    private long delayTime = 4 * 1000;
    private Handler handler = new Handler();
    private List<String> images = new ArrayList<>();
    private int currentItem;

    public AutoViewPager(Context context) {
        this(context, null);
    }

    public AutoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //        viewPager = new ViewPager(getContext());
        //        viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //        addView(viewPager);
        //
        //        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        //        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        //        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //        relativeLayout.setLayoutParams(layoutParams);
        //
        //        indicatorLayout = new LinearLayout(getContext());
        //        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
        //        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //        params.setMargins(0, 0, dip2px(10), dip2px(5));
        //        indicatorLayout.setLayoutParams(params);
        //
        //        relativeLayout.addView(indicatorLayout);
        //        addView(relativeLayout);

        View.inflate(getContext(), R.layout.auto_viewpager_layout, this);
        indicatorLayout = (LinearLayout) findViewById(R.id.indicatorLayout);
        viewPager = (ViewPager) findViewById(R.id.autoViewPager);
        viewPager.addOnPageChangeListener(this);
    }

    private void initIndicator(int count) {
        indicators = new ArrayList<>();
        indicatorLayout.setVisibility(count <= 1 ? GONE : VISIBLE);
        for (int i = 0; i < count; i++) {
            ImageView indicator = new ImageView(getContext());
            LinearLayout.LayoutParams indicatorParams = new LinearLayout.LayoutParams(-2, -2);
            indicatorParams.rightMargin = dip2px(7);
            indicator.setLayoutParams(indicatorParams);
            indicators.add(indicator);
            indicator.setImageResource(R.drawable.icon_dian_nor);
            indicatorLayout.addView(indicator);
        }
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public void setImages(List<String> images) {
        if (images == null || images.size() == 0) {
            Log.e("AutoViewPager", "url为空");
            return;
        }
        this.images = images;
        AutoViewPageAdapter adapter = new AutoViewPageAdapter(images);
        viewPager.setAdapter(adapter);
        if (adapter.getCount() == Integer.MAX_VALUE) {
            currentItem = images.size() * 1000;
            viewPager.setCurrentItem(currentItem);
        }
//        handler.postDelayed(r, delayTime);
        initIndicator(images.size());
        viewPager.addOnPageChangeListener(new AutoViewPageChangListener());
        indicators.get(0).setImageResource(R.drawable.icon_dian_set);
    }

    private void start() {
        if (images.size() <= 1)
            return;
        handler.removeCallbacks(r);
        handler.postDelayed(r, delayTime);
    }

    private void stop() {
        handler.removeCallbacks(r);
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            currentItem++;
            viewPager.setCurrentItem(currentItem);
            handler.removeCallbacks(r);
            handler.postDelayed(r, delayTime);
        }
    };

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
//        if (visibility == View.VISIBLE) {
//            start();
//        } else {
//            stop();
//        }
    }

    private int dip2px(int val) {
        return (int) (Resources.getSystem().getDisplayMetrics().density * val);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class AutoViewPageAdapter extends PagerAdapter {

        private List<String> list;

        public AutoViewPageAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size() == 0 || list.size() == 1 ? 1 : Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final ImageView iv = new ImageView(getContext());
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            iv.setAdjustViewBounds(true);
            iv.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            Glide.with(getContext()).load(list.get(Math.abs(position % list.size())))
                    .placeholder(R.drawable.defaultpic)
                    .fallback(R.drawable.defaultpic)
                    .error(R.drawable.defaultpic).into(iv);
            iv.setOnClickListener(v -> {
                if (listener != null) {
                    int abs = Math.abs(position % list.size());
                    listener.itemClick(iv, list.get(abs), abs);
                }
            });
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    private class AutoViewPageChangListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            int currentPosition = position % images.size();
            for (int i = 0; i < images.size(); i++) {
                if (i == currentPosition) {
                    indicators.get(i).setImageResource(R.drawable.icon_dian_set);
                } else {
                    indicators.get(i).setImageResource(R.drawable.icon_dian_nor);
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            if (state == ViewPager.SCROLL_STATE_IDLE)
//                start();
//            if (state == ViewPager.SCROLL_STATE_DRAGGING)
//                stop();
        }
    }


    public interface OnItemClickListener<T> {
        void itemClick(View view, T t, int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
