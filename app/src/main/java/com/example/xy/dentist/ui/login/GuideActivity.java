package com.example.xy.dentist.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;


import com.example.xy.dentist.R;
import com.example.xy.dentist.adapter.ViewPagerAdapter;
import com.example.xy.dentist.utils.SharedPreferencesUtil;
import com.jaydenxiao.common.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 开发者：Zcq
 * 内容：引导界面
 * Created by Administrator on 2015/11/20.
 */
public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    // 底部小点图片
    private ImageView[] dots;
    // 记录当前选中位置
    private int currentIndex;



    @Override
    public int getLayoutId() {
        return R.layout.welcome_guide;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        // 初始化页面
        initViews();
        SharedPreferencesUtil.saveBooleanData(getApplicationContext(),"ifIsFirstInstall",false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }


    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);

        views = new ArrayList<View>();
        // 初始化引导图片列表
        View view1=inflater.inflate(R.layout.welcome_one, null);
        View view2=inflater.inflate(R.layout.welcome_two, null);
        View view3=inflater.inflate(R.layout.welcome_three, null);
        View view4=inflater.inflate(R.layout.welcome_four, null);

//        ImageView iv1= (ImageView) view1.findViewById(R.id.iv1);
//        ImageView iv2= (ImageView) view2.findViewById(R.id.iv2);
//        ImageView iv3= (ImageView) view3.findViewById(R.id.iv3);
//        ImageView iv4= (ImageView) view4.findViewById(R.id.iv4);
        scaleImage(GuideActivity.this,view1,R.mipmap.indao_1);
        scaleImage(GuideActivity.this,view2,R.mipmap.indao_2);
        scaleImage(GuideActivity.this,view3,R.mipmap.indao_3);
        scaleImage(GuideActivity.this,view4,R.mipmap.indao_4);
        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转
                intent = new Intent(mActivity, LoginActivity.class);
                intent.putExtra("title", "登录");
                startActivity(intent);
                finish();
            }
        });

        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
//        views.add(inflater.inflate(R.layout.welcome_five, null));

        //  views.add(inflater.inflate(R.layout.w, null));
      //  views.add(inflater.inflate(R.layout.welcome_four, null));
        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(views, this);
        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);
    }
    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1
                || currentIndex == position) {
            return;
        }

        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = position;
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    // 当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状状态
        //setCurrentDot(arg0);
    }

    public  void scaleImage(final Activity activity, final View view, int drawableResId) {
  
        // 获取屏幕的高宽  
        Point outSize = new Point();
        activity.getWindow().getWindowManager().getDefaultDisplay().getSize(outSize);  
  
        // 解析将要被处理的图片  
        Bitmap resourceBitmap = BitmapFactory.decodeResource(activity.getResources(), drawableResId);
  
        if (resourceBitmap == null) {  
            return;  
        }  
  
        // 开始对图片进行拉伸或者缩放  
  
        // 使用图片的缩放比例计算将要放大的图片的高度  
        int bitmapScaledHeight = Math.round(resourceBitmap.getHeight() * outSize.x * 1.0f / resourceBitmap.getWidth());  
  
        // 以屏幕的宽度为基准，如果图片的宽度比屏幕宽，则等比缩小，如果窄，则放大  
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(resourceBitmap, outSize.x, bitmapScaledHeight, false);  
          
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override  
            public boolean onPreDraw() {  
                //这里防止图像的重复创建，避免申请不必要的内存空间  
                if (scaledBitmap.isRecycled())  
                    //必须返回true  
                    return true;  
  
  
                // 当UI绘制完毕，我们对图片进行处理  
                int viewHeight = view.getMeasuredHeight();  
  
  
                // 计算将要裁剪的图片的顶部以及底部的偏移量  
                int offset = (scaledBitmap.getHeight() - viewHeight) / 2;  
  
  
                // 对图片以中心进行裁剪，裁剪出的图片就是非常适合做引导页的图片了  
                Bitmap finallyBitmap = Bitmap.createBitmap(scaledBitmap, 0, offset, scaledBitmap.getWidth(),  
                                                scaledBitmap.getHeight() - offset * 2);  
  
  
                if (!finallyBitmap.equals(scaledBitmap)) {//如果返回的不是原图，则对原图进行回收  
                    scaledBitmap.recycle();  
                    System.gc();  
                }  
  
  
                // 设置图片显示  
                view.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), finallyBitmap));
                return true;  
            }  
        });  
    }  
}
