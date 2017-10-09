package com.jmm.csg.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jmm.csg.utils.DensityUtils;


public class DashedView extends View {

    private int screenWidth;

    public DashedView(Context context) {
        this(context, null);
    }

    public DashedView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        screenWidth = DensityUtils.getScreenWidth(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.DKGRAY);
        Path path = new Path();
        path.moveTo(0, 10);
        path.lineTo(screenWidth, 10);
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }
}
