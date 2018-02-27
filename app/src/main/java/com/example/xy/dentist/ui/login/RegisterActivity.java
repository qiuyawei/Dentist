package com.example.xy.dentist.ui.login;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.Userbean;
import com.example.xy.dentist.model.RegisterModel;
import com.example.xy.dentist.presenter.RegisterPresenter;
import com.example.xy.dentist.ui.patientside.activity.PatientActivity;
import com.example.xy.dentist.utils.CommonUtils;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.viewholder.RegisterContract;
import com.google.gson.Gson;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import butterknife.Bind;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity<RegisterPresenter, RegisterModel> implements RegisterContract.View  {
    private String type,openId,mType;

    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.et_code)
    EditText mEtCode;
    @Bind(R.id.tv_getcode)
    TextView mTvGetcode;
    @Bind(R.id.et_passw)
    EditText mEtPassw;
    @Bind(R.id.iv_check)
    ImageView mIvCheck;
    @Bind(R.id.tv_xiy)
    TextView mTvXiy;
    @Bind(R.id.tv_login)
    TextView mTvLogin;
    @Bind(R.id.tv_register)
    TextView mTvRegister;
    @Bind(R.id.tv_tips)
    TextView tv_tips;
    private String mPhone,phone_captcha,new_password;
    private int time = 60;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (time > 0) {
                    if(mTvGetcode!=null){
                        mTvGetcode.setText("还剩" + time + "秒");
                        mTvGetcode.setTextColor(getResources().getColor(R.color.white));
                        mTvGetcode.setBackgroundResource(R.mipmap.code);
                        mHandler.sendEmptyMessageDelayed(1, 1000);
                        time--;
                    }

                } else {
                    time = 60;
                    getCoded();
                }
            }

        }
    };
    private boolean flag=false;
    private String title;
    private String terminal="2";//终端1ios,2android,0其他
    private boolean code=false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        title=getIntent().getStringExtra("title");
        type=getIntent().getStringExtra("type");
        openId=getIntent().getStringExtra("code");
        mType=getIntent().getStringExtra("mType");
        Log.i("TAG",type+"");
        Log.i("TAG",openId+"");
        mNtb.setTitleText(title);
        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);
        if(title.equals("注册")){
            tv_tips.setText("我已阅读并同意");
            mIvCheck.setVisibility(View.VISIBLE);
            mTvXiy.setVisibility(View.VISIBLE);
        }else{
            tv_tips.setText("注册后,您的QQ账号和牙医联盟账号都可以登录");
            mIvCheck.setVisibility(View.GONE);
            mTvXiy.setVisibility(View.GONE);
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



    @OnClick({R.id.tv_getcode, R.id.iv_check, R.id.tv_xiy, R.id.tv_login, R.id.tv_register})
    public void onClick(View view) {
        mPhone = mEtPhone.getText().toString().trim();
        phone_captcha=  mEtCode.getText().toString().trim();
        new_password=    mEtPassw.getText().toString().trim();
        switch (view.getId()) {
            case R.id.tv_getcode:
                getCode();
                break;
            case R.id.iv_check:
                if(!flag){
                    flag=true;
                    mIvCheck.setImageResource(R.mipmap.check_un);
                }else{
                    mIvCheck.setImageResource(R.mipmap.check);
                    flag=false;
                }


                break;
            case R.id.tv_xiy:
                WebViewActivity.launch(mActivity, "注册协议", ApiConstants.SERVER_URL+"home/h5?id=1");


                break;
            case R.id.tv_login:
                intent=new Intent(mActivity,LoginActivity.class);
                if(title.equals("注册")) {
                    intent.putExtra("title", "登录");
                }else{
                    intent.putExtra("title", "登录绑定");
                    intent.putExtra("type",type);
                    intent.putExtra("code",openId);
                    intent.putExtra("mType",mType);

                    Log.i("TAG","typeReg="+type);
                    Log.i("TAG","code="+openId);
                    Log.i("TAG","mType="+mType);

                }
                startActivity(intent);
                break;
            case R.id.tv_register:
                if (BaseJudge())
                    return;
                if(!code){
                    ToastUitl.showShort("请获取短信验证码");
                }else if(TextUtils.isEmpty(phone_captcha)) {
                    ToastUitl.showShort("请输入短信验证码");
                }else if(TextUtils.isEmpty(new_password)){
                    ToastUitl.showShort("请输入密码");
                }else if(flag){
                    ToastUitl.showShort("请选择用户协议");
                }
                else{
                    mPresenter.register(new_password, mPhone, phone_captcha, terminal,openId);

                   /* //判断是第三方 注册 还单纯的用户注册
                    switch (mType){
                        case "no":
                            //说明是第三方过来  分两中 1是医生  2  用户

                            break;
                        case "":
                            //为空说明不是第三方
                            mPresenter.register(new_password, mPhone, phone_captcha, terminal);
                            break;

                    }*/
                }

                break;
        }
    }


    private void getCode() {

        if (BaseJudge())
            return;

        getCodeing();
        mHandler.sendEmptyMessage(1);


    }

    private boolean BaseJudge() {
        if (TextUtils.isEmpty(mPhone)) {
            ToastUitl.showShort("请输入手机号");
            return true;
        } else if (!CommonUtils.isMobileNO(mPhone)) {
            ToastUitl.showShort( "请输入正确的手机号");
            return true;
        }
        return false;
    }



    private void getCodeing() {
        mTvGetcode.setEnabled(false);
       mPresenter.getcaptcha(mPhone);

    }

    private void getCoded() {
        mTvGetcode.setEnabled(true);
        mTvGetcode.setBackgroundResource(R.mipmap.get_code);
        mTvGetcode.setText("");
        mTvGetcode.setTextColor(getResources().getColor(R.color.special_color));

    }

    @Override
    public void captcha(BaseResult result) {
        LogUtils.print("REGresult",new Gson().toJson(result));
        if(result.code==200){
            ToastUitl.showShort("验证码获取成功");
            code=true;
//            mEtCode.setText(result.response);
        }
    }

    @Override
    public void UpdateState(BaseResult<Userbean> result) {
        GlobalParams.saveuser_token(result.data.user_token);
        intent=new Intent(mActivity, PatientActivity.class);
        startActivity(intent);




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


}
