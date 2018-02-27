package com.example.xy.dentist.presenter;


import android.app.Activity;
import android.util.Log;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.contract.FeedbackContract;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import rx.Subscriber;

/**
 * Created by XY on 2017/9/25.
 */
public class FeedbackPresenter extends FeedbackContract.Presenter{
    @Override
    public void commitfeedback(boolean isdoc, String driver_token, String content) {
        LoadingDialog.showDialogForLoading((Activity) mContext);

        if(!isdoc){
            mRxManage.add(mModel.commitfeedback(driver_token, content).subscribe(new Subscriber<BaseResult>() {
                @Override
                public void onCompleted() {
                    LoadingDialog.cancelDialogForLoading();
                }

                @Override
                public void onError(Throwable e) {
                    ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
                }

                @Override
                public void onNext(BaseResult result) {
                    Log.i("TAG","feedResult="+result.code+"++="+result.message);
                    switch (result.code) {
                        case 200:
                            mView.commitState(result);
                            break;
                        case 400:
                            ToastUitl.showShort(result.message);
                            break;

                    }

                }
            }));
        }else{
            mRxManage.add(mModel.commitdocfeedback(driver_token, content).subscribe(new Subscriber<BaseResult>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
                }

                @Override
                public void onNext(BaseResult result) {
                    switch (result.code) {
                        case 200:
                            mView.commitState(result);
                            break;
                        case 400:
                            ToastUitl.showShort(result.message);
                            break;

                    }

                }
            }));
        }

    }
}
