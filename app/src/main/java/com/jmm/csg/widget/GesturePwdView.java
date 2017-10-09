package com.jmm.csg.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jmm.csg.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GesturePwdView extends View {
    /**
     * 手势结束监听
     **/
    private OnGestureFinishListener onGestureFinishListener;
    /**
     * 解锁圆点数组
     */
    private LockCircle[] cycles;
    /**
     * 存储触碰圆的序列
     */
    private List<Integer> linedCycles = new ArrayList<Integer>();
    // 画笔
    /**
     * 空心外圆
     */
    private Paint paintNormal;
    /**
     * 点击后内部圆
     */
    private Paint paintInnerCycle;
    /**
     * 实心外圆
     */
    private Paint paintOutCycle;
    /**
     * 画路径
     */
    private Paint paintLines;
    private Path linePath = new Path();
    /**
     * 当前手指X,Y位置
     */
    private int eventX, eventY;
    /**
     * 能否操控界面绘画
     */
    public boolean canContinue = true;
    /**
     * 验证结果
     */
    private boolean result;
    private Timer timer;
    /**
     * 未选中颜色
     */
    private int NORMAL_COLOR;
    /**
     * 错误颜色
     */
    private int ERROE_COLOR;// 错误外圆颜色
    /**
     * 当前进行状态
     **/
    public int validationStatus = GesturePwdStatus.INITIAL;
    /**
     * 选中时颜色
     */
    private int TOUCH_COLOR;
    /**
     * 选中时内圆颜色
     */
    private int SELECT_TOUCH_COLOR;
    /**
     * 临时存储密码 与原密码对比
     **/
    public StringBuffer tempResString = new StringBuffer();

    public GesturePwdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GesturePwdView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GesturePwdView(Context context) {
        this(context, null);
    }

    /**
     * 初始化
     */
    public void init() {
        paintNormal = new Paint();
        paintNormal.setAntiAlias(true);
        paintNormal.setStrokeWidth(3);
        paintNormal.setStyle(Paint.Style.STROKE);
        paintInnerCycle = new Paint();
        paintInnerCycle.setAntiAlias(true);
        paintInnerCycle.setStyle(Paint.Style.FILL);
        paintLines = new Paint();
        paintLines.setAntiAlias(true);
        paintLines.setStyle(Paint.Style.STROKE);
        paintLines.setStrokeWidth(6);
        paintOutCycle = new Paint();
        paintOutCycle.setAntiAlias(true);
        paintOutCycle.setStyle(Paint.Style.FILL);


        SELECT_TOUCH_COLOR = getColor(R.color.btn_nor);
//        TOUCH_COLOR = getColor(R.color.gray_f1);
        TOUCH_COLOR = getColor(android.R.color.transparent);
        ERROE_COLOR = getColor(R.color.red);
        NORMAL_COLOR = getColor(R.color.gray_96);
    }

    private int getColor(int colorId) {
        return getContext().getResources().getColor(colorId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int spceSize = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (spceSize * 0.85 + 0.5f), specMode);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int perWidthSize = getWidth() / 7;
        int perHeightSize = getHeight() / 6;
        if (cycles == null && (perWidthSize > 0) && (perHeightSize > 0)) {
            cycles = new LockCircle[9];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    LockCircle lockCircle = new LockCircle();
                    lockCircle.setNum(i * 3 + j);
                    lockCircle.setOx(perWidthSize * (j * 2 + 1.5f) + 0.5f);
                    lockCircle.setOy(perHeightSize * (i * 2 + 1f) + 0.5f);
                    lockCircle.setR(perWidthSize * 0.6f);
                    cycles[i * 3 + j] = lockCircle;
                }
            }
        }
    }

    public void setOnGestureFinishListener(OnGestureFinishListener onGestureFinishListener) {
        this.onGestureFinishListener = onGestureFinishListener;
    }

    /**
     * 手势输入完成后回调接口
     */
    public interface OnGestureFinishListener {
        /**
         * 手势输入完成后回调函数
         */
        void OnGestureFinish(int status, String key, List<Integer> linedCycles);
    }

    /**
     * 监听手势
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canContinue) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    eventX = (int) event.getX();
                    eventY = (int) event.getY();
                    for (LockCircle cycle : cycles) {
                        if (cycle.isPointIn(eventX, eventY)) {
                            cycle.setOnTouch(true);
                            if (!linedCycles.contains(cycle.getNum())) {
                                linedCycles.add(cycle.getNum());
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (linedCycles.size() < 4 && validationStatus != GesturePwdStatus.VALIDATE_AGAIN) {// 最少连接四个点,并且不处于二次验证状态
                        result = false;
                        if (onGestureFinishListener != null && linedCycles.size() > 0)// 调用监听,向上层返回结果
                        {
                            onGestureFinishListener.OnGestureFinish(GesturePwdStatus.NUMBER_ERROR, null, null);
                        }
                        // 手指离开暂停触碰
                        canContinue = false;
                        // 绘制完成间隔一段时间才可以触碰
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                eventX = eventY = 0;
                                for (int i = 0; i < 9; i++) {
                                    cycles[i].setOnTouch(false);
                                }
                                linedCycles.clear();
                                linePath.reset();
                                canContinue = true;
                                postInvalidate();// 在非ui线程刷新界面
                            }
                        }, 10);
                    } else {
                        result = true;
                        switch (validationStatus) {
                            case GesturePwdStatus.INITIAL:// 记录第一次手势
                                canContinue = true;
                                for (int i = 0; i < linedCycles.size(); i++) {
                                    tempResString.append(linedCycles.get(i));// 存储结果数字
                                }
                                if (onGestureFinishListener != null && linedCycles.size() > 0)// 调用监听,向上层返回结果
                                {
                                    onGestureFinishListener.OnGestureFinish(GesturePwdStatus.VALIDATE_AGAIN,
                                            tempResString.toString(), linedCycles);
                                }
                                validationStatus = GesturePwdStatus.VALIDATE_AGAIN;
                                eventX = eventY = 0;
                                for (int i = 0; i < 9; i++) {
                                    cycles[i].setOnTouch(false);
                                }
                                linedCycles.clear();
                                linePath.reset();
                                canContinue = true;
                                postInvalidate();// 在非ui线程刷新界面
                                break;
                            case GesturePwdStatus.VALIDATE:// 与成功记录手势对比
                            case GesturePwdStatus.VALIDATE_AGAIN:// 与第一次手势对比
                                canContinue = false;
                                StringBuffer stringBuffer = new StringBuffer();
                                for (int i = 0; i < linedCycles.size(); i++) {
                                    stringBuffer.append(linedCycles.get(i));// 存储结果数字
                                }
                                if (tempResString.toString().equals(stringBuffer.toString())) {// 二次验证成功
                                    if (onGestureFinishListener != null && linedCycles.size() > 0)// 调用监听,向上层返回结果
                                    {
                                        if (validationStatus == GesturePwdStatus.VALIDATE_AGAIN) {// 初始状态,二次验证
                                            onGestureFinishListener.OnGestureFinish(GesturePwdStatus.VALIDATE_AGAIN_SUCCESS,
                                                    stringBuffer.toString(), null);
                                        } else if (validationStatus == GesturePwdStatus.VALIDATE) {// 与成功记录手势对比
                                            onGestureFinishListener.OnGestureFinish(GesturePwdStatus.VALIDATE_SUCCESS,
                                                    stringBuffer.toString(), null);
                                        }
                                    }
                                } else {// 二次验证失败
                                    result = false;
                                    if (onGestureFinishListener != null && linedCycles.size() > 0)// 调用监听,向上层返回结果
                                    {
                                        if (validationStatus == GesturePwdStatus.VALIDATE_AGAIN) {// 初始状态,二次验证
                                            onGestureFinishListener.OnGestureFinish(GesturePwdStatus.VALIDATE_AGAIN_ERROR, null,
                                                    null);
                                        } else if (validationStatus == GesturePwdStatus.VALIDATE) {// 与成功记录手势对比
                                            onGestureFinishListener.OnGestureFinish(GesturePwdStatus.VALIDATE_ERROR,
                                                    stringBuffer.toString(), null);
                                        }
                                    }
                                    // 手指离开暂停触碰
                                    canContinue = false;
                                    // 绘制完成间隔一段时间才可以触碰
                                    timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            eventX = eventY = 0;
                                            for (int i = 0; i < 9; i++) {
                                                cycles[i].setOnTouch(false);
                                            }
                                            linedCycles.clear();
                                            linePath.reset();
                                            canContinue = true;
                                            postInvalidate();// 在非ui线程刷新界面
                                        }
                                    }, 500);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    break;
            }
            invalidate();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int cycleSize = cycles.length;
        for (int i = 0; i < cycleSize; i++) {
            // 画完并且错误
            if (!canContinue && !result) {
                if (cycles[i].isOnTouch()) {
                    drawOutCycle(cycles[i], canvas, TOUCH_COLOR);
                    drawInnerCycle(cycles[i], canvas, ERROE_COLOR);
                    drawOutsideCycle(cycles[i], canvas, ERROE_COLOR);
                } else {
                    drawOutsideCycle(cycles[i], canvas, NORMAL_COLOR);
                }
            } else
            // 绘画中
            {
                if (cycles[i].isOnTouch())// 圆形被选中，重新绘制内外圆
                {
                    drawOutCycle(cycles[i], canvas, TOUCH_COLOR);
                    drawInnerCycle(cycles[i], canvas, SELECT_TOUCH_COLOR);
                    drawOutsideCycle(cycles[i], canvas, SELECT_TOUCH_COLOR);
                } else
                // 未被选中
                {
                    drawOutsideCycle(cycles[i], canvas, NORMAL_COLOR);
                }
            }
        }
        if (!canContinue && !result)// 不可触碰 并且 结果错误
        {
            drawLine(canvas, ERROE_COLOR);
        } else
        // 可触碰 或者 结果正确
        {
            drawLine(canvas, SELECT_TOUCH_COLOR);
        }
    }

    /**
     * 画空心外部圆
     */
    private void drawOutsideCycle(LockCircle lockCircle, Canvas canvas, int color) {
        paintNormal.setColor(color);
        canvas.drawCircle(lockCircle.getOx(), lockCircle.getOy(), lockCircle.getR(), paintNormal);
    }

    /**
     * 画横线
     */
    private void drawLine(Canvas canvas, int color) {
        // 构建路径
        linePath.reset();
        if (linedCycles.size() > 0) {
            int size = linedCycles.size();
            for (int i = 0; i < size; i++) {
                int index = linedCycles.get(i);
                float x = cycles[index].getOx();
                float y = cycles[index].getOy();
                if (i == 0) {
                    linePath.moveTo(x, y);
                } else {
                    linePath.lineTo(x, y);
                }
            }
            if (canContinue) {
                linePath.lineTo(eventX, eventY);
            } else {
                linePath.lineTo(cycles[linedCycles.get(linedCycles.size() - 1)].getOx(),
                        cycles[linedCycles.get(linedCycles.size() - 1)].getOy());
            }
            paintLines.setColor(color);
            canvas.drawPath(linePath, paintLines);
        }
    }

    /**
     * 画实心内部圆
     */
    private void drawInnerCycle(LockCircle myCycle, Canvas canvas, int color) {
        paintInnerCycle.setColor(color);
        canvas.drawCircle(myCycle.getOx(), myCycle.getOy(), myCycle.getR() / 3f, paintInnerCycle);
    }

    /**
     * 画实心外部圆
     */
    private void drawOutCycle(LockCircle myCycle, Canvas canvas, int color) {
        paintOutCycle.setColor(color);
        canvas.drawCircle(myCycle.getOx(), myCycle.getOy(), myCycle.getR(), paintOutCycle);
    }

    /**
     * <每个圆点类> <功能详细描述>
     *
     * @author 李令 NJ003345
     * @version [版本号, 2015-10-16]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    class LockCircle {
        /**
         * 圆心横坐标
         */
        private float ox;
        /**
         * 圆心纵坐标
         */
        private float oy;
        /**
         * 半径长度
         */
        private float r;
        /**
         * 代表数值
         */
        private Integer num;
        /**
         * 是否选择:false=未选中
         */
        private boolean onTouch;

        public float getOx() {
            return ox;
        }

        public void setOx(float ox) {
            this.ox = ox;
        }

        public float getOy() {
            return oy;
        }

        public void setOy(float oy) {
            this.oy = oy;
        }

        public void setOy(int oy) {
            this.oy = oy;
        }

        public float getR() {
            return r;
        }

        public void setR(float r) {
            this.r = r;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }

        public boolean isOnTouch() {
            return onTouch;
        }

        public void setOnTouch(boolean onTouch) {
            this.onTouch = onTouch;
        }

        /**
         * 判读传入位置是否在圆心内部
         */
        public boolean isPointIn(int x, int y) {
            double distance = Math.sqrt((x - ox) * (x - ox) + (y - oy) * (y - oy));
            return distance < r;
        }
    }

    /**
     * <状态标示> <标示当前进行到何种状态>
     *
     * @author 李令 NJ003345
     * @version [版本号, 2015-10-16]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public interface GesturePwdStatus {
        /**
         * 初始状态 记录手势
         **/
        public final int INITIAL = 1001;
        /**
         * 二次验证状态 与临时存储手势对比
         **/
        public final int VALIDATE_AGAIN = 1002;
        /**
         * 验证状态 与成功存储手势进行对比返回结果
         **/
        public final int VALIDATE = 1003;
        /**
         * 验证成功
         **/
        public final int VALIDATE_SUCCESS = 1004;
        /**
         * 验证失败
         **/
        public final int VALIDATE_ERROR = 1005;
        /**
         * 连接点数不够
         **/
        public final int NUMBER_ERROR = 1006;
        /**
         * 二次验证失败
         **/
        public final int VALIDATE_AGAIN_ERROR = 1007;
        /**
         * 二次验证成功
         **/
        public final int VALIDATE_AGAIN_SUCCESS = 1008;
    }
}
