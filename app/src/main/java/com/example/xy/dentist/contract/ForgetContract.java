package com.example.xy.dentist.contract;


import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.Doctorbean;
import com.example.xy.dentist.bean.UserTypeBean;
import com.example.xy.dentist.bean.Userbean;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import rx.Observable;

/**
 * Created by XY on 2017/9/21.
 */
public interface ForgetContract {
    interface Model extends BaseModel {


        Observable<BaseResult> getcaptcha(String phone);
        Observable<BaseResult<Userbean>> updatePassw(String new_password, String phone, String phone_captcha);
        Observable<BaseResult<UserTypeBean>> checkUserType(String phone);
        Observable<BaseResult<Doctorbean>> docupdatePassw(String new_password, String phone, String phone_captcha);
    }

    interface View extends BaseView {


        void captcha(BaseResult result);
        void checkUserTypeState(BaseResult<UserTypeBean> result);

        void UpdateState(BaseResult<Userbean> baseResult);
        void docUpdateState(BaseResult<Doctorbean> baseResult);
    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //获取数据
        public abstract void getcaptcha(String phone);

        public abstract void checkUserType(String phone);
        public abstract void updatePassw(String new_password, String phone, String phone_captcha);
        public abstract void docupdatePassw(String new_password, String phone, String phone_captcha);
    }
}
