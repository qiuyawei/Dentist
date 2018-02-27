package com.example.xy.dentist.ui.login;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.Doctorbean;
import com.example.xy.dentist.bean.UserTypeBean;
import com.example.xy.dentist.bean.Userbean;
import com.example.xy.dentist.contract.ForgetContract;
import com.example.xy.dentist.model.ForgetModel;
import com.example.xy.dentist.presenter.ForgetPresenter;
import com.example.xy.dentist.ui.doctor.activity.MainActivity;
import com.example.xy.dentist.ui.patientside.activity.PatientActivity;
import com.example.xy.dentist.utils.CommonUtils;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.google.gson.Gson;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.baseapp.AppManager;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import butterknife.Bind;
import butterknife.OnClick;

public class ForgetPassWordActivity extends BaseActivity<ForgetPresenter, ForgetModel> implements ForgetContract.View  {

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
    @Bind(R.id.et_re_passw)
    EditText mEtRePassw;
    @Bind(R.id.tv_register)
    TextView mTvRegister;

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
    private String title;
    private boolean code=false;
    private String mType;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_pass_word;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        title=  getIntent().getStringExtra("title");
        mNtb.setTitleText(title);
        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);
        if(title.equals("忘记密码")){
            mEtRePassw.setVisibility(View.VISIBLE);
        }else  if(title.equals("修改密码")){
            mEtRePassw.setVisibility(View.GONE);
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



    @OnClick({R.id.tv_getcode, R.id.tv_register})
    public void onClick(View view) {
        mPhone = mEtPhone.getText().toString().trim();
        phone_captcha=  mEtCode.getText().toString().trim();
        new_password=    mEtPassw.getText().toString().trim();

        switch (view.getId()) {
            case R.id.tv_getcode:
                getCode();
                break;
            case R.id.tv_register:
                if (BaseJudge())
                    return;

                if(!code){
                    ToastUitl.showShort("请获取短信验证码");
                }else if(TextUtils.isEmpty(phone_captcha)) {
                    ToastUitl.showShort("请输入短信验证码");
                }else if(TextUtils.isEmpty(new_password)){
                    ToastUitl.showShort("请输入新登录密码");
                }else{

                    mPresenter.checkUserType(mPhone);

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
        LogUtils.print("ForCODEresut",new Gson().toJson(result));
        if(result.code==200){
            ToastUitl.showShort("验证码获取成功");
            code=true;

        }
    }

    @Override
    public void checkUserTypeState(BaseResult<UserTypeBean> result) {
        LogUtils.print("result",new Gson().toJson(result));
        mType = result.data.type;
        if(!TextUtils.isEmpty(mType)){
            switch (mType){
                case "doctor":
                    mPresenter.docupdatePassw(new_password, mPhone, phone_captcha);

                    break;
                case "user":
                    mPresenter.updatePassw(new_password,mPhone,phone_captcha);

                    break;
                case "no":
                    break;

            }

        }
    }

    @Override
    public void UpdateState(BaseResult<Userbean> result) {
      /*  ToastUitl.showShort("密码修改成功，请重新登录！");
       // GlobalParams.saveuser_token(result.data.user_token);
        intent=new Intent(mActivity, PatientActivity.class);
        startActivity(intent);*/
        ToastUitl.showShort("密码修改成功，请重新登录！");
        GlobalParams.savaFirstIn(false);
        AppManager.getAppManager().AppExit(this,true);
        intent =new Intent(mActivity, LoginActivity.class);
        intent.putExtra("title", "登录");
        startActivity(intent);
    }

    @Override
    public void docUpdateState(BaseResult<Doctorbean> baseResult) {
       /* ToastUitl.showShort("密码修改成功，请重新登录！");
       // GlobalParams.saveuser_token(result.data.user_token);
        intent=new Intent(mActivity, MainActivity.class);
        startActivity(intent);*/
        ToastUitl.showShort("密码修改成功，请重新登录！");
        GlobalParams.savaFirstIn(false);
        AppManager.getAppManager().AppExit(this,true);
        intent =new Intent(mActivity, LoginActivity.class);
        intent.putExtra("title", "登录");
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
