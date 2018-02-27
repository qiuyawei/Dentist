package com.example.xy.dentist.model;

import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.PicBean;
import com.example.xy.dentist.contract.UserInfoContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/11/1.
 */
public class UserInfoModel implements UserInfoContract.Model{
    @Override
    public Observable<BaseResult> updateInfo(String user_token,String name, String avatar, String age) {
        return Api.getDefault(HostType.NETEASE_DRIVER).changeMyInfo(user_token, name, avatar, age).map(new Func1<BaseResult, BaseResult>() {
            @Override
            public BaseResult call(BaseResult baseResult) {
                return baseResult;
            }
        }).compose(RxSchedulers.<BaseResult>io_main());
    }

    @Override
    public Observable<BaseResult<PicBean>> updateAvatar(Map<String, RequestBody> map) {
        return Api.getDefault(HostType.NETEASE_DRIVER).changeMyAvatar(map).map(new Func1<BaseResult<PicBean>, BaseResult<PicBean>>() {
            @Override
            public BaseResult<PicBean> call(BaseResult<PicBean> picBeanStringBaseListResult) {
                return picBeanStringBaseListResult;
            }
        }).compose(RxSchedulers.<BaseResult<PicBean>>io_main());
    }

    @Override
    public Observable<BaseResult<PicBean>> updateDoctorAvatar(Map<String, RequestBody> map) {
        return Api.getDefault(HostType.NETEASE_DRIVER).doctor_changeMyAvatar(map).map(new Func1<BaseResult<PicBean>, BaseResult<PicBean>>() {
            @Override
            public BaseResult<PicBean> call(BaseResult<PicBean> picBeanStringBaseListResult) {
                return picBeanStringBaseListResult;
            }
        }).compose(RxSchedulers.<BaseResult<PicBean>>io_main());    }

    @Override
    public Observable<BaseResult> updateDocInfo(String doctor_token, String name, String avatar, String age) {
        return Api.getDefault(HostType.NETEASE_DRIVER).doctor_changeMyInfo(doctor_token, name, avatar, age,"","","","").map(new Func1<BaseResult, BaseResult>() {
            @Override
            public BaseResult call(BaseResult baseResult) {
                return baseResult;
            }
        }).compose(RxSchedulers.<BaseResult>io_main());

    }
}
