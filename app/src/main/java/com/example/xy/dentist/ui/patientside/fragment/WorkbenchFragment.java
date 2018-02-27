package com.example.xy.dentist.ui.patientside.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.example.xy.dentist.R;
import com.example.xy.dentist.adapter.WorkbenchAdapter;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.DoctorInfoBean;
import com.example.xy.dentist.bean.eventbus.GetWorkState;
import com.example.xy.dentist.bean.eventbus.UpdateWorkState;
import com.example.xy.dentist.contract.WorkbenchContract;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.model.WorkbenchModel;
import com.example.xy.dentist.presenter.WorkbenchPresenter;
import com.example.xy.dentist.ui.doctor.activity.work.LogEvaluationActivity;
import com.example.xy.dentist.utils.DensityUtil;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.utils.MyTimeUtils;
import com.example.xy.dentist.widget.RecycleViewDivider;
import com.google.gson.Gson;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingTip;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkbenchFragment extends BaseFragment<WorkbenchPresenter, WorkbenchModel> implements WorkbenchContract.View, ItemClickListener, ItemLongClickListener {

    @Bind(R.id.irc)
    IRecyclerView mIrc;
    @Bind(R.id.loadedTip)
    LoadingTip mLoadedTip;
    private WorkbenchAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int state;//0新预约1待确认2进行中3已完成-1已取消
    private String doctor_token;
    int page = 1;
    private String limit = "20";
    private String name, start_time, end_time;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_workbench;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }


    public static WorkbenchFragment newInstance(int state) {
        WorkbenchFragment baseFragment = new WorkbenchFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("state", state);

        baseFragment.setArguments(bundle);
        return baseFragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initData() {
        doctor_token = GlobalParams.getdoctor_token();
        state = getArguments().getInt("state", -1);
        if (state == 4) {
            state = -1;
        }
        mAdapter = new WorkbenchAdapter(getActivity(), mPresenter, this, this, state);
        mAdapter.openLoadAnimation(new ScaleInAnimation());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mIrc.setLayoutManager(linearLayoutManager);
        mIrc.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, DensityUtil.dip2px(getActivity(), 8), getResources().getColor(R.color.bg_color)));
        mIrc.setAdapter(mAdapter);
        loadData();
    }

    @Override
    public void initListener() {


        mIrc.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //全部参数置空
//                clearAll();
                mAdapter.clear();
                mAdapter.getPageBean().setRefresh(true);
                page = 1;
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


                    }
                }, 1000);

            }
        });

    }

    @Override
    public void setSearch(String search) {
        super.setSearch(search);
        page = 1;
        name = search;
        loadData();
    }

    @Override
    public void setTime(String type, String date) {
        super.setTime(type, date);
        if (!TextUtils.isEmpty(date)) {
            page = 1;
            switch (type) {
                case "start_time":
                    start_time = date;
                    break;
                case "end_time":
                    end_time = date;
                    break;
            }

            loadData();
        }

    }


    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getListData(doctor_token, page + "", limit, state + "", name, "", "", "", start_time, end_time, mAdapter.getPageBean().getRows());
            }
        }, 500);
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
        if (state == 2 || state == 3) {
            if (bean != null) {

                DoctorInfoBean doctorInfoBean = bean.get(postion - 2);
                String tag1 = "", tag2 = "";
                List<String> tag = Arrays.asList(doctorInfoBean.tags);
                if (tag.size() > 0) {
                    tag1 = tag.get(0);
                }
                if (tag.size() > 1) {
                    tag2 = tag.get(1);
                }
                Intent intent = new Intent(getActivity(), LogEvaluationActivity.class);
                if (state == 3) {
                    intent.putExtra("title", "诊疗详情");
                } else if (state == 2) {
                    intent.putExtra("title", "填写日志评价");
                }

                intent.putExtra("id", doctorInfoBean.id);//预约ID
                intent.putExtra("DoctorInfoBean", doctorInfoBean);
                intent.putExtra("tag1", tag1);
                intent.putExtra("tag2", tag2);

                startActivity(intent);
            }

        }

    }

    @Override
    public void onItemLongClick(View view, int postion) {

    }

    List<DoctorInfoBean> bean;

    @Override
    public void setListData(List<DoctorInfoBean> bean, String message) {
        Gson gson = new Gson();
        LogUtils.print("page", gson.toJson(page));
        if (page == 1) {
            mAdapter.reset(bean);
            this.bean = bean;
        } else {
            mAdapter.addAll(bean);
            this.bean.addAll(bean);
        }
        mIrc.setRefreshing(false);
        mIrc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
        EventBus.getDefault().post(new GetWorkState(message));//预约单状态，0新预约1待确认2进行中3已完成-1已取消
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

        }
    }

    @Override
    public void canclestate(BaseResult bean, String message) {
        Intent intent = new Intent("NEED_CHANGE_PARENT_AND_CHILD_MAIN");
        intent.putExtra("parentIndex", 0);
        intent.putExtra("childIndex", 4);
        getActivity().sendBroadcast(intent);
        ToastUitl.showShort("取消预约成功");
//        EventBus.getDefault().post(new UpdateWorkState(1));//预约单状态，0新预约1待确认2进行中3已完成-1已取消
//        loadData();
    }


    @Override
    public void onEvent(Object o) {
        super.onEvent(o);
        if (o instanceof UpdateWorkState) {
            state = ((UpdateWorkState) o).state;
            LogUtils.print("state", state + "");
            mAdapter.flag = state;
            if (state == 4) {
                state = -1;
            }
            mAdapter.flag=state;
            page=1;
            loadData();
        }
    }


}
