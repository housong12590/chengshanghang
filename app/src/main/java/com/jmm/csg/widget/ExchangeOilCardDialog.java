package com.jmm.csg.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.jmm.csg.Constant;
import com.jmm.csg.R;
import com.jmm.csg.bean.OilCard;
import com.jmm.csg.callback.OnResultListener;
import com.jmm.csg.utils.DensityUtils;
import com.jmm.csg.utils.ToastUtils;


public class ExchangeOilCardDialog extends Dialog implements View.OnClickListener {

    private OnResultListener listener;
    private EditText etCardId;
    private EditText etName;
    private EditText etPhone;

    public ExchangeOilCardDialog(@NonNull Context context) {
        super(context, R.style.comment_dialog);
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_exchange_oil_card_layout);
        etCardId = (EditText) findViewById(R.id.et_cardId);
        etName = (EditText) findViewById(R.id.et_name);
        etPhone = (EditText) findViewById(R.id.et_phone);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_affirm).setOnClickListener(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        int screenWidth = DensityUtils.getScreenWidth(getContext());
        params.width = screenWidth / 5 * 4;
        window.setAttributes(params);
        setCanceledOnTouchOutside(false);
    }

    public ExchangeOilCardDialog setOnResultListener(OnResultListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_affirm:
                String cardId = etCardId.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String name = etName.getText().toString().trim();
                if (TextUtils.isEmpty(cardId)) {
                    ToastUtils.showMsg("请输入卡号");
                    return;
                }
                if (TextUtils.isEmpty(phone) && !phone.matches(Constant.PHONE_CHECK)) {
                    ToastUtils.showMsg("请输入正确的手机号码");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    ToastUtils.showMsg("请输入姓名");
                    return;
                }
                OilCard oilCard = new OilCard(cardId, name, phone);
                if (listener != null) {
                    listener.onResult(0, oilCard);
                }
                dismiss();
                break;
        }
    }
}
