package com.example.xy.dentist.presenter;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.Doctorbean;
import com.example.xy.dentist.bean.Meassagebean;
import com.example.xy.dentist.bean.UserTypeBean;
import com.example.xy.dentist.bean.Userbean;
import com.example.xy.dentist.contract.LoginContract;
import com.example.xy.dentist.contract.MessageContract;
import com.example.xy.dentist.utils.LogUtils;
import com.google.gson.Gson;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import rx.Subscriber;

/**
 * Created by XY on 2017/9/21.
 */
public class MessagePresenter extends MessageContract.Presenter {
    @Override
    public void getListDatas(String uid, String states) {
        mRxManage.add(mModel.getListDatas(uid, states).subscribe(new Subscriber<BaseResult<BaseListResult<Meassagebean, String>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Log.i("TAG","throwable="+throwable.getMessage()+throwable.getLocalizedMessage());

            }

            @Override
            public void onNext(BaseResult<BaseListResult<Meassagebean, String>> result) {
                Log.i("TAG","result"+new Gson().toJson(result));
                switch (result.code) {
                    case 200:
                        mView.setListData(result.data.list, result.message);
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }
            }
        }));


   /* @Override
    public void getListDatas(String uid, String states) {
        mRxManage.add(mModel.getListDatas(uid, states).subscribe(new Subscriber<BaseResult<Meassagebean>>() {
            @Override
            public void onCompleted() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        LoadingDialog.cancelDialogForLoading();
                        LoadingDialog.cancelDialogForLoading();
                    }
                },1000);
            }

            @Override
            public void onError(Throwable e) {
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(BaseResult<Meassagebean> result) {
                switch (result.code){
                    case 200:
                        mView.setListData(result,result.message);
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }

            }
        }));*/
    }


  /*  @Override
    public void login(String phone, String password) {
//        LoadingDialog.showDialogForLoading((Activity) mContext);
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.login(phone, password).subscribe(new Subscriber<BaseResult<Userbean>>() {
            @Override
            public void onCompleted() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        LoadingDialog.cancelDialogForLoading();
                        LoadingDialog.cancelDialogForLoading();
                    }
                },1000);
            }

            @Override
            public void onError(Throwable e) {
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(BaseResult<Userbean> result) {
                switch (result.code){
                    case 200:
                       mView.loginState(result);
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }

            }
        }));
    }

    @Override
    public void thirdLogin(String type, String code) {
//        LoadingDialog.showDialogForLoading((Activity) mContext);
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.thirdLogin(type, code).subscribe(new Subscriber<BaseResult<Userbean>>() {
            @Override
            public void onCompleted() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        LoadingDialog.cancelDialogForLoading();
                        LoadingDialog.cancelDialogForLoading();
                    }
                },1000);
            }

            @Override
            public void onError(Throwable e) {
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(BaseResult<Userbean> result) {
                switch (result.code){
                    case 200:
                        mView.thirdLoginState(result);
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }

            }
        }));
    }

    @Override
    public void bindLogin(String phone, String password, String type, String code) {
        Log.i("TAG","phone="+phone);
        Log.i("TAG","password="+password);
        Log.i("TAG","type="+type);
        Log.i("TAG","code="+code);

        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.bindLogin(phone, password,type,code).subscribe(new Subscriber<BaseResult<Userbean>>() {
            @Override
            public void onCompleted() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        LoadingDialog.cancelDialogForLoading();
                        LoadingDialog.cancelDialogForLoading();
                    }
                },1000);
            }

            @Override
            public void onError(Throwable e) {
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(BaseResult<Userbean> result) {
                switch (result.code){
                    case 200:
                        mView.bindLoginState(result);
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }

            }
        }));
    }

    @Override
    public void checkUserType(String phone) {
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.checkUserType(phone).subscribe(new Subscriber<BaseResult<UserTypeBean>>() {
            @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {
                LoadingDialog.cancelDialogForLoading();
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(BaseResult<UserTypeBean> result) {
                LoadingDialog.cancelDialogForLoading();
                switch (result.code){
                    case 200:
                        mView.checkUserTypeState(result);
                        if(result.data.type.contains("no")){
                            ToastUitl.showShort("账号不存在！");
                        }
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }

            }
        }));
    }


    @Override
    public void checkUserType2(String code,String type) {
        Log.i("TAG","code===="+code);
        Log.i("TAG","type===="+type);
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.checkUserType2(code,type).subscribe(new Subscriber<BaseResult<UserTypeBean>>() {
            @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {
                Log.i("TAG","error===="+e.getMessage());


                LoadingDialog.cancelDialogForLoading();
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(BaseResult<UserTypeBean> result) {
                LoadingDialog.cancelDialogForLoading();
                switch (result.code){
                    case 200:
                        mView.checkUserTypeState2(result);
                        if(result.data.type.contains("no")){
                            ToastUitl.showShort("账号不存在！");
                        }
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }

            }
        }));
    }


    @Override
    public void doctorlogin(String phone, String password, String type, String code) {
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.doctorlogin(phone, password,type,code).subscribe(new Subscriber<BaseResult<Doctorbean>>() {
            @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(BaseResult<Doctorbean> result) {
                LogUtils.print("resut",new Gson().toJson(result));

                switch (result.code){
                    case 200:
                        mView.doctorloginState(result);
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }

            }
        }));
    }
*/



}
