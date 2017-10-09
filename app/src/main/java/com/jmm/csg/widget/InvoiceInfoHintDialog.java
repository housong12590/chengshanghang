package com.jmm.csg.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jmm.csg.R;
import com.jmm.csg.utils.DensityUtils;


public class InvoiceInfoHintDialog extends Dialog {

    public InvoiceInfoHintDialog(@NonNull Context context) {
        this(context, R.style.comment_dialog);
    }

    public InvoiceInfoHintDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.dialog_invoice_info, null);
        setContentView(view);
        findViewById(R.id.tv_affirm).setOnClickListener(view1 -> dismiss());
        findViewById(R.id.tv_link).setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("http://www.nacao.org.cn/");
            intent.setData(content_url);
            getContext().startActivity(intent);
        });
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        int screenWidth = DensityUtils.getScreenWidth(getContext());
        params.width = screenWidth / 4 * 3;
        window.setAttributes(params);
    }
}
