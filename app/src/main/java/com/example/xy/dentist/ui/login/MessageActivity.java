package com.example.xy.dentist.ui.login;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.example.xy.dentist.AppApplication;
import com.example.xy.dentist.R;
import com.example.xy.dentist.adapter.EvaluateAdapter;
import com.example.xy.dentist.adapter.MessageAdapter;
import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.Meassagebean;
import com.example.xy.dentist.contract.MessageContract;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.model.MessageModel;
import com.example.xy.dentist.presenter.MessagePresenter;
import com.example.xy.dentist.utils.DensityUtil;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.utils.SharedPreferencesUtil;
import com.example.xy.dentist.widget.RecycleViewDivider;
import com.example.xy.dentist.widget.SpacesItemDecoration;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingTip;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

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
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lenovo on 2017/12/12.
 */

public class MessageActivity extends  BaseActivity <MessagePresenter, MessageModel> implements MessageContract.View,ItemClickListener,ItemLongClickListener {
    @Bind(R.id.irc)
    IRecyclerView mIrc;
    @Bind(R.id.loadedTip)
    LoadingTip mLoadedTip;
    private LinearLayoutManager linearLayoutManager;
    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    private MessageAdapter mAdapter;
    private String states="1";//1 用户  2  医生
    private List<Meassagebean> data=new ArrayList<>();
    @Override
    public int getLayoutId() {
        return R.layout.activity_message;
    }
    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        mNtb.setTitleText("消息列表");
        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);
        boolean b= SharedPreferencesUtil.getBooleanData(mActivity,"isdoc",false);
        if(b){
            states="2";
        }else {
            states="1";
        }
    }

    @Override
    protected void initData() {
        mAdapter = new MessageAdapter(mActivity, mPresenter, this, this);
        mAdapter.openLoadAnimation(new ScaleInAnimation());
        linearLayoutManager = new LinearLayoutManager(mActivity);
        mIrc.setLayoutManager(linearLayoutManager);
        int leftRight = DensityUtil.dip2px(mActivity, 5);
        int topBottom = DensityUtil.dip2px(mActivity, 5);
        mIrc.addItemDecoration(new SpacesItemDecoration(leftRight, topBottom, getResources().getColor(R.color.bg_color)));
        mIrc.setAdapter(mAdapter);
        getData();
    }

    @Override
    protected void initListener() {
        mNtb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIrc.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.getPageBean().setRefresh(true);
                getData();
            }
        });
        //下拉加载更多
        mIrc.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(View loadMoreView) {


            }
        });
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

    @Override
    public void onItemClick(View view, int postion) {

    }

    @Override
    public void onItemLongClick(View view, int postion) {

    }

    @Override
    public void setListData(List<Meassagebean> result, String message) {
        data.clear();
        data.addAll(result);
        mAdapter.reset(result);
    }

    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mIrc.setRefreshing(false);
                //刷新时才查未读条数

                mPresenter.getListDatas(GlobalParams.getUserId(),states);


            }
        }, 500);
    }

    private void getData(){
        LogUtils.print("states",states);

        OkHttpClient client=new OkHttpClient();
        FormBody formBody=new FormBody.Builder().add("uid",GlobalParams.getUserId())
                .add("states",states)
                .build();

        Request requestBody=new Request.Builder().url(ApiConstants.SERVER_URL+ApiConstants.getMessage)
                .post(formBody)
                .build();

        client.newCall(requestBody).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.print("err",e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIrc.setRefreshing(false);

                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                data.clear();
                final String json=response.body().string();
                LogUtils.print("json",json);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIrc.setRefreshing(false);

                        try {
                            JSONObject jsonObject=new JSONObject(json);
                            if(jsonObject.getString("code").equals("200")){
                                JSONObject mJson=jsonObject.getJSONObject("data");
                                JSONArray jsonArray=mJson.getJSONArray("list");
                                data.addAll(JSON.parseArray(jsonArray.toString(),Meassagebean.class));
                                mAdapter.reset(data);


                            }else {
                                ToastUitl.showShort(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }
}
