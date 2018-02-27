package com.example.xy.dentist.ui.patientside.activity.query;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.ClinicBean;
import com.example.xy.dentist.bean.DoctorDetailbean;
import com.example.xy.dentist.bean.UserInfo;
import com.example.xy.dentist.contract.QueryDetailContract;
import com.example.xy.dentist.model.QueryDetailModel;
import com.example.xy.dentist.presenter.QueryDetailPresenter;
import com.example.xy.dentist.ui.patientside.activity.PatientActivity;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.utils.PopUtil;
import com.example.xy.dentist.utils.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class PhysicianActivity extends BaseActivity<QueryDetailPresenter,QueryDetailModel> implements QueryDetailContract.View{
    private String phone;

    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.iv_person)
    ImageView mIvPerson;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_indro)
    TextView mTvIndro;
    @Bind(R.id.tv_prod)
    TextView mTvProd;
    @Bind(R.id.sv)
    ScrollView mSv;
    @Bind(R.id.tv_login)
    TextView mTvLogin;
    private String title;
    private String id;
    private Drawable mDrawable;
    private String clinic_id;
    private String mToken;

    @Override
    public int getLayoutId() {
        return R.layout.activity_physician;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        title=  getIntent().getStringExtra("title");
        id=getIntent().getStringExtra("id");//医生ID
        clinic_id=getIntent().getStringExtra("clinic_id");//診所id
//        phone=getIntent().getStringExtra("phone");
//        LogUtils.print("clinic_id",clinic_id);
//        LogUtils.print("id",id);
//        LogUtils.print("phone",phone);

//        LogUtils.print("token", SharedPreferencesUtil.getStringData(mContext,));

        mNtb.setTitleText("医生详情");
        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);
        mTvLogin.setText(title);
        if(title.equals("即时预约")){
            mTvLogin.setBackgroundColor(getResources().getColor(R.color.gree));
        }else{
            mTvLogin.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }


    }

    @Override
    protected void initData() {
        mPresenter.doctorInfo(id);
        mToken = GlobalParams.getuser_token();
        ImageLoaderUtils.displayRound(mActivity,mIvPerson, R.mipmap.bna);


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



    @OnClick({R.id.tv_prod, R.id.tv_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_prod:
                break;
            case R.id.tv_login:
                if(title.equals("即时预约"))
                    mPresenter.appointment(title, mToken,clinic_id,id,"1");
                else
                    rightYuyue();
                break;
        }
    }

    @Override
    public void setInfoData(ClinicBean info, List<UserInfo> bean, String message) {

    }
    DoctorDetailbean info;
    @Override
    public void setDicData(DoctorDetailbean info, String message) {
//        Log.i("TAG","============");
//        Gson gson=new Gson();
//        String json=gson.toJson(info);
//        Log.i("TAG","======json======"+json);

        if(info!=null){
//            Log.i("TAG","======NOTnulll======");
//            Log.i("TAG","======phone======"+info.phone);
//            Log.i("TAG","======NOTnulll======");
//
//            LogUtils.print("phone",info.phone);
//            LogUtils.print("name",info.name);
//
            this.info=info;
            ImageLoaderUtils.displayRound(mContext, mIvPerson, ApiConstants.IMAGE_URL + info.avatar);
            mTvName.setText(info.name);


            if(info.status.equals("0")){//状态1忙碌0空闲
                mDrawable = getResources().getDrawable(R.mipmap.free);


            }else{
                mDrawable = getResources().getDrawable(R.mipmap.busy);

            }
            /**这一步必须要做,否则不会显示.*/
            mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());//对图片进行压缩
            /**设置图片位置，四个参数分别方位是左上右下，都设置为null就表示不显示图片*/
            mTvName.setCompoundDrawables(null, null, null, mDrawable);
            mTvIndro.setText(info.introduce);
            mTvProd.setText(info.skill);




        }else {
            Log.i("TAG","======nulll======");

//            LogUtils.print("docBean","nulll");
        }

    }

    @Override
    public void setAppoint(BaseResult result, String message, String status) {
        if(title.equals("即时预约")) {
            PopUtil.showView(mActivity, PopUtil.getView(mActivity, "预约成功", "请在15分钟内到达该诊所", mActivity, new PopUtil.onSelectFinishListener() {
                @Override
                public void onSelectFinish(String type) {
                    if (type.equals("yes")) {
                        intent=new Intent(mActivity, PatientActivity.class);
                        intent.putExtra("position",1);
                        startActivity(intent);

                    }

                }
            }), mActivity.getWindow(), mIvPerson);
        } else {
            if (result.code == 200)
                ToastUitl.showShort("预约成功");
            else
                ToastUitl.showShort(result.message);
        }





        /*else{
            PopUtil.showView(mActivity, PopUtil.getView(mActivity, "即将拨打电话",info.phone, mActivity, new PopUtil.onSelectFinishListener() {
                @Override
                public void onSelectFinish(String type) {
                    if (type.equals("yes")) {

                        GlobalParams.callPhone(mActivity, info.phone);
                    }

                }
            }), mActivity.getWindow(), mIvPerson);
        }*/
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


    /*拨打电话 在线预约*/
    private void rightYuyue(){

        PopUtil.showView(mActivity, PopUtil.getView(mActivity, "即将拨打电话", info.phone, mActivity, new PopUtil.onSelectFinishListener() {
            @Override
            public void onSelectFinish(String type) {
                if (type.equals("yes")) {

                    GlobalParams.callPhone(mActivity, info.phone);
//                                    mPresenter.appointment(bean.status, GlobalParams.getuser_token(), bean.clinic_id, bean.id);
//打完电话预约
                    PopUtil.showView(mActivity, PopUtil.getViewSure(mActivity, "是否确定预约？","", mActivity, new PopUtil.onSelectFinishListener() {
                        @Override
                        public void onSelectFinish(String type) {
                            if (type.equals("yes")) {
                                mPresenter.appointment(title, mToken,clinic_id,id,"2");
                            }

                        }
                    }), mActivity.getWindow(), mIvPerson);

                }

            }
        }), mActivity.getWindow(), mIvPerson);
    }
}
