package com.jmm.csg.pro.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jmm.csg.Constant;
import com.jmm.csg.R;
import com.jmm.csg.base.activity.XActivity;
import com.jmm.csg.bean.PayResult;
import com.jmm.csg.bean.UnionPayBean;
import com.jmm.csg.bean.WeChatBean;
import com.jmm.csg.config.AppConfig;
import com.jmm.csg.helper.DialogHelper;
import com.jmm.csg.pro.contract.PayOrderContract;
import com.jmm.csg.pro.presenter.PayOrderPresenter;
import com.jmm.csg.utils.ToastUtils;
import com.jmm.csg.widget.TitleView;
import com.jmm.csg.wxapi.WXPayEntryActivity;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;

import butterknife.Bind;
import butterknife.OnClick;

public class SelectPayActivity extends XActivity<PayOrderPresenter> implements PayOrderContract.V {

    @Bind(R.id.titleView) TitleView mTitleView;
    @Bind(R.id.rb_weiXin) RadioButton mRbWeiXin;
    @Bind(R.id.rb_zhiFuBao) RadioButton mRbZhiFuBao;
    @Bind(R.id.rb_yinLian) RadioButton mRbYinLian;
    @Bind(R.id.radioGroup) RadioGroup radioGroup;
    @Bind(R.id.tv_pay) TextView mTvPay;
    @Bind(R.id.contentView) View contentView;
    private String orderId;
    private String proName;
    private String proSpecification;
    private String price;
    private String payStatus = "cancel";
    private UnionPayBean unionPayBean;

    @Override
    public void parseIntent(Intent intent) {
        orderId = intent.getStringExtra("orderId");
        proName = intent.getStringExtra("proName");
        proSpecification = intent.getStringExtra("proSpecification");
        price = intent.getStringExtra("price");
    }

    public static void start(Activity activity, String orderId, String proName, String proSpecification,
                             String price, int requestCode) {
        Intent intent = new Intent(activity, SelectPayActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("proName", proName);
        intent.putExtra("proSpecification", proSpecification);
        intent.putExtra("price", price);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_pay;
    }

    @Override
    public void initView() {
        mTitleView.setOnLeftImgClickListener(view -> showHintDialog());
        mRbWeiXin.setChecked(true);
        onCompleted();
    }

    @Override
    public View getVisibilityView() {
        return contentView;
    }

    //只用于微信支付回调
    @Override
    protected void onStart() {
        super.onStart();
        int code = WXPayEntryActivity.PAY_STATUS_CODE;
        if (code == BaseResp.ErrCode.ERR_OK) { //支付成功
            PaySucceedActivity.start(this, orderId, proName, proSpecification, price);
        }
        WXPayEntryActivity.PAY_STATUS_CODE = 10;
        if (code <= 0 && code >= -6) {
//            payStatus = String.valueOf(code);

        }
        onCompleted();
    }


    @Override
    public void onWeChatRequestSuccess(WeChatBean bean) {
        IWXAPI wxapi = WXAPIFactory.createWXAPI(this, null);
        WeChatBean.Entity data = bean.getDate();
        wxapi.registerApp(data.getAppid());
        PayReq request = new PayReq();
        request.appId = data.getAppid();
        request.partnerId = data.getPartnerid();
        request.prepayId = data.getPrepayid();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = data.getNoncestr();
        request.timeStamp = data.getTimestamp();
        request.sign = data.getSign();
        boolean b = wxapi.sendReq(request);
        Log.e("wxapi.sendReq:", b + "");
        if (!b) {
            ToastUtils.showMsg("启动微信失败");
        }
        if (!b) {
            onCompleted();
        }
    }

    @Override
    public void onUnionPaySuccess(UnionPayBean bean) {
        unionPayBean = bean;
        UPPayAssistEx.startPay(SelectPayActivity.this, null, null, bean.getData().getTn(),
                AppConfig.ROUTE_CONFIG.unionPayCode());
    }

    @Override
    public void queryResult(PayResult result) {
        if (result.getResult().equals("SUCCESS")) {
            PaySucceedActivity.start(this, orderId, proName, proSpecification, price);
        } else {
            ToastUtils.showMsg("支付失败");
        }
    }

    @Override
    public PayOrderPresenter newPresenter() {
        return new PayOrderPresenter();
    }

    @OnClick({R.id.tv_pay})
    public void pay() {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_yinLian:
                getP().unionPay(orderId);
                break;
            case R.id.rb_weiXin:
//                showDialog();
                getP().payWeChat(orderId);
                break;
        }
    }

    @Override
    public void showDialog() {
        super.showDialog();
        loadingDialog.setCancelable(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.PAY_SUCCESS) {
            setResult(RESULT_OK, data);
            finish();
            return;
        }
        if (data == null) {
            return;
        }
        String result = data.getExtras().getString("pay_result");
        System.out.println(result);
//        String msg = "";
        onCompleted();
//        if (result.equalsIgnoreCase("success")) {
//
//            msg = "支付成功";
//        } else if (result.equalsIgnoreCase("fail")) {
//            msg = "支付失败";
//        } else if (result.equalsIgnoreCase("cancel")) {
//            msg = "取消支付";
//        }
        UnionPayBean.Data d = unionPayBean.getData();
        getP().orderQuery(d.getPayid(), d.getTxnTime()); //查询支付结果
//        ToastUtils.showMsg(msg);
    }

    private void showHintDialog() {
        DialogHelper.getMessageDialog(this, "你确定要放弃支付订单", (dialog, which) -> {
            Intent intent = new Intent();
            intent.putExtra("flag", Constant.NON_PAYMENT);
            setResult(RESULT_OK, intent);
            finish();
        }).show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showHintDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
