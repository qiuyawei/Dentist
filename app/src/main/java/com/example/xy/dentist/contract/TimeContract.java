package com.example.xy.dentist.contract;

import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.TimeSetBean;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by XY on 2017/11/3.
 */
public interface TimeContract {
    interface Model extends BaseModel {

        Observable<BaseResult> setTime(String doctor_token, String id, String date_id,String date);
        Observable<BaseResult<BaseListResult<TimeSetBean,String>>>
        doctor_getTimeList( String doctor_token);

        Observable<BaseResult<BaseListResult<TimeSetBean,String>>>
        doctor_getTimeListNew( String doctor_token,String date);

    }

    interface View extends BaseView {


        void setTimeState(BaseResult bean, String message);
        void doctor_setTimeList(List<TimeSetBean> bean, String message);

        void doctor_setTimeListNew(List<TimeSetBean> bean, String message);

    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        public abstract void setTime(String doctor_token, String id, String date_id,String date);
        public abstract void doctor_getTimeList( String doctor_token);
        public abstract void doctor_getTimeListNew( String doctor_token,String date);


    }
}
