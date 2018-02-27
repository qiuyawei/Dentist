package com.example.xy.dentist.model;

import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.TimeSetBean;
import com.example.xy.dentist.contract.TimeContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/11/3.
 */
public class TimeModel implements TimeContract.Model {





    @Override
    public Observable<BaseResult> setTime(String doctor_token, String id, String date_id,String date) {
        return Api.getDefault(HostType.NETEASE_DRIVER).setTime(doctor_token, id, date_id,date).map(new Func1<BaseResult, BaseResult>() {
            @Override
            public BaseResult call(BaseResult result) {
                return result;
            }
        }).compose(RxSchedulers.<BaseResult>io_main());
    }

    @Override
    public Observable<BaseResult<BaseListResult<TimeSetBean, String>>> doctor_getTimeList(String doctor_token) {
        return Api.getDefault(HostType.NETEASE_DRIVER).doctor_getTimeList(doctor_token).map(new Func1<BaseResult<BaseListResult<TimeSetBean, String>>, BaseResult<BaseListResult<TimeSetBean, String>>>() {
            @Override
            public BaseResult<BaseListResult<TimeSetBean, String>> call(BaseResult<BaseListResult<TimeSetBean, String>> baseListResultBaseResult) {
                return baseListResultBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<BaseListResult<TimeSetBean, String>>>io_main());
    }
    @Override
    public Observable<BaseResult<BaseListResult<TimeSetBean, String>>> doctor_getTimeListNew(String doctor_token,String date) {
        return Api.getDefault(HostType.NETEASE_DRIVER).doctor_getTimeListNew(doctor_token,date).map(new Func1<BaseResult<BaseListResult<TimeSetBean, String>>, BaseResult<BaseListResult<TimeSetBean, String>>>() {
            @Override
            public BaseResult<BaseListResult<TimeSetBean, String>> call(BaseResult<BaseListResult<TimeSetBean, String>> baseListResultBaseResult) {
                return baseListResultBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<BaseListResult<TimeSetBean, String>>>io_main());
    }
}
