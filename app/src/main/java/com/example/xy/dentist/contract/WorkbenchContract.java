package com.example.xy.dentist.contract;

import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.DoctorInfoBean;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by XY on 2017/11/3.
 */
public interface WorkbenchContract {
    interface Model extends BaseModel {


        Observable<BaseResult<BaseListResult<DoctorInfoBean,String>>>
        getListData(String doctor_token, String page, String limit, String status
                , String name, String year, String month, String day,String start_time, String end_time, int rows);
        Observable<BaseResult> doctor_cancel( String doctor_token,String id);


    }

    interface View extends BaseView {


        void setListData(List<DoctorInfoBean> circleItems, String message);
        void canclestate(BaseResult bean, String message);


    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //获取数据
        public abstract void getListData(String doctor_token, String page, String limit, String status
                , String name, String year, String month, String day,String start_time, String end_time, int rows);

        public abstract void cancel(String clinic_id);
    }
}
