package com.example.xy.dentist.model;

import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.contract.MineContract;
import com.example.xy.dentist.utils.DatasUtil;
import com.jaydenxiao.common.baserx.RxSchedulers;
import com.jaydenxiao.common.commonutils.LogUtils;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XY on 2017/10/12.
 */
public class MineModel implements MineContract.Model {

    /**
     * 获取列表
     *
     * @param type
     * @param userId
     * @param page
     * @param rows
     * @return
     */
    @Override
    public Observable<BaseResult> getListDatas(String type, String userId, int page, int rows) {
        return Observable.create(new Observable.OnSubscribe<BaseResult>() {
            @Override
            public void call(Subscriber<? super BaseResult> subscriber) {
                BaseResult result = DatasUtil.getZoneListDatas();
                subscriber.onNext(result);
                subscriber.onCompleted();
                LogUtils.logd("result" + result.toString());
            }
        }).compose(RxSchedulers.<BaseResult>io_main());
    }
}
