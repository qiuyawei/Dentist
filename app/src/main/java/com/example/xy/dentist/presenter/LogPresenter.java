package com.example.xy.dentist.presenter;

import android.app.Activity;
import android.util.Log;

import com.example.xy.dentist.bean.AppointBean;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.TimeSetBean;
import com.example.xy.dentist.contract.LogContract;
import com.google.gson.Gson;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import rx.Subscriber;

/**
 * Created by XY on 2017/11/3.
 */
public class LogPresenter extends LogContract.Presenter {
    @Override
    public void doctor_getUserInfo(String doctor_token, String id) {
//        Log.i("TAG","id="+id);
//        Log.i("TAG","doctor_token="+doctor_token);
//
//        Log.i("TAG","getDInfor");
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.doctor_getUserInfo(doctor_token, id).subscribe(new Subscriber<BaseResult<BaseListResult<String,AppointBean>>>() {
            @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorTip("" + e.getMessage());
                Log.i("TAG","onError="+e.getMessage());

            }

            @Override
            public void onNext(BaseResult<BaseListResult<String,AppointBean>> result) {
//                Log.i("TAG","onNext=");
//
//                Log.i("TAG","coddd="+result.code);
//                Log.i("TAG","infor="+result.data.info.toString());
//                Log.i("TAG","mess="+result.data.message);
//                if(result.data!=null&&result.data.info!=null&&result.data.info.tags!=null)
//                Log.i("TAG","mess="+result.data.info.tags.size());

                if (result != null) {
                    try {
                        if(result.code==200){
                            mView.setData(result.data.info, result.message);
                        }else{
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
    public void tags() {
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.tags().subscribe(new Subscriber<BaseResult<BaseListResult<TimeSetBean, String>>>() {
            @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorTip("" + e.getMessage());
                com.example.xy.dentist.utils.LogUtils.print("Error",e);
            }

            @Override
            public void onNext(BaseResult<BaseListResult<TimeSetBean,String>> result) {
                com.example.xy.dentist.utils.LogUtils.print("Error",new Gson().toJson(result));

                if (result != null) {
                    try {
                        if (result.code == 200) {
                            mView.setTag(result.data.list);
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
    public void doctor_judge(String doctor_token, String id, String content, String tooth, String tags) {

        mView.showLoading("评价提交中...");
        mRxManage.add(mModel.doctor_judge(doctor_token,id,content,tooth,tags).subscribe(new Subscriber<BaseResult>() {
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
                    try {
                        if (result.code == 200) {
                            mView.set_judge(result);
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
