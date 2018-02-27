package com.example.xy.dentist.ui.login;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.Doctorbean;
import com.example.xy.dentist.bean.UserTypeBean;
import com.example.xy.dentist.bean.Userbean;
import com.example.xy.dentist.contract.LoginContract;
import com.example.xy.dentist.model.LoginModel;
import com.example.xy.dentist.presenter.LoginPresenter;
import com.example.xy.dentist.ui.doctor.activity.MainActivity;
import com.example.xy.dentist.ui.patientside.activity.PatientActivity;
import com.example.xy.dentist.utils.CommonUtils;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.NotificationsUtils;
import com.example.xy.dentist.utils.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.jaydenxiao.common.utils.RxPermissionsUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity<LoginPresenter, LoginModel> implements LoginContract.View {
    private long startTime, endTime;

    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.et_passw)
    EditText mEtPassw;
    @Bind(R.id.tv_login)
    TextView mTvLogin;
    @Bind(R.id.tv_register)
    TextView mTvRegister;
    @Bind(R.id.tv_forgetpass)
    TextView mTvForgetpass;
    @Bind(R.id.tv_qq)
    TextView mTvQq;
    @Bind(R.id.tv_wechat)
    TextView mTvWechat;
    @Bind(R.id.ll_third)
    LinearLayout mLlThird;
    @Bind(R.id.tv_tips)
    TextView tv_tips;
    @Bind(R.id.LL_state)
    LinearLayout mLLState;
    @Bind(R.id.ll_tap)
    LinearLayout mLlTap;
    @Bind(R.id.tv_forpass)
    TextView tv_forpass;
    private String title;
    private Userbean data;
    private String phone, password;
    private String mType;
    private String type = "0", code;


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        code = getIntent().getStringExtra("code");
        mType = getIntent().getStringExtra("mType");
//        Log.i("TAG","type==="+type);
        if (TextUtils.isEmpty(title)) {
            mNtb.setTitleText("登录");
        } else {
            mNtb.setTitleText(title);
        }
        if (!title.equals("登录")) {
            tv_tips.setVisibility(View.VISIBLE);
            mLLState.setVisibility(View.GONE);
            mLlTap.setVisibility(View.GONE);
            mLlThird.setVisibility(View.GONE);
            mTvLogin.setText("确认绑定");
        } else {
            tv_tips.setVisibility(View.GONE);
            mLLState.setVisibility(View.VISIBLE);
            mLlTap.setVisibility(View.VISIBLE);
            mLlThird.setVisibility(View.VISIBLE);
            mTvLogin.setText("登录");
        }


        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);
    }

    @Override
    public void initData() {
        //6.0 以上再判断
        if (!NotificationsUtils.isNotificationEnabled(mActivity)) {
//            ToastUitl.showLong("该app通知权限未开启，您将收不到消息，请到设置里面设置");
            // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
            openNtD();
            com.example.xy.dentist.utils.LogUtils.print("通知未打开", "检测已未打开");

        } else {
            com.example.xy.dentist.utils.LogUtils.print("通知一打开", "检测已经打开了");
        }
    }


    private void openNtD() {
        final Dialog dialog;
        View view = View.inflate(mActivity, R.layout.dialog_common2, null);
        dialog = new Dialog(mActivity, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        TextView tv_dialog_title = (TextView) view.findViewById(R.id.tv_dialog_title);
        TextView tv_cancell = (TextView) view.findViewById(R.id.tv_dialog_cancell);
        TextView tv_sure = (TextView) view.findViewById(R.id.tv_dialog_sure);

//        tv_dialog_title.setText("是否去开启通知");
        tv_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                ToastUitl.showLong("该app通知权限未开启，您将收不到推送消息");
            }
        });

        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
