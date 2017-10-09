package com.jmm.csg.pro.order.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jmm.csg.R;
import com.jmm.csg.base.adapter.BaseRVAdapter;
import com.jmm.csg.base.fragment.BaseRVFragment;
import com.jmm.csg.bean.BaseResp;
import com.jmm.csg.bean.LoadStatus;
import com.jmm.csg.bean.Order;
import com.jmm.csg.callback.OnListItemChildClickListener;
import com.jmm.csg.helper.DialogHelper;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.http.ProgressSubscriber;
import com.jmm.csg.pro.order.activity.LogisticsDetailActivity;
import com.jmm.csg.pro.order.activity.OrderDetailActivity;
import com.jmm.csg.pro.order.activity.SelectPayActivity;
import com.jmm.csg.pro.order.adapter.OrderAdapter;
import com.jmm.csg.pro.service.EvaluateActivity;
import com.jmm.csg.pro.service.ExchangeActivity;
import com.jmm.csg.utils.ToastUtils;
import com.jmm.csg.widget.SpaceItemDecoration;

import java.util.List;

import rx.Observable;
import rx.Subscription;

/**
 * 订单列表
 */

public class OrderListFragment extends BaseRVFragment<Order.Entity> {

    private String pageCount;
    private String status; // 订单状态
    private boolean isPer; // 是否个人
    private Subscription subscribe;


    @Override
    protected void parseIntent(Bundle bundle) {
        status = bundle.getString("status");
        isPer = bundle.getBoolean("isPer");
    }

    @Override
    protected String getCurrentPage() {
        return pageCount;
    }

    @Override
    protected void initView() {
        super.initView();
        View emptyView = View.inflate(getActivity(), R.layout.order_empty_layout, null);
        mAdapter.setEmptyView(emptyView);
//        getVisibilityLayout().setEmptyView(emptyView);
    }

//    @Subscribe(code = Constant.REFRESH_ORDER_STATUS)
//    public void refreshStatus(String str) {
//        requestData(LoadStatus.REFRESH);
//    }

    @Override
    protected Observable<List<Order.Entity>> getApi(String currNum) {
        String accId = null;
        String perId = null;
        if (isPer) {
            perId = UserDataHelper.getUserId();
        } else {
            accId = UserDataHelper.getUserId();
        }
        return HttpModule.orderList(HttpParams.orderList(accId, perId, status, currNum, String.valueOf(PAGE_SIZE)))
                .doOnNext(order -> pageCount = order.getPageCount())
                .map(order -> order.getOrderList());
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new SpaceItemDecoration(6);
    }

    @Override
    protected BaseRVAdapter<Order.Entity> getRecyclerViewAdapter() {
        OrderAdapter adapter = new OrderAdapter(isPer);
        adapter.setOnItemChildClickListener(getItemChildClickListener());
        return adapter;
    }

    @Override
    protected void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    protected void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.ll_detail:
                Order.Entity item = (Order.Entity) adapter.getData().get(position);
                String address = item.getConsignee() + "-" + item.getPhonenumber() + "-" + item.getAddress();
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("orderId", item.getOrdersId());
                intent.putExtra("address", address);
                intent.putExtra("isPer", isPer);
                startActivityForResult(intent, 100);
                break;
        }
    }


    private OnListItemChildClickListener getItemChildClickListener() {
        return (v, position) -> {
            Order.Entity entity = mAdapter.getData().get(position);
            String address = entity.getConsignee() + "-" + entity.getPhonenumber() + "-" + entity.getAddress();
            Intent intent = new Intent();
            String orderId = entity.getOrdersId();
            intent.putExtra("orderId", orderId);
            switch (((TextView) v).getText().toString()) {
                case "确认收货":
//                    affirmOrder(entity.getOrdersId());
                    changeOrderState(entity.getOrdersId(), "F", "确认收货?");
                    break;
                case "取消订单":
//                    cancelOrder(entity.getOrdersId());
                    changeOrderState(entity.getOrdersId(), "X", "确定取消订单?");
                    break;
                case "申请售后":
                    intent.setClass(getActivity(), ExchangeActivity.class);
                    intent.putExtra("address", address);
                    startActivityForResult(intent, 100);
                    break;
                case "查看物流":
                    intent.setClass(getActivity(), LogisticsDetailActivity.class);
                    intent.putExtra("proImage", entity.getPro_pic());
                    startActivity(intent);
                    break;
                case "付款":
//                    SelectPayActivity.start(getActivity(), orderId, entity.getPro_Name(),
//                            entity.getPro_Specification(), entity.getOrderspayprice());
//                    Intent intent = new Intent(this, SelectPayActivity.class);
                    intent.setClass(getActivity(), SelectPayActivity.class);
                    intent.putExtra("proName", entity.getPro_Name());
                    intent.putExtra("proSpecification", entity.getPro_Specification());
                    intent.putExtra("price", entity.getOrderspayprice());
                    startActivityForResult(intent, 100);
                    break;
                case "评价":
                    intent.setClass(getActivity(), EvaluateActivity.class);
                    intent.putExtra("orderItemId", entity.getOrderitemsid());
                    intent.putExtra("productId", entity.getId());
                    startActivityForResult(intent, 100);
                    break;
                case "查看订单":
                    intent.setClass(getActivity(), OrderDetailActivity.class);
                    intent.putExtra("address", address);
                    startActivity(intent);
                    break;
                case "取消退款":
                    DialogHelper.getMessageDialog(getActivity(), "温馨提示", "确定取消退款", (dialog, which) -> HttpModule.cancleRefun(HttpParams.cancleRefun(UserDataHelper.getUserId(), entity.getOrdersId())).subscribe(new ProgressSubscriber<BaseResp>(getActivity()) {
                        @Override
                        public void onNext(BaseResp o) {
                            String code = o.getCode();
                            String message = o.getMessage();
                            requestData(LoadStatus.LOADING);
                            ToastUtils.showShort(message);
                        }
                    })).show();
                    break;
            }
        };
    }


    private void changeOrderState(String orderId, String type, String msg) {
        DialogHelper.getMessageDialog(getActivity(), msg, (dialog, which) -> {
            //                                RxBus.getDefault().post(Constant.REFRESH_MAIN_DATA, LoadStatus.REFRESH);
            subscribe = HttpModule.changeOrderState(HttpParams.changeOrderState(orderId, UserDataHelper.getUserId(), type))
                    .subscribe(new ProgressSubscriber<BaseResp>(getActivity()) {
                        @Override
                        public void onNext(BaseResp resp) {
                            if (resp.getStatus().equals("0")) {
                                requestData(LoadStatus.REFRESH);
//                                RxBus.getDefault().post(Constant.REFRESH_MAIN_DATA, LoadStatus.REFRESH);
                            }
                        }
                    });
        }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            requestData(LoadStatus.REFRESH);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }
}
