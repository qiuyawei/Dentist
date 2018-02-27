package com.example.xy.dentist.ui.patientside.fragment;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.example.xy.dentist.AppApplication;
import com.example.xy.dentist.R;
import com.example.xy.dentist.adapter.EvaluateAdapter;
import com.example.xy.dentist.bean.ClinicBean;
import com.example.xy.dentist.contract.QueryContract;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.model.QueryModel;
import com.example.xy.dentist.presenter.QueryPresenter;
import com.example.xy.dentist.ui.patientside.activity.query.QueryDetailActivity;
import com.example.xy.dentist.utils.DensityUtil;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.widget.RecycleViewDivider;
import com.example.xy.dentist.widget.city.CityPicker;
import com.google.gson.Gson;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingTip;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.jaydenxiao.common.utils.RxPermissionsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class QueryMainFragment extends BaseFragment<QueryPresenter, QueryModel> implements QueryContract.View, ItemClickListener, ItemLongClickListener {


    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.tv_desc)
    TextView mTvDesc;
    @Bind(R.id.tv_area)
    TextView mTvArea;
    @Bind(R.id.irc)
    IRecyclerView mIrc;
    @Bind(R.id.loadedTip)
    LoadingTip mLoadedTip;
    private EvaluateAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String area, id, province, zipCode, city, region;
    private Intent mIntent;
    private String distict_id;//区域ID
    private String mToken;
    private int page = 1;
    private String limit = "20";

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_query_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mNtb.setTitleText("牙医联盟");
        mNtb.setRightImagVisibility(true);
        mNtb.setTvLeftVisiable(false);
        mNtb.setRightImagSrc(R.mipmap.map);
        mNtb.setRightTitleVisibility(true);
        mNtb.setRightTitle("福建省");
    }

    @Override
    public void initData() {
        mToken = GlobalParams.getuser_token();
        mAdapter = new EvaluateAdapter(getActivity(), mPresenter, this, this);
        mAdapter.openLoadAnimation(new ScaleInAnimation());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mIrc.setLayoutManager(linearLayoutManager);
        mIrc.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, DensityUtil.dip2px(getActivity(), 8), getResources().getColor(R.color.bg_color)));
        mIrc.setAdapter(mAdapter);
        loadData();

    }

    @Override
    public void initListener() {
        mNtb.setOnRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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


    @OnClick({R.id.tv_desc, R.id.tv_area})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_desc:
                mTvDesc.setTextColor(getResources().getColor(R.color.filter));
                mTvArea.setTextColor(getResources().getColor(R.color.desc_defau));
                page = 1;
                distict_id = "";
                loadData();

                break;
            case R.id.tv_area:
                mTvArea.setTextColor(getResources().getColor(R.color.filter));
                mTvDesc.setTextColor(getResources().getColor(R.color.desc_defau));
                showAddressDialog();
                break;
        }
    }

    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新时才查未读条数

                mPresenter.getListData(mToken, page + "", limit, AppApplication.latitude, AppApplication.longitude, distict_id, page);


            }
        }, 500);
    }

    List<ClinicBean> bean;

    @Override
    public void setListData(List<ClinicBean> circleItems, String message) {
//        mAdapter.clear();
//        LogUtils.print("result", new Gson().toJson(circleItems));
//        LogUtils.print("size", circleItems.size());

        if (page == 1) {

                mAdapter.reset(circleItems);
                this.bean = circleItems;
        } else {
            this.bean.addAll(circleItems);
            mAdapter.addAll(circleItems);
        }
        mIrc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
        mIrc.setRefreshing(false);

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

        if(page==1&&circleItems.size()==0){
            ToastUitl.showShort("暂无数据");
        }else if(page>1&&circleItems.size()==0){
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
    }

    @Override
    public void showErrorTip(String msg) {
        mLoadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
        mLoadedTip.setTips(msg);
    }

    /**
     * 弹出地址控件
     */
    private void showAddressDialog() {
        int mScreenWidth;
        int mScreenHeight;
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_city, null, false);
        final Dialog dialog = new Dialog(getActivity(), R.style.about_dialog);
        dialog.setContentView(view);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);

        lp.width = mScreenWidth;
        lp.height = (int) (mScreenHeight * 0.35);
        dialogWindow.setWindowAnimations(R.style.dialog_anim_bottom);
        dialogWindow.setAttributes(lp);

        dialog.setCanceledOnTouchOutside(true);

        dialog.show();

        TextView tvConfirm = (TextView) view.findViewById(R.id.tvText_confirm);
        TextView tvCancel = (TextView) view.findViewById(R.id.tvText_cancel);
        final CityPicker cityPicker = (CityPicker) view.findViewById(R.id.cpCity);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = cityPicker.getSelect();
                Log.i("TAG","cityJson="+new Gson().toJson(list));
                distict_id = cityPicker.getSelectid();
                area = list.get(0) + " " + list.get(1) + " " + list.get(2);
                province = list.get(0);
                city = list.get(1);
                region = list.get(2);
                zipCode = cityPicker.getSelectZip();
                dialog.dismiss();
                page = 1;
                loadData();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                loadData();
            }
        });
    }

    @Override
    public void onItemClick(View view, int postion) {

        mIntent = new Intent(getActivity(), QueryDetailActivity.class);
        mIntent.putExtra("bean", bean.get(postion - 2));
        startActivity(mIntent);

    }

    @Override
    public void onItemLongClick(View view, int postion) {

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            RxPermissionsUtils.checkPermission(getActivity(), new RxPermissionsUtils.onBackListener() {
                @Override
                public void listener(Boolean type) {
                    if(type){

                    }else {
                       ToastUitl.showShort("定位失败，请到设置里面打开，然后刷新页面");
                    }
                }
            }, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }
}
