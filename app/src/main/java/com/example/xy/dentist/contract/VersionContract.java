package com.example.xy.dentist.contract;


import com.example.xy.dentist.bean.AppointuBean;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.Userbean;
import com.example.xy.dentist.bean.VersionBean;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by XY on 2017/11/1.
 */
public interface VersionContract {
    interface Model extends BaseModel {


        Observable<BaseResult<VersionBean>> getVersion(String user_token);


    }

    interface View extends BaseView {


        void setVersion(VersionBean versionBean);


    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //获取数据
        public abstract void getVersion(String user_token);


    }
}
