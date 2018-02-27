package com.example.xy.dentist.model;

import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.AppointBean;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.TimeSetBean;
import com.example.xy.dentist.contract.LogContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/11/3.
 */
public class LogModel implements LogContract.Model {


    @Override
    public Observable<BaseResult<BaseListResult<String, AppointBean>>> doctor_getUserInfo(String doctor_token, String id) {
        return Api.getDefault(HostType.NETEASE_DRIVER).doctor_getUserInfo(doctor_token, id).map(new Func1<BaseResult<BaseListResult<String, AppointBean>>, BaseResult<BaseListResult<String, AppointBean>>>() {
            @Override
            public BaseResult<BaseListResult<String, AppointBean>> call(BaseResult<BaseListResult<String, AppointBean>> baseListResultBaseResult) {
                return baseListResultBaseResult;
            }
        }) .compose(RxSchedulers.< BaseResult<BaseListResult<String, AppointBean>>>io_main());
    }

    @Override
    public Observable<BaseResult<BaseListResult<TimeSetBean, String>>> tags() {
        return Api.getDefault(HostType.NETEASE_DRIVER).tags().map(new Func1<BaseResult<BaseListResult<TimeSetBean, String>>, BaseResult<BaseListResult<TimeSetBean, String>>>() {
            @Override
            public BaseResult<BaseListResult<TimeSetBean, String>> call(BaseResult<BaseListResult<TimeSetBean, String>> baseListResultBaseResult) {
                return baseListResultBaseResult;
            }
        }) .compose(RxSchedulers.< BaseResult<BaseListResult<TimeSetBean, String>>>io_main());
    }

    @Override
    public Observable<BaseResult> doctor_judge(String doctor_token, String id, String content, String tooth, String tags) {
        return Api.getDefault(HostType.NETEASE_DRIVER).doctor_judge(doctor_token,id,content,tooth,tags).map(new Func1<BaseResult, BaseResult>() {
            @Override
            public BaseResult call(BaseResult result) {
                return result;
            }
        }).compose(RxSchedulers.<BaseResult>io_main());
    }
}
