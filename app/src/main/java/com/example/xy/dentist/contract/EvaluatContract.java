package com.example.xy.dentist.contract;

import com.example.xy.dentist.bean.BaseResult;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import rx.Observable;

/**
 * Created by XY on 2017/11/2.
 */
public interface EvaluatContract {
    interface Model extends BaseModel {


        Observable<BaseResult> judge(String user_token, String id, String comment, String star);

    }

    interface View extends BaseView {


        void judgeState(BaseResult result);


    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //获取数据
        public abstract void judge(String user_token, String id, String comment, String star);



    }
}
