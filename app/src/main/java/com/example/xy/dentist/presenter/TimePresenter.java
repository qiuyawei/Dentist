package com.example.xy.dentist.presenter;

import android.app.Activity;
import android.util.Log;

import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.TimeSetBean;
import com.example.xy.dentist.contract.TimeContract;
import com.example.xy.dentist.utils.LogUtils;
import com.google.gson.Gson;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import rx.Subscriber;

/**
 * Created by XY on 2017/11/3.
 */
public class TimePresenter extends TimeContract.Presenter {
    @Override
    public void setTime(String doctor_token, String id, final String date_id,String data) {
        LogUtils.print("date",data);
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.setTime(doctor_token, id, date_id,data).subscribe(new Subscriber<BaseResult>() {
            @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {
//                mView.showErrorTip("" + e.getMessage());
                LogUtils.print("setTimeError",e.getMessage());
            }

            @Override
            public void onNext(BaseResult result) {
                LogUtils.print("result",new Gson().toJson(result));


                if (result != null) {
                    switch (result.code){
                        case 200:
                            mView.setTimeState(result,result.message);
                            break;
                        case 400:
                            ToastUitl.showShort(result.message);
                            break;

                    }
                }
            }
        }));
    }

    @Override
    public void doctor_getTimeList(String doctor_token) {
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.doctor_getTimeList(doctor_token).subscribe(new Subscriber<BaseResult<BaseListResult<TimeSetBean, String>>>() {
            @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorTip("" + e.getMessage());
//                Log.i("TAG","error="+e.getMessage());
            }

            @Override
            public void onNext(BaseResult<BaseListResult<TimeSetBean, String>> result) {
//                Log.i("TAG","resultCode="+result.toString());
//                LogUtils.print("timeList",new Gson().toJson(result));
                if (result != null) {
                    switch (result.code){
                        case 200:
                            mView.doctor_setTimeList(result.data.list,result.data.message);
                            break;
                        case 400:
                            ToastUitl.showShort(result.message);
                            break;

                    }
                }
            }
        }));
    }


    @Override
    public void doctor_getTimeListNew(String doctor_token,String date) {
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.doctor_getTimeListNew(doctor_token,date).subscribe(new Subscriber<BaseResult<BaseListResult<TimeSetBean, String>>>() {
            @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorTip("" + e.getMessage());
//                Log.i("TAG","error="+e.getMessage());
            }

            @Override
            public void onNext(BaseResult<BaseListResult<TimeSetBean, String>> result) {
//                Log.i("TAG","resultCode="+result.toString());
                LogUtils.print("timeList",new Gson().toJson(result));
                if (result != null) {
                    switch (result.code){
                        case 200:
                            mView.doctor_setTimeListNew(result.data.list,result.data.message);
                            break;
                        case 400:
                            ToastUitl.showShort(result.message);
                            break;

                    }
                }
            }
        }));
    }
}
