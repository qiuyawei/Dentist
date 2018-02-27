package com.example.xy.dentist.presenter;

import android.app.Activity;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.contract.EvaluatContract;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import rx.Subscriber;

/**
 * Created by XY on 2017/11/2.
 */
public class EvaluatPresenter extends EvaluatContract.Presenter{


    @Override
    public void judge(String user_token, String id, String comment, String star) {
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.judge(user_token, id,comment,star).subscribe(new Subscriber<BaseResult>() {
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
                switch (result.code) {
                    case 200:
                        mView.judgeState(result);
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }

            }
        }));
    }
}
