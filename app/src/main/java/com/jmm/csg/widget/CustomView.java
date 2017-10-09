package com.jmm.csg.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jmm.csg.utils.DensityUtils;


public class CustomView extends View {

    private Paint paint;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(dp2px(1));
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), paint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), dp2px(8), dp2px(8), paint);
        paint.setStyle(Paint.Style.FILL);
//        paint.setStrokeWidth(dp2px(2));
        Path path1 = new Path();
        path1.moveTo(getWidth() - dp2px(35), getHeight());
        path1.lineTo(getWidth(), getHeight());
        path1.lineTo(getWidth(), getHeight() - dp2px(35));
        path1.close();
        paint.setColor(Color.parseColor("#D17307"));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawPath(path1, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(sc);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(dp2px(3));
        canvas.drawLine(getWidth() - dp2px(20), getHeight() - dp2px(10), getWidth() - dp2px(15), getHeight() - dp2px(5), paint);
        canvas.drawLine(getWidth() - dp2px(15), getHeight() - dp2px(3), getWidth() - dp2px(5), getHeight() - dp2px(15), paint);

//        paint.setColor(Color.BLUE);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(dp2px(1));
//        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), dp2px(8), dp2px(8), paint);
    }

    private int dp2px(int dp) {
        return DensityUtils.dip2px(getContext(), dp);
    }
}
