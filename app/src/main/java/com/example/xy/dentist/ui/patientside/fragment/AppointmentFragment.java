package com.example.xy.dentist.ui.patientside.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.example.xy.dentist.R;
import com.example.xy.dentist.adapter.AppointmentAdapter;
import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.AppointuBean;
import com.example.xy.dentist.bean.eventbus.UpdateAppointState;
import com.example.xy.dentist.bean.eventbus.UpdateWorkState;
import com.example.xy.dentist.contract.AppointmentContract;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.model.AppointmentModel;
import com.example.xy.dentist.presenter.AppointmentPresenter;
import com.example.xy.dentist.ui.patientside.activity.query.PhysicianActivity;
import com.example.xy.dentist.utils.DensityUtil;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.widget.RecycleViewDivider;
import com.google.gson.Gson;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingTip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends BaseFragment<AppointmentPresenter, AppointmentModel> implements AppointmentContract.View, ItemClickListener, ItemLongClickListener {

    @Bind(R.id.irc)
    IRecyclerView mIrc;
    @Bind(R.id.loadedTip)
    LoadingTip mLoadedTip;
    private Intent mIntent;
    private AppointmentAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int type;
    private int page = 1;
    private String mToken;
    private String limit = "20";

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_appointment;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }


    public static AppointmentFragment newInstance(int type) {
        AppointmentFragment baseFragment = new AppointmentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        baseFragment.setArguments(bundle);
        return baseFragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initData() {
        mToken = GlobalParams.getuser_token();
        type = getArguments().getInt("type", 1);//1进行中2待评价3已完成
        mAdapter = new AppointmentAdapter(getActivity(), mPresenter, this, this, type);
        mAdapter.openLoadAnimation(new ScaleInAnimation());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mIrc.setLayoutManager(linearLayoutManager);
        mIrc.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, DensityUtil.dip2px(getActivity(), 8), getResources().getColor(R.color.bg_color)));
        mIrc.setAdapter(mAdapter);
        loadData();
//        getData();
    }

    @Override
    public void initListener() {


        mIrc.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mAdapter.getPageBean().setRefresh(true);
                loadData();
            }
        });
        //下拉加载更多
        mIrc.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(View loadMoreView) {
                mIrc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
//                page++;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.getPageBean().setRefresh(false);
                        page++;
                        loadData();

//                        loadData();
                    }
                }, 1000);

            }
        });

    }




    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新时才查未读条数

                mPresenter.getListData(mToken, page + "", limit, type + "", mAdapter.getPageBean().getRows());


            }
        }, 500);
    }

    List<AppointuBean> bean = new ArrayList<>();

    @Override
    public void setListData(List<AppointuBean> bean, String message) {


        if (mAdapter.getPageBean().isRefresh()) {
            mAdapter.reset(bean);
            this.bean = bean;
            mIrc.setRefreshing(false);

        } else {
            mAdapter.addAll(bean);
            this.bean.addAll(bean);
            mIrc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
        }
        //判断是否还可以加载更多
        /*if (pageBean.getTotalPage() <= pageBean.getPage()) {
            mIrc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
        }*/
        //加载完成
        if (mAdapter.getData().size() > 0) {
            mLoadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        } else {
            mLoadedTip.setLoadingTip(LoadingTip.LoadStatus.empty);
        }


        if (page == 1 && bean.size() == 0) {
            ToastUitl.showShort("暂无数据");
        } else if (page > 1 && bean.size() == 0) {
            //加载更多
            ToastUitl.showShort("已经是最后一页");
            page--;
        }
    }

    @Override
    public void showLoading(String title) {
        mLoadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
    }

    @Override
    public void stopLoading() {
        mLoadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        mIrc.setRefreshing(false);
        mIrc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);

    }

    @Override
    public void showNetErrorTip() {
        super.showNetErrorTip();
    }

    @Override
    public void showErrorTip(String msg) {
        mLoadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
        mLoadedTip.setTips(msg);

    }

    @Override
    public void onItemClick(View view, int postion) {

        mIntent = new Intent(getActivity(), PhysicianActivity.class);
        AppointuBean appointBean = bean.get(postion - 2);
        mIntent = new Intent(getActivity(), PhysicianActivity.class);
        if (!appointBean.status.equals("0")) {//状态1忙碌0空闲
            mIntent.putExtra("title", "在线预约");

        } else {

            mIntent.putExtra("title", "即时预约");
        }
        mIntent.putExtra("phone", appointBean.phone);
        mIntent.putExtra("id", appointBean.doctor_id);
        mIntent.putExtra("clinic_id", appointBean.clinic_id);
        startActivity(mIntent);
    }

    @Override
    public void onItemLongClick(View view, int postion) {

    }


    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    /*public void refreshData(int pos) {
        page = 1;
        type = pos;
        mAdapter.flag = type;
        loadData();
    }*/

    @Override
    public void onEvent(Object o) {
        super.onEvent(o);
        if (o instanceof UpdateAppointState) {
            LogUtils.print("onEvent", "Appointment="+((UpdateAppointState) o).state);
            page = 1;
            type = ((UpdateAppointState) o).state;
            if (type != 3){
                mAdapter.flag=type;
                loadData();
            }
        }
    }
}
