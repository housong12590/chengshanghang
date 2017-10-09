package com.jmm.csg.pro.order.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.bean.BaseResp;
import com.jmm.csg.bean.ROrderDetail;
import com.jmm.csg.helper.DialogHelper;
import com.jmm.csg.http.CommonSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.http.ProgressSubscriber;
import com.jmm.csg.imgsel.activity.ImagePreviewActivity;
import com.jmm.csg.pro.product.adapter.ImageAdapter;
import com.jmm.csg.pro.service.AddExpressNumActivity;
import com.jmm.csg.pro.service.AfterSaleActivity;
import com.jmm.csg.utils.OSSUtils;
import com.jmm.csg.utils.SystemUtils;
import com.jmm.csg.utils.ToastUtils;
import com.jmm.csg.widget.ContainsEmojiEditText;
import com.jmm.csg.widget.TitleView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscription;

public class ReOrderDetailActivity extends BaseActivity {


    @Bind(R.id.titleView)
    TitleView titleView;
    @Bind(R.id.tv_cause)
    TextView tvCause;
    @Bind(R.id.tv_desc)
    TextView tvDesc;
    @Bind(R.id.rv_image1)
    RecyclerView rvImage1;
    @Bind(R.id.tv_logistics_num)
    TextView tvLogisticsNum;
    @Bind(R.id.rv_image2)
    RecyclerView rvImage2;
    @Bind(R.id.ll_btn_layout)
    View llBtnLayout;
    @Bind(R.id.tv_resubmit)
    TextView tvResubmit;
    @Bind(R.id.tv_yes)
    TextView tvYes;
    @Bind(R.id.tv_no)
    TextView tvNo;
    @Bind(R.id.ll_layout2)
    View llLayout2;
    @Bind(R.id.et_remark)
    ContainsEmojiEditText etRemark;
    @Bind(R.id.tv_cancel)
    TextView tvCancel;
    @Bind(R.id.ll_cstatus)
    LinearLayout llCstatus;
    private String orderId;
    private Subscription subscribe;
    private ROrderDetail data;
    private ImageAdapter imageAdapter1;
    private ImageAdapter imageAdapter2;
    private boolean isPer;
    private String consignee;
    private String phonenumber;
    private String address1;


    @Override
    public void parseIntent(Intent intent) {
        orderId = intent.getStringExtra("orderId");
        isPer = intent.getBooleanExtra("isPer", false);
        consignee = intent.getStringExtra("consignee");
        phonenumber = intent.getStringExtra("phonenumber");
        address1 = intent.getStringExtra("address");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_re_order_detail;
    }