//                ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY},REQUEST_CODE);
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getApplication().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    @Override
    public void initListener() {
        mNtb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @OnClick({R.id.tv_login, R.id.tv_register, R.id.tv_forgetpass, R.id.tv_qq, R.id.tv_wechat})
    public void onClick(View view) {
        phone = mEtPhone.getText().toString().trim();
        password = mEtPassw.getText().toString().trim();
        switch (view.getId()) {
            case R.id.tv_login:
                if (title.contains("登录绑定")) {
                    com.example.xy.dentist.utils.LogUtils.print("mtype", mType);

                    if (!TextUtils.isEmpty(mType)) {
                        switch (mType) {
                            case "doctor":
                                mPresenter.doctorlogin(phone, password, type, code);//type 0默认1QQ2微信  code 第三方标识（当type不为0时必填）
                                break;
                            case "user":
                                mPresenter.bindLogin(phone, password, type, code);
                                break;
                            case "no":
                                mPresenter.bindLogin(phone, password, type, code);
                                break;

                        }

                    }
                } else {
                    //登录
                    if (TextUtils.isEmpty(phone)) {
                        ToastUitl.showShort("请输入手机号");

                    } else if (!CommonUtils.isMobileNO(phone)) {
                        ToastUitl.showShort("手机号格式错误，请重新输入");
                    } else if (TextUtils.isEmpty(password)) {
                        ToastUitl.showShort("请输入密码");
                    } else {

                        mPresenter.checkUserType(phone);

                    }
                }


                break;
            case R.id.tv_register:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("title", "注册");
                startActivity(intent);
                break;
            case R.id.tv_forgetpass:
                intent = new Intent(mActivity, ForgetPassWordActivity.class);
                intent.putExtra("title", "忘记密码");
                startActivity(intent);
                break;
            case R.id.tv_qq:
                type = "1";
                authorQQ();
                break;
            case R.id.tv_wechat:
                type = "2";
                authorWx();
                break;
        }
    }


    @Override
    public void loginState(BaseResult<Userbean> result) {
        GlobalParams.savaFirstIn(true);
        GlobalParams.saveuser_token(result.data.user_token);
        GlobalParams.save_userId(result.data.id);

        //用户角色的判断
        //  mPresenter.checkUserType(phone);
        SharedPreferencesUtil.saveBooleanData(mActivity, "isdoc", false);
        intent = new Intent(LoginActivity.this, PatientActivity.class);
        startActivity(intent);
    }

    @Override
    public void checkUserTypeState(BaseResult<UserTypeBean> result) {
        mType = result.data.type;
        if (!TextUtils.isEmpty(mType)) {
            switch (mType) {
                case "doctor":
                    mPresenter.doctorlogin(phone, password, type, code);//type 0默认1QQ2微信  code 第三方标识（当type不为0时必填）
                    break;
                case "user":
                    Log.i("TAG", title);
                    if (title.contains("登录绑定")) {
                        mPresenter.bindLogin(phone, password, type, code);

                    } else
                        mPresenter.bindLogin(phone, password, type, code);
                    ;

                    break;
                case "no":
                    break;

            }

        }


    }

    @Override
    public void doctorloginState(BaseResult<Doctorbean> result) {
        GlobalParams.savaFirstIn(true);
        GlobalParams.savedoctor_token(result.data.doctor_token);
        GlobalParams.save_userId(result.data.id);
        //存储医生是否忙路
//        SharedPreferencesUtil.saveStringData(mActivity,"doctor_status",result.data.status);
        SharedPreferencesUtil.saveBooleanData(mActivity, "isdoc", true);
        intent = new Intent(LoginActivity.this, MainActivity.class);

        startActivity(intent);
//        Log.i("TAG","doc_token="+GlobalParams.getdoctor_token());
    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }


    /**
     * qq 第三方授权获取 id
     */
    private void authorQQ() {
//        LoadingDialog.showDialogForLoading(this);
        final Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.SSOSetting(false);//设置false表示使用SSO授权方式
//        qq.authorize();//单独授权,OnComplete返回的hashmap是空的  qq.authorize()和 qq.showUser(null) 只能调用一个
        qq.showUser(null);//授权并获取用户信息
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.i("TAG", "onComplete=" + i);
                if (i == Platform.ACTION_USER_INFOR) {
                    PlatformDb platDB = platform.getDb();//获取数平台数据DB
                    code = platDB.getUserId();

//                    LogUtils.print("openId", openId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPresenter.checkUserType2(code, type);
                        }
                    });
                }

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.i("TAG", "QQonError=" + throwable.getMessage());

            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.i("TAG", "QQonCancel=" + i);

            }
        });
    }

    /**
     * wx第三方授权获取 id
     */
    private void authorWx() {
//        LoadingDialog.showDialogForLoading(this);
//        startTime= SystemClock.currentThreadTimeMillis();
        Log.i("TAG", "authorWx");
        final Platform platform = ShareSDK.getPlatform(Wechat.NAME);

        if (platform != null) {
            platform.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(final Platform platform, final int i, HashMap<String, Object> hashMap) {
                    Log.i("TAG", platform.getDb().getUserId());
                    Log.i("TAG", platform.getDb().getToken());
                    Log.i("TAG", platform.getDb().exportData());

                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           if (i == Platform.ACTION_USER_INFOR) {
                               PlatformDb platDB = platform.getDb();//获取数平台数据DB
                               code = platDB.getUserId();
                               mPresenter.checkUserType2(code, type);
                           }
                       }
                   });


                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    Log.i("TAG", throwable + "");
                    throwable.printStackTrace();
                    if (throwable.toString().contains("WechatClientNotExistException")) {
                        ToastUitl.showShort("您还没有安装微信，请安装后再试！");
                    }

                }

                @Override
                public void onCancel(Platform platform, int i) {

                }
            });
     /*       if (platform.isAuthValid()) {
                platform.removeAccount(true);
                Log.i("TAG","platform.isAuthValid="+platform.isAuthValid());
                return;
            }*/

            platform.SSOSetting(false);//设置false表示使用SSO授权方式
//          wx.authorize();//单独授权,OnComplete返回的hashmap是空的  qq.authorize()和 qq.showUser(null) 只能调用一个

            platform.showUser(null);//授权并获取用户信息

        }else {
            Log.i("TAG", "platForm=null");

        }
    }

    //用户绑定 第三方账号
    @Override
    public void bindLoginState(BaseResult<Userbean> result) {
        Log.i("TAG", new Gson().toJson(result));
        GlobalParams.savaFirstIn(true);
        GlobalParams.saveuser_token(result.data.user_token);
        GlobalParams.save_userId(result.data.id);

        //用户角色的判断
        //  mPresenter.checkUserType(phone);
        SharedPreferencesUtil.saveBooleanData(mActivity, "isdoc", false);
        intent = new Intent(LoginActivity.this, PatientActivity.class);
        startActivity(intent);
    }

    @Override
    public void checkUserTypeState2(BaseResult<UserTypeBean> result) {
        if (result.code != 200) {
            ToastUitl.showShort(result.message);
        }
        if (result.code == 401) {
            //暂未绑定
            intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra("title", "注册绑定");
            intent.putExtra("type", type);
            intent.putExtra("code", code);
            intent.putExtra("mType", "no");
            startActivity(intent);
            return;
        }

        mType = result.data.type;
        if (!TextUtils.isEmpty(mType)) {
            switch (mType) {
                case "doctor":
                    mPresenter.doctorlogin(phone, password, type, code);//type 0默认1QQ2微信  code 第三方标识（当type不为0时必填）
                    break;
                case "user":
                    mPresenter.thirdUserLogin(type, code);
                    break;
                case "no":
                    intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.putExtra("title", "注册绑定");
                    intent.putExtra("type", type);
                    intent.putExtra("code", code);
                    intent.putExtra("mType", mType);

                    startActivity(intent);
                    break;

            }

        }
    }


    @Override
    public void thirdUserLoginState(BaseResult<Userbean> result) {
        GlobalParams.savaFirstIn(true);
        GlobalParams.saveuser_token(result.data.user_token);
        GlobalParams.save_userId(result.data.id);

        //用户角色的判断
        //  mPresenter.checkUserType(phone);
        SharedPreferencesUtil.saveBooleanData(mActivity, "isdoc", false);
        intent = new Intent(LoginActivity.this, PatientActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(LoginActivity.this);
        builder.statusBarDrawable = R.mipmap.logo_small;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS; //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS; // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(1, builder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RxPermissionsUtils.checkPermission(mActivity, new RxPermissionsUtils.onBackListener() {
            @Override
            public void listener(Boolean type) {
                if (type) {

                } else {
                    ToastUitl.showShort("定位失败，请到设置里面打开，然后刷新页面");
                }
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION);
        saveIcon();
//        setAlisa();
    }


    private void saveIcon() {
        RxPermissionsUtils.checkPermission(mActivity, new RxPermissionsUtils.onBackListener() {
            @Override
            public void listener(Boolean type) {
//                Log.i("TAG", String.valueOf(type));
//                Log.i("TAG", String.valueOf(Environment.getExternalStorageDirectory()));

                if (!type) {
//                   ToastUitl.showShort("您拒绝此权限可能影响app部分功能正常使用");
                } else {
                    FileOutputStream out = null;
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo_small);
                    File file = new File(Environment.getExternalStorageDirectory(), "yayiicon" + ".jpg");
                    if (file.exists() && file.length() > 0) {
//                        Log.i("TAG", file.length() + "");

                        return;
                    }
                    try {
                        out = new FileOutputStream(file);
                        boolean b = bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        com.example.xy.dentist.utils.LogUtils.print("save", b);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);


//        Toast.makeText(LoginActivity.this, "保存已经至" + Environment.getExternalStorageDirectory() + "下", Toast.LENGTH_SHORT).show();
    }

}


