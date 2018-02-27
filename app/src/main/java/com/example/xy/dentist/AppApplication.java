package com.example.xy.dentist;

import android.*;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.xy.dentist.utils.PicUtil;
import com.jaydenxiao.common.baseapp.BaseApplication;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.utils.RxPermissionsUtils;
import com.mob.MobSDK;
import com.orhanobut.logger.Logger;
import com.pgyersdk.crash.PgyCrashManager;

import java.lang.ref.SoftReference;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by XY on 2017/10/10.
 */
public class AppApplication extends BaseApplication implements AMapLocationListener {
    public static SoftReference<Context> applicationContext;
    private static Application context;
    private String mToken;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    public static String longitude, latitude, address;
    private WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化logger
        context = this;
        LogUtils.logInit(BuildConfig.DEBUG);
        applicationContext = new SoftReference(this.getApplicationContext());
        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);
        //初始化定位
        initBDLocation();
//        异常捕捉  并上传到蒲公英   可到平台查看
        PgyCrashManager.register(this);
        MobSDK.init(this, "2259bc7acd4e4", "992767c7cbef33d1a778c090724b8a53");

    }


    public WindowManager.LayoutParams getMywmParams(){
        return wmParams;
    }
    private void initBDLocation() {

        mlocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location.getErrorCode() == 0) {
            longitude = String.valueOf(location.getLongitude());//经    度
            latitude = String.valueOf(location.getLatitude());//纬    度
            address = location.getAddress();
            location.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
            location.getCountry();//国家信息
            location.getProvince();//省信息
            location.getCity();//城市信息
            location.getDistrict();//城区信息
            location.getStreet();//街道信息
            location.getStreetNum();//街道门牌号信息
            location.getCityCode();//城市编码
            location.getAdCode();//地区编码
            location.getAoiName();//获取当前定位点的AOI信息
            location.getBuildingId();//获取当前室内定位的建筑物Id
            location.getFloor();//获取当前室内定位的楼层

//            Log.i("TAG","===============定位成功==================");
//
//            Log.i("TAG","===============longitude=================="+longitude);
//            Log.i("TAG","===============latitude=================="+latitude);


        } else {


            Logger.t("定位失败").d("loc is null");
//            Log.i("TAG","errCodeMap="+location.getErrorCode()+"==infor="+location.getErrorInfo());
          /*  RxPermissionsUtils.checkPermission(this, new RxPermissionsUtils.onBackListener() {
                @Override
                public void listener(Boolean isAgree) {
                    if (isAgree) {

                    } else {
                        ToastUitl.showShort("没有拍照权限或没有写入SD 卡权限");
                    }
                }
            }, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }).show();*/
}

}
}