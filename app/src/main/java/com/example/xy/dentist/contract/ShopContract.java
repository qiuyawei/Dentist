package com.example.xy.dentist.contract;

import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.ShopBean;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by XY on 2017/10/12.
 */
public interface ShopContract {
    interface Model extends BaseModel {




        Observable<BaseResult<BaseListResult<ShopBean,String>>>
        shop(String doctor_token,String page, String limit);



        Observable<BaseResult<BaseListResult<String,ShopBean>>>
        shop_info(String doctor_token, String id);

    }

    interface View extends BaseView {


        void setListData(List<ShopBean> bean, String message);

        void setInfo(ShopBean bean, String message);
    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //获取数据
        public abstract void shop(String doctor_token,String page, String limit);
        public abstract void  shop_info(String doctor_token, String id);


    }
}
