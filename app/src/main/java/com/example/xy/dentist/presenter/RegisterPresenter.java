package com.example.xy.dentist.presenter;

import android.app.Activity;
import android.util.Log;

import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.Userbean;
import com.example.xy.dentist.viewholder.RegisterContract;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import rx.Subscriber;

/**
 * Created by XY on 2017/10/23.
 */
public class RegisterPresenter  extends RegisterContract.Presenter{
    @Override
    public void getcaptcha(String phone) {
//        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.getcaptcha(phone).subscribe(new Subscriber<BaseResult>() {
            @Override
            public void onCompleted() {
//                LoadingDialog.cancelDialogForLoading();
//                com.example.xy.dentist.utils.LogUtils.print("onCompleted","");

            }

            @Override
            public void onError(Throwable e) {
//                com.example.xy.dentist.utils.LogUtils.print("regErr",e.getMessage());
                ToastUitl.showShort("获取失败，请稍后重新获取");

            }

            @Override
            public void onNext(BaseResult baseResult) {

                mView.captcha(baseResult);
            }
        }));
    }

    @Override
    public void register(String password, String phone, String phone_captcha, String terminal,String code) {
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.register(password, phone, phone_captcha,terminal,code).subscribe(new Subscriber<BaseResult<Userbean>>() {
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
}
