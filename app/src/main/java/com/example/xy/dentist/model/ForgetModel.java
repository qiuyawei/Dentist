package com.example.xy.dentist.model;


import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.Doctorbean;
import com.example.xy.dentist.bean.UserTypeBean;
import com.example.xy.dentist.bean.Userbean;
import com.example.xy.dentist.contract.ForgetContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/9/21.
 */
public class ForgetModel implements ForgetContract.Model {
    @Override
    public Observable<BaseResult> getcaptcha(String phone) {
        return Api.getDefault(HostType.NETEASE_DRIVER).captcha(phone).map(new Func1<BaseResult, BaseResult>() {
            @Override
            public BaseResult call(BaseResult baseResult) {
                return baseResult;
            }
        }).compose(RxSchedulers.<BaseResult>io_main());
    }

    @Override
    public Observable<BaseResult<Userbean>> updatePassw(String new_password, String phone, String phone_captcha) {
        return Api.getDefault(HostType.NETEASE_DRIVER).forget_password(new_password, phone, phone_captcha).map(new Func1<BaseResult<Userbean>, BaseResult<Userbean>>() {
            @Override
            public BaseResult<Userbean> call(BaseResult<Userbean> loginBeanBaseResult) {
                return loginBeanBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<Userbean>>io_main());
    }

    @Override
    public Observable<BaseResult<UserTypeBean>> checkUserType(String phone) {
        return Api.getDefault(HostType.NETEASE_DRIVER).checkUserType(phone).map(new Func1<BaseResult<UserTypeBean>, BaseResult<UserTypeBean>>() {
            @Override
            public BaseResult<UserTypeBean> call(BaseResult<UserTypeBean> userTypeBeanBaseResult) {
                return userTypeBeanBaseResult;
            }
        }) .compose(RxSchedulers.<BaseResult<UserTypeBean>>io_main());
    }

    @Override
    public Observable<BaseResult<Doctorbean>> docupdatePassw(String new_password, String phone, String phone_captcha) {
        return Api.getDefault(HostType.NETEASE_DRIVER).doctor_forget_password(new_password, phone, phone_captcha).map(new Func1<BaseResult<Doctorbean>, BaseResult<Doctorbean>>() {
            @Override
            public BaseResult<Doctorbean> call(BaseResult<Doctorbean> loginBeanBaseResult) {
                return loginBeanBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<Doctorbean>>io_main());
    }

}
