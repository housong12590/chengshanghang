package com.jmm.csg.pro.order.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.utils.ToastUtils;
import com.jmm.csg.widget.InvoiceInfoHintDialog;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 发票信息
 */
public class InvoiceInfoActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.titleView) TitleView mTitleView;
    @Bind(R.id.rb_person) RadioButton mRbPerson;
    @Bind(R.id.radioGroup) RadioGroup radioGroup;
    @Bind(R.id.et_content) EditText mEtContent;
    @Bind(R.id.ll_code) LinearLayout mLlCode;
    @Bind(R.id.et_code) EditText mEtCode;
    @Bind(R.id.rb_enterprise) RadioButton mRbEnterprise;
    @Bind(R.id.rg_invoice) RadioGroup mRgInvoice;
    @Bind(R.id.rb_no_invoice) RadioButton mRbNoInvoice;
    @Bind(R.id.rb_paper) RadioButton mRbPaper;
    @Bind(R.id.ll_content) LinearLayout mLlContent;
    private String invoiceType = "N";
    private String titleType = "P";
    private String content;
    private String taxIdentNum;

    @Override
    public void parseIntent(Intent intent) {
        content = intent.getStringExtra("content");
        titleType = intent.getStringExtra("titleType");
        taxIdentNum = intent.getStringExtra("taxIdentNum");
        invoiceType = intent.getStringExtra("invoiceType");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_invoice_info;
    }

    @Override
    public void initView() {
        mTitleView.setOnLeftImgClickListener(view -> finish());
        radioGroup.setOnCheckedChangeListener(this);
        mRbPerson.setChecked(titleType.equals("P"));
        mRbEnterprise.setChecked(titleType.equals("S"));
        if (invoiceType.equals("N")) {
            mRbNoInvoice.setChecked(true);
            mLlContent.setVisibility(View.INVISIBLE);
        } else if (invoiceType.equals("P")) {
            mRbPaper.setChecked(true);
            mLlContent.setVisibility(View.VISIBLE);
        }
        mEtContent.setText(content);
        mEtCode.setText(taxIdentNum);
        mRgInvoice.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_person:
                titleType = "P";
                mEtContent.setVisibility(View.INVISIBLE);
                mLlCode.setVisibility(View.GONE);
                break;
            case R.id.rb_enterprise:
                titleType = "S";
                mEtContent.setVisibility(View.VISIBLE);
                mLlCode.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_no_invoice:
                invoiceType = "N";
                mLlContent.setVisibility(View.INVISIBLE);
                break;
            case R.id.rb_paper:
                invoiceType = "P";
                mLlContent.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick(R.id.tv_commit)
    public void commit() {
        String companyName = mEtContent.getText().toString().trim(); //企业名称
        String taxIdentNum = mEtCode.getText().toString().trim(); //纳税人识别号
        if (titleType.equals("S") && !invoiceType.equals("N")) {
            if (TextUtils.isEmpty(companyName)) {
                ToastUtils.showMsg("请输入单位名称");
                return;
            }
            if (TextUtils.isEmpty(taxIdentNum)) {
                ToastUtils.showMsg("请输入纳税人识别号");
                return;
            }
        } else {
            companyName = "";
            taxIdentNum = "";
        }
        Intent intent = new Intent();
        intent.putExtra("titleType", titleType); // 发票抬头
        intent.putExtra("invoiceType", invoiceType); // 发票类型
        intent.putExtra("companyName", titleType.equals("P") ? "" : companyName);
        intent.putExtra("taxIdentNum", titleType.equals("P") ? "" : taxIdentNum);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.iv_hint)
    public void onClick() {
        new InvoiceInfoHintDialog(this).show();
    }
}
