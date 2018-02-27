package com.example.xy.dentist.model;

import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.ClinicBean;
import com.example.xy.dentist.bean.DoctorDetailbean;
import com.example.xy.dentist.bean.UserInfo;
import com.example.xy.dentist.contract.QueryDetailContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/11/2.
 */
public class QueryDetailModel implements QueryDetailContract.Model {

    @Override
    public  Observable<BaseResult<BaseListResult<UserInfo,ClinicBean>>> getInfoData(String id, String latitude, String longitude) {
        return Api.getDefault(HostType.NETEASE_DRIVER).info(id, latitude, longitude).map(new Func1<BaseResult<BaseListResult<UserInfo, ClinicBean>>, BaseResult<BaseListResult<UserInfo, ClinicBean>>>() {
            @Override
            public BaseResult<BaseListResult<UserInfo, ClinicBean>> call(BaseResult<BaseListResult<UserInfo, ClinicBean>> baseListResultBaseResult) {
                return baseListResultBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<BaseListResult<UserInfo, ClinicBean>>>io_main());

    }

    @Override
    public Observable<BaseResult<BaseListResult<String, DoctorDetailbean>>> doctorInfo(String id) {
        return Api.getDefault(HostType.NETEASE_DRIVER).clinic_detail(id).map(new Func1<BaseResult<BaseListResult<String, DoctorDetailbean>>, BaseResult<BaseListResult<String, DoctorDetailbean>>>() {
            @Override
            public BaseResult<BaseListResult<String, DoctorDetailbean>> call(BaseResult<BaseListResult<String, DoctorDetailbean>> baseListResultBaseResult) {
                return baseListResultBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<BaseListResult<String, DoctorDetailbean>>>io_main());
    }

    @Override
    public Observable<BaseResult> appointment(String user_token, String clinic_id, String doctor_id,String type) {
        return Api.getDefault(HostType.NETEASE_DRIVER).appoint(user_token, clinic_id, doctor_id,type).map(new Func1<BaseResult, BaseResult>() {
            @Override
            public BaseResult call(BaseResult result) {
                return result;
            }
        }).compose(RxSchedulers.<BaseResult>io_main());

    }
}
