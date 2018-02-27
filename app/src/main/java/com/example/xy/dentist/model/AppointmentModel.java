package com.example.xy.dentist.model;

import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.AppointuBean;
import com.example.xy.dentist.bean.AppointuBean;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.contract.AppointmentContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/11/1.
 */
public class AppointmentModel implements  AppointmentContract.Model {
    @Override
    public Observable<BaseResult<BaseListResult<AppointuBean,String>>> getListDatas(String user_token, String page,  String limit, String type, int rows) {
        return Api.getDefault(HostType.NETEASE_DRIVER).myDate(user_token, page, limit ,type).map(new Func1<BaseResult<BaseListResult<AppointuBean, String>>, BaseResult<BaseListResult<AppointuBean, String>>>() {
            @Override
            public BaseResult<BaseListResult<AppointuBean, String>> call(BaseResult<BaseListResult<AppointuBean, String>> baseListResultBaseResult) {
                return baseListResultBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<BaseListResult<AppointuBean, String>>>io_main());
    }
}
