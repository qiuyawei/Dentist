package com.example.xy.dentist.contract;

import com.aspsine.irecyclerview.bean.PageBean;
import com.example.xy.dentist.bean.BaseBean;
import com.example.xy.dentist.bean.BaseResult;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by XY on 2017/10/12.
 */
public interface MineContract {
    interface Model extends BaseModel {


        Observable<BaseResult> getListDatas(String type, String userId, final int page, int rows);


    }

    interface View extends BaseView {


        void setListData(List<BaseBean> circleItems, PageBean pageBean);


    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //获取数据
        public abstract void getListData(String type, String userId, int page, int rows);



    }
}
