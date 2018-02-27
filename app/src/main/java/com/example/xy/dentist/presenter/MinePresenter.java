package com.example.xy.dentist.presenter;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.aspsine.irecyclerview.bean.PageBean;
import com.example.xy.dentist.bean.BaseBean;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.contract.MineContract;
import com.jaydenxiao.common.commonutils.JsonUtils;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

import java.util.List;

import rx.Subscriber;

/**
 * Created by XY on 2017/10/12.
 */
public class MinePresenter extends MineContract.Presenter {
    @Override
    public void getListData(String type, String userId, int page, int rows) {
        //加载更多不显示加载条
        if (page <= 1)
            LoadingDialog.showDialogForLoading((Activity) mContext);
        mRxManage.add(mModel.getListDatas(type, userId, page, rows).subscribe(new Subscriber<BaseResult>() {
            @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorTip("" + e.getMessage());
            }

            @Override
            public void onNext(BaseResult result) {
                if (result != null) {
                    try {
                        List<BaseBean> circleItems = JSON.parseArray(JsonUtils.getValue(result.msg, "list"), BaseBean.class);

                        PageBean pageBean = JSON.parseObject(JsonUtils.getValue(result.msg, "page"), PageBean.class);
                        mView.setListData(circleItems, pageBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }));
    }
}
