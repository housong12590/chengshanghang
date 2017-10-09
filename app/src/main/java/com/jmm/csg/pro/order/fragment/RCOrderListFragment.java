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
import com.jmm.csg.http.CommonSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.pro.order.activity.ReOrderDetailActivity;
import com.jmm.csg.pro.order.adapter.ReOrderAdapter;
import com.jmm.csg.pro.service.AddExpressNumActivity;
import com.jmm.csg.pro.service.AfterSaleActivity;
import com.jmm.csg.utils.SystemUtils;
import com.jmm.csg.widget.SpaceItemDecoration;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

public class RCOrderListFragment extends BaseRVFragment<Order.Entity> {

    private boolean isPer;
    private Subscription subscribe;

    @Override
    protected void parseIntent(Bundle bundle) {
        isPer = bundle.getBoolean("isPer");
    }

    @Override
    protected String getCurrentPage() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        View emptyView = View.inflate(getActivity(), R.layout.rorder_empty_layout, null);
        mAdapter.setEmptyView(emptyView);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new SpaceItemDecoration(6);
    }

    @Override
    protected Observable<List<Order.Entity>> getApi(String currNum) {
        String userId = null;
        String accountManagerId = null;
        if (isPer) {
            userId = UserDataHelper.getUserId();
        } else {
            accountManagerId = UserDataHelper.getUserId();
        }
        return HttpModule.orderList(HttpParams.orderList(accountManagerId, userId, "T", currNum, String.valueOf(PAGE_SIZE)))
                .map(new Func1<Order, List<Order.Entity>>() {
                    @Override
                    public List<Order.Entity> call(Order order) {
                        return order.getOrderList();
                    }
                });
    }

    @Override
    protected BaseRVAdapter<Order.Entity> getRecyclerViewAdapter() {
        ReOrderAdapter adapter = new ReOrderAdapter(isPer);
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
                Intent intent = new Intent(getActivity(), ReOrderDetailActivity.class);
                intent.putExtra("orderId", item.getOrdersId());
                intent.putExtra("isPer", isPer);
                startActivityForResult(intent, 100);
                break;
        }
    }

    private OnListItemChildClickListener getItemChildClickListener() {
        return new OnListItemChildClickListener() {
            @Override
            public void onChildClick(View view, int position) {
                String text = ((TextView) view).getText().toString();
                Order.Entity item = mAdapter.getData().get(position);
                Intent intent = new Intent();
                intent.putExtra("orderId", item.getOrdersId());
                intent.putExtra("consignee", item.getConsignee());
                intent.putExtra("phonenumber", item.getPhonenumber());
                intent.putExtra("address", item.getAddress());
                switch (text) {
//                    case "关闭订单":
//                        ToastUtils.showMsg(text);
//                        break;
                    case "联系客服":
//                        ToastUtils.showMsg(text);
                        DialogHelper.getMessageDialog(getActivity(), "联系客服 电话:400-040-6755", (dialogInterface, i) ->
                                SystemUtils.startDial(getActivity(), "4000406755")).show();
                        break;
//                    case "查看物流":
//                        intent.setClass(getActivity(), LogisticsDetailActivity.class);
//                        intent.putExtra("proImage", item.getPro_pic());
//                        startActivity(intent);
//                        break;
                    case "上传快递单号":
                        intent.setClass(getActivity(), AddExpressNumActivity.class);
                        intent.putExtra("orderItemsId", item.getOrderitemsid());
                        startActivityForResult(intent, 100);
                        break;
                    case "取消退货":
                        DialogHelper.getMessageDialog(getActivity(), "是否取消退货?", (dialogInterface, i) ->
                                verfyReturn(item.getOrdersId(), "0", "0")).show();
                        break;
//                    case "同意退款":
//                        verfyReturn(item.getOrdersId(), "KS", "K");
//                        break;
//                    case "拒绝退款":
//                        verfyReturn(item.getOrdersId(), "", "F");
//                        break;
                    case "去审核":
                        intent.setClass(getActivity(), ReOrderDetailActivity.class);
                        intent.putExtra("orderId", item.getOrdersId());
                        intent.putExtra("isPer", isPer);
                        startActivityForResult(intent, 100);
                        break;
                    case "再次申请":
                        String address = item.getConsignee() + "-" + item.getPhonenumber() + "-" + item.getAddress();
                        intent.setClass(getActivity(), AfterSaleActivity.class);
                        intent.putExtra("type", "return");
                        intent.putExtra("address", address);
                        startActivityForResult(intent, 100);
                        break;
                }
            }
        };
    }

    public void verfyReturn(String orderId, String flag, String status) {
        subscribe = HttpModule.verfyReturn(HttpParams.verfyReturn(orderId, flag, status, ""))
                .subscribe(new CommonSubscriber<BaseResp>() {
                    @Override
                    public void onNext(BaseResp baseResp) {
                        requestData(LoadStatus.REFRESH);
                    }
                });
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
