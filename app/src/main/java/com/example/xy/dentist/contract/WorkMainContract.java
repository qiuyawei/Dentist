package com.example.xy.dentist.contract;

import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.StateBean;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import rx.Observable;

/**
 * Created by XY on 2017/11/10.
 */
public interface WorkMainContract {
    interface Model extends BaseModel {




        Observable<BaseResult<StateBean>> updateInfo(String doctor_token, String status);
}

    interface View extends BaseView {




        void  updateState(BaseResult<StateBean> result);
    }

    abstract static class Presenter extends BasePresenter<View, Model> {




        public abstract void updateInfo(String doctor_token, String status);
    }
}
