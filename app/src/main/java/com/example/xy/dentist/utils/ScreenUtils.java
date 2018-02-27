package com.example.xy.dentist.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

/**
 * Created by lenovo on 2018/1/25.
 */

public class ScreenUtils {
    public static boolean screenIsLock(Context activity){
        PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
        KeyguardManager mKeyguardManager = (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);
//      如果flag为true，表示有两种状态：a、屏幕是黑的 b、目前正处于解锁状态 。
//      如果flag为false，表示目前未锁屏
        boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();
        if(flag&&!isScreenOn){
            return true;
        }else
            return false;

    }


}
