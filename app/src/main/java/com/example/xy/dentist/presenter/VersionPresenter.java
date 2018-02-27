package com.example.xy.dentist.presenter;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.Doctorbean;
import com.example.xy.dentist.bean.StateBean;
import com.example.xy.dentist.bean.UserTypeBean;
import com.example.xy.dentist.bean.Userbean;
import com.example.xy.dentist.bean.VersionBean;
import com.example.xy.dentist.contract.LoginContract;
import com.example.xy.dentist.contract.VersionContract;
import com.example.xy.dentist.utils.LogUtils;
import com.google.gson.Gson;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import rx.Subscriber;

/**
 * Created by XY on 2017/9/21.
 */
public class VersionPresenter extends VersionContract.Presenter{


    @Override
    public void getVersion(String  user_token) {
        mRxManage.add(mModel.getVersion(user_token).subscribe(new Subscriber<BaseResult<VersionBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtils.print("error",e.getMessage());
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(BaseResult<VersionBean> result) {
//                LogUtils.print("resultsssssss////",new Gson().toJson(result));

                switch (result.code) {
                    case 200:
                        mView.setVersion(result.data);
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }

            }
        }));
    }


}
