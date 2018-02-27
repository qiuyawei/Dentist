package com.example.xy.dentist.model;

import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.InfoBean;
import com.example.xy.dentist.contract.UserContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/10/12.
 */
public class UserModel implements UserContract.Model {



    @Override
    public Observable<BaseResult<InfoBean>> getMyInfo(String user_token) {
        return Api.getDefault(HostType.NETEASE_DRIVER).getMyInfo(user_token).map(new Func1<BaseResult<InfoBean>, BaseResult<InfoBean>>() {
            @Override
            public BaseResult<InfoBean> call(BaseResult<InfoBean> infoBeanBaseResult) {
                return infoBeanBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<InfoBean>>io_main());
    }

    @Override
    public Observable<BaseResult<InfoBean>> doctor_getMyInfo(String doctor_token) {
        return Api.getDefault(HostType.NETEASE_DRIVER).doctor_getMyInfo(doctor_token).map(new Func1<BaseResult<InfoBean>, BaseResult<InfoBean>>() {
            @Override
            public BaseResult<InfoBean> call(BaseResult<InfoBean> infoBeanBaseResult) {
                return infoBeanBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<InfoBean>>io_main());
    }

    @Override
    public Observable<BaseResult> updateInfo(String doctor_token, String introduce, String skill, String resume,String experice) {

        return Api.getDefault(HostType.NETEASE_DRIVER).doctor_changeMyInfo(doctor_token, "", "", "",introduce,skill,resume,experice).map(new Func1<BaseResult, BaseResult>() {
            @Override
            public BaseResult call(BaseResult baseResult) {
                return baseResult;
            }
        }).compose(RxSchedulers.<BaseResult>io_main());
    }
}
