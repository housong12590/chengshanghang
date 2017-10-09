package com.jmm.csg.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.jmm.csg.Constant;


public class DelayedTimeView extends TextView {

    private static final int DEF_TIME = 60;
    private int tempTime = DEF_TIME;
    private EditText editText;


    public DelayedTimeView(Context context) {
        this(context, null);
    }

    public DelayedTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setEnabled(false);
    }

    private Runnable r = () -> {
        if (tempTime >= 0) {
            setText(tempTime + "秒");
            tempTime--;
            start();
        } else {
            setText("获取验证码");
            tempTime = DEF_TIME;
            setEnabled(true);
        }
    };

    public void bindEditText(EditText editText) {
        this.editText = editText;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setEnabled(s.toString().matches(Constant.PHONE_CHECK));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void start() {
        postDelayed(r, isEnabled() ? 0 : 1000);
        setEnabled(false);
    }
}
