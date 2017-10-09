package com.jmm.csg.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * author：hs
 * date: 2017/5/24 0024 11:05
 */
public class CustomImageView extends ImageView {
    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Drawable d = getDrawable();
        if (d != null) {// ceil not round - avoid thin vertical gaps along the left/right edges
            int width = MeasureSpec.getSize(widthMeasureSpec);
            // 高度根据使得图片的宽度充满屏幕计算而得
            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());

            setMeasuredDimension(width, height);

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        }
    }
}
