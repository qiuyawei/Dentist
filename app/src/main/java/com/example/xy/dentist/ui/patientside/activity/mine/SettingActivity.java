package com.example.xy.dentist.ui.patientside.activity.mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.ui.login.AboutUsActivity;
import com.example.xy.dentist.ui.login.ForgetPassWordActivity;
import com.example.xy.dentist.ui.login.LoginActivity;
import com.example.xy.dentist.ui.login.MessageActivity;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.utils.SharedPreferencesUtil;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.baseapp.AppManager;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class SettingActivity extends BaseActivity {


    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.ll_feed)
    LinearLayout mLlFeed;
    @Bind(R.id.ll_update_pass)
    LinearLayout mLlUpdatePass;
    @Bind(R.id.tv_exsice)
    TextView mTvExsice;
    @Bind(R.id.iv_messageDot)
    ImageView iv_messageDot;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mNtb.setTitleText("设置");
        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);

        if (SharedPreferencesUtil.getBooleanData(mActivity, "MessageIfHasRedDot", false)) {
            iv_messageDot.setVisibility(View.VISIBLE);
        }else {
            iv_messageDot.setVisibility(View.GONE);

        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mNtb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick({R.id.ll_feed, R.id.ll_update_pass, R.id.tv_exsice, R.id.ll_about_us, R.id.ll_message,R.id.ll_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_feed:
                intent = new Intent(mActivity, FeedBackActivity.class);
                intent.putExtra("title", "意见反馈");
                intent.putExtra("hint", "请输入您要反馈的内容");
                intent.putExtra("content", "");
                startActivity(intent);
                break;
            case R.id.ll_update_pass:

                intent = new Intent(mActivity, ForgetPassWordActivity.class);
                intent.putExtra("title", "修改密码");
                startActivity(intent);
                break;
            case R.id.tv_exsice:
                setAlisa();
                //退出 红点全部清除
                SharedPreferencesUtil.saveBooleanData(mContext, "SetIfHasRedDot", false);
                SharedPreferencesUtil.saveBooleanData(mContext, "MessageIfHasRedDot", false);

                SharedPreferencesUtil.saveBooleanData(mContext, "ifHasSetAlisa", false);
                GlobalParams.savaFirstIn(false);
                AppManager.getAppManager().AppExit(this, true);
                intent = new Intent(mActivity, LoginActivity.class);
                intent.putExtra("title", "登录");
                startActivity(intent);
                break;
            case R.id.ll_about_us:
                startActivity(new Intent(SettingActivity.this, AboutUsActivity.class));
                break;
            case R.id.ll_message:
                iv_messageDot.setVisibility(View.INVISIBLE);
                SharedPreferencesUtil.saveBooleanData(mContext, "SetIfHasRedDot", false);
                SharedPreferencesUtil.saveBooleanData(mContext, "MessageIfHasRedDot", false);

                startActivity(new Intent(SettingActivity.this, MessageActivity.class));
                break;
            case R.id.ll_share:
//                分享

                showShare();
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("Has_New_Message");
        filter.addAction("Open_Notification");
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "Has_New_Message":
                    iv_messageDot.setVisibility(View.VISIBLE);
                    break;
                case "Open_Notification":
                    iv_messageDot.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };




    private void showShare() {
     OnekeyShare oks = new OnekeyShare();
     //关闭sso授权
     oks.disableSSOWhenAuthorize();
    // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
     //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
     // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
     oks.setTitle(getString(R.string.app_name));
     // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
     oks.setTitleUrl(ApiConstants.SERVER_URL+"share.html");
     // text是分享文本，所有平台都需要这个字段
     oks.setText("");
     // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
     oks.setImagePath(Environment.getExternalStorageDirectory()+"/yayiicon" + ".jpg");//确保SDcard下面存在此张图片
     // url仅在微信（包括好友和朋友圈）中使用
     oks.setUrl(ApiConstants.SERVER_URL+"share.html");
     // comment是我对这条分享的评论，仅在人人网和QQ空间使用
     oks.setComment("");
     // site是分享此内容的网站名称，仅在QQ空间使用
     oks.setSite(getString(R.string.app_name));
     // siteUrl是分享此内容的网站地址，仅在QQ空间使用
     oks.setSiteUrl(ApiConstants.SERVER_URL+"share.html");
    // 启动分享GUI
    oks.show(this);
    }


    private void setAlisa() {
        String alisa = "";
        boolean isdoc = SharedPreferencesUtil.getBooleanData(mContext, "isdoc", false);
//        String user_id = GlobalParams.getUserId();
//        com.example.xy.dentist.utils.LogUtils.print("Patinuser_id", user_id);
//        com.example.xy.dentist.utils.LogUtils.print("Pati_token", GlobalParams.getuser_token());

        if (isdoc) {
            alisa = "doctor_" + "";
        } else {
            alisa = "user_" + "";
        }
        //设置过了不再设置

        JPushInterface.setAlias(mContext, alisa, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                if (i == 0) {
                    com.example.xy.dentist.utils.LogUtils.print("Settt别名", "设置成功！");
//                    SharedPreferencesUtil.saveBooleanData(mContext, "ifHasSetAlisa", true);
                }
                LogUtils.print("i=set=",i+"");
            }
        });



    }

}
