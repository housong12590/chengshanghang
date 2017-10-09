package com.jmm.csg.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.jmm.csg.R;
import com.jmm.csg.utils.DensityUtils;


/**
 * 通用对话框
 */

public class CommonDialog extends Dialog {

    public CommonDialog(Context context) {
        super(context);
    }

    public CommonDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder implements View.OnClickListener {

        private Context mContext;
        private int gravity = Gravity.CENTER;//默认居中
        private String title;
        private String message;
        private TextView tv_title;
        private TextView tv_message;
        private Button bt_positive;
        private Button bt_negative;
        private CommonDialog dialog;
        private String positiveButtonText;
        private String negativeButtonText;
        private boolean cancelable = true;
        private boolean canceledOnTouchOutside = true;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private View.OnClickListener dismissListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder(Context context, String title, String message) {
            this.mContext = context;
            this.title = title;
            this.message = message;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(int resId) {
            title = (String) mContext.getText(resId);
            return this;
        }

        public Builder setMessage(int resId) {
            title = (String) mContext.getText(resId);
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessageGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setOnDismissListener(View.OnClickListener listener) {
            this.dismissListener = listener;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Builder setPositiveButtonText(int textId, OnClickListener listener) {
            this.positiveButtonText = (String) mContext.getText(textId);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButtonText(String text, OnClickListener listener) {
            this.positiveButtonText = text;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButtonText(String text, OnClickListener listener) {
            this.negativeButtonText = text;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButtonText(int textId, OnClickListener listener) {
            this.negativeButtonText = (String) mContext.getText(textId);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CommonDialog create() {
            dialog = new CommonDialog(mContext, R.style.comment_dialog);
            View view = View.inflate(mContext, R.layout.dialog_layout, null);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_message = (TextView) view.findViewById(R.id.tv_message);
            bt_positive = (Button) view.findViewById(R.id.bt_positive);
            bt_negative = (Button) view.findViewById(R.id.bt_negative);
//            dialog.setCanceledOnTouchOutside(false);
            if (title != null) {
                tv_title.setText(title);
            }
            if (message != null) {
                tv_message.setText(message);
                tv_message.setGravity(gravity);
            }
            if (negativeButtonText != null) {
                bt_negative.setText(negativeButtonText);
            } else {
                bt_negative.setVisibility(View.GONE);
            }
            if (positiveButtonText != null) {
                bt_positive.setText(positiveButtonText);
            } else {
                bt_positive.setVisibility(View.GONE);
            }
            bt_negative.setOnClickListener(this);
            bt_positive.setOnClickListener(this);
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            dialog.setCancelable(cancelable);
            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (dismissListener != null) {
                        dismissListener.onClick(null);
                    }
                }
            });
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            int screenWidth = DensityUtils.getScreenWidth(dialog.getContext());
            params.width = screenWidth / 7 * 5;
            window.setAttributes(params);
            return dialog;
        }

        public CommonDialog show() {
            if (dialog == null) {
                dialog = create();
            }
            if (!dialog.isShowing()) {
                dialog.show();
            }
            return dialog;
        }


        public void dismiss() {
            if (dialog == null) {
                dialog = create();
            } else
                dialog.dismiss();
        }

        private boolean dismiss = true;

        public void setDismiss(boolean dismiss) {
            this.dismiss = dismiss;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_negative:
                    if (negativeButtonClickListener != null) {
                        negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                    if (dismiss) {
                        dismiss();
                    }
                    break;
                case R.id.bt_positive:
                    if (positiveButtonClickListener != null) {
                        positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                    dismiss();
                    break;
            }
        }
    }
}
