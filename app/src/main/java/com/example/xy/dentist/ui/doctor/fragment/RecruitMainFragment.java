package com.example.xy.dentist.ui.doctor.fragment;


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
import com.example.xy.dentist.R;
import com.example.xy.dentist.adapter.RecruitAdapter;
import com.example.xy.dentist.bean.RecruitBean;
import com.example.xy.dentist.contract.EvaluateContract;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.model.EvaluateModel;
import com.example.xy.dentist.presenter.EvaluatePresenter;
import com.example.xy.dentist.ui.doctor.activity.recruit.RecruitDetailActivity;
import com.example.xy.dentist.utils.DensityUtil;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.PopUtil;
import com.example.xy.dentist.widget.RecycleViewDivider;
import com.example.xy.dentist.widget.city.CityPicker;
import com.google.gson.Gson;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingTip;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class RecruitMainFragment extends BaseFragment<EvaluatePresenter, EvaluateModel> implements EvaluateContract.View, ItemClickListener, ItemLongClickListener {
    private RecruitAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String area, distict_id, province, zipCode, city, region;
    private Intent mIntent;

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
    private String limit = "20";
    int page = 1;
    private String time_desc;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recruit_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mNtb.setTitleText("招聘");
        mNtb.setRightImagVisibility(false);
        mNtb.setTvLeftVisiable(false);
        mNtb.setRightTitleVisibility(false);
        time_desc = "1";
    }

    @Override
    public void initData() {

        mAdapter = new RecruitAdapter(getActivity(), mPresenter, this, this);
        mAdapter.openLoadAnimation(new ScaleInAnimation());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mIrc.setLayoutManager(linearLayoutManager);
        mIrc.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, DensityUtil.dip2px(getActivity(), 5), getResources().getColor(R.color.bg_color)));
        mIrc.setAdapter(mAdapter);
        loadData();
//        mAdapter.getPageBean().setRefresh(true);

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


    @OnClick({R.id.tv_desc, R.id.tv_area})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_desc:
                mTvDesc.setTextColor(getResources().getColor(R.color.filter));
                mTvArea.setTextColor(getResources().getColor(R.color.desc_defau));
                time_desc = time_desc.equals("0") ? "1" : "0";//时间倒序1是0否
                distict_id="";
                page=1;
                loadData();
                // showTime();

                break;
            case R.id.tv_area:
                mTvArea.setTextColor(getResources().getColor(R.color.filter));
                mTvDesc.setTextColor(getResources().getColor(R.color.desc_defau));
                page=1;
                time_desc="0";
                showAddressDialog();
                break;
        }
    }

    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新时才查未读条数

                mPresenter.recruit(GlobalParams.getdoctor_token(), page + "", limit, distict_id, time_desc);


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
                distict_id = cityPicker.getSelectid();
                area = list.get(0) + " " + list.get(1) + " " + list.get(2);
                province = list.get(0);
                city = list.get(1);
                region = list.get(2);
                //  tv_area.setText(area);
                zipCode = cityPicker.getSelectZip();
                // mTvYuban.setText(zipCode);
                // Log.d("邮编", zipCode);
                dialog.dismiss();
                loadData();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distict_id = "";
                dialog.dismiss();
                loadData();
            }
        });
    }


    private void showTime() {
        PopUtil.showBirthdayPopwindow(getActivity(),
                PopUtil.getDataPick(getActivity(), new PopUtil.onSelectFinishListener() {
                    @Override
                    public void onSelectFinish(String date) {
                        // mTvYear.setText(date);
                        // loadData();
                    }
                }));
    }

    @Override
    public void onItemClick(View view, int postion) {
        RecruitBean bean = this.bean.get(postion - 2);
        mIntent = new Intent(getActivity(), RecruitDetailActivity.class);
        mIntent.putExtra("id", bean.id);
        startActivity(mIntent);

    }

    @Override
    public void onItemLongClick(View view, int postion) {

    }

    List<RecruitBean> bean;

    @Override
    public void setRecruitListData(List<RecruitBean> bean, String message) {
        Log.i("TAG","setRecruitListData="+new Gson().toJson(bean));
        if (page==1) {
            mAdapter.reset(bean);
            this.bean = bean;
            mIrc.setRefreshing(false);
        } else {
            mAdapter.addAll(bean);
            this.bean.addAll(bean);
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

        if(page==1&&bean.size()==0){
            ToastUitl.showShort("暂无数据");
        }else if(page>1&&bean.size()==0){
            //加载更多
            ToastUitl.showShort("已经是最后一页");
            page--;

        }

    }

    @Override
    public void setRecruit_infoData(RecruitBean bean, String message) {

    }
}
