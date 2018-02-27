package com.jaydenxiao.common.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaydenxiao.common.R;
import com.jaydenxiao.common.commonutils.DisplayUtil;


public class NormalTitleBar extends RelativeLayout {
    private static final int DEFAULT_BORDER_COLOR = Color.TRANSPARENT;
    private  int backgroundColor;
    private ImageView ivRight,redDot;
    private TextView ivBack,tvTitle, tvRight;
    private RelativeLayout rlCommonTitle;
    private Context context;

    public NormalTitleBar(Context context) {
        super(context, null);
        this.context = context;
    }

    public NormalTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NormalTitleBarStyle, 0, 0);
        //这里采用的是xml定义+style备用。
        backgroundColor = a.getColor(R.styleable.NormalTitleBarStyle_backgroundColors, DEFAULT_BORDER_COLOR);// 其实这些值是从0开始enum的，就是index;
        this.context = context;
        a.recycle();
        View.inflate(context, R.layout.bar_normal, this);
        ivBack = (TextView) findViewById(R.id.tv_back);
        tvTitle = (TextView) findViewById(R.id.tv_titl);
        tvRight = (TextView) findViewById(R.id.tv_right);
        ivRight = (ImageView) findViewById(R.id.image_right);
        redDot= (ImageView) findViewById(R.id.red_dot);
        rlCommonTitle = (RelativeLayout) findViewById(R.id.common_title);
        rlCommonTitle.setBackgroundColor(backgroundColor);
        //setHeaderHeight();
    }

    public void setHeaderHeight() {
            rlCommonTitle.setPadding(0, DisplayUtil.getStatusBarHeight(context), 0, 0);
            rlCommonTitle.requestLayout();
    }

    /**
     * 管理返回按钮
     */
    public void setBackVisibility(boolean visible) {
        if (visible) {
            ivBack.setVisibility(View.VISIBLE);
        } else {
            ivBack.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题栏左侧字符串
     * @param visiable
     */
    public void setTvLeftVisiable(boolean visiable){
        if (visiable){
            ivBack.setVisibility(View.VISIBLE);
        }else{
            ivBack.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题栏左侧字符串
     * @param tvLeftText
     */
    public void setTvLeft(String tvLeftText){
        ivBack.setText(tvLeftText);
    }


    /**
     * 管理标题
     */
    public void setTitleVisibility(boolean visible) {
        if (visible) {
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
    }

    public void setTitleText(String string) {
        tvTitle.setText(string);
    }

    public void setTitleText(int string) {
        tvTitle.setText(string);
    }

    public void setTitleColor(int color) {
        tvTitle.setTextColor(color);
    }

    /**
     * 右图标
     */
    public void setRightImagVisibility(boolean visible) {
        ivRight.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setRightImagSrc(int id) {
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(id);
    }

    /**
     * 获取右按钮
     * @return
     */
    public View getRightImage() {
       return ivRight;
    }

    /**
     * 左图标
     *
     * @param id
     */
    public void setLeftImagSrc(int id) {
        Drawable drawable= getResources().getDrawable(id);

/// 这一步必须要做,否则不会显示.

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        ivBack.setCompoundDrawables(drawable,null,null,null);
    }
    /**
     * 左文字
     *
     * @param str
     */
    public void setLeftTitle(String str) {
        ivBack.setText(str);
    }

    /**
     * 右标题
     */
    public void setRightTitleVisibility(boolean visible) {
        tvRight.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 右小红点
     */
    public void setRightDotVisibility(boolean visible) {
        redDot.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setRightTitle(String text) {
        tvRight.setText(text);
    }
    public String getRightTitle() {
       return tvRight.getText().toString().trim();
    }
    /*
     * 点击事件
     */
    public void setOnBackListener(OnClickListener listener) {
        ivBack.setOnClickListener(listener);
    }

    public void setOnRightImagListener(OnClickListener listener) {
        ivRight.setOnClickListener(listener);
    }

    public void setOnRightTextListener(OnClickListener listener) {
        tvRight.setOnClickListener(listener);
    }

    /**
     * 标题背景颜色
     *
     * @param color
     */
    public void setBackGroundColor(int color) {
        rlCommonTitle.setBackgroundColor(color);
    }
    public Drawable getBackGroundDrawable() {
        return rlCommonTitle.getBackground();
    }

}
