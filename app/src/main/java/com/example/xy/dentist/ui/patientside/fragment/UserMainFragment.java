package com.example.xy.dentist.ui.patientside.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.example.xy.dentist.R;
import com.example.xy.dentist.adapter.UserAdapter;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.InfoBean;
import com.example.xy.dentist.bean.eventbus.UpdateInfo;
import com.example.xy.dentist.contract.UserContract;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.model.UserModel;
import com.example.xy.dentist.presenter.UserPresenter;
import com.example.xy.dentist.ui.patientside.activity.mine.CaseLogDetails;
import com.example.xy.dentist.ui.patientside.activity.mine.SettingActivity;
import com.example.xy.dentist.ui.patientside.activity.mine.UserInfoActivity;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.utils.SharedPreferencesUtil;
import com.example.xy.dentist.widget.HeadView;
import com.example.xy.dentist.widget.RecycleViewDivider;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.commonutils.DisplayUtil;
import com.jaydenxiao.common.commonwidget.LoadingTip;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import butterknife.Bind;


public class UserMainFragment extends BaseFragment<UserPresenter, UserModel> implements UserContract.View, ItemClickListener, ItemLongClickListener {


    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.irc)
    IRecyclerView mIrc;
    @Bind(R.id.loadedTip)
    LoadingTip mLoadedTip;
    private Intent mIntent;
    private HeadView homeHeaderView;
    private LinearLayoutManager linearLayoutManager;
    private UserAdapter mAdapter;
    private String user_token;
    private InfoBean mBean;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_user_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (SharedPreferencesUtil.getBooleanData(getActivity(), "SetIfHasRedDot", false)) {
            mNtb.setRightDotVisibility(true);
        } else {
            mNtb.setRightDotVisibility(false);

        }
        mNtb.setTitleText("我的");
        mNtb.setRightImagVisibility(true);
        mNtb.setTvLeftVisiable(false);
        mNtb.setRightImagSrc(R.mipmap.setting);
        mNtb.setRightTitleVisibility(false);
        homeHeaderView = new HeadView(getActivity());
//        params.width=ViewGroup.LayoutParams.MATCH_PARENT;
//        params.height=ViewGroup.LayoutParams.WRAP_CONTENT;
//        homeHeaderView=View.inflate(getActivity(),R.layout.head_layout,null);
//        homeHeaderView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        mIrc.addHeaderView(homeHeaderView);

    }

    @Override
    public void initData() {
        user_token = GlobalParams.getuser_token();

        mAdapter = new UserAdapter(getActivity(), mPresenter, this, this);
        mAdapter.openLoadAnimation(new ScaleInAnimation());

        linearLayoutManager = new LinearLayoutManager(getActivity());
        mIrc.setLayoutManager(linearLayoutManager);
        mIrc.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, DisplayUtil.dip2px(1), getResources().getColor(R.color.bg_color)));
        mIrc.setAdapter(mAdapter);
        loadData("initData");
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
                SharedPreferencesUtil.saveBooleanData(getActivity(), "SetIfHasRedDot", false);

                mIntent = new Intent(getActivity(), SettingActivity.class);
                startActivity(mIntent);

            }
        });


        mIrc.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.getPageBean().setRefresh(true);
                loadData("setRefresh");
            }
        });
        //下拉加载更多
        mIrc.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(View loadMoreView) {
//                mIrc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.getPageBean().setRefresh(false);
//                        loadData();
                    }
                }, 1000);

            }
        });

    }

    private void loadData(String type) {
        LogUtils.print("type",type);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新时才查未读条数
                mPresenter.getMyInfo(user_token);
            }
        }, 500);
    }


    @Override
    public void setMyInfo(BaseResult<InfoBean> data) {
        if (data != null) {
            mBean = data.data;
            homeHeaderView.setData(mBean.info);
            if (mAdapter.getPageBean().isRefresh()) {
                mAdapter.reset(mBean.year);

                mIrc.setRefreshing(false);
            } else {
//                mAdapter.addAll(mBean.year);

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
    public void setDocInfo(BaseResult<InfoBean> data) {

    }

    @Override
    public void updateInfo(BaseResult result, String type, String content) {

    }

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

    }

    @Override
    public void onItemLongClick(View view, int postion) {

    }

    @Override
    public void onEvent(Object o) {
        super.onEvent(o);
        if (o instanceof UpdateInfo) {
            Log.i("TAG","UpdateInfo");
            loadData("onEvent");
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i("TAG","start");
        IntentFilter filter = new IntentFilter();
        filter.addAction("Has_New_Message");
        filter.addAction("Open_Notification");
        getActivity().registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i("TAG","hidden"+hidden);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "Has_New_Message":
                    mNtb.setRightDotVisibility(true);
                    break;
                case "Open_Notification":
                    mNtb.setRightDotVisibility(false);
                    break;
            }
        }
    };



}
