//package com.jmm.csg.widget;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.jmm.csg.R;
//import com.jmm.csg.utils.DensityUtils;
//
///**
// * author：hs
// * date: 2017/5/25 0025 16:04
// */
//public class MessageDialog extends Dialog implements View.OnClickListener {
//
//    private TextView tv_title;
//    private TextView tv_message;
//    private Button bt_positive;
//    private Button bt_negative;
//
//    public MessageDialog(Context context) {
//        this(context, R.style.comment_dialog);
//    }
//
//    public MessageDialog(Context context, int themeResId) {
//        super(context, themeResId);
//        init();
//    }
//
//    private void init() {
//        View view = View.inflate(getContext(), R.layout.dialog_layout, null);
//        tv_title = (TextView) view.findViewById(R.id.tv_title);
//        tv_message = (TextView) view.findViewById(R.id.tv_message);
//        bt_positive = (Button) view.findViewById(R.id.bt_positive);
//        bt_negative = (Button) view.findViewById(R.id.bt_negative);
////            dialog.setCanceledOnTouchOutside(false);
//        if (title != null) {
//            tv_title.setText(title);
//        }
//        if (message != null) {
//            tv_message.setText(message);
//            tv_message.setGravity(gravity);
//        }
//        if (negativeButtonText != null) {
//            bt_negative.setText(negativeButtonText);
//        } else {
//            bt_negative.setVisibility(View.GONE);
//        }
//        if (positiveButtonText != null) {
//            bt_positive.setText(positiveButtonText);
//        } else {
//            bt_positive.setVisibility(View.GONE);
//        }
//        bt_negative.setOnClickListener(this);
//        bt_positive.setOnClickListener(this);
//        dialog.setContentView(view);
//        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
//        dialog.setCancelable(cancelable);
//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams params = window.getAttributes();
//        int screenWidth = DensityUtils.getScreenWidth(dialog.getContext());
//        params.width = screenWidth / 7 * 5;
//        window.setAttributes(params);
//    }
//
//
//    @Override
//    public void onClick(View view) {
//
//    }
//}
