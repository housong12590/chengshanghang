package com.jmm.csg.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmm.csg.R;

public class TipLayout extends LinearLayout {

    private int imageId;
    private String tipName;
    private TextView tvTip;

    public TipLayout(Context context) {
        this(context, null);
    }

    public TipLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TipLayout);
//        tipName = ta.getString(R.styleable.TipLayout_tip_name);
        imageId = ta.getResourceId(R.styleable.TipLayout_tip_image, 0);
        ta.recycle();
        initView();
    }


    private void initView() {
        inflate(getContext(), R.layout.tip_layout, this);
        ImageView ivImg = (ImageView) findViewById(R.id.iv_image);
        tvTip = (TextView) findViewById(R.id.tv_tip);
        TextView tvName = (TextView) findViewById(R.id.tv_name);
//        if (!TextUtils.isEmpty(tipName)) {
//            tvName.setText(tipName);
//        }
        if (imageId != 0) {
            ivImg.setBackgroundResource(imageId);
        }
    }

    public void setTip(String text) {
        if (TextUtils.isEmpty(text) || text.equals("0")) {
            tvTip.setVisibility(GONE);
            tvTip.setText("");
        } else {
            tvTip.setVisibility(VISIBLE);
            tvTip.setText(text);
        }
    }
}
