package com.jmm.csg.pro.product.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmm.csg.R;
import com.jmm.csg.base.fragment.BaseFragment;
import com.jmm.csg.bean.ProductReview;

import butterknife.Bind;


public class EvaluateCategoryFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.ll_group) LinearLayout llGroup;
    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.all_evaluate) Button allEvaluate;
    @Bind(R.id.p_evaluate) Button pEvaluate;
    @Bind(R.id.b_evaluate) Button bEvaluate;
    private String productId;

    @Override
    protected void parseIntent(Bundle bundle) {
        productId = bundle.getString("productId");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_evaluate_category_layout;
    }

    @Override
    protected void initView() {
//        radioGroup.setOnCheckedChangeListener(this);
//        rbAllEvaluate.performClick();
        allEvaluate.setOnClickListener(this);
        pEvaluate.setOnClickListener(this);
        bEvaluate.setOnClickListener(this);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("productId", productId);
                bundle.putString("type", position == 0 ? "" : position == 1 ? "P" : "B");
                System.out.println(position);
                return Fragment.instantiate(getActivity(), EvaluateFragment2.class.getName(), bundle);
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        setTabTextColor(allEvaluate);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_evaluate:
                viewPager.setCurrentItem(0, false);
                setTabTextColor(allEvaluate);
                break;
            case R.id.p_evaluate:
                if (!allP.equals("0")) {
                    viewPager.setCurrentItem(1, false);
                    setTabTextColor(pEvaluate);
                }
                break;
            case R.id.b_evaluate:
                if (!allB.equals("0")) {
                    viewPager.setCurrentItem(2, false);
                    setTabTextColor(bEvaluate);
                }
                break;
        }
    }

    private void setTabTextColor(TextView view) {
        allEvaluate.setTextColor(getResources().getColor(R.color.black));
        pEvaluate.setTextColor(getResources().getColor(R.color.black));
        bEvaluate.setTextColor(getResources().getColor(R.color.black));

        view.setTextColor(getResources().getColor(R.color.btn_nor));
    }

    private String allReviewCount;
    private String allP;
    private String allB;

    public void refreshEvaluateCount(ProductReview.DataBean.ReviewCountBean reviewCountBean, String type) {
        switch (type) {
            case "":
                allReviewCount = reviewCountBean.getAllReviewCount();
                allP = reviewCountBean.getAllP();
                allB = reviewCountBean.getAllB();
                allEvaluate.setText("全部评价:" + reviewCountBean.getAllReviewCount());
                pEvaluate.setText("好评:" + reviewCountBean.getAllP());
                bEvaluate.setText("差评:" + reviewCountBean.getAllB());
                break;
            case "P":
                pEvaluate.setText("好评:" + reviewCountBean.getAllP());
                break;
            case "B":
                bEvaluate.setText("差评:" + reviewCountBean.getAllB());
                break;
        }
    }

}
