package com.example.xy.dentist.contract;

import com.example.xy.dentist.bean.AppointuBean;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.Doctorbean;
import com.example.xy.dentist.bean.Meassagebean;
import com.example.xy.dentist.bean.ShopBean;
import com.example.xy.dentist.bean.UserTypeBean;
import com.example.xy.dentist.bean.Userbean;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by XY on 2017/9/21.
 */
public interface MessageContract {
    interface Model extends BaseModel {

//        Observable<BaseResult<Meassagebean>> getMessage(String uid, String states);
        Observable<BaseResult<BaseListResult<Meassagebean,String>>> getListDatas(String uid, String states);

    }

    interface View extends BaseView {
        void setListData(List<Meassagebean> result, String message);


    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //获取数据
        public abstract void getListDatas(String uid, String states);

    }
}
