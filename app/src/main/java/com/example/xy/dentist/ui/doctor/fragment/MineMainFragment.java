package com.example.xy.dentist.ui.doctor.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.example.xy.dentist.R;
import com.example.xy.dentist.adapter.MineAdapter;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.InfoBean;
import com.example.xy.dentist.bean.TabBean;
import com.example.xy.dentist.bean.UserInfo;
import com.example.xy.dentist.bean.eventbus.UpdateDocInfo;
import com.example.xy.dentist.contract.UserContract;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.model.UserModel;
import com.example.xy.dentist.presenter.UserPresenter;
import com.example.xy.dentist.ui.patientside.activity.mine.FeedBackActivity;
import com.example.xy.dentist.ui.patientside.activity.mine.SettingActivity;
import com.example.xy.dentist.ui.patientside.activity.mine.UserInfoActivity;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.SharedPreferencesUtil;
import com.example.xy.dentist.widget.HeadView;
import com.example.xy.dentist.widget.RecycleViewDivider;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.commonutils.DisplayUtil;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingTip;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 我的页面
 */

public class MineMainFragment extends BaseFragment<UserPresenter,UserModel> implements UserContract.View ,ItemClickListener,ItemLongClickListener {



    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.irc)
    IRecyclerView mIrc;
    @Bind(R.id.loadedTip)
    LoadingTip mLoadedTip;
    private Intent mIntent;
    private HeadView homeHeaderView;
    private LinearLayoutManager linearLayoutManager;
    private MineAdapter mAdapter;
    private String doctor_token;
    private InfoBean mBean;
    private List<TabBean> mInfo;
    private UserInfo bean;
    private String content;
    private String introduce,skill,resume,experice;
    String type="-1";

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_mine_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        if(SharedPreferencesUtil.getBooleanData(getActivity(),"SetIfHasRedDot",false)){
            mNtb.setRightDotVisibility(true);
        }else {
            mNtb.setRightDotVisibility(false);

        }
        mNtb.setTitleText("我的");
        mNtb.setRightImagVisibility(true);
        mNtb.setTvLeftVisiable(false);
        mNtb.setRightImagSrc(R.mipmap.setting);
        mNtb.setRightTitleVisibility(false);
        homeHeaderView = new HeadView(getActivity());
        mIrc.addHeaderView(homeHeaderView);

    }

    @Override
    public void initData() {
        doctor_token=   GlobalParams.getdoctor_token();
        mAdapter = new MineAdapter(getActivity(), mPresenter,this,this);
        mAdapter.openLoadAnimation(new ScaleInAnimation());

        linearLayoutManager = new LinearLayoutManager(getActivity());
        mIrc.setLayoutManager(linearLayoutManager);
        mIrc.setAdapter(mAdapter);
        mIrc .addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, DisplayUtil.dip2px(1), getResources().getColor(R.color.bg_color)));
