package com.example.xy.dentist.ui.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.RequestBuilder;
import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.api.ApiService;
import com.example.xy.dentist.bean.VersionBean;
import com.example.xy.dentist.contract.LoginContract;
import com.example.xy.dentist.contract.VersionContract;
import com.example.xy.dentist.model.LoginModel;
import com.example.xy.dentist.model.VersonModel;
import com.example.xy.dentist.presenter.LoginPresenter;
import com.example.xy.dentist.presenter.VersionPresenter;
import com.example.xy.dentist.ui.doctor.activity.MainActivity;
import com.example.xy.dentist.ui.patientside.activity.PatientActivity;
import com.example.xy.dentist.utils.DownLoadUtils;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.utils.SharedPreferencesUtil;
import com.example.xy.dentist.widget.ScreenUtil;
import com.google.gson.Gson;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.xy.dentist.api.ApiConstants.checkVersion;

public class WelcomeActivity extends BaseActivity implements AMapLocationListener {

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean mFirstLogin;
    private boolean ifIsFirstInstall;
    private ProgressDialog mProgressDialog;
    // 创建okHttpClient对象
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.show();
            switch (msg.what) {
                case 1:
                    int progress = msg.arg1;
                    mProgressDialog.setProgress(progress);
//                    Log.i("TAG","pr="+progress);
                    break;
                case 2:
                    mProgressDialog.dismiss();
                    ToastUitl.showShort("文件下载成功");
                    DownLoadUtils.installApk(WelcomeActivity.this, Apkfile);
                    break;
                case 3:
                    mProgressDialog.dismiss();
                    doNext();//下载更新失败
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        ApiConstants.SCREEN_WITH= ScreenUtil.getInstance(mActivity).getScreenWidth();
        LogUtils.print("SCREEN_WITH",ApiConstants.SCREEN_WITH);
        mProgressDialog = new ProgressDialog(WelcomeActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        mProgressDialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        mProgressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        mProgressDialog.setTitle("发现新的版本，正在下载...");
        mProgressDialog.setMax(100);
        getVersion();
    }
    File Apkfile;
    private VersionBean versionBean;
    private void getVersion(){
        LoadingDialog.showDialogForLoading(WelcomeActivity.this,"正在检查更新，请稍等...",false);
        final Request request= new Request.Builder().url(ApiConstants.SERVER_URL+checkVersion).header("user_token","").build();
        OkHttpClient client=new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.print("e",e.getMessage());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadingDialog.cancelDialogForLoading();
                        doNext();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadingDialog.cancelDialogForLoading();
                        try {
                            String result=response.body().string();
                            LogUtils.print("versin",result);
                            try {
                                JSONObject jsonObject=new JSONObject(result);
                                if(jsonObject.getString("code").equals("200")){
                                    JSONObject jsonObject1=jsonObject.getJSONObject("data").getJSONObject("info");
                                    Gson gson=new Gson();
                                    versionBean=gson.fromJson(jsonObject1.toString(),VersionBean.class);
                                    PackageManager pm =getPackageManager();
                                    PackageInfo pi = null;
                                    try {
                                        pi = pm.getPackageInfo(getPackageName(), 0);
                                        int versioncode = pi.versionCode;

                                        int serverCode=Integer.parseInt(versionBean.versionCode);
                                        LogUtils.print("vebean",new Gson().toJson(versionBean));
                                        if (versioncode <serverCode ) {
                                            //当前版本小于服务器版本  更新
                                            show(ApiConstants.SERVER_URL + versionBean.downloadUrl);
                                        }else {
                                            doNext();
                                        }


                                    } catch (PackageManager.NameNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    doNext();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

    }

    public void show(final String dowloadUrl) {

        final Dialog dialog = new Dialog(WelcomeActivity.this, R.style.ActionSheetDialogStyle);
        final LayoutInflater inflater = LayoutInflater.from(WelcomeActivity.this);
        View viewcall = inflater.inflate(R.layout.um_dialog, null, true);

        TextView tv_content = (TextView) viewcall.findViewById(R.id.tv_content);
        Button bt_sure = (Button) viewcall.findViewById(R.id.bt_sure);
        Button bt_cancell = (Button) viewcall.findViewById(R.id.bt_cancell);

        tv_content.setText("发现新的版本，是否更新？");

//            更新
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                dowLoad();
                dowLoad(dowloadUrl);
            }
        });
        bt_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                doNext();
            }
        });
        dialog.setContentView(viewcall);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

    }


    private void dowLoad(String url) {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        doNext();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                com.example.xy.dentist.utils.LogUtils.print("leng", response.headers().get("Accept-Length"));
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                try {
                    is = response.body().byteStream();

                    long total = Long.parseLong(response.headers().get("Accept-Length"));
                    Apkfile = new File(SDPath, "denlist.apk");
                    fos = new FileOutputStream(Apkfile);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.d("h_bl", "progress=" + progress);
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        msg.arg1 = progress;
                        mHandler.sendMessage(msg);

                    }
                    fos.flush();
                    Log.d("h_bl", "文件下载成功");
                    Message msg = mHandler.obtainMessage();
                    msg.what = 2;
                    mHandler.sendMessage(msg);

                } catch (Exception e) {
                    Log.d("h_bl", "文件下载失败");
                    Message msg = mHandler.obtainMessage();
                    msg.what = 3;
                    mHandler.sendMessage(msg);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                }
            }


        });
    }


    private void doNext(){
        mFirstLogin = GlobalParams.getFirstIn();
        //默认是第一次安装 默认true  到了引导页 改为false
        ifIsFirstInstall=SharedPreferencesUtil.getBooleanData(getApplicationContext(),"ifIsFirstInstall",true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //第一层 是否是第一次安装
                if(ifIsFirstInstall){
                    //第一次安装 进入引导页
                    startActivity(new Intent(getApplicationContext(),GuideActivity.class));
                    finish();
                } else {
                    //不是第一次  判断是否登录过
                    if (mFirstLogin) {
                        boolean isdoc = SharedPreferencesUtil.getBooleanData(mActivity, "isdoc", false);
                        if (isdoc) {
                            intent = new Intent(mActivity, MainActivity.class);
                            startActivity(intent);
                        } else {
                            intent = new Intent(mActivity, PatientActivity.class);
                            startActivity(intent);
                        }

                        finish();

                    } else {
                        intent = new Intent(mActivity, LoginActivity.class);
                        intent.putExtra("title", "登录");
                        startActivity(intent);
                        finish();
                    }
                }

            }
        }, 1000);
    }

    @Override
    protected void initData() {


    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        //已进入  APP 则开始定位
        initBDLocation();
    }
    private void initBDLocation() {

        mlocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        mLocationOption.setInterval(3000);//3秒定位一次
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，

        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mlocationClient.startLocation();
    }


    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location.getErrorCode() == 0) {
//            longitude = String.valueOf(location.getLongitude());//经    度
//            latitude = String.valueOf(location.getLatitude());//纬    度
//            address = location.getAddress();
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

//            address = location.getCity();
//            保存经纬度
//            Constant.LATITUDE = location.getLatitude() + "";
//            Constant.LONGITUDE = location.getLongitude() + "";
//            Constant.LOCATION_STATE = location.getErrorCode();
//            Constant.LOCATION_MESSAGE=location.getErrorInfo();
//            Constant.MOVE_SPEED=location.getSpeed();
//            LogUtils.print("定位成功：", location.getAddress());
//            LogUtils.print("定位成功：Speed", location.getSpeed()+"");

        } else {
//            LogUtils.print("定位失败：", location.getErrorInfo() + "-" + location.getErrorCode());
//            Constant.LOCATION_STATE = location.getErrorCode();
//            Constant.LOCATION_MESSAGE=location.getErrorInfo();


        }


    }
}
