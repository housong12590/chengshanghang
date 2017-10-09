package com.jmm.csg.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jmm.csg.R;

public class TitleView extends LinearLayout {

    private TextView mTvTitle;
    private ImageView mIvLeftImg;
    private TextView mTvRightText;
    private TextView mTvLeftText;
    private ImageView mIvRightImg;
    private RelativeLayout mRootLayout;
    private String titleText;
    private String rightText;
    private int leftImageId;
    private int rightImageId;
    private int background;
    private int titleColor;
    private String leftText;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
        initView();
        initData();
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView);
        titleText = ta.getString(R.styleable.TitleView_title_text);
        rightText = ta.getString(R.styleable.TitleView_right_text);
        leftImageId = ta.getResourceId(R.styleable.TitleView_left_image, 0);
        rightImageId = ta.getResourceId(R.styleable.TitleView_right_image, 0);
        background = ta.getColor(R.styleable.TitleView_title_background, -1);
        titleColor = ta.getColor(R.styleable.TitleView_title_color, 0);
        leftText = ta.getString(R.styleable.TitleView_left_text);
        ta.recycle();
    }

    private void initView() {
        inflate(getContext(), R.layout.base_title_layout, this);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvLeftImg = (ImageView) findViewById(R.id.iv_left_image);
        mIvRightImg = (ImageView) findViewById(R.id.iv_right_image);
        mTvRightText = (TextView) findViewById(R.id.tv_right_text);
        mRootLayout = (RelativeLayout) findViewById(R.id.root_layout);
        mTvLeftText = (TextView) findViewById(R.id.tv_left_text);
    }

    private void initData() {
        if (!TextUtils.isEmpty(titleText)) {
            mTvTitle.setText(titleText);
        }
        if (leftImageId != 0) {
            mIvLeftImg.setImageResource(leftImageId);
        }
        if (rightImageId != 0) {
            mIvRightImg.setImageResource(rightImageId);
        }
        if (!TextUtils.isEmpty(rightText)) {
            mTvRightText.setText(rightText);
        }
        if (background != -1) {
            mRootLayout.setBackgroundColor(background);
        }
        if (titleColor != 0) {
            mTvTitle.setTextColor(titleColor);
        }
        if(!TextUtils.isEmpty(leftText)){
            mTvLeftText.setText(leftText);
        }
    }


    public TitleView setBackground(int color){
        mRootLayout.setBackgroundColor(color);
        return this;
    }

    public TitleView setTitleColor(int color){
        mTvTitle.setTextColor(color);
        return this;
    }

    public TitleView setTitle(String text) {
        mTvTitle.setText(text);
        return this;
    }

    public TitleView setLeftText(String text){
        mTvLeftText.setText(text);
        return this;
    }

    public TitleView setTitle(int resId) {
        mTvTitle.setText(resId);
        return this;
    }

    public TitleView setLeftImage(int resId) {
        mIvLeftImg.setImageResource(resId);
        return this;
    }

    public TitleView setRightImage(int resId) {
        mIvRightImg.setImageResource(resId);
        return this;
    }

    public TitleView setRightText(String text) {
        mTvRightText.setText(text);
        return this;
    }

    public TitleView setOnRightTextClickListener(View.OnClickListener listener) {
        mTvRightText.setOnClickListener(listener);
        return this;
    }

    public TitleView setOnLeftImgClickListener(View.OnClickListener listener) {
        mIvLeftImg.setOnClickListener(listener);
        mIvLeftImg.setVisibility(VISIBLE);
        return this;
    }

    public TitleView setOnRightImgClickListener(View.OnClickListener listener) {
        mIvRightImg.setOnClickListener(listener);
        mIvRightImg.setVisibility(VISIBLE);
        return this;
    }
    public TitleView setOnLeftTextAndImageListener(View.OnClickListener listener){
        mIvLeftImg.setOnClickListener(listener);
        mTvLeftText.setOnClickListener(listener);
        return this;
    }
}
