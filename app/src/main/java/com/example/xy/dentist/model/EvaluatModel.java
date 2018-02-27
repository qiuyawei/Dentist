package com.example.xy.dentist.model;

import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.contract.EvaluatContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/11/2.
 */
public class EvaluatModel implements EvaluatContract.Model{

    @Override
    public Observable<BaseResult> judge(String user_token, String id, String comment, String star) {
        return Api.getDefault(HostType.NETEASE_DRIVER).judge(user_token,id,comment,star).map(new Func1<BaseResult, BaseResult>() {
            @Override
            public BaseResult call(BaseResult baseResult) {
                return baseResult;
            }
        }).compose(RxSchedulers.<BaseResult>io_main());
    }
}
