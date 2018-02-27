package com.example.xy.dentist.model;

import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.AppointuBean;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.Userbean;
import com.example.xy.dentist.bean.VersionBean;
import com.example.xy.dentist.contract.AppointmentContract;
import com.example.xy.dentist.contract.VersionContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/11/1.
 */
public class VersonModel implements  VersionContract.Model {

    @Override
    public Observable<BaseResult<VersionBean>> getVersion(String user_token) {
        return Api.getDefault(HostType.NETEASE_DRIVER).getAppVersion(user_token).map(new Func1<BaseResult<VersionBean>, BaseResult<VersionBean>>() {
            @Override
            public BaseResult<VersionBean> call(BaseResult<VersionBean> loginBeanBaseResult) {
                return loginBeanBaseResult;
            }
        }) .compose(RxSchedulers.<BaseResult<VersionBean>>io_main());
    }
}
