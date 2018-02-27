package com.example.xy.dentist.model;

import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.StateBean;
import com.example.xy.dentist.contract.WorkMainContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/11/10.
 */
public class WorkMainModel implements WorkMainContract.Model {
    @Override
    public Observable<BaseResult<StateBean>> updateInfo(String doctor_token, String status) {
        return Api.getDefault(HostType.NETEASE_DRIVER).status(doctor_token, status).map(new Func1<BaseResult<StateBean>, BaseResult<StateBean>>() {
            @Override
            public BaseResult<StateBean> call(BaseResult<StateBean> stateBeanBaseResult) {
                return stateBeanBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<StateBean>>io_main());
    }
}