    @Override
    public void initView() {
        titleView.setOnLeftImgClickListener(v -> finish());
        rvImage1.setLayoutManager(new GridLayoutManager(this, 3));
        rvImage1.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<String> item = adapter.getData();
                ImagePreviewActivity.start(ReOrderDetailActivity.this, view, OSSUtils.BASE_URL + item.get(position));
            }
        });
        imageAdapter1 = new ImageAdapter();
        rvImage1.setAdapter(imageAdapter1);
        rvImage2.setLayoutManager(new GridLayoutManager(this, 3));
        rvImage2.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<String> item = adapter.getData();
                ImagePreviewActivity.start(ReOrderDetailActivity.this, view, OSSUtils.BASE_URL + item.get(position));
            }
        });
        imageAdapter2 = new ImageAdapter();
        rvImage2.setAdapter(imageAdapter2);

        llCstatus.removeAllViews();
    }


    @Override
    public void initData() {
        subscribe = HttpModule.getReturnGoods(HttpParams.getReturnGoods(orderId))
                .subscribe(new ProgressSubscriber<ROrderDetail>(this) {
                    @Override
                    public void onNext(ROrderDetail resp) {
                        data = resp;
                        setUiData();
                    }
                });
    }

    private void setUiData() {
        tvCause.setText(data.getReason());
        tvDesc.setText(data.getRemark());
        List<String> images1 = new ArrayList<>();
        String image1 = data.getImage1();
        if (!TextUtils.isEmpty(image1)) {
            images1.add(image1);
        }
        String image2 = data.getImage2();
        if (!TextUtils.isEmpty(image2)) {
            images1.add(image2);
        }
        String image3 = data.getImage3();
        if (!TextUtils.isEmpty(image3)) {
            images1.add(image3);
        }
        String image4 = data.getImage4();
        if (!TextUtils.isEmpty(image4)) {
            images1.add(image4);
        }
        String image5 = data.getImage5();
        if (!TextUtils.isEmpty(image5)) {
            images1.add(image5);
        }


        imageAdapter1.setNewData(images1);
        String status = data.getStatus();
        String flag = data.getFlag();

//        if (data.getFlag().equals("0") || (status.equals("0") && flag.equals("1"))) {
        if (data.getFlag().equals("0") && !status.equals("0")) {
            etRemark.setVisibility(View.VISIBLE);
        } else {
            etRemark.setVisibility(View.GONE);
        }
        etRemark.setEnabled(false); //用户不可编辑
        String remark = "";
        try {
            remark = URLDecoder.decode(data.getCheeckremark(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        etRemark.setText(remark);

        if (status.equals("0") && flag.equals("1")) {
            tvYes.setVisibility(isPer ? View.GONE : View.VISIBLE);
            tvNo.setVisibility(isPer ? View.GONE : View.VISIBLE);
            llBtnLayout.setVisibility(View.VISIBLE);
            tvResubmit.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);
            etRemark.setEnabled(!isPer);
        } else if ((status.equals("1") || status.equals("2")) && flag.equals("0")) {
            if (isPer) {
                llBtnLayout.setVisibility(View.VISIBLE);
                tvResubmit.setVisibility(View.VISIBLE);
                tvCancel.setVisibility(View.VISIBLE);
                tvYes.setVisibility(View.GONE);
                tvNo.setVisibility(View.GONE);
            } else {
                tvResubmit.setVisibility(View.GONE);
                llBtnLayout.setVisibility(View.GONE);
                tvCancel.setVisibility(View.GONE);
            }
        } else {
            llBtnLayout.setVisibility(View.GONE);
        }


        if (TextUtils.isEmpty(data.getExpressNo())) {
            llLayout2.setVisibility(View.GONE);
        } else {
            llLayout2.setVisibility(View.VISIBLE);
        }

        tvLogisticsNum.setText(data.getExpressNo());

        List<String> images2 = new ArrayList<>();
        String image6 = data.getImage6();
        if (!TextUtils.isEmpty(image6)) {
            images2.add(image6);
        }
        String image7 = data.getImage7();
        if (!TextUtils.isEmpty(image7)) {
            images2.add(image7);
        }
        imageAdapter2.setNewData(images2);

        if (status.equals("0")) {
            if (flag.equals("0")) {//用户取消,退款申请
            } else if (flag.equals("1")) { //客户经理审核
                if (isPer) {
                    addView(llCstatus, "取消退货", Style.red);
                } else {
                    //addView(llCstatus, "去审核", Style.red);
                }
            }
        } else if (status.equals("1")) {
            if (flag.equals("0")) { // 客户经理审核不通过
                if (isPer) {
                    addView(llCstatus, "再次申请", Style.gray);
                    addView(llCstatus, "取消退货", Style.red);
                }
            } else if (flag.equals("1")) { // 待平台审核
                if (isPer) {
                    addView(llCstatus, "取消退货", Style.red);
                }
            }
        } else if (status.equals("2")) {
            if (flag.equals("0")) { //平台审核不通过
                if (isPer) {
                    addView(llCstatus, "再次申请", Style.red);
                }
            } else if (flag.equals("1")) { //平台审核通过,用户待发货/上传快递单号
                if (isPer) {
                    addView(llCstatus, "上传快递单号", Style.border);
                    addView(llCstatus, "取消退货", Style.red);
                }
            }
        } else if (status.equals("3") && flag.equals("1")) { // 用户已发货
        } else if (status.equals("4")) {
            if (flag.equals("0")) { // 供应商拒绝收货
                if (isPer) {
                    addView(llCstatus, "联系客服", Style.red);
                }
            } else if (flag.equals("1")) { //供应商确认收货, 待退款状态
            }
        } else if (status.equals("5")) {
            if (flag.equals("0")) { //平台拒绝退款,退款失败
                if (isPer) {
                    addView(llCstatus, "联系客服", Style.red);
                }
            } else if (flag.equals("1")) {//平台同意退款
            }
        } else if (status.equals("6") && flag.equals("1")) {//退款成功
        }
        llCstatus.setVisibility(llCstatus.getChildCount() == 0 ? View.GONE : View.VISIBLE);
    }

    private void addView(ViewGroup parent, String text, Style style) {
        TextView tv = null;
        switch (style) {
            case red:
                tv = getRedTextView(parent);
                break;
            case gray:
                tv = getGrayTextView(parent);
                break;
            case small:
                tv = getSmallTextView(parent);
                break;
            case border:
                tv = getBorderTextView(parent);
                break;
        }
        tv.setText(text);
        parent.addView(tv, parent.getChildCount());
        final TextView finalTv = tv;
        tv.setOnClickListener(v -> {
            String text1 = ((TextView) v).getText().toString();
            Intent intent = new Intent();
            intent.putExtra("orderId", data.getOrdersId());
            switch (text1) {
//                    case "关闭订单":
//                        ToastUtils.showMsg(text);
//                        break;
                case "联系客服":
//                        ToastUtils.showMsg(text);
                    DialogHelper.getMessageDialog(ReOrderDetailActivity.this, "联系客服 电话:400-040-6755", (dialogInterface, i) ->
                            SystemUtils.startDial(ReOrderDetailActivity.this, "4000406755")).show();
                    break;
//                    case "查看物流":
//                        intent.setClass(ReOrderDetailActivity.this, LogisticsDetailActivity.class);
//                        intent.putExtra("proImage", item.getPro_pic());
//                        startActivity(intent);
//                        break;
                case "上传快递单号":
                    intent.setClass(ReOrderDetailActivity.this, AddExpressNumActivity.class);
                    intent.putExtra("orderItemsId", data.getOrderitemsId());
                    startActivityForResult(intent, 100);
                    break;
                case "取消退货":
                    DialogHelper.getMessageDialog(ReOrderDetailActivity.this, "是否取消退货?", (dialogInterface, i) ->
                            verfyReturn(data.getOrdersId(), "0", "0", "")).show();
                    break;
//                    case "同意退款":
//                        verfyReturn(item.getOrdersId(), "KS", "K");
//                        break;
//                    case "拒绝退款":
//                        verfyReturn(item.getOrdersId(), "", "F");
//                        break;
                case "去审核":
                    intent.setClass(ReOrderDetailActivity.this, ReOrderDetailActivity.class);
                    intent.putExtra("orderId", data.getOrdersId());
                    intent.putExtra("isPer", isPer);
                    startActivityForResult(intent, 100);
                    break;
                case "再次申请":
                    String address = consignee + "-" + phonenumber + "-" + address1;
                    intent.setClass(ReOrderDetailActivity.this, AfterSaleActivity.class);
                    intent.putExtra("type", "return");
                    intent.putExtra("address", address);
                    startActivityForResult(intent, 100);
                    break;
            }
        });
    }

    private TextView getRedTextView(ViewGroup parent) {
        return (TextView) LayoutInflater.from(this).inflate(R.layout.item_red_text_layout, parent, false);
    }

    private TextView getGrayTextView(ViewGroup parent) {
        return (TextView) LayoutInflater.from(this).inflate(R.layout.item_gray_text_layout, parent, false);
    }

    private TextView getSmallTextView(ViewGroup parent) {
        return (TextView) LayoutInflater.from(this).inflate(R.layout.item_small_text_layout, parent, false);
    }

    private TextView getBorderTextView(ViewGroup parent) {
        return (TextView) LayoutInflater.from(this).inflate(R.layout.item_border_text_layout, parent, false);
    }

    enum Style {
        red, gray, small, border
    }

    @OnClick({R.id.tv_yes, R.id.tv_no, R.id.tv_resubmit, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_yes: // 客户经理同意退货
                verfyReturn(data.getOrdersId(), "1", "1", "");
                break;
            case R.id.tv_no: // 客户经理不同意退货
                String remark = etRemark.getText().toString();
                if (TextUtils.isEmpty(remark)) {
                    ToastUtils.showMsg("备注信息不能为空");
                    return;
                }
                verfyReturn(data.getOrdersId(), "0", "1", remark);
                break;
            case R.id.tv_resubmit: // 重新提交
                Intent intent = new Intent(this, AfterSaleActivity.class);
                intent.putExtra("orderId", data.getOrdersId());
                intent.putExtra("type", "return");
                intent.putExtra("address", data.getAddressinfo());
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_cancel:
                DialogHelper.getMessageDialog(this, "是否取消退货?", (dialogInterface, i) ->
                        verfyReturn(data.getOrdersId(), "0", "0", "")).show();
                break;
        }
    }

    public void verfyReturn(String orderId, String flag, String status, String checkremark) {
//        String remark = "";
//        try {
//            remark = URLEncoder.encode(checkremark, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        subscribe = HttpModule.verfyReturn(HttpParams.verfyReturn(orderId, flag, status, checkremark))
                .subscribe(new CommonSubscriber<BaseResp>() {
                    @Override
                    public void onNext(BaseResp baseResp) {
//                        if (flag.equals("0")) { //客户经理同意退货
                        String statusName = status.equals("0") ? "取消订单成功!" : "客户经理审核成功!";
                        DialogHelper.getSingleButtonDialog(ReOrderDetailActivity.this, "温馨提示",
                                statusName, "确认", view -> finish()).show();
//                        }
                        initData();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }
}
