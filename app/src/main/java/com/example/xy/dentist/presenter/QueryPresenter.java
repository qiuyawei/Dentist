package com.example.xy.dentist.presenter;

import android.app.Activity;

import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.ClinicBean;
import com.example.xy.dentist.contract.QueryContract;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import rx.Subscriber;

/**
 * Created by XY on 2017/11/1.
 */
public class QueryPresenter  extends QueryContract.Presenter {
    @Override
    public void getListData(String user_token, String page, String limit, String latitude, String longitude, String distict_id, int rows) {
        //加载更多不显示加载条
        if (Integer.parseInt(page) <= 1)
            mView.showLoading("加载中...");
        mRxManage.add(mModel.getListDatas(user_token, page, limit, latitude, longitude,distict_id,rows).subscribe(new Subscriber<BaseResult<BaseListResult<ClinicBean,String>>>() {
            @Override
            public void onCompleted() {
                mView.stopLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorTip("" + e.getMessage());
            }

            @Override
            public void onNext(BaseResult<BaseListResult<ClinicBean,String>> result) {
                if (result != null) {
                    try {
                        if(result.code==200){
                            mView.setListData(result.data.list,result.message);
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
}
