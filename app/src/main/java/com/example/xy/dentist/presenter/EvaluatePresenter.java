package com.example.xy.dentist.presenter;

import android.app.Activity;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.RecruitBean;
import com.example.xy.dentist.contract.EvaluateContract;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import rx.Subscriber;

/**
 * Created by XY on 2017/9/11.
 */
public class EvaluatePresenter extends EvaluateContract.Presenter {



    @Override
    public void recruit(String doctor_token, String page, String limit, String distict_id, String time_desc) {
        if (Integer.parseInt(page) <= 1)
//        LoadingDialog.showDialogForLoading((Activity) mContext);
        mView.showLoading("加载中...");
        mRxManage.add(mModel.recruit(doctor_token, page, limit,  distict_id,  time_desc).subscribe(new Subscriber<BaseResult<BaseListResult<RecruitBean, String>>>() {
            @Override
            public void onCompleted() {
                mView.stopLoading();
            }

            @Override
            public void onError(Throwable e) {
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(BaseResult<BaseListResult<RecruitBean,String>> result) {
                switch (result.code) {
                    case 200:
                        mView.setRecruitListData(result.data.list,result.message);
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }

            }
        }));
    }

    @Override
    public void recruit_info(String doctor_token, String id) {
        LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.recruit_info(doctor_token, id).subscribe(new Subscriber<BaseResult<BaseListResult<String,RecruitBean>>>() {
            @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(BaseResult<BaseListResult<String,RecruitBean>> result) {
                switch (result.code) {
                    case 200:
                        mView.setRecruit_infoData(result.data.info,result.message);
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }

            }
        }));
    }
}
