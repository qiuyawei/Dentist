package com.example.xy.dentist.contract;

import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.ClinicBean;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by XY on 2017/11/1.
 */
public interface QueryContract {
    interface Model extends BaseModel {


        Observable<BaseResult<BaseListResult<ClinicBean,String>>> getListDatas(String user_token, String page, String limit, String latitude,String longitude,String distict_id,int rows);


    }

    interface View extends BaseView {


        void setListData(List<ClinicBean> circleItems, String message);


    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //获取数据
        public abstract void getListData(String user_token, String page, String limit, String latitude,String longitude,String distict_id,int rows);


    }
}
