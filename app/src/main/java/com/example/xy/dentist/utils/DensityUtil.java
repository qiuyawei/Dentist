package com.example.xy.dentist.utils;

import android.content.Context;

/**
 * Created by Administrator on 2015/12/31.
 */
public class DensityUtil {
    //根据手机的分辨率仿 dp 的单使 转成丿 px(像素)
    public static int  dip2px(Context context, float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 根据手机的分辨率仿 px(像素) 的单使 转成丿 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
