package com.example.xy.dentist.ui.patientside.activity.appoint;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.AppointuBean;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.eventbus.UpdateAppointState;
import com.example.xy.dentist.contract.EvaluatContract;
import com.example.xy.dentist.model.EvaluatModel;
import com.example.xy.dentist.presenter.EvaluatPresenter;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.widget.StarBarView;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.OnClick;

public class EvaluateActivity extends BaseActivity<EvaluatPresenter, EvaluatModel> implements EvaluatContract.View {

    @Bind(R.id.et_comment)
    EditText et_comment;
    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.iv_pic)
    ImageView mIvPic;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_add)
    TextView mTvAdd;
    @Bind(R.id.tv_phone)
    TextView mTvPhone;
    @Bind(R.id.tv_appo)
    TextView mTvAppo;
    @Bind(R.id.starBar_recoveryrate)
    StarBarView mStarBarRecoveryrate;
    @Bind(R.id.tv_sure)
    TextView mTvSure;
    private AppointuBean bean;
    private Drawable mDrawable;
    private String star, comment;
    private String mToken;

    @Override
    public int getLayoutId() {
        return R.layout.activity_evaluate;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        mNtb.setTitleText("评价");
        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);
        mTvAppo.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");


        long half = (long) (0.5 * 60 * 60 * 1000);
        mToken = GlobalParams.getuser_token();
        bean = (AppointuBean) getIntent().getSerializableExtra("bean");
        if (bean != null) {
            ImageLoaderUtils.displayRound(mContext, mIvPic, ApiConstants.IMAGE_URL + bean.avatar);
//            mTvTime.setText("预约时间：" + bean.start_time + "~" + bean.end_time);
            if (TextUtils.isEmpty(bean.start_time)) {
                mTvTime.setText("预约时间：" + df3.format(Long.parseLong(bean.create_time) * 1000) + " (" + df1.format(Long.parseLong(bean.create_time) * 1000) + "~" + df1.format(Long.parseLong(bean.create_time) * 1000 + half) + ")");
            } else
//                    mViewHolder.mTvTime.setText("预约时间：" + bean.start_time + "~" + bean.end_time);
                mTvTime.setText("预约时间：" + bean.year + "-" + bean.month + "-" + bean.day + " (" + bean.start_time + "~" + bean.end_time + ")");


            if (bean.status.equals("0")) {//状态1忙碌0空闲
                mDrawable = mContext.getResources().getDrawable(R.mipmap.free);

                //  mViewHolder.mTvAppo.setText("即时预约");
                //  mViewHolder.mTvAppo.setBackgroundResource(R.mipmap.hur_appo);
            } else {
                mDrawable = mContext.getResources().getDrawable(R.mipmap.busy);

                //   mViewHolder.mTvAppo.setText("在线预约");
                //  mViewHolder.mTvAppo.setBackgroundResource(R.mipmap.line_appo);
            }
            /**这一步必须要做,否则不会显示.*/
            mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());//对图片进行压缩
            /**设置图片位置，四个参数分别方位是左上右下，都设置为null就表示不显示图片*/
            mTvTitle.setCompoundDrawables(null, null, mDrawable, null);
            mTvTitle.setText(bean.name);
            mTvAdd.setText(bean.experience);
            mTvPhone.setText("擅长：" + bean.skill);
        }
    }

    @Override
    protected void initListener() {
        mNtb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mStarBarRecoveryrate.setOnStarChangeListener(new StarBarView.OnStarChangeListener() {
            @Override
            public void onStarChange(float mark) {
                System.out.println("----------" + mark);
                star = String.valueOf(mark);

            }
        });
    }


    @OnClick(R.id.tv_sure)
    public void onClick() {
        if (bean != null) {
            comment = et_comment.getText().toString().trim();
            mPresenter.judge(mToken, bean.id, comment, star);
        }
    }

    @Override
    public void judgeState(BaseResult result) {
        Intent intent=new Intent("NEED_CHANGE_PARENT_AND_CHILD");
        intent.putExtra("parentIndex",1);
        intent.putExtra("childIndex",2);

        sendBroadcast(intent);
        ToastUitl.showShort("评价成功");
//        EventBus.getDefault().post(new UpdateAppointState(2));//1进行中2待评价3已完成
        finish();

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
