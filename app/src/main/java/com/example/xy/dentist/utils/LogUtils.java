package com.example.xy.dentist.utils;

import android.util.Log;

/**
 * Created by lenovo on 2017/11/23.
 */

public class LogUtils {
    public   static  boolean isLog=true;
    public   static void print(String tag,Object content){
        if(isLog){
            Log.i("TAG",tag+"="+content.toString());
        }
    }
}
