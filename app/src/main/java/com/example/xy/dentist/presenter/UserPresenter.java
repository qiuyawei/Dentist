package com.example.xy.dentist.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.InfoBean;
import com.example.xy.dentist.contract.UserContract;
import com.example.xy.dentist.utils.LogUtils;
import com.google.gson.Gson;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import rx.Subscriber;

/**
 * Created by XY on 2017/10/12.
 */
public class UserPresenter extends UserContract.Presenter {


    @Override
    public void getMyInfo(String user_token) {
        mView.showLoading("加载中...");
        mRxManage.add(mModel.getMyInfo(user_token).subscribe(new Subscriber<BaseResult<InfoBean>>() {
            @Override
            public void onCompleted() {
                mView.stopLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorTip("" + e.getMessage());
            }

            @Override
            public void onNext(BaseResult<InfoBean> result) {
                LogUtils.print("病例=",new Gson().toJson(result));
                if (result != null) {
                    switch (result.code){
                        case 200:
                            mView.setMyInfo(result);
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
    public void getDoctor_MyInfo(String doctor_token) {
//        LoadingDialog.showDialogForLoading((Activity) mContext);
        mView.showLoading("加载中...");
        mRxManage.add(mModel.doctor_getMyInfo(doctor_token).subscribe(new Subscriber<BaseResult<InfoBean>>() {
            @Override
            public void onCompleted() {
               mView.stopLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorTip("" + e.getMessage());
            }

            @Override
            public void onNext(BaseResult<InfoBean> result) {

                if (result != null) {
//                    Log.i("TAG","result="+result.data.info.age);
//                    Log.i("TAG","result="+result.data.info.name);
                    switch (result.code){
                        case 200:
                            mView.setDocInfo(result);
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
    public void updateInfo(String doctor_token, final String introduce, final String skill, final String resume,final String experice) {
        //mView.showLoading("更新中...");
        mRxManage.add(mModel.updateInfo(doctor_token, introduce, skill, resume,experice).subscribe(new Subscriber<BaseResult>() {
            @Override
            public void onCompleted() {
             //   LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(BaseResult result) {
                switch (result.code) {
                    case 200:
                        String type = null;
                        String content = null;
                        if (!TextUtils.isEmpty(introduce)) {
                            type = "0";
                            content=introduce;
                        } else if (!TextUtils.isEmpty(skill)) {
                            type = "1";
                            content=skill;
                        } else if (!TextUtils.isEmpty(resume)) {
                            type = "2";
                            content=resume;
                        } else if (!TextUtils.isEmpty(experice)) {
                            type = "3";
                            content=experice;
                        }
                        mView.updateInfo(result, type,content);
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }

            }
        }));
    }
}
