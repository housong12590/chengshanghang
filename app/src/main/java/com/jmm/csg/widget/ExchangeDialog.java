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
import com.jmm.csg.callback.OnResultListener;
import com.jmm.csg.utils.DensityUtils;
import com.jmm.csg.utils.ToastUtils;


public class ExchangeDialog extends Dialog implements View.OnClickListener {


    private EditText etContent;
    private OnResultListener listener;

    public ExchangeDialog(@NonNull Context context) {
        super(context, R.style.comment_dialog);
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_exchange_layout);
        etContent = (EditText) findViewById(R.id.et_content);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_affirm).setOnClickListener(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        int screenWidth = DensityUtils.getScreenWidth(getContext());
        params.width = screenWidth / 4 * 3;
        window.setAttributes(params);
        setCanceledOnTouchOutside(false);
    }

    public ExchangeDialog setOnResultListener(OnResultListener listener) {
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
                String content = etContent.getText().toString().trim();
                if (!TextUtils.isEmpty(content) && content.matches(Constant.PHONE_CHECK)) {
                    if (listener != null) {
                        listener.onResult(0, content);
                    }
                    dismiss();
                } else {
                    ToastUtils.showMsg("请填写正确的电话号码");
                }
                break;
        }
    }
}
