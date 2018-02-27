package com.example.xy.dentist.model;


import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.contract.FeedbackContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/9/25.
 */
public class FeedbackModel implements FeedbackContract.Model{
    @Override
    public Observable<BaseResult> commitfeedback(String user_token, String content) {
        return Api.getDefault(HostType.NETEASE_DRIVER).feedback(user_token,content).map(new Func1<BaseResult, BaseResult>() {
            @Override
            public BaseResult call(BaseResult baseResult) {
                return baseResult;
            }
        }).compose(RxSchedulers.<BaseResult>io_main());
    }

    @Override
    public Observable<BaseResult> commitdocfeedback(String driver_token, String content) {
        return Api.getDefault(HostType.NETEASE_DRIVER).doctor_feedback(driver_token, content).map(new Func1<BaseResult, BaseResult>() {
            @Override
            public BaseResult call(BaseResult baseResult) {
                return baseResult;
            }
        }).compose(RxSchedulers.<BaseResult>io_main());
    }
}
