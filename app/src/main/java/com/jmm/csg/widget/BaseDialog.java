package com.jmm.csg.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.jmm.csg.R;
import com.jmm.csg.utils.DensityUtils;


public abstract class BaseDialog extends Dialog {

    private int width;
    private int height;

    public BaseDialog(Context context) {
        super(context, R.style.comment_dialog);
        width = DensityUtils.getScreenWidth(context);
        height = DensityUtils.getScreenHeight(context);
        initDialogParams();
        initView();
        initData();
    }

    private void initDialogParams() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = width;
        lp.height = height / 2;
        window.setAttributes(lp);
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();
}
