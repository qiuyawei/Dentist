package com.example.xy.dentist.model;


import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.RecruitBean;
import com.example.xy.dentist.contract.EvaluateContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/9/11.
 */
public class EvaluateModel implements  EvaluateContract.Model {



    @Override
    public Observable<BaseResult<BaseListResult<RecruitBean, String>>> recruit(String doctor_token, String page, String limit, String distict_id, String time_desc) {
        return Api.getDefault(HostType.NETEASE_DRIVER).recruit(doctor_token,page,limit,distict_id,time_desc).map(new Func1<BaseResult<BaseListResult<RecruitBean, String>>, BaseResult<BaseListResult<RecruitBean, String>>>() {
            @Override
            public BaseResult<BaseListResult<RecruitBean, String>> call(BaseResult<BaseListResult<RecruitBean, String>> baseListResultBaseResult) {
                return baseListResultBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<BaseListResult<RecruitBean, String>>>io_main());
    }

    @Override
    public Observable<BaseResult<BaseListResult<String, RecruitBean>>> recruit_info(String doctor_token, String id) {
        return Api.getDefault(HostType.NETEASE_DRIVER).recruit_info(doctor_token, id).map(new Func1<BaseResult<BaseListResult<String, RecruitBean>>, BaseResult<BaseListResult<String, RecruitBean>>>() {
            @Override
            public BaseResult<BaseListResult<String, RecruitBean>> call(BaseResult<BaseListResult<String, RecruitBean>> baseListResultBaseResult) {
                return baseListResultBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<BaseListResult<String, RecruitBean>>>io_main());
    }
}
