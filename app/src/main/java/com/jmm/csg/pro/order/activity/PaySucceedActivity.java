package com.jmm.csg.pro.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import com.jmm.csg.Constant;
import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.utils.TimeUtils;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;

/**
 * 支付成功
 */
public class PaySucceedActivity extends BaseActivity {

    @Bind(R.id.titleView) TitleView mTitleView;
    @Bind(R.id.tv_orderId) TextView mTvOrderId;
    @Bind(R.id.tv_price) TextView mTvPrice;
    @Bind(R.id.tv_create_time) TextView mTvCreateTime;
    @Bind(R.id.tv_pro_name) TextView mTvProName;
    @Bind(R.id.tv_pro_specification) TextView mTvProSpecification;
    private String orderId;
    private String proName;
    private String proSpecification;
    private String price;

    @Override
    public void parseIntent(Intent intent) {
        orderId = intent.getStringExtra("orderId");
        proName = intent.getStringExtra("proName");
        proSpecification = intent.getStringExtra("proSpecification");
        price = intent.getStringExtra("price");
    }

    public static void start(Activity context, String orderId, String proName, String proSpecification, String price) {
        Intent intent = new Intent(context, PaySucceedActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("proName", proName);
        intent.putExtra("proSpecification", proSpecification);
        intent.putExtra("price", price);
        context.startActivityForResult(intent, Constant.PAY_SUCCESS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_succeed;
    }

    @Override
    public void initView() {
        mTitleView.setOnLeftImgClickListener(view -> finish());
        mTvPrice.setText("¥" + price);
        mTvProName.setText(proName);
        mTvOrderId.setText(orderId);
        mTvProSpecification.setText(proSpecification);
        long aLong = Long.parseLong(orderId) / 1000;
        mTvCreateTime.setText(TimeUtils.timeToString2(String.valueOf(aLong)));
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("flag", Constant.PAY_SUCCESS);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
