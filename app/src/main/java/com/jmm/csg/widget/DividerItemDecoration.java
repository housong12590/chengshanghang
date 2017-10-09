package com.jmm.csg.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/**
 * Author: 30453
 * Date: 2016/7/27 18:24
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private int mOrientation = LinearLayoutManager.VERTICAL;
    private int mItemSize = 1;
    private Paint mPaint;
    private int dividerColor = Color.parseColor("#DADADA");
    private int margeSize = 0;

    public DividerItemDecoration(Context context, int margeSize) {
        this(context, margeSize, LinearLayoutManager.VERTICAL);
    }

    public DividerItemDecoration(Context context) {
        this(context, LinearLayoutManager.VERTICAL);
    }

    public DividerItemDecoration(Context context, int margeSize, int orientation) {
        this.margeSize = dp2px(margeSize);
        this.mOrientation = orientation;
        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
            throw new IllegalArgumentException("请传入正确的参数");
        }
        mItemSize = (int) TypedValue.applyDimension(mItemSize, TypedValue.COMPLEX_UNIT_DIP, context.getResources().getDisplayMetrics());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        int left = parent.getPaddingLeft() + margeSize;
        int right = parent.getMeasuredWidth() - parent.getPaddingRight() - margeSize;
        int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mItemSize;
            canvas.drawRect(left,top,right,bottom,mPaint);
        }
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mItemSize;
            canvas.drawRect(left,top,right,bottom,mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mItemSize);
        } else {
            outRect.set(0, 0, mItemSize, 0);
        }
    }

    private int dp2px(int space) {
        return (int) (Resources.getSystem().getDisplayMetrics().density * space);
    }
}
