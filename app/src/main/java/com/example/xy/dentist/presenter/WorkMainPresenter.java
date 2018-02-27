package com.example.xy.dentist.presenter;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.StateBean;
import com.example.xy.dentist.contract.WorkMainContract;
import com.example.xy.dentist.utils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;

import rx.Subscriber;

/**
 * Created by XY on 2017/11/10.
 */
public class WorkMainPresenter extends WorkMainContract.Presenter {
    @Override
    public void updateInfo(String doctor_token, String status) {
        mRxManage.add(mModel.updateInfo(doctor_token, status).subscribe(new Subscriber<BaseResult<StateBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtils.print("error",e.getMessage());
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(BaseResult<StateBean> result) {
                LogUtils.print("result",result.code);

                switch (result.code) {
                    case 200:
                        mView.updateState(result);
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }

            }
        }));
    }
}
