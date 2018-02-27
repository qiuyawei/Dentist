package com.example.xy.dentist.presenter;

import android.app.Activity;
import android.util.Log;

import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.ClinicBean;
import com.example.xy.dentist.bean.DoctorDetailbean;
import com.example.xy.dentist.bean.UserInfo;
import com.example.xy.dentist.contract.QueryDetailContract;
import com.example.xy.dentist.utils.LogUtils;
import com.google.gson.Gson;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import rx.Subscriber;

/**
 * Created by XY on 2017/11/2.
 */
public class QueryDetailPresenter extends QueryDetailContract.Presenter {


    @Override
    public void getInfoData(String id, String latitude, String longitude) {
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.getInfoData(id, latitude, longitude).subscribe(new Subscriber<BaseResult<BaseListResult<UserInfo, ClinicBean>>>() {
            @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorTip("" + e.getMessage());
            }

            @Override
            public void onNext(BaseResult<BaseListResult<UserInfo, ClinicBean>> result) {
                if (result != null) {
                    try {
                        if (result.code == 200) {
                            mView.setInfoData(result.data.info, result.data.list, result.message);
//                            Log.i("TAG","distance="+result.data.info.distance);
//                            Log.i("TAG","latitude="+result.data.info.latitude);

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

    @Override
    public void doctorInfo(String id) {
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.doctorInfo(id).subscribe(new Subscriber<BaseResult<BaseListResult<String, DoctorDetailbean>>>() {
            @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorTip("" + e.getMessage());
            }

            @Override
            public void onNext(BaseResult<BaseListResult<String, DoctorDetailbean>> result) {
                if (result != null) {
                    Log.i("TAG", "code=" + result.code);
                    Log.i("TAG", "messag=" + result.message);

                    try {
                        if (result.code == 200) {
                            mView.setDicData(result.data.info, result.message);
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

    @Override
    public void appointment(final String status, String user_token, String clinic_id, String doctor_id, String type) {
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.appointment(user_token, clinic_id, doctor_id, type).subscribe(new Subscriber<BaseResult>() {
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
                    LogUtils.print("res", new Gson().toJson(result));
                    mView.setAppoint(result, status, result.message);
                }
            }
        }));
    }
}
