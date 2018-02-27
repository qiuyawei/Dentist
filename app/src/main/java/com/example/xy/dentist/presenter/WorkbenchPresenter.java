package com.example.xy.dentist.presenter;

import android.app.Activity;

import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.DoctorInfoBean;
import com.example.xy.dentist.contract.WorkbenchContract;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.google.gson.Gson;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import rx.Subscriber;

/**
 * Created by XY on 2017/11/3.
 */
public class WorkbenchPresenter  extends WorkbenchContract.Presenter {

    @Override
    public void getListData(String doctor_token, String page, String limit, final String status, String name, String year, String month, String day, String start_time, String end_time, int rows) {
        mView.showLoading("加载中...");
        mRxManage.add(mModel.getListData(doctor_token,  page,  limit,  status,  name,  year,  month,  day,start_time,end_time,rows).subscribe(new Subscriber<BaseResult<BaseListResult<DoctorInfoBean, String>>>() {
            @Override
            public void onCompleted() {
                mView.stopLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorTip("" + e.getMessage());
            }

            @Override
            public void onNext(BaseResult<BaseListResult<DoctorInfoBean, String>> result) {
                LogUtils.print("status",status+"");

                LogUtils.print("res",new Gson().toJson(result));
                if (result != null) {
                    switch (result.code){
                        case 200:
                            mView.setListData(result.data.list,result.data.status);
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
    public void cancel(String clinic_id) {
        mView.showLoading("取消中...");
        mRxManage.add(mModel.doctor_cancel(GlobalParams.getdoctor_token(),  clinic_id).subscribe(new Subscriber<BaseResult>() {
                @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorTip("" + e.getMessage());
            }

            @Override
            public void onNext(BaseResult result) {
                if (result != null) {
                    switch (result.code){
                        case 200:
                            mView.canclestate(result,result.message);
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
