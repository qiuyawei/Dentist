package com.example.xy.dentist.presenter;

import android.app.Activity;
import android.util.Log;

import com.example.xy.dentist.bean.AppointuBean;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.contract.AppointmentContract;
import com.google.gson.Gson;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import rx.Subscriber;

/**
 * Created by XY on 2017/11/1.
 */
public class AppointmentPresenter extends AppointmentContract.Presenter {
    @Override
    public void getListData(String user_token, String page, String limit, String type, int rows) {
//        Log.i("TAG", "type=" + type);
        //加载更多不显示加载条
        if (Integer.parseInt(page) <= 1)
//            LoadingDialog.showDialogForLoading((Activity) mContext);
            mView.showLoading("加载中...");
        mRxManage.add(mModel.getListDatas(user_token, page, limit, type, rows).subscribe(new Subscriber<BaseResult<BaseListResult<AppointuBean, String>>>() {
            @Override
            public void onCompleted() {
                mView.stopLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorTip("" + e.getMessage());
                Log.i("TAG", "onError=" + e.getMessage());

            }

            @Override
            public void onNext(BaseResult<BaseListResult<AppointuBean, String>> result) {
//                Log.i("TAG", "resultCode=" + new Gson().toJson(result));

                if (result != null) {
                    try {
                        if (result.code == 200) {
                            mView.setListData(result.data.list, result.message);
//                            Log.i("TAG", "size=" + result.data.list.size());
//                            Log.i("TAG", "message=" + result.message);
//                            Gson gson = new Gson();
//                            String json = gson.toJson(result.data.list);
//                            Log.i("TAG", "json=" + json);
//                            if(result.data.list.get(0)==null){
//                                Log.i("TAG","00=null");
//                            }else {
//                                Log.i("TAG","00="+result.data.list.get(0).avatar);
//
//                            }
//                            Log.i("TAG","size="+result.data.list.size());
//                            Log.i("TAG","size="+result.data.list.size());

                        } else {
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
