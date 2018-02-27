package com.example.xy.dentist.ui.doctor.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xy.dentist.AppConstant;
import com.example.xy.dentist.Manifest;
import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.TabEntity;
import com.example.xy.dentist.bean.VersionBean;
import com.example.xy.dentist.bean.eventbus.UpdateWorkState;
import com.example.xy.dentist.contract.VersionContract;
import com.example.xy.dentist.model.VersonModel;
import com.example.xy.dentist.presenter.VersionPresenter;
import com.example.xy.dentist.ui.doctor.fragment.MineMainFragment;
import com.example.xy.dentist.ui.doctor.fragment.RecruitMainFragment;
import com.example.xy.dentist.ui.doctor.fragment.ShopMainFragment;
import com.example.xy.dentist.ui.doctor.fragment.WorkMainFragment;
import com.example.xy.dentist.utils.DownLoadUtils;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.NotificationsUtils;
import com.example.xy.dentist.utils.SharedPreferencesUtil;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.utils.RxPermissionsUtils;
import com.pgyersdk.javabean.AppBean;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.functions.Action1;

import static com.pgyersdk.update.UpdateManagerListener.startDownloadTask;

public class MainActivity extends BaseActivity<VersionPresenter, VersonModel> implements VersionContract.View {

    public long preTime;
    public static final long TWO_SECOND = 2 * 1000;

    @Bind(R.id.tab_layout)
    CommonTabLayout tabLayout;

    private String[] mTitles = {"工作台", "招聘", "商品", "我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.ic_home_normal, R.mipmap.ic_girl_normal, R.mipmap.ic_video_normal, R.mipmap.ic_care_normal};
    private int[] mIconSelectIds = {
            R.mipmap.ic_home_selected, R.mipmap.ic_girl_selected, R.mipmap.ic_video_selected, R.mipmap.ic_care_selected};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private WorkMainFragment workMainFragment;
    private RecruitMainFragment recruitMainFragment;
    private ShopMainFragment shopMainFragment;
    private MineMainFragment mineMainFragment;
    private static int tabLayoutHeight;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

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
        //初始化菜单
        initTab();
    }


