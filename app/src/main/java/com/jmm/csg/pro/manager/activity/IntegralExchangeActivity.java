package com.jmm.csg.pro.manager.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.bean.Integral;
import com.jmm.csg.bean.IntegralItem;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.BaseSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.pro.manager.adapter.CreditsExchangeAdapter;
import com.jmm.csg.utils.ToastUtils;
import com.jmm.csg.widget.SpaceItemDecoration;
import com.jmm.csg.widget.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class IntegralExchangeActivity extends BaseActivity {

    @Bind(R.id.titleView) TitleView titleView;
    @Bind(R.id.iv_rule) ImageView ivRule;
    @Bind(R.id.tv_integral) TextView tvIntegral;
    @Bind(R.id.tv_detail) TextView tvDetail;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    private List<IntegralItem> list = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_integral_exchange;
    }

    @Override
    public void initView() {
        titleView.setOnLeftImgClickListener(v -> finish());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new SpaceItemDecoration(25));

        list.add(new IntegralItem(R.drawable.icon_grid_1, "话费"));
        list.add(new IntegralItem(R.drawable.icon_grid_2, "流量"));
        list.add(new IntegralItem(R.drawable.icon_grid_3, "加油卡"));
        list.add(new IntegralItem(R.drawable.icon_grid_4, "其他"));

        CreditsExchangeAdapter adapter = new CreditsExchangeAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(IntegralExchangeActivity.this, IntegralProductActivity.class);
                intent.putExtra("type", String.valueOf(position + 1));
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    public void initData() {
        HttpModule.getAvailable(HttpParams.getAvailable(UserDataHelper.getUserId()))
                .subscribe(new BaseSubscriber<Integral>() {
                    @Override
                    public void onNext(Integral resp) {
                        if (resp.getCode().equals("1")) {
                            tvIntegral.setText(resp.getData());
                        } else {
                            ToastUtils.showMsg(resp.getMessage());
                        }
                    }
                });
    }

    @OnClick({R.id.tv_detail, R.id.iv_rule})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_detail:
                Intent intent = new Intent(this, IntegralDetailsActivity.class);
                intent.putExtra("integral", tvIntegral.getText().toString());
                startActivity(intent);
                break;
            case R.id.iv_rule:
                startActivity(new Intent(this, IntegralRuleActivity.class));
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            initData();
        }
    }
}

