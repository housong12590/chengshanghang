package com.jmm.csg.pro.service;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.XActivity;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.pro.contract.EvaluateContract;
import com.jmm.csg.pro.presenter.EvaluatePresenter;
import com.jmm.csg.widget.TitleView;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class EvaluateActivity extends XActivity<EvaluatePresenter> implements EvaluateContract.V,
        RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.rb_p) RadioGroup mRbP;
    @Bind(R.id.rb_a) RadioGroup mRbA;
    @Bind(R.id.titleView) TitleView mTitleView;
    @Bind(R.id.et_p_content) EditText mEtPContent;
    @Bind(R.id.et_a_content) EditText mEtAContent;
    @Bind(R.id.rb_p_positive) RadioButton mRbPPositive;
    @Bind(R.id.rb_p_negative) RadioButton mRbPNegative;
    @Bind(R.id.rb_a_positive) RadioButton mRbAPositive;
    @Bind(R.id.rb_a_negative) RadioButton mRbANegative;
    private String orderId;
    private String orderItemId;
    private String a = "P";
    private String p = "P";
    private String productId;

    @Override
    public void parseIntent(Intent intent) {
        orderId = intent.getStringExtra("orderId");
        orderItemId = intent.getStringExtra("orderItemId");
        productId = intent.getStringExtra("productId");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_publish_evaluate;
    }

    @Override
    public void initView() {
        mTitleView.setOnLeftImgClickListener(view -> finish());
        mRbA.setOnCheckedChangeListener(this);
        mRbP.setOnCheckedChangeListener(this);
        mRbPPositive.setChecked(true);
        mRbAPositive.setChecked(true);
    }

    @Override
    public EvaluatePresenter newPresenter() {
        return new EvaluatePresenter();
    }

    @OnClick(R.id.tv_commit)
    public void onClick() {
        String pContent = mEtPContent.getText().toString().trim();
        String aContent = mEtAContent.getText().toString().trim();
        if (TextUtils.isEmpty(pContent)) {
            pContent = "此用户未填写评价";
        }
        if (TextUtils.isEmpty(aContent)) {
            aContent = "此用户未填写评价";
        }
        String userId = UserDataHelper.getUserId();
        Map<String, String> params = HttpParams.commitComment(p, a, pContent, aContent, orderId,
                orderItemId, productId, userId, userId, "");
        getP().commitComment(params);
    }

    @Override
    public void onSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_p_positive:
                p = "P";
                break;
            case R.id.rb_p_negative:
                p = "B";
                break;
            case R.id.rb_a_positive:
                a = "P";
                break;
            case R.id.rb_a_negative:
                a = "B";
                break;
        }
    }
}
