package com.example.xy.dentist.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;


import com.example.xy.dentist.R;
import com.example.xy.dentist.ui.login.LoginActivity;

import java.util.List;

/**
 * 作者：Zcq
 * 内容：引导界面适配器
 * Created by Administrator on 2015/11/20.
 */
public class ViewPagerAdapter extends PagerAdapter {
    // 界面列表
    private List<View> views;
    private Activity activity;


    public ViewPagerAdapter(List<View> views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }
    // 销毁arg1位置的界面
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(views.get(arg1));
    }

    @Override
    public void finishUpdate(View arg0) {
    }

    // 获得当前界面�??
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    // 初始化arg1位置的界�??
    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(views.get(arg1), 0);
       if (arg1 == views.size() - 1) {
           /* ImageView mStartWeiboImageButton = (ImageView) arg0
                    .findViewById(R.id.iv_start_weibo);*/
          /* arg0.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    goLogin();

                }

            });*/
        }
        return views.get(arg1);
    }

    private void goLogin() {
        // 跳转
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }




    // 判断是否由对象生成界�??
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }
}
