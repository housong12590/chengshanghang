package com.jmm.csg.pro.order.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.XActivity;
import com.jmm.csg.bean.LogisticsBean;
import com.jmm.csg.imgload.ImageLoaderUtils;
import com.jmm.csg.pro.contract.LogisticsContract;
import com.jmm.csg.pro.order.adapter.LogisticsAdapter;
import com.jmm.csg.pro.presenter.LogisticsPresenter;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;

/**
 * 物流详情
 */
public class LogisticsDetailActivity extends XActivity<LogisticsPresenter> implements LogisticsContract.V {

    @Bind(R.id.titleView) TitleView mTitleView;
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.tv_logistics_code) TextView mTvLogisticsCode;
    @Bind(R.id.tv_logistics_name) TextView mTvLogisticsName;
    @Bind(R.id.iv_pro_pic) ImageView mIvProImage;
    @Bind(R.id.tv_logistics_status) TextView mTvLogisticsStatus;
    private LogisticsAdapter adapter;
    private String orderId;
    private String proImage;

    @Override
    public void parseIntent(Intent intent) {
        orderId = intent.getStringExtra("orderId");
        proImage = intent.getStringExtra("proImage");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_logistics_detail;
    }

    @Override
    public LogisticsPresenter newPresenter() {
        return new LogisticsPresenter();
    }

    @Override
    public void onSuccess(LogisticsBean bean) {
        mTvLogisticsCode.setText(bean.getNu());
        mTvLogisticsName.setText(bean.getCom());
        mTvLogisticsStatus.setText(bean.getState());
        adapter.setNewData(bean.getData());
        if (bean.getMessage().equals("ok")) {
            adapter.setNewData(bean.getData());
            vml.showContentView();
        } else {
            vml.setEmptyView(View.inflate(this, R.layout.logistics_empty_layout, null));
            vml.showEmptyView();
        }
    }

    @Override
    public void initView() {
        ImageLoaderUtils.getInstance().loadOSSImage(this, proImage, mIvProImage);
        mTitleView.setOnLeftImgClickListener(view -> finish());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LogisticsAdapter(this);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public View getVisibilityView() {
        return mRecyclerView;
    }

    @Override
    public void initData() {
        getP().logisticLis(orderId);
//        getP().logisticLis("1491451476918");
    }
}
