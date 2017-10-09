package com.jmm.csg.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jmm.csg.R;
import com.jmm.csg.bean.BaseResp;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.http.ProgressSubscriber;
import com.jmm.csg.pro.order.activity.OrderDetailActivity;
import com.jmm.csg.utils.ToastUtils;

import static android.app.Activity.RESULT_OK;
/**
 * Created by Administrator on 2017/9/28/028.
 */

public class RefundReasonDialog extends Dialog implements View.OnClickListener {

    Context context;
    String userId;
    String orderId;
    private RadioGroup mRadioGroup;

    public RefundReasonDialog(@NonNull Context context, String userId, String orderId) {
        super(context, R.style.comment_dialog);
        this.context = context;
        this.userId = userId;
        this.orderId = orderId;
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_refund_reason);
        findViewById(R.id.affirm).setOnClickListener(this);
        mRadioGroup = (RadioGroup) findViewById(R.id.rg);

        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        lp.width = wm.getDefaultDisplay().getWidth();
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.comment_dialog_animation);
    }

    @Override
    public void onClick(View v) {
        int checkedId = mRadioGroup.getCheckedRadioButtonId();
        if (checkedId == -1) {
            ToastUtils.showShort("请选择退款原因");
        } else {
            RadioButton radioButton = (RadioButton) RefundReasonDialog.this.findViewById(checkedId);
            String refundReason = radioButton.getText().toString();
            applyRefund(refundReason);
        }
    }

    private void applyRefund(String s) {
        HttpModule.applyRefun(HttpParams.applyRefun(UserDataHelper.getUserId(), orderId, s)).subscribe(new ProgressSubscriber<BaseResp>(context) {
            @Override
            public void onNext(BaseResp b) {
                if (b.getCode().equals("1")) {
                    RefundReasonDialog.this.dismiss();
                    ((OrderDetailActivity) context).setResult(RESULT_OK);
                    ((OrderDetailActivity) context).finish();
                }
                ToastUtils.showShort(b.getMessage());
            }
        });
    }
}
