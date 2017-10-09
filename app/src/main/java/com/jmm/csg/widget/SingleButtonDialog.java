package com.jmm.csg.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.jmm.csg.R;
import com.jmm.csg.utils.DensityUtils;

/**
 * author：hs
 * date: 2017/4/25 0025 18:39
 */
public class SingleButtonDialog extends Dialog {

    private TextView message;
    private TextView title;
    private Button button;

    public SingleButtonDialog(Context context) {
        this(context, R.style.comment_dialog);
        View view = View.inflate(getContext(), R.layout.dialog_single_button, null);
        message = (TextView) view.findViewById(R.id.tv_message);
        title = (TextView) view.findViewById(R.id.tv_title);
        button = (Button) view.findViewById(R.id.button);
//        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(view);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        int screenWidth = DensityUtils.getScreenWidth(getContext());
        params.width = screenWidth / 7 * 5;
        window.setAttributes(params);
    }

    public SingleButtonDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public SingleButtonDialog setMessage(String text) {
        message.setText(text);
        return this;
    }

    public SingleButtonDialog setTitle(String title) {
        this.title.setText(title);
        return this;
    }

    public SingleButtonDialog setOnClickListener(String text, View.OnClickListener listener) {
        button.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(text)) {
            button.setText(text);
        }
        button.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.onClick(v);
            }
        });
        return this;
    }
}
