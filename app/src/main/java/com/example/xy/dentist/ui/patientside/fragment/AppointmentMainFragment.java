package com.example.xy.dentist.ui.patientside.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.eventbus.UpdateAppointState;
import com.example.xy.dentist.bean.eventbus.UpdateWorkState;
import com.example.xy.dentist.utils.LogUtils;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentMainFragment extends BaseFragment {


    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.tv_desc)
    TextView mTvDesc;
    @Bind(R.id.tv_comment)
    TextView mTvComment;
    @Bind(R.id.tv_finish)
    TextView mTvFinish;


    private FragmentManager fragmentManager;
    private BaseFragment classIndexFragment, paperFragment, loveFragment,baseFragment;
    private Intent intent;
    public boolean hasInnit=false;
    public int currentPos=0;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_appointment_main;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mNtb.setTitleText("我的预约");
        mNtb.setRightImagVisibility(true);
        mNtb.setTvLeftVisiable(false);
        mNtb.setRightImagSrc(R.mipmap.map);
        mNtb.setRightTitleVisibility(true);
        mNtb.setRightTitle("福建省");
    }

    @Override
    public void initData() {
        fragmentManager = getChildFragmentManager();
        setTabSelection(currentPos);
        hasInnit=true;
    }


    public void setTabSelection(int position) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (position) {
            case 0:
               baseFragment=AppointmentFragment.newInstance(1);
                break;
            case 1:
                baseFragment=AppointmentFragment.newInstance(2);
                break;
            case 2:
                baseFragment=new AppointmentFinishFragment();
                break;
        }
        // 提交
        transaction.replace(R.id.fl_content, baseFragment);
        transaction.commitAllowingStateLoss();
    }



    @Override
    public void initListener() {
        mNtb.setOnRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


    @OnClick({R.id.tv_desc, R.id.tv_comment, R.id.tv_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_desc:
                mTvDesc.setTextColor(getResources().getColor(R.color.filter));
                mTvComment.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvFinish.setTextColor(getResources().getColor(R.color.desc_defau));
                setTabSelection(0);

                break;
            case R.id.tv_comment:
                mTvComment.setTextColor(getResources().getColor(R.color.filter));
                mTvDesc.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvFinish.setTextColor(getResources().getColor(R.color.desc_defau));
                setTabSelection(1);
                break;
            case R.id.tv_finish:
                mTvFinish.setTextColor(getResources().getColor(R.color.filter));
                mTvDesc.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvComment.setTextColor(getResources().getColor(R.color.desc_defau));
                setTabSelection(2);
                break;
        }
    }

    public void refresh(int postion) {
        switch (postion){
            case 0:
                mTvDesc.setTextColor(getResources().getColor(R.color.filter));
                mTvComment.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvFinish.setTextColor(getResources().getColor(R.color.desc_defau));

                break;

            case 1:
                mTvComment.setTextColor(getResources().getColor(R.color.filter));
                mTvDesc.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvFinish.setTextColor(getResources().getColor(R.color.desc_defau));

                break;

            case 2:
                mTvFinish.setTextColor(getResources().getColor(R.color.filter));
                mTvDesc.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvComment.setTextColor(getResources().getColor(R.color.desc_defau));

                break;
        }

//        EventBus.getDefault().post(new UpdateAppointState(postion+1));//预约单状态，0新预约1待确认2进行中3已完成-1已取消

        setTabSelection(postion);

    }


}
