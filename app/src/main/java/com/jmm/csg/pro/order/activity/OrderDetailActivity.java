package com.jmm.csg.pro.order.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.bean.BaseResp;
import com.jmm.csg.bean.OrderDetail;
import com.jmm.csg.bean.OrderStatus;
import com.jmm.csg.bean.RefundDetail;
import com.jmm.csg.helper.DialogHelper;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.http.ProgressSubscriber;
import com.jmm.csg.imgload.ImageLoaderUtils;
import com.jmm.csg.imgsel.activity.ImagePreviewActivity;
import com.jmm.csg.pro.service.EvaluateActivity;
import com.jmm.csg.pro.service.ExchangeActivity;
import com.jmm.csg.utils.OSSUtils;
import com.jmm.csg.utils.SystemUtils;
import com.jmm.csg.utils.ToastUtils;
import com.jmm.csg.widget.RefundReasonDialog;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 订单详情
 */
public class OrderDetailActivity extends BaseActivity {

    @Bind(R.id.titleView)
    TitleView mTitleView;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_phone)
    TextView mTvPhone;
    @Bind(R.id.tv_address)
    TextView mTvAddress;
    @Bind(R.id.tv_pro_name)
    TextView mTvProName;
    @Bind(R.id.tv_price)
    TextView mTvPrice;
    @Bind(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @Bind(R.id.tv_count)
    TextView mTvCount;
    @Bind(R.id.tv_charge)
    TextView mTvCharge;
    @Bind(R.id.tv_create_time)
    TextView mTvCreateTime;
    @Bind(R.id.tv_payment_time)
    TextView mTvPaymentTime;
    @Bind(R.id.tv_order_coding)
    TextView mTvOrderCoding;
    @Bind(R.id.tv_manage_name)
    TextView mTvManageName;
    @Bind(R.id.tv_bank)
    TextView mTvBank;
    @Bind(R.id.tv_job_Num)
    TextView mTvJobNum;
    @Bind(R.id.tv_manage_phone)
    TextView mTvManagePhone;
    @Bind(R.id.iv_manage_pic)
    ImageView mIvManagePic;
    @Bind(R.id.iv_pro_pic)
    ImageView mIvProPic;
    @Bind(R.id.tv_price1)
    TextView mTvPrice1;
    @Bind(R.id.tv_remark)
    TextView mTvRemark;
    @Bind(R.id.ll_order_status)
    LinearLayout mOrderStatusLayout;
    @Bind(R.id.ll_address)
    LinearLayout mLlAddress;
    @Bind(R.id.tv_refund_state)
    TextView mTvRefundState;
    @Bind(R.id.tv_refund_time)
    TextView mTvRefundTime;
    @Bind(R.id.ll_refund_state)
    LinearLayout mLlRefundState;
    @Bind(R.id.tv_refund_reason)
    TextView mTvRefundReason;
    @Bind(R.id.ll_refund_reason)
    LinearLayout mLlRefundReason;
    @Bind(R.id.ll_payment_time)
    LinearLayout mLlPaymentTime;
    @Bind(R.id.tv_time_state)
    TextView mTvTimeState;
    private OrderDetail.Entity entity;
    private String orderId;
    private String address;
    private boolean isPer;
    private String mCheck_status;
    private String mCheck_result;

    @Override
    public void parseIntent(Intent intent) {
        orderId = intent.getStringExtra("orderId");
        address = intent.getStringExtra("address");
        isPer = intent.getBooleanExtra("isPer", false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView() {
        mTitleView.setOnLeftImgClickListener(view -> finish());
        mOrderStatusLayout.setVisibility(isPer ? View.VISIBLE : View.GONE);
        mTvName.getPaint().setFakeBoldText(true);
        mTvPhone.getPaint().setFakeBoldText(true);
    }

    @Override
    public void initData() {
        HttpModule.getOrderDetail(HttpParams.getOrderDetail(UserDataHelper.getUserId(), orderId))
                .subscribe(new ProgressSubscriber<OrderDetail>(this) {
                    @Override
                    public void onNext(OrderDetail detail) {
                        entity = detail.getOrderdetail();
                        setUiData(detail.getOrderdetail());
                    }
                });
    }

    private void setUiData(OrderDetail.Entity entity) {
        mCheck_status = entity.getCheck_status();
        mCheck_result = entity.getCheck_result();
        mTvName.setText(entity.getConsignee());
        mTvPhone.setText(entity.getPhonenumber());
        mTvAddress.setText(entity.getAddress());
        Log.e("TAG", entity.getAddress());
        mTvProName.setText(entity.getPro_Name());
        mTvPrice.setText("¥" + entity.getPro_price());
        mTvPrice1.setText("¥" + entity.getPro_price());
        mTvCount.setText("x" + entity.getQuantity());
        mTvCharge.setText("¥0.00");
        mTvCreateTime.setText(entity.getCreatetime());
        mTvPaymentTime.setText(entity.getPaytime());
        mTvOrderCoding.setText(entity.getOrdersId());
        mTvManageName.setText("客户经理 : " + entity.getACCOUNTMANAGERNAME());
        mTvBank.setText("所属银行 : " + entity.getBelongBank());
        mTvJobNum.setText("工号 : " + entity.getJOBNUMBER());
        mTvTotalPrice.setText("¥" + entity.getOrderspayprice());
        mTvManagePhone.setText(entity.getACCOUNTMANAGERMOBILE());

        if (TextUtils.isEmpty(entity.getRemark())) {
            mTvRemark.setText("暂无备注");
        } else {
            mTvRemark.setText("备注信息 : " + entity.getRemark());
        }
        ImageLoaderUtils.getInstance().loadOSSAvatar(this, entity.getACCOUNTMANAGERPIC(), mIvManagePic);
        ImageLoaderUtils.getInstance().loadOSSImage(this, entity.getPro_pic(), mIvProPic);
        orderStatusView(OrderStatus.valueOf(entity.getOrdesstatus()));
        if (!isPer) {
            mOrderStatusLayout.setVisibility(View.GONE);
            return;
        }
    }

    @OnClick({R.id.iv_manage_pic, R.id.tv_manage_phone})
    public void onClick(View view) {
        if (entity == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_manage_phone:
                SystemUtils.startDial(this, entity.getACCOUNTMANAGERMOBILE());
                break;
            case R.id.iv_manage_pic:
                ImagePreviewActivity.start(this, mIvManagePic, OSSUtils.BASE_URL + entity.getACCOUNTMANAGERPIC());
                break;
        }
    }

    private void orderStatusView(OrderStatus status) {
        View view = null;
        mOrderStatusLayout.removeAllViews();
        switch (status) {
            case M:
                mLlPaymentTime.setVisibility(View.GONE);
                view = View.inflate(this, R.layout.order_status_fk, null);
                view.findViewById(R.id.tv_close_order).setOnClickListener(v -> cancelOrder());
                view.findViewById(R.id.tv_pay).setOnClickListener(v -> pay());
                break;
            case C:
                view = View.inflate(this, R.layout.order_status_fh, null);
                //申请/取消退款
                TextView mRefundView = (TextView) view.findViewById(R.id.tv_logistics);
                //联系客服
                TextView mServiceView = (TextView) view.findViewById(R.id.tv_service);
                mServiceView.setVisibility(View.VISIBLE);
                mRefundView.setVisibility(View.VISIBLE);
                mServiceView.setOnClickListener(v -> connectionService());
                if (equalsState("", "") || equalsState("0", "0")) {
                    mRefundView.setOnClickListener(v -> refund());
                    mRefundView.setText("申请退款");
                    mServiceView.setVisibility(View.GONE);
                    mLlAddress.setVisibility(View.VISIBLE);
                    mLlRefundState.setVisibility(View.GONE);
                } else {
                    if (equalsState("0", "1") || equalsState("1", "1")) {
                        mRefundView.setText("取消退款");
                        mRefundView.setOnClickListener(v -> cancelRefund());
                        mTvRefundState.setText("审核中");
                    } else if (equalsState("2", "1")) {
                        mRefundView.setVisibility(View.GONE);
                        mTvRefundState.setText("退款中");
                    } else if (equalsState("3", "1")) {
                        mRefundView.setVisibility(View.GONE);
                        mTvRefundState.setText("退款成功");
                    } else {
                        mLlRefundReason.setVisibility(View.VISIBLE);
                        mRefundView.setOnClickListener(v -> refund());
                        mRefundView.setText("申请退款");
                        mTvRefundState.setText("退款失败");
                    }
                    queryRefund();
                }
                break;
            case S:
                view = View.inflate(this, R.layout.order_status_sh, null);
                view.findViewById(R.id.tv_affirm).setOnClickListener(v -> affirm());
                view.findViewById(R.id.tv_logistics).setOnClickListener(v -> logistics());
                view.findViewById(R.id.tv_service).setOnClickListener(v -> connectionService());
                TextView mRefundAView = (TextView) view.findViewById(R.id.tv_apply_refund);
                if (equalsState("", "") || equalsState("0", "0")) {
                    mRefundAView.setOnClickListener(v -> refund());
                    mRefundAView.setText("申请退款");
                    mLlAddress.setVisibility(View.VISIBLE);
                    mLlRefundState.setVisibility(View.GONE);
                } else {
                    if (equalsState("0", "1") || equalsState("1", "1")) {
                        mRefundAView.setText("取消退款");
                        mRefundAView.setOnClickListener(v -> cancelRefund());
                        mTvRefundState.setText("审核中");
                    } else if (equalsState("2", "1")) {
                        mRefundAView.setVisibility(View.GONE);
                        mTvRefundState.setText("退款中");
                    } else if (equalsState("3", "1")) {
                        mRefundAView.setVisibility(View.GONE);
                        mTvRefundState.setText("退款成功");
                    } else {
                        mLlRefundReason.setVisibility(View.VISIBLE);
                        mRefundAView.setOnClickListener(v -> refund());
                        mRefundAView.setText("申请退款");
                        mTvRefundState.setText("退款失败");
                    }
                    queryRefund();
                }
                break;
            case F:
                view = View.inflate(this, R.layout.order_status_pj, null);
                view.findViewById(R.id.tv_evaluate).setOnClickListener(v -> evaluate());
                view.findViewById(R.id.tv_after_sale).setOnClickListener(v -> afterSale());
                break;
            case Q:
                view = View.inflate(this, R.layout.order_status_fh, null);
                TextView logistics = (TextView) view.findViewById(R.id.tv_service);
                view.findViewById(R.id.tv_logistics).setVisibility(View.GONE);
                logistics.setText("查看物流");
                logistics.setOnClickListener(v -> logistics());
                break;
            case X:
                if (equalsState("3", "1")) {
                    queryRefund();
                    mTvRefundState.setText("退款成功");
                }
                break;
        }
        if (view != null) {
            mOrderStatusLayout.setVisibility(View.VISIBLE);
            mOrderStatusLayout.addView(view);
        } else {
            mOrderStatusLayout.setVisibility(View.GONE);
        }
    }


    private void cancelOrder() {//关闭订单
        changeOrderState(entity.getOrdersId(), "X", "确定取消订单?");
    }

    private void pay() { // 付款
//        SelectPayActivity.start(this, orderId, entity.getPro_Name(), entity.getPro_Specification(),
//                entity.getOrderspayprice());
        Intent intent = new Intent(this, SelectPayActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("proName", entity.getPro_Name());
        intent.putExtra("proSpecification", entity.getPro_Specification());
        intent.putExtra("price", entity.getOrderspayprice());
        startActivityForResult(intent, 100);
    }

    private void queryRefund() { //查询退款详情
        HttpModule.queryRefun(HttpParams.queryRefun(UserDataHelper.getUserId(), orderId)).subscribe(new ProgressSubscriber<RefundDetail>(this) {
            @Override
            public void onNext(RefundDetail refundDetail) {
                mTitleView.setTitle("退款详情");
                mLlAddress.setVisibility(View.GONE);
                mLlRefundState.setVisibility(View.VISIBLE);
                if (refundDetail.getCode().equals("1")) {
                    mTvRefundTime.setText(refundDetail.getData().getRefund_date());
                    if (equalsState("1", "0") || equalsState("2", "0")) {
                        mTvRefundReason.setText(refundDetail.getData().getCheck_remark());
                        mTvRefundTime.setText(refundDetail.getData().getRefund_reject_date());
                        mTvTimeState.setText("驳回时间");
                    } else if (equalsState("3", "1")) {
                        mTvRefundTime.setText(refundDetail.getData().getRefund_finish_date());
                        mTvTimeState.setText("退款时间");
                    }
                } else {
                    ToastUtils.showShort(refundDetail.getMessage());
                }
            }
        });
    }

    private void refund() { // 申请退款
        new RefundReasonDialog(this, UserDataHelper.getUserId(), entity.getOrdersId()).show();
    }

    private void cancelRefund() { // 取消退款
        DialogHelper.getMessageDialog(this, "温馨提示", "确定取消退款", (dialog, which) -> HttpModule.cancleRefun(HttpParams.cancleRefun(UserDataHelper.getUserId(), entity.getOrdersId())).subscribe(new ProgressSubscriber<BaseResp>(this) {
            @Override
            public void onNext(BaseResp o) {
                String message = o.getMessage();
                initData();
                ToastUtils.showShort(message);
            }
        })).show();
    }

    public void affirm() { // 确认收货
        changeOrderState(entity.getOrdersId(), "F", "确认收货?");
    }


    private void connectionService() { //联系客服
        DialogHelper.getMessageDialog(this, "联系客服 电话:400-040-6755", (dialogInterface, i) ->
                SystemUtils.startDial(this, "4000406755")).show();
    }

    public void afterSale() { // 售后
        Intent intent = new Intent(this, ExchangeActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("address", address);
        startActivityForResult(intent, 100);
    }

    public void logistics() { // 物流
        Intent intent = new Intent(this, LogisticsDetailActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("proImage", entity.getPro_pic());
        startActivity(intent);
    }

    public void evaluate() { // 评价
        Intent intent = new Intent(this, EvaluateActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("orderItemId", entity.getOrderitemsid());
        intent.putExtra("productId", entity.getId());
        startActivityForResult(intent, 100);
    }

    private void changeOrderState(String orderId, String type, String msg) {
        DialogHelper.getMessageDialog(this, msg, (dialog, which) -> {
            HttpModule.changeOrderState(HttpParams.changeOrderState(orderId, UserDataHelper.getUserId(), type))
                    .subscribe(new ProgressSubscriber<BaseResp>(this) {
                        @Override
                        public void onNext(BaseResp resp) {
                            if (resp.getStatus().equals("0")) {
                                isChanged = true;
                                if (type.equals("X")) {
                                    finish();
                                } else {
                                    initData();
                                }
//                                RxBus.getDefault().post(Constant.REFRESH_MAIN_DATA, LoadStatus.REFRESH);
                            }
                        }
                    });
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            setResult(RESULT_OK);
            initData();
            isChanged = true;
        }
    }

    boolean isChanged;

    @Override
    public void finish() {
        if (isChanged) {
            setResult(RESULT_OK);
        }
        super.finish();
    }

    private boolean equalsState(String status, String result) {
        return mCheck_status.equals(status) && mCheck_result.equals(result);
    }
}
