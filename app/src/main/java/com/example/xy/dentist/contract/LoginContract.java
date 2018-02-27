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
public interface LoginContract {
    interface Model extends BaseModel {
        Observable<BaseResult<Userbean>> login(String phone, String password);
        Observable<BaseResult<Userbean>> thirdUserLogin(String type, String code);
        Observable<BaseResult<Userbean>> bindLogin(String phone, String password,String type,String code);
        Observable<BaseResult<UserTypeBean>> checkUserType(String phone);
        Observable<BaseResult<UserTypeBean>> checkUserType2(String code,String type);
        Observable<BaseResult<Doctorbean>> doctorlogin(String phone, String password, String type, String code);
    }

    interface View extends BaseView {
        void loginState(BaseResult<Userbean> result);
        void thirdUserLoginState(BaseResult<Userbean> result);

        void bindLoginState(BaseResult<Userbean> result);

        void checkUserTypeState(BaseResult<UserTypeBean> result);
        void checkUserTypeState2(BaseResult<UserTypeBean> result);

        void doctorloginState(BaseResult<Doctorbean> result);

    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //获取数据
        public abstract void login(String phone, String password);
        public abstract void thirdUserLogin(String type, String code);

        public abstract void bindLogin(String phone, String password,String type,String code);


        public abstract void checkUserType(String phone);
        public abstract void checkUserType2(String code,String type);

        public abstract void doctorlogin(String phone, String password, String type, String code);
    }
}