//        mAdapter.getPageBean().setRefresh(true);
        loadData(type);
    }

    @Override
    public void initListener() {
        homeHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getActivity(), UserInfoActivity.class);
                mIntent.putExtra("info", mBean);
                startActivity(mIntent);
            }
        });
        mNtb.setOnRightImagListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNtb.setRightDotVisibility(false);
                SharedPreferencesUtil.saveBooleanData(getActivity(),"SetIfHasRedDot",false);
                mIntent = new Intent(getActivity(), SettingActivity.class);
                startActivity(mIntent);

            }
        });


        mIrc.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.getPageBean().setRefresh(true);
                loadData(type);
            }
        });
        //下拉加载更多
        mIrc.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(View loadMoreView) {
                //  mIrc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        mAdapter.getPageBean().setRefresh(false);
//                        loadData(type);
                    }
                }, 0);

            }
        });

    }
    private void loadData(String type) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新时才查未读条数
                mPresenter.getDoctor_MyInfo(doctor_token);

            }
        }, 500);
    }



  /*  @Override
    public void setListData(List<BaseBean> circleItems, PageBean pageBean) {
        if (mAdapter.getPageBean().isRefresh()) {
            mAdapter.reset(circleItems);
            mIrc.setRefreshing(false);
        } else {
            mAdapter.addAll(circleItems);
            mIrc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
        }
        //判断是否还可以加载更多
        if (pageBean.getTotalPage() <= pageBean.getPage()) {
            mIrc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
        }
        //加载完成
        if (mAdapter.getData().size() > 0) {
            mLoadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        } else {
            mLoadedTip.setLoadingTip(LoadingTip.LoadStatus.empty);
        }
    }*/

    @Override
    public void showLoading(String title) {
        mLoadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
    }

    @Override
    public void stopLoading() {
        mLoadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
    }

    @Override
    public void showErrorTip(String msg) {
        mLoadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
        mLoadedTip.setTips(msg);
    }


    @Override
    public void onItemClick(View view, int postion) {
        if(mInfo==null){
            return;
        }
       int REQUESTCODE = 200;
        mIntent = new Intent(getActivity(), FeedBackActivity.class);
        switch (postion){
            case 0:
                mIntent.putExtra("title","医生介绍");
                mIntent.putExtra("hint", "请输入医生介绍");
                REQUESTCODE=200;
                break;
            case 1:
                mIntent.putExtra("title","擅长");
                mIntent.putExtra("hint","请输入擅长");
                REQUESTCODE=201;
                break;
            case 2:
                mIntent.putExtra("title","履历");
                mIntent.putExtra("hint","请输入个人履历");
                REQUESTCODE=202;
                break;
            case 3:
                mIntent.putExtra("title","经验");
                mIntent.putExtra("hint","请输入个人经验");
                REQUESTCODE=203;
                break;

        }
        mIntent.putExtra("content",mInfo.get(postion).value);
        startActivityForResult(mIntent,REQUESTCODE);


    }

    @Override
    public void onItemLongClick(View view, int postion) {

    }

    @Override
    public void setMyInfo(BaseResult<InfoBean> data) {

    }

    @Override
    public void setDocInfo(BaseResult<InfoBean> data) {
        if(data!=null){
            mBean = data.data;
            homeHeaderView.setData(mBean.info);
            mInfo = new ArrayList<>();
            bean = mBean.info;
            TabBean tab=new TabBean();
            tab.key="医生介绍";
            tab.value= bean.introduce;
            TabBean tab1=new TabBean();
            tab1.key="擅长";
            tab1.value= bean.skill;
            TabBean tab2=new TabBean();
            tab2.key="履历";
            tab2.value= bean.resume;

            TabBean tab3=new TabBean();
            tab3.key="经验";
            tab3.value= bean.experience;

            mInfo.add(tab);
            mInfo.add(tab1);
            mInfo.add(tab2);
            mInfo.add(tab3);

            if (mAdapter.getPageBean().isRefresh()) {
                mAdapter.reset(mInfo);
                mIrc.setRefreshing(false);
            } else {
                mAdapter.addAll(mInfo);
                mIrc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
            }
            //判断是否还可以加载更多
           /* if (pageBean.getTotalPage() <= pageBean.getPage()) {
                mIrc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
            }*/
            //加载完成
            if (mAdapter.getData().size() > 0) {
                mLoadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
            } else {
                mLoadedTip.setLoadingTip(LoadingTip.LoadStatus.empty);
            }
        }
    }

    @Override
    public void updateInfo(BaseResult result, String type,String content) {
       // loadData(type);
        if(mInfo==null){
            return;
        }
        switch (type){
            case "0":
                ToastUitl.showShort("更新成功");
                mInfo.get(0).value=content;
                break;
            case  "1":
                ToastUitl.showShort("更新成功");
                mInfo.get(1).value=content;
                break;
            case "2":
                mInfo.get(2).value=content;
                ToastUitl.showShort("更新成功");
                break;
            case "3":
                mInfo.get(3).value=content;
                ToastUitl.showShort("更新成功");
                break;
        }

        mAdapter.notifyDataSetChanged();

        /*if (mAdapter.getPageBean().isRefresh()) {
            mAdapter.reset(mInfo);
            mIrc.setRefreshing(false);
        } else {
            mAdapter.addAll(mInfo);
            mIrc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
        }
        //判断是否还可以加载更多
           *//* if (pageBean.getTotalPage() <= pageBean.getPage()) {
                mIrc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
            }*//*
        //加载完成
        if (mAdapter.getData().size() > 0) {
            mLoadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        } else {
            mLoadedTip.setLoadingTip(LoadingTip.LoadStatus.empty);
        }*/

    }


    @Override
    public void onEvent(Object o) {
        super.onEvent(o);
        if (o instanceof UpdateDocInfo) {

            loadData(type);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==getActivity().RESULT_OK){
            content= data.getStringExtra("data");


            switch (requestCode){
                case 200:
                    skill="";
                    resume="";
                    experice="";
                    introduce=content;
                    mPresenter.updateInfo(doctor_token, introduce, skill, resume,experice);

                    break;
                case  201:
                    introduce="";
                    resume="";
                    experice="";

                    skill=content;

                    mPresenter.updateInfo(doctor_token, introduce, skill, resume,experice);

                    break;
                case 202:
                    introduce="";
                    skill="";
                    experice="";

                    resume=content;
                    mPresenter.updateInfo(doctor_token, introduce, skill, resume,experice);

                    break;
                case 203:
                    introduce="";
                    skill="";
                    resume="";
                    experice=content;
                    mPresenter.updateInfo(doctor_token, introduce, skill, resume,experice);

                    break;
            }





        }
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter=new IntentFilter();
        filter.addAction("Has_New_Message");
        filter.addAction("Open_Notification");
        getActivity().registerReceiver(broadcastReceiver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case "Has_New_Message":
                    mNtb.setRightDotVisibility(true);
                    break;
                case "Open_Notification":
                    mNtb.setRightDotVisibility(false);
                    break;
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }
}
