package com.example.xy.dentist.ui.doctor.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.example.xy.dentist.R;
import com.example.xy.dentist.adapter.ShopAdapter;
import com.example.xy.dentist.bean.ShopBean;
import com.example.xy.dentist.contract.ShopContract;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.model.ShopModel;
import com.example.xy.dentist.presenter.ShopPresenter;
import com.example.xy.dentist.ui.doctor.activity.shop.ShopDetailActivity;
import com.example.xy.dentist.utils.DensityUtil;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.widget.SpacesItemDecoration;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingTip;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import java.util.List;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopMainFragment extends BaseFragment<ShopPresenter, ShopModel> implements ShopContract.View, ItemClickListener, ItemLongClickListener {


    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.irc)
    IRecyclerView mIrc;
    @Bind(R.id.loadedTip)
    LoadingTip mLoadedTip;
    private Intent mIntent;
    private GridLayoutManager mLayoutManage;
    private ShopAdapter mAdapter;
    private String doctor_token;
    private String limit = "20";
    private int page = 1;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_shop_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mNtb.setTitleText("商品");
        mNtb.setRightImagVisibility(false);
        mNtb.setTvLeftVisiable(false);
        mNtb.setRightTitleVisibility(false);


    }

    @Override
    public void initData() {
        doctor_token = GlobalParams.getdoctor_token();

        mAdapter = new ShopAdapter(getActivity(), mPresenter, this, this);
        mAdapter.openLoadAnimation(new ScaleInAnimation());
        mLayoutManage = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        mIrc.setLayoutManager(mLayoutManage);
        //添加ItemDecoration，item之间的间隔
        int leftRight = DensityUtil.dip2px(getActivity(), 5);
        int topBottom = DensityUtil.dip2px(getActivity(), 5);
        mIrc.addItemDecoration(new SpacesItemDecoration(leftRight, topBottom, getResources().getColor(R.color.bg_color)));
        mIrc.setAdapter(mAdapter);
//        mAdapter.getPageBean().setRefresh(true);
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


    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新时才查未读条数

                mPresenter.shop(doctor_token, page + "", limit);


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
        ShopBean bean = this.bean.get(postion - 2);
        mIntent = new Intent(getActivity(), ShopDetailActivity.class);
        mIntent.putExtra("id", bean.id);
        startActivity(mIntent);

    }

    @Override
    public void onItemLongClick(View view, int postion) {

    }

    List<ShopBean> bean;

    @Override
    public void setListData(List<ShopBean> bean, String message) {
        Log.i("TAG", "setShopListData=" + bean.size());

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
    public void setInfo(ShopBean bean, String message) {

    }
}
