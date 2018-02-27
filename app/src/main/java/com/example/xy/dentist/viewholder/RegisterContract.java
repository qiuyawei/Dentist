package com.example.xy.dentist.viewholder;

import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.Userbean;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import rx.Observable;

/**
 * Created by XY on 2017/10/23.
 */
public interface RegisterContract {
    interface Model extends BaseModel {


        Observable<BaseResult> getcaptcha(String phone);
        Observable<BaseResult<Userbean>> register(String new_password, String phone, String phone_captcha, String terminal,String code);

    }

    interface View extends BaseView {


        void captcha(BaseResult result);


        void UpdateState(BaseResult<Userbean> baseResult);
    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //获取数据
        public abstract void getcaptcha(String phone);


        public abstract void register(String password, String phone, String phone_captcha, String terminal,String code);
    }
}
