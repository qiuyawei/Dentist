package com.example.xy.dentist.presenter;


import android.app.Activity;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.Doctorbean;
import com.example.xy.dentist.bean.UserTypeBean;
import com.example.xy.dentist.bean.Userbean;
import com.example.xy.dentist.contract.ForgetContract;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import rx.Subscriber;

/**
 * Created by XY on 2017/9/21.
 */
public class ForgetPresenter extends ForgetContract.Presenter {
    @Override
    public void getcaptcha(String phone) {
//        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.getcaptcha(phone).subscribe(new Subscriber<BaseResult>() {
            @Override
            public void onCompleted() {
//                LoadingDialog.cancelDialogForLoading();
                com.example.xy.dentist.utils.LogUtils.print("ForgetPresenteronCompleted","");

            }

            @Override
            public void onError(Throwable e) {
                com.example.xy.dentist.utils.LogUtils.print("ForregErr",e.getMessage());
                ToastUitl.showShort("获取失败，请稍后重新获取");

            }

            @Override
            public void onNext(BaseResult baseResult) {
                mView.captcha(baseResult);
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
                com.example.xy.dentist.utils.LogUtils.print("KKKK","CANCELL");
            }

            @Override
            public void onError(Throwable e) {
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(BaseResult<UserTypeBean> result) {
                com.example.xy.dentist.utils.LogUtils.print("codeGG",result.code);

                switch (result.code){
                    case 200:
                        mView.checkUserTypeState(result);
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }

            }
        }));
    }

    @Override
    public void updatePassw(String new_password, String phone, String phone_captcha) {
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.updatePassw(new_password,phone,phone_captcha).subscribe(new Subscriber<BaseResult<Userbean>>() {
            @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResult<Userbean> baseResult) {
                if (baseResult.code==200){

                    mView.UpdateState(baseResult);
                }else{
                    ToastUitl.showShort(baseResult.message);
                }

            }
        }));
    }

    @Override
    public void docupdatePassw(String new_password, String phone, String phone_captcha) {
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.docupdatePassw(new_password, phone, phone_captcha).subscribe(new Subscriber<BaseResult<Doctorbean>>() {
            @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResult<Doctorbean> baseResult) {
                if (baseResult.code==200){

                    mView.docUpdateState(baseResult);
                }else{
                    ToastUitl.showShort(baseResult.message);
                }

            }
        }));
    }
}
