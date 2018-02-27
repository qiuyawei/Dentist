package com.example.xy.dentist.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.example.xy.dentist.R;


/**
 * Created by 杨涛 on 2016/11/18.
 * 版权所有 翻版必究
 * 自定义的容器控件
 * 可以实现的功能如下:
 * 1.可以根据指定的宽高比和模式来让子控件动态适应屏幕
 */

public class RatioLayout extends FrameLayout {
    private float mRatio;
    private static final int RATIOMODE_WIDTH = 0;
    private static final int RATIOMODE_HEIGHT = 1;
    private int ratioMode = RATIOMODE_WIDTH;

    public RatioLayout(Context context) {
        this(context, null);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        mRatio = typedArray.getFloat(R.styleable.RatioLayout_ratio, 1);
        ratioMode = typedArray.getInt(R.styleable.RatioLayout_ratioMode, RATIOMODE_WIDTH);
        typedArray.recycle();
    }

    public void setRatio(float ratio) {
        mRatio = ratio;
    }

    public void setRatioMode(int ratioMode) {
        this.ratioMode = ratioMode;
    } /* 根据固定的宽计算高 宽是已经确定的 高需要根据ratio来确定 */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY && ratioMode == RATIOMODE_WIDTH) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            //拿到了控件的宽
             int height = (int) ((width / mRatio) + .5f); setMeasuredDimension(width, height);
             setAndMeasureChilds(width, height); }
             else if (heightMode == MeasureSpec.EXACTLY && ratioMode == RATIOMODE_HEIGHT) {
            //如果高度已经确定的话
             int height = MeasureSpec.getSize(heightMeasureSpec);
             //拿到了控件的宽
             int width = (int) ((height * mRatio) + .5f); setMeasuredDimension(width, height); setAndMeasureChilds(width, height); }
             else { super.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec); } }
             /** * 得到子控件应有的宽和高,然后调用方法测量子控件的宽和高 * @param width * @param height */
             private void setAndMeasureChilds(int width, int height) {
                 int childWidth = width - getPaddingLeft() - getPaddingRight();
                 int childHeight = height - getPaddingTop() - getPaddingBottom();
                 int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
                 int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
                 measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);
             }
 }