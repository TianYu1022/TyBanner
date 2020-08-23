package com.tianyu.banner.banner.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @date:2020/8/10
 * @describe：圆形指示器
 * @author：TianYu
 */
public class CircleIndicator extends View implements Indicator {
    private static final int MAX_COUNT = 10; //最大显示10个圆点

    private Paint mPaint;//画笔

    private int mCurrentIndex;//个数下标
    private int mUnSelectColor;//未选中的颜色
    private int mSelectColor;//选中的颜色

    private int mHeight;//高度
    private int mWidth;//宽度
    private int mRadiu;//半径
    private int mMargin;//边距
    private int mCount;//最终显示的个数
    private int mRealCount;//实际个数

    public CircleIndicator(Context context) {
        super(context);
        initPain();
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPain();
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPain();
    }

    //初始化画笔
    private void initPain() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //去抗锯齿
        mPaint.setStyle(Paint.Style.FILL);//空心圆
        mPaint.setColor(Color.WHITE);//设置画笔为白色
    }

    //计算
    private void calculation() {
        mHeight = mRadiu * 2;//高度 = 半径 * 2
        mCount = Math.min(mRealCount, MAX_COUNT);//最终显示的个数 = 从实际要显示的个数 和 最大限制的个数 中选择最小数
        mWidth = (mCount * mRadiu * 2) + (mCount - 1) * mMargin;
        invalidate(); //刷新页面，重新onMeasure onlayout，ondraw
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //EXACTLY 准确
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mCount; i++) {
            // 第一：cX  = 0 * （2* mRadio） + (0 * mMargin) +  mRadio, cY = mRadio
            // 第二：cx = 1* （2* mRadio） + （1* margin） + mRadio ,   cy = mRadio
            // 第三：cx = 2* (2 * mRadio) + (2 * margin) + mRadio ,   cY = mRadio
            if (i == mCurrentIndex % mCount) {
                mPaint.setColor(mSelectColor);
            } else {
                mPaint.setColor(mUnSelectColor);
            }
            canvas.drawCircle((i * (2 * mRadiu) + (i * mMargin) + mRadiu), mRadiu, mRadiu, mPaint);
        }
    }

    @Override
    public void setRadiu(int radiu) {
        mRadiu = radiu;
    }

    @Override
    public void setCount(int count) {
        if(mRealCount != count){
            mRealCount = count;
            calculation();
        }
    }

    @Override
    public void setCurrent(int index) {
        if(mCurrentIndex != index){
            mCurrentIndex = index;
            invalidate();
        }
    }

    @Override
    public void setIndicatorUnSelectColor(int color) {
        mUnSelectColor = color;
    }

    @Override
    public void setIndicatorSelectColor(int color) {
        mSelectColor = color;
    }

    @Override
    public void setMargin(int margin) {
        mMargin = margin;
    }
}
