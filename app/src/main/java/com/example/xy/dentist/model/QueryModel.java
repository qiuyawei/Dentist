package com.example.xy.dentist.model;

import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.ClinicBean;
import com.example.xy.dentist.contract.QueryContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/11/1.
 */
public class QueryModel implements QueryContract.Model {
    @Override
    public Observable<BaseResult<BaseListResult<ClinicBean, String>>> getListDatas(String user_token, String page, String limit, String latitude, String longitude, String distict_id, int rows) {
        return Api.getDefault(HostType.NETEASE_DRIVER).clinic(user_token, page, limit ,latitude,longitude,distict_id).map(new Func1<BaseResult<BaseListResult<ClinicBean, String>>, BaseResult<BaseListResult<ClinicBean, String>>>() {
            @Override
            public BaseResult<BaseListResult<ClinicBean, String>> call(BaseResult<BaseListResult<ClinicBean, String>> baseListResultBaseResult) {
                return baseListResultBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<BaseListResult<ClinicBean, String>>>io_main());
    }
}
