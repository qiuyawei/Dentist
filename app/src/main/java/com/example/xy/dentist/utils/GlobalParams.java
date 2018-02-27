package com.example.xy.dentist.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import com.example.xy.dentist.AppApplication;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.utils.RxPermissionsUtils;

/**
 * Created by XY on 2017/9/21.
 */
public class GlobalParams {
    public static final String phone = "trip_phone";
    public static final String password = "trip_password";


    public static void saveuser_token(String user_token) {
        SharedPreferencesUtil.saveStringData(AppApplication.applicationContext.get(), "user_token", user_token);
    }

    public static String getuser_token() {
        String user_token = SharedPreferencesUtil.getStringData(AppApplication.applicationContext.get(), "user_token", "-1");
        return user_token;
    }


    public static void savedoctor_token(String doctor_token) {
        SharedPreferencesUtil.saveStringData(AppApplication.applicationContext.get(), "doctor_token", doctor_token);
    }

    public static String getdoctor_token() {
        String doctor_token = SharedPreferencesUtil.getStringData(AppApplication.applicationContext.get(), "doctor_token", "-1");
        return doctor_token;
    }

    public static void save_userId(String user_id) {
        SharedPreferencesUtil.saveStringData(AppApplication.applicationContext.get(), "userId", user_id);
    }

    public static String getUserId() {
        String userId = SharedPreferencesUtil.getStringData(AppApplication.applicationContext.get(), "userId", "-1");
        return userId;
    }



    public static void savaFirstLogin(boolean flag) {
        SharedPreferencesUtil.saveBooleanData(AppApplication.applicationContext.get(), "isFirstLogin", flag);
    }

    public static void savaFirstIn(boolean flag) {
        SharedPreferencesUtil.saveBooleanData(AppApplication.applicationContext.get(), "isFirstIn", flag);
    }

    public static Boolean getFirstLogin() {
        Boolean userId = SharedPreferencesUtil.getBooleanData(AppApplication.applicationContext.get(), "isFirstLogin", false);
        return userId;
    }

    public static Boolean getFirstIn() {
        Boolean userId = SharedPreferencesUtil.getBooleanData(AppApplication.applicationContext.get(), "isFirstIn", false);
        return userId;
    }


    public static boolean isLogin() {
        String userId = getUserId();
        if (userId.equals("-1")) {
            return false;
        }
        return true;
    }


    public static void callPhone(final Context context, final String phone) {

        RxPermissionsUtils.checkPermission((Activity) context, new RxPermissionsUtils.onBackListener() {
            @Override
            public void listener(Boolean isgree) {
                if (isgree) {
                    CommonUtils.callPhone(context, phone);
                } else {
                    ToastUitl.showShort("没有通话权限");
                }
            }
        }, Manifest.permission.CALL_PHONE);

    }
}
