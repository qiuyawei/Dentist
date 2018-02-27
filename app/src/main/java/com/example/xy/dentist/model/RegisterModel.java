package com.example.xy.dentist.model;

import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.Userbean;
import com.example.xy.dentist.viewholder.RegisterContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/10/23.
 */
public class RegisterModel implements RegisterContract.Model{
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
    public Observable<BaseResult<Userbean>> register(String new_password, String phone, String phone_captcha, String terminal, String code) {
        return Api.getDefault(HostType.NETEASE_DRIVER).register(new_password,phone,phone_captcha,terminal,code).map(new Func1<BaseResult<Userbean>, BaseResult<Userbean>>() {
            @Override
            public BaseResult<Userbean> call(BaseResult<Userbean> loginBeanBaseResult) {
                return loginBeanBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<Userbean>>io_main());
    }
}
