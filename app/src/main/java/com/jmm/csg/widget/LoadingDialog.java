package com.jmm.csg.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jmm.csg.R;


public class LoadingDialog extends Dialog {


    public LoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        init();
    }


    public LoadingDialog(Context context, int val) {
        super(context, val);
        init();
    }


    private void init() {
        View view = View.inflate(getContext(), R.layout.loading_layout, null);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.dimAmount = 0f;
        window.setAttributes(params);
        setCanceledOnTouchOutside(false);
        setContentView(view);
    }
}