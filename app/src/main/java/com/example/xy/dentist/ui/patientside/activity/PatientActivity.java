package com.example.xy.dentist.ui.patientside.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xy.dentist.AppConstant;
import com.example.xy.dentist.R;
import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.TabEntity;
import com.example.xy.dentist.bean.VersionBean;
import com.example.xy.dentist.contract.VersionContract;
import com.example.xy.dentist.model.VersonModel;
import com.example.xy.dentist.presenter.VersionPresenter;
import com.example.xy.dentist.ui.doctor.activity.MainActivity;
import com.example.xy.dentist.ui.patientside.fragment.AppointmentMainFragment;
import com.example.xy.dentist.ui.patientside.fragment.QueryMainFragment;
import com.example.xy.dentist.ui.patientside.fragment.UserMainFragment;
import com.example.xy.dentist.utils.DownLoadUtils;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.NotificationsUtils;
import com.example.xy.dentist.utils.SharedPreferencesUtil;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.utils.RxPermissionsUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.functions.Action1;

public class PatientActivity extends BaseActivity<VersionPresenter, VersonModel> implements VersionContract.View {

    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    setAlisa();
                    break;
                default:
            }
        }
    };
    public long preTime;
    public static final long TWO_SECOND = 2 * 1000;
    private int position;

    @Override
    public int getLayoutId() {
        return R.layout.activity_patient;
    }

    @Bind(R.id.tab_layout)
    CommonTabLayout tabLayout;

    private String[] mTitles = {"诊所查询", "我的预约", "我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.ic_sele_normal, R.mipmap.ic_appoint_normal, R.mipmap.ic_pati_normal};
    private int[] mIconSelectIds = {
            R.mipmap.ic_sele_selected, R.mipmap.ic_appoint_selected, R.mipmap.ic_pati_selected};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private QueryMainFragment queryMainFragment;
    private AppointmentMainFragment appointmentMainFragment;
    private UserMainFragment userMainFragment;
    private static int tabLayoutHeight;


    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,
                com.jaydenxiao.common.R.anim.fade_out);
    }


    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);

    }

    @Override
    public void initView() {
        //6.0 以上再判断
        if (!NotificationsUtils.isNotificationEnabled(mActivity)) {
//            ToastUitl.showLong("该app通知权限未开启，您将收不到消息，请到设置里面设置");
            // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
            openNtD();
        }
        //初始化菜单
        initTab();

    }


    @Override
    protected void initData() {
        setAlisa();
    }

    @Override
    public void initListener() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化frament
        initFragment(savedInstanceState);
        tabLayout.measure(0, 0);
        tabLayoutHeight = tabLayout.getMeasuredHeight();
        //监听菜单显示或隐藏
        mRxManager.on(AppConstant.MENU_SHOW_HIDE, new Action1<Boolean>() {

            @Override
            public void call(Boolean hideOrShow) {
                startAnimation(hideOrShow);
            }
        });

    }


    /**
     * 初始化tab
     */
    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntities);
        //点击监听
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                SwitchTo(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    /**
     * 初始化碎片
     */
    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int currentTabPosition = 0;
        if (savedInstanceState != null) {
            queryMainFragment = (QueryMainFragment) getSupportFragmentManager().findFragmentByTag("queryMainFragment");
            appointmentMainFragment = (AppointmentMainFragment) getSupportFragmentManager().findFragmentByTag("appointmentMainFragment");
            userMainFragment = (UserMainFragment) getSupportFragmentManager().findFragmentByTag("userMainFragment");
            currentTabPosition = savedInstanceState.getInt(AppConstant.HOME_CURRENT_TAB_POSITION);
        } else {
            queryMainFragment = new QueryMainFragment();
            appointmentMainFragment = new AppointmentMainFragment();
            userMainFragment = new UserMainFragment();

            transaction.add(R.id.fl_body, queryMainFragment, "queryMainFragment");
            transaction.add(R.id.fl_body, appointmentMainFragment, "appointmentMainFragment");
            transaction.add(R.id.fl_body, userMainFragment, "userMainFragment");
        }
        transaction.commit();
        SwitchTo(currentTabPosition);
        tabLayout.setCurrentTab(currentTabPosition);
    }

    /**
     * 切换
     */
    private void SwitchTo(int position) {
        LogUtils.logd("主页菜单position" + position);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            //首页
            case 0:
                transaction.hide(appointmentMainFragment);
                transaction.hide(userMainFragment);
                transaction.show(queryMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            //美女
            case 1:
                transaction.hide(queryMainFragment);
                transaction.hide(userMainFragment);
                transaction.show(appointmentMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            //视频
            case 2:
                transaction.hide(queryMainFragment);
                transaction.hide(appointmentMainFragment);
                transaction.show(userMainFragment);
                transaction.commitAllowingStateLoss();
                break;

            default:
                break;
        }
    }

    /**
     * 菜单显示隐藏动画
     *
     * @param showOrHide
     */
    private void startAnimation(boolean showOrHide) {
        final ViewGroup.LayoutParams layoutParams = tabLayout.getLayoutParams();
        ValueAnimator valueAnimator;
        ObjectAnimator alpha;
        if (!showOrHide) {
            valueAnimator = ValueAnimator.ofInt(tabLayoutHeight, 0);
            alpha = ObjectAnimator.ofFloat(tabLayout, "alpha", 1, 0);
        } else {
            valueAnimator = ValueAnimator.ofInt(0, tabLayoutHeight);
            alpha = ObjectAnimator.ofFloat(tabLayout, "alpha", 0, 1);
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.height = (int) valueAnimator.getAnimatedValue();
                tabLayout.setLayoutParams(layoutParams);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(valueAnimator, alpha);
        animatorSet.start();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //奔溃前保存位置
        LogUtils.loge("onSaveInstanceState进来了1");
        if (tabLayout != null) {
            LogUtils.loge("onSaveInstanceState进来了2");
            outState.putInt(AppConstant.HOME_CURRENT_TAB_POSITION, tabLayout.getCurrentTab());
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = System.currentTimeMillis();
            //  moveTaskToBack(false);
            if (currentTime - preTime > TWO_SECOND) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                // 更新时间
                preTime = System.currentTimeMillis();

            } else {
                PatientActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void setAlisa() {
        String alisa = "";
        boolean isdoc = SharedPreferencesUtil.getBooleanData(mContext, "isdoc", false);
        String user_id = GlobalParams.getUserId();
        com.example.xy.dentist.utils.LogUtils.print("Patinuser_id", user_id);
        com.example.xy.dentist.utils.LogUtils.print("Pati_token", GlobalParams.getuser_token());
        if (isdoc) {
            alisa = "doctor_" + user_id;
        } else {
            alisa = "user_" + user_id;
        }
        //设置过了不再设置
        if (!SharedPreferencesUtil.getBooleanData(mContext, "ifHasSetAlisa", false)) {
            final String finalAlisa = alisa;
            JPushInterface.setAlias(mContext, alisa, new TagAliasCallback() {
                @Override
                public void gotResult(int code, String s, Set<String> set) {

                    switch (code) {
                        case 0:
                            // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                            com.example.xy.dentist.utils.LogUtils.print("别名", "设置成功！==" + finalAlisa);
                            SharedPreferencesUtil.saveBooleanData(mContext, "ifHasSetAlisa", true);
                            break;
                        case 6002:
                            // 延迟 10 秒来调用 Handler 设置别名
                            com.example.xy.dentist.utils.LogUtils.print("6002", "-==");
                            mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, ""), 1000 * 5);
                            break;
                        default:
                            com.example.xy.dentist.utils.LogUtils.print("JpushErroCode", code + "");

                    }


                }
            });
        }


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        position = intent.getIntExtra("position", 0);
        com.example.xy.dentist.utils.LogUtils.print("position", position);
        boolean ifIsJpush = SharedPreferencesUtil.getBooleanData(mContext, "Jpush", false);

        if (ifIsJpush) {
            int childIndex = intent.getIntExtra("childPos", 0);
            SwitchTo(position);
            tabLayout.setCurrentTab(position);
            if (appointmentMainFragment.hasInnit)
                appointmentMainFragment.refresh(childIndex);
            else {
                appointmentMainFragment.currentPos = childIndex;
                appointmentMainFragment.initData();
            }
            SharedPreferencesUtil.saveBooleanData(mContext, "Jpush", false);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        com.example.xy.dentist.utils.LogUtils.print("isstop", JPushInterface.isPushStopped(mActivity));
        if (JPushInterface.isPushStopped(mActivity)) {
            JPushInterface.resumePush(mActivity);
        }

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

    @Override
    public void setVersion(VersionBean versionBean) {

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
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NEED_CHANGE_PARENT_AND_CHILD");
        registerReceiver(broadcastReceiver, intentFilter);


        //获取省市区
        getAllAreas();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "NEED_CHANGE_PARENT_AND_CHILD":
                    //改变父下标 和子下标  主要是我的预约
                    int parentIndex = intent.getIntExtra("parentIndex", 1);
                    int childIndex = intent.getIntExtra("childIndex", 0);
                    SwitchTo(parentIndex);
                    tabLayout.setCurrentTab(parentIndex);
                    if (appointmentMainFragment.hasInnit)
                        appointmentMainFragment.refresh(childIndex);
                    else {
                        appointmentMainFragment.currentPos = childIndex;
                        appointmentMainFragment.initData();
                    }
                    break;


            }
        }
    };

    //获取省份
    private void getAllAreas() {
        OkHttpClient client = new OkHttpClient(); //创建一个
        RequestBody body = new FormBody.Builder().add("type","Province").build();
        Request request = new Request.Builder().post(body).url(ApiConstants.SERVER_URL+"home/area2").build();
        //通过client发起请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                }
            }
        });


    }
}
