package com.example.xy.dentist.ui.doctor.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.StateBean;
import com.example.xy.dentist.bean.eventbus.GetWorkState;
import com.example.xy.dentist.bean.eventbus.UpdateWorkState;
import com.example.xy.dentist.contract.WorkMainContract;
import com.example.xy.dentist.model.WorkMainModel;
import com.example.xy.dentist.presenter.WorkMainPresenter;
import com.example.xy.dentist.ui.patientside.fragment.WorkbenchFragment;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.PopUtil;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkMainFragment extends BaseFragment<WorkMainPresenter, WorkMainModel> implements WorkMainContract.View {
    public int check_postion=0;
    public boolean hasInnit=false;
    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.tv_new_appo)
    TextView mTvNewAppo;
    @Bind(R.id.tv_wait_sure)
    TextView mTvWaitSure;
    @Bind(R.id.tv_doing)
    TextView mTvDoing;
    @Bind(R.id.tv_finish)
    TextView mTvFinish;
    @Bind(R.id.tv_cancel)
    TextView mTvCancel;
    @Bind(R.id.fl_content)
    RelativeLayout mFlContent;
    @Bind(R.id.et_content)
    EditText mEtContent;
    @Bind(R.id.iv_search)
    ImageView mIvSearch;
    @Bind(R.id.tv_start_time)
    TextView mTvStartTime;
    @Bind(R.id.tv_end_time)
    TextView mTvEndTime;
    @Bind(R.id.ll_head)
    LinearLayout mLlHead;
    private FragmentManager fragmentManager;
    private WorkbenchFragment workbenchFragment;
    private String search;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_work_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        mNtb.setTitleText("工作台");
        mNtb.setRightImagVisibility(false);
        mNtb.setTvLeftVisiable(false);
        mNtb.setRightTitleVisibility(true);
        fragmentManager = getChildFragmentManager();

    }

    @Override
    public void initData() {
        setTabSelection(check_postion);
        hasInnit=true;
    }

    public void setTabSelection(int position) {
        com.example.xy.dentist.utils.LogUtils.print("setTabSelection",position+"");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        hideFragments(transaction);
        workbenchFragment = WorkbenchFragment.newInstance(position);
        transaction.replace(R.id.fl_content, workbenchFragment);

        // 提交
        transaction.commitAllowingStateLoss();
    }




    @Override
    public void initListener() {
        mNtb.setOnRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNtb.setRightTitle(mNtb.getRightTitle().equals("关闭空闲") ? "开启空闲" : "关闭空闲");


                mPresenter.updateInfo(GlobalParams.getdoctor_token(), mNtb.getRightTitle().equals("关闭空闲") ? "0" : "1");//1忙绿0空闲
            }
        });


    }


    @OnClick({R.id.tv_new_appo, R.id.tv_wait_sure, R.id.tv_doing, R.id.tv_finish, R.id.tv_cancel, R.id.iv_search, R.id.tv_start_time, R.id.tv_end_time})
    public void onClick(View view) {
        search = mEtContent.getText().toString().trim();
        switch (view.getId()) {
            case R.id.tv_new_appo:
                mTvNewAppo.setTextColor(getResources().getColor(R.color.filter));
                mTvWaitSure.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvDoing.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvFinish.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvCancel.setTextColor(getResources().getColor(R.color.desc_defau));
                setTabSelection(0);
                mLlHead.setVisibility(View.GONE);
                //  mNtb.setRightTitle("关闭空闲");
                break;
            case R.id.tv_wait_sure:
                mTvWaitSure.setTextColor(getResources().getColor(R.color.filter));
                mTvNewAppo.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvDoing.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvFinish.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvCancel.setTextColor(getResources().getColor(R.color.desc_defau));
                setTabSelection(1);
                mLlHead.setVisibility(View.GONE);
                // mNtb.setRightTitle("开启空闲");
                break;
            case R.id.tv_doing:
                mTvDoing.setTextColor(getResources().getColor(R.color.filter));
                mTvWaitSure.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvNewAppo.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvFinish.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvCancel.setTextColor(getResources().getColor(R.color.desc_defau));
                setTabSelection(2);
                mLlHead.setVisibility(View.GONE);
                //  mNtb.setRightTitle("开启空闲");
                break;
            case R.id.tv_finish:
                mTvFinish.setTextColor(getResources().getColor(R.color.filter));
                mTvWaitSure.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvDoing.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvNewAppo.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvCancel.setTextColor(getResources().getColor(R.color.desc_defau));
                setTabSelection(3);
                mLlHead.setVisibility(View.VISIBLE);
                // mNtb.setRightTitle("关闭空闲");
                break;
            case R.id.tv_cancel:
                mTvCancel.setTextColor(getResources().getColor(R.color.filter));
                mTvWaitSure.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvDoing.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvFinish.setTextColor(getResources().getColor(R.color.desc_defau));
                mTvNewAppo.setTextColor(getResources().getColor(R.color.desc_defau));
                setTabSelection(4);
                mLlHead.setVisibility(View.GONE);
                // mNtb.setRightTitle("开启空闲");
                break;
            case R.id.iv_search:
                if (workbenchFragment != null) {
                    workbenchFragment.setSearch(search);
                }
                break;
            case R.id.tv_start_time:
                showTime("start_time");
                break;
            case R.id.tv_end_time:
                showTime("end_time");
                break;

        }
    }

    private String startTime = "", endTime = "";

    private void showTime(final String type) {
        PopUtil.showBirthdayPopwindow(getActivity(),
                PopUtil.getDataPick2(getActivity(), new PopUtil.onSelectFinishListener() {
                    @Override
                    public void onSelectFinish(String date) {
                        // mTvYear.setText(date);
                        // loadData();
                        if (workbenchFragment != null) {
                            //先判断时间结束时间是否大于开始时间
                            if (type.contains("start_time")) {
                                //先判断是否选了结束时间
                                if (TextUtils.isEmpty(endTime)) {
                                    workbenchFragment.setTime(type, date);
                                    mTvStartTime.setText(date);
                                    startTime = date;
                                    PopUtil.dialog.dismiss();
                                } else {
                                    //先选择了结束时间 判断开始时间是否合理
                                    if (checkCanSelect(date, endTime)) {
                                        workbenchFragment.setTime(type, date);
                                        mTvStartTime.setText(date);
                                        startTime = date;
                                        PopUtil.dialog.dismiss();

                                    } else {
                                        ToastUitl.showShort("开始时间不能超过结束时间");
                                    }
                                }

                            } else if (type.contains("end_time")) {
                                if (TextUtils.isEmpty(startTime)) {
                                    //空的直接选
                                    workbenchFragment.setTime(type, date);
                                    mTvEndTime.setText(date);
                                    endTime = date;
                                    PopUtil.dialog.dismiss();
                                } else {
                                    //先选择了开始时间 判断开始时间是否合理
                                    if (checkCanSelect(startTime, date)) {
                                        workbenchFragment.setTime(type, date);
                                        mTvEndTime.setText(date);
                                        endTime = date;
                                        PopUtil.dialog.dismiss();

                                    } else {
                                        ToastUitl.showShort("结束时间不能小于开始时间");
                                    }


                                }

                            }
                        }
                    }
                }));
    }


    @Override
    public void onEvent(Object o) {
        super.onEvent(o);
        if (o instanceof GetWorkState) {
            String state = ((GetWorkState) o).state;
            if (mNtb != null) {
                mNtb.setRightTitle(state.equals("1") ? "开启空闲" : "关闭空闲");//1忙绿0空闲
            }


        }
    }

    @Override
    public void updateState(BaseResult<StateBean> result) {


        ToastUitl.showShort(result.data.status.equals("1") ? "关闭成功" : "开启成功");//1忙绿0空闲

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


    private boolean checkCanSelect(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = start;
        String endTime = end.trim();

        try {
            long t1 = sdf.parse(startTime).getTime();
            long t2 = sdf.parse(endTime).getTime();

            if (t2 >= t1) {
                return true;
            } else {
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return true;
    }

   /* @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter("resetxxx");
        getActivity().registerReceiver(mBroadcastReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("reset")){
                int pos=intent.getIntExtra("childIndxxx",0);
                com.example.xy.dentist.utils.LogUtils.print("cpos",pos+"");
                setTabSelection(pos);
            }
        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.xy.dentist.utils.LogUtils.print("WorkMainonCreate","onCreate");

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        com.example.xy.dentist.utils.LogUtils.print("WorkMainoonHiddenChanged","onHiddenChanged="+hidden);

    }*/


   public void setCheck_postion(int postion){
//       initData();


       switch (postion){
           case 0:
               mTvNewAppo.setTextColor(getResources().getColor(R.color.filter));
               mTvWaitSure.setTextColor(getResources().getColor(R.color.desc_defau));
               mTvDoing.setTextColor(getResources().getColor(R.color.desc_defau));
               mTvFinish.setTextColor(getResources().getColor(R.color.desc_defau));
               mTvCancel.setTextColor(getResources().getColor(R.color.desc_defau));
               mLlHead.setVisibility(View.GONE);
               break;

           case 1:

               mTvWaitSure.setTextColor(getResources().getColor(R.color.filter));
               mTvNewAppo.setTextColor(getResources().getColor(R.color.desc_defau));
               mTvDoing.setTextColor(getResources().getColor(R.color.desc_defau));
               mTvFinish.setTextColor(getResources().getColor(R.color.desc_defau));
               mTvCancel.setTextColor(getResources().getColor(R.color.desc_defau));
               mLlHead.setVisibility(View.GONE);

               break;
           case 2:
               mTvDoing.setTextColor(getResources().getColor(R.color.filter));
               mTvWaitSure.setTextColor(getResources().getColor(R.color.desc_defau));
               mTvNewAppo.setTextColor(getResources().getColor(R.color.desc_defau));
               mTvFinish.setTextColor(getResources().getColor(R.color.desc_defau));
               mTvCancel.setTextColor(getResources().getColor(R.color.desc_defau));
               mLlHead.setVisibility(View.GONE);

               break;
           case 3:
               mTvFinish.setTextColor(getResources().getColor(R.color.filter));
               mTvWaitSure.setTextColor(getResources().getColor(R.color.desc_defau));
               mTvDoing.setTextColor(getResources().getColor(R.color.desc_defau));
               mTvNewAppo.setTextColor(getResources().getColor(R.color.desc_defau));
               mTvCancel.setTextColor(getResources().getColor(R.color.desc_defau));
               mLlHead.setVisibility(View.VISIBLE);
               // mNtb.setRightTitle("关闭空闲");

               break;
           case 4:
               mTvCancel.setTextColor(getResources().getColor(R.color.filter));
               mTvWaitSure.setTextColor(getResources().getColor(R.color.desc_defau));
               mTvDoing.setTextColor(getResources().getColor(R.color.desc_defau));
               mTvFinish.setTextColor(getResources().getColor(R.color.desc_defau));
               mTvNewAppo.setTextColor(getResources().getColor(R.color.desc_defau));
               mLlHead.setVisibility(View.GONE);

               break;

       }
       setTabSelection(postion);
//       EventBus.getDefault().post(new UpdateWorkState(postion));//预约单状态，0新预约1待确认2进行中3已完成-1已取消

   }
}
