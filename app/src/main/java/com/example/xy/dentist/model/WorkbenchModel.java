package com.example.xy.dentist.model;

import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.DoctorInfoBean;
import com.example.xy.dentist.contract.WorkbenchContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/11/3.
 */
public class WorkbenchModel implements WorkbenchContract.Model  {
    @Override
    public Observable<BaseResult<BaseListResult<DoctorInfoBean, String>>> getListData(String doctor_token, String page, String limit, String status, String name, String year, String month, String day, String start_time, String end_time, int rows) {
        return Api.getDefault(HostType.NETEASE_DRIVER).doctor_date( doctor_token,  page,  limit,  status,  name,  year,  month,  day,start_time, end_time).map(new Func1<BaseResult<BaseListResult<DoctorInfoBean, String>>, BaseResult<BaseListResult<DoctorInfoBean, String>>>() {
            @Override
            public BaseResult<BaseListResult<DoctorInfoBean, String>> call(BaseResult<BaseListResult<DoctorInfoBean, String>> baseListResultBaseResult) {
                return baseListResultBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<BaseListResult<DoctorInfoBean, String>>>io_main());
    }

    @Override
    public Observable<BaseResult> doctor_cancel(String doctor_token, String id) {
        return Api.getDefault(HostType.NETEASE_DRIVER).doctor_cancel(doctor_token, id).map(new Func1<BaseResult, BaseResult>() {
            @Override
            public BaseResult call(BaseResult result) {
                return result;
            }
        }).compose(RxSchedulers.<BaseResult>io_main());
    }
}
