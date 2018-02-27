package com.jaydenxiao.common.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Observer;

/**
 * Created by XY on 2017/9/13.
 */
public class RxPermissionsUtils {


    public  interface onBackListener{
        public void  listener(Boolean type);
    }

    private static  boolean isAgree;


    //正常获取权限
    public  static void  checkPermissionForNormal(Activity context) {
        //判断是否同意此权限
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE   }, 100);
        }
    }

    public  static  void checkPermission(final Activity context, final onBackListener listener,String... params) {

        isAgree=false;
        RxPermissions rxPermissions = new RxPermissions(context);
        rxPermissions.request(params)
                .subscribe(new Observer<Boolean>() {


                    @Override
                    public void onNext(Boolean value) {
                       if (value) {
                           isAgree=true;

                        } else {
                           isAgree=false;


                        }
                        if(listener!=null){
                            listener.listener(isAgree);
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }


                });

    }
}
