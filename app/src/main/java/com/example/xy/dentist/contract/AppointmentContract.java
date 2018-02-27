package com.example.xy.dentist.contract;


import com.example.xy.dentist.bean.AppointuBean;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by XY on 2017/11/1.
 */
public interface AppointmentContract {
    interface Model extends BaseModel {


        Observable<BaseResult<BaseListResult<AppointuBean,String>>> getListDatas( String user_token, String page,  String limit, String type, int rows);


    }

    interface View extends BaseView {


        void setListData(List<AppointuBean> bean, String message);


    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //获取数据
        public abstract void getListData(String user_token, String page,  String limit, String type, int rows);


    }
}
