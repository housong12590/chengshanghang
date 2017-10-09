package com.jmm.csg.pro.service;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.helper.DialogHelper;
import com.jmm.csg.utils.SystemUtils;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 退换货
 */

public class ExchangeActivity extends BaseActivity {

    @Bind(R.id.titleView) TitleView mTitleView;
    private String orderId;
    private String address;

    @Override
    public void parseIntent(Intent intent) {
        orderId = intent.getStringExtra("orderId");
        address = intent.getStringExtra("address");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_exchange;
    }

    @Override
    public void initView() {
        mTitleView.setOnLeftImgClickListener(view -> finish());
    }

    @OnClick({R.id.tv_exchange, R.id.tv_return})
    public void onClick(View view) {
        Intent intent = new Intent(this, AfterSaleActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("address", address);
        switch (view.getId()) {
            case R.id.tv_exchange:
                DialogHelper.getMessageDialog(this, "换货请联系客服人员", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SystemUtils.startDial(ExchangeActivity.this, "4000406755");
                    }
                }).show();
//                intent.putExtra("type", "exchange");
//                startActivityForResult(intent, 100);
                break;
            case R.id.tv_return:
                intent.putExtra("type", "return");
                startActivityForResult(intent, 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