    private Timer timer;
    @Override
    protected void initData() {
        com.example.xy.dentist.utils.LogUtils.print("TAG","initData");
        timer=new Timer();
        //定时器 用来设置别名  如果设置失败  再继续设置
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setAlisa();
            }
        },1000,2000);
    }

    @Override
    protected void initListener() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //6.0 以上再判断
        if (!NotificationsUtils.isNotificationEnabled(mActivity)) {
            openNtD();
        }

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
            workMainFragment = (WorkMainFragment) getSupportFragmentManager().findFragmentByTag("workMainFragment");
            recruitMainFragment = (RecruitMainFragment) getSupportFragmentManager().findFragmentByTag("recruitMainFragment");
            shopMainFragment = (ShopMainFragment) getSupportFragmentManager().findFragmentByTag("shopMainFragment");
            mineMainFragment = (MineMainFragment) getSupportFragmentManager().findFragmentByTag("mineMainFragment");
            currentTabPosition = savedInstanceState.getInt(AppConstant.HOME_CURRENT_TAB_POSITION);
        } else {
            workMainFragment = new WorkMainFragment();
            recruitMainFragment = new RecruitMainFragment();
            shopMainFragment = new ShopMainFragment();
            mineMainFragment = new MineMainFragment();

            transaction.add(R.id.fl_body, workMainFragment, "workMainFragment");
            transaction.add(R.id.fl_body, recruitMainFragment, "recruitMainFragment");
            transaction.add(R.id.fl_body, shopMainFragment, "shopMainFragment");
            transaction.add(R.id.fl_body, mineMainFragment, "mineMainFragment");
        }
        transaction.commit();
        SwitchTo(currentTabPosition);
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
                transaction.hide(recruitMainFragment);
                transaction.hide(shopMainFragment);
                transaction.hide(mineMainFragment);
                transaction.show(workMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            //美女
            case 1:
                transaction.hide(workMainFragment);
                transaction.hide(shopMainFragment);
                transaction.hide(mineMainFragment);
                transaction.show(recruitMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            //视频
            case 2:
                transaction.hide(workMainFragment);
                transaction.hide(recruitMainFragment);
                transaction.hide(mineMainFragment);
                transaction.show(shopMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            //关注
            case 3:
                transaction.hide(workMainFragment);
                transaction.hide(recruitMainFragment);
                transaction.hide(shopMainFragment);
                transaction.show(mineMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
        tabLayout.setCurrentTab(position);

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = System.currentTimeMillis();
            //  moveTaskToBack(false);
            if (currentTime - preTime > TWO_SECOND) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                // 更新时间
                preTime = System.currentTimeMillis();

            } else {
                MainActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //奔溃前保存位置
        if (tabLayout != null) {
            outState.putInt(AppConstant.HOME_CURRENT_TAB_POSITION, tabLayout.getCurrentTab());
        }
    }


    private void setAlisa() {
        String alisa = "";

        boolean isdoc = SharedPreferencesUtil.getBooleanData(mContext, "isdoc", false);
        String user_id = GlobalParams.getUserId();
        com.example.xy.dentist.utils.LogUtils.print("DOc_user_id", user_id);
        com.example.xy.dentist.utils.LogUtils.print("DOc_token", GlobalParams.getdoctor_token());

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
                public void gotResult(int i, String s, Set<String> set) {
                    if (i == 0) {
                        com.example.xy.dentist.utils.LogUtils.print("别名", "设置成功！="+ finalAlisa);
                        SharedPreferencesUtil.saveBooleanData(mContext, "ifHasSetAlisa", true);
                    }else {
                        com.example.xy.dentist.utils.LogUtils.print("别名", "设置失敗！"+s);

                    }
                }
            });
        }else {
            timer.cancel();
            com.example.xy.dentist.utils.LogUtils.print("timer.cancel();", "timer.cancel();");

        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        com.example.xy.dentist.utils.LogUtils.print("onRs","onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        int childPos = SharedPreferencesUtil.getIntData(mActivity,"JpushchildPos",0);
        boolean ifIsJpush = SharedPreferencesUtil.getBooleanData(mContext, "Jpush", false);
        Log.i("TAG", "Jpush"+ifIsJpush);
        com.example.xy.dentist.utils.LogUtils.print("cPos",childPos+"");

        if (ifIsJpush) {
            SwitchTo(0);
            if(workMainFragment!=null){
                workMainFragment.setCheck_postion(childPos);
            }else {
                com.example.xy.dentist.utils.LogUtils.print("workMainFragment","NULL");
            }
            SharedPreferencesUtil.saveBooleanData(mContext, "Jpush", false);

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
//        com.example.xy.dentist.utils.LogUtils.print("vercode", versionBean.info.versionCode);

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
//                ActivityCompat.requestPermissions(mActivity,new String[]{android.Manifest.permission.ACCESS_NOTIFICATION_POLICY},REQUEST_CODE);
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
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
        intentFilter.addAction("NEED_CHANGE_PARENT_AND_CHILD_MAIN");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();

        unregisterReceiver(broadcastReceiver);

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "NEED_CHANGE_PARENT_AND_CHILD_MAIN":
                    //改变父下标 和子下标  主要是我的预约
                    int parentIndex = intent.getIntExtra("parentIndex", 0);
                    int childIndex = intent.getIntExtra("childIndex", 0);
                    com.example.xy.dentist.utils.LogUtils.print("parentIndex",parentIndex);
                    com.example.xy.dentist.utils.LogUtils.print("childIndex",childIndex);

                    SwitchTo(parentIndex);
                    tabLayout.setCurrentTab(parentIndex);
                    if (workMainFragment != null)
                        workMainFragment.setCheck_postion(childIndex);
                    break;


            }
        }
    };
}