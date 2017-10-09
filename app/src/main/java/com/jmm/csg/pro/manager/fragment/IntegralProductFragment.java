package com.jmm.csg.pro.manager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jmm.csg.Constant;
import com.jmm.csg.R;
import com.jmm.csg.base.fragment.BaseFragment;
import com.jmm.csg.bean.Integral;
import com.jmm.csg.bean.IntegralProduct;
import com.jmm.csg.bean.OilCard;
import com.jmm.csg.callback.ConnectCallback;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.http.ProgressSubscriber;
import com.jmm.csg.utils.ToastUtils;
import com.jmm.csg.widget.ExchangeDialog;
import com.jmm.csg.widget.ExchangeOilCardDialog;
import com.jmm.csg.widget.SpaceItemDecoration;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class IntegralProductFragment extends BaseFragment {

    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.tv_desc) TextView tvDesc;
    @Bind(R.id.tv_commit) TextView tvCommit;
    private ConnectCallback callback;
    private String type;
    private ProductAdapter adapter;
    private String pid;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (ConnectCallback) context;
    }

    @Override
    protected void parseIntent(Bundle bundle) {
        type = bundle.getString("type");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cost_exchange;
    }

    @Override
    protected void initView() {
        String desc;
        switch (type) {
            case Constant.INTEGRAL_COST:
                desc = getString(R.string.cost_exchange_explain);
                break;
            case Constant.INTEGRAL_FLOW:
                desc = getString(R.string.flow_exchange_explain);
                break;
            case Constant.INTEGRAL_CARD:
                desc = getString(R.string.refuelling_cards_exchange_explain);
                break;
            default:
                desc = getString(R.string.other_exchange_explain);
                tvCommit.setVisibility(View.INVISIBLE);
                break;
        }
        tvDesc.setText(desc);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.addItemDecoration(new SpaceItemDecoration(15));
        adapter = new ProductAdapter(Collections.emptyList());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<IntegralProduct.Entity> list = adapter.getData();
                for (int i = 0; i < list.size(); i++) {
                    IntegralProduct.Entity item = list.get(i);
                    if (position == i) {
                        pid = item.getID();
                        item.setSelect(true);
                    } else {
                        item.setSelect(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        View emptyView = View.inflate(getActivity(), R.layout.integeral_empty_layout, null);
        TextView tvText = (TextView) emptyView.findViewById(R.id.tv_text);
        tvText.setText("暂时没有相关的商品\n我们会尽快的备货");
        adapter.setEmptyView(emptyView);
    }

    @Override
    protected void initData() {
        HttpModule.getProductList(HttpParams.getProductList(type))
                .subscribe(new ProgressSubscriber<IntegralProduct>(getActivity()) {
                    @Override
                    public void onNext(IntegralProduct integralProduct) {
                        List<IntegralProduct.Entity> data = integralProduct.getData();
                        if (data != null && !data.isEmpty()) {
                            IntegralProduct.Entity entity = data.get(0);//默认选中第一个
                            pid = entity.getID();
                            entity.setSelect(true);
                            adapter.setNewData(data);
                        }
                    }
                });
    }

    @OnClick(R.id.tv_commit)
    public void onClick(View view) {
        String userId = UserDataHelper.getUserId();
        switch (type) {
            case Constant.INTEGRAL_COST:
                new ExchangeDialog(getActivity()).setOnResultListener((position, val) ->
                        exchange(pid, userId, (String) val, "1")).show();
                break;
            case Constant.INTEGRAL_FLOW:
                new ExchangeDialog(getActivity()).setOnResultListener((position, val) ->
                        exchange(pid, userId, (String) val, "2")).show();
                break;
            case Constant.INTEGRAL_CARD:
                new ExchangeOilCardDialog(getActivity()).setOnResultListener((position, val) -> {
                    OilCard bean = (OilCard) val;
                    exchangeOilCard(userId, pid, bean.cardId, bean.phone, bean.name);
                }).show();
                break;
        }
    }

    //话费流量兑换
    public void exchange(String pid, String userId, String phone, String type) {
        if (type.equals("1")) { //话费
            HttpModule.huaFeiExchange(HttpParams.exchange(pid, userId, phone))
                    .subscribe(new ProgressSubscriber<Integral>(getActivity()) {
                        @Override
                        public void onNext(Integral o) {
                            ToastUtils.showMsg(o.getMessage());
                            if (o.getCode().equals("1")) {
                                if (callback != null) {
                                    callback.onRefresh();
                                }
                            }
                        }
                    });
        } else if (type.equals("2")) {//流量
            HttpModule.liuLiangIntegalExchange(HttpParams.exchange(pid, userId, phone))
                    .subscribe(new ProgressSubscriber<Integral>(getActivity()) {
                        @Override
                        public void onNext(Integral o) {
                            ToastUtils.showMsg(o.getMessage());
                            if (o.getCode().equals("1")) {
                                if (callback != null) {
                                    callback.onRefresh();
                                }
                            }
                        }
                    });
        }
    }

    //加油卡兑换
    public void exchangeOilCard(String userId, String proId, String game_userid, String gasCardTel, String gasCardName) {
        HttpModule.exchangeOilCard(HttpParams.exchangeOilCard(userId, proId, game_userid, gasCardTel, gasCardName))
                .subscribe(new ProgressSubscriber<Integral>(getActivity()) {
                    @Override
                    public void onNext(Integral o) {
                        ToastUtils.showMsg(o.getMessage());
                        if (o.getCode().equals("1")) {
                            if (callback != null) {
                                callback.onRefresh();
                            }
                        }
                    }
                });
    }

    private class ProductAdapter extends BaseQuickAdapter<IntegralProduct.Entity, BaseViewHolder> {

        public ProductAdapter(List<IntegralProduct.Entity> data) {
            super(R.layout.item_integeral_product_layout, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, IntegralProduct.Entity item) {
            View selectView = helper.getView(R.id.selectView);
            selectView.setVisibility(item.isSelect() ? View.VISIBLE : View.GONE);
            TextView text = helper.getView(R.id.tv_text);
            text.setText(item.getPRODUCT_NAME() + "\n" + item.getINTEGRAL_NUM() + "积分/次");
        }
    }
}
