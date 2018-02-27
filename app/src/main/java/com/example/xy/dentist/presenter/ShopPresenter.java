package com.example.xy.dentist.presenter;

import android.app.Activity;
import android.util.Log;

import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.ShopBean;
import com.example.xy.dentist.contract.ShopContract;
import com.example.xy.dentist.ui.login.LoginActivity;
import com.google.gson.Gson;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import rx.Subscriber;

/**
 * Created by XY on 2017/10/12.
 */
public class ShopPresenter extends ShopContract.Presenter {


    @Override
    public void shop(String doctor_token, final String page, String limit) {
        mView.showLoading("加载中...");
        mRxManage.add(mModel.shop(doctor_token, page, limit).subscribe(new Subscriber<BaseResult<BaseListResult<ShopBean, String>>>() {
            @Override
            public void onCompleted() {
                mView.stopLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorTip("" + e.getMessage());
            }

            @Override
            public void onNext(BaseResult<BaseListResult<ShopBean, String>> result) {
                Log.i("TAG","rsult="+new Gson().toJson(result));
                Log.i("TAG","page="+page);

                if (result.code == 200) {
                    mView.setListData(result.data.list, result.message);
                } else {
                    ToastUitl.showShort(result.message);
                }

            }
        }));
    }

    @Override
    public void shop_info(String doctor_token, String id) {
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.shop_info(doctor_token, id).subscribe(new Subscriber<BaseResult<BaseListResult<String, ShopBean>>>() {
            @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorTip("" + e.getMessage());
            }

            @Override
            public void onNext(BaseResult<BaseListResult<String, ShopBean>> result) {
                Log.i("TAG", "resultCod=" + result.code);
                if (result != null) {
                    try {
                        if (result.code == 200) {
                            mView.setInfo(result.data.info, result.message);
                        } else {
                            ToastUitl.showShort(result.message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }));
    }
}
