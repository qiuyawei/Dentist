package com.example.xy.dentist.model;

import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.Doctorbean;
import com.example.xy.dentist.bean.UserTypeBean;
import com.example.xy.dentist.bean.Userbean;
import com.example.xy.dentist.contract.LoginContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/9/21.
 */
public class LoginModel implements LoginContract.Model {
    @Override
    public Observable<BaseResult<Userbean>> login(String phone, String password) {
        return Api.getDefault(HostType.NETEASE_DRIVER).getLogin(phone, password).map(new Func1<BaseResult<Userbean>, BaseResult<Userbean>>() {
            @Override
            public BaseResult<Userbean> call(BaseResult<Userbean> loginBeanBaseResult) {
                return loginBeanBaseResult;
            }
        }) .compose(RxSchedulers.< BaseResult<Userbean>>io_main());

               
    }

    @Override
    public Observable<BaseResult<Userbean>> thirdUserLogin(String code, String type) {
        return Api.getDefault(HostType.NETEASE_DRIVER).thirdLogin(code, type).map(new Func1<BaseResult<Userbean>, BaseResult<Userbean>>() {
            @Override
            public BaseResult<Userbean> call(BaseResult<Userbean> loginBeanBaseResult) {
                return loginBeanBaseResult;
            }
        }) .compose(RxSchedulers.< BaseResult<Userbean>>io_main());


    }

    @Override
    public Observable<BaseResult<Userbean>> bindLogin(String phone, String password,String type,String code){
        return Api.getDefault(HostType.NETEASE_DRIVER).getBindLogin(phone, password,type,code).map(new Func1<BaseResult<Userbean>, BaseResult<Userbean>>() {
            @Override
            public BaseResult<Userbean> call(BaseResult<Userbean> loginBeanBaseResult) {
                return loginBeanBaseResult;
            }
        }) .compose(RxSchedulers.< BaseResult<Userbean>>io_main());


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
    public Observable<BaseResult<UserTypeBean>> checkUserType2(String code,String type) {
        return Api.getDefault(HostType.NETEASE_DRIVER).checkUserType2(code,type).map(new Func1<BaseResult<UserTypeBean>, BaseResult<UserTypeBean>>() {
            @Override
            public BaseResult<UserTypeBean> call(BaseResult<UserTypeBean> userTypeBeanBaseResult) {
                return userTypeBeanBaseResult;
            }
        }) .compose(RxSchedulers.<BaseResult<UserTypeBean>>io_main());
    }
    @Override
    public Observable<BaseResult<Doctorbean>> doctorlogin(String phone, String password, String type, String code) {
        return Api.getDefault(HostType.NETEASE_DRIVER).doctor_login(phone, password, type, code).map(new Func1<BaseResult<Doctorbean>, BaseResult<Doctorbean>>() {
            @Override
            public BaseResult<Doctorbean> call(BaseResult<Doctorbean> doctorbeanBaseResult) {
                return doctorbeanBaseResult;
            }
        }) .compose(RxSchedulers.< BaseResult<Doctorbean>>io_main());
    }
}
