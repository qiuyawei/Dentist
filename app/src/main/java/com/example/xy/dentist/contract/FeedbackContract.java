package com.example.xy.dentist.contract;

import com.example.xy.dentist.bean.BaseResult;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import rx.Observable;

/**
 * Created by XY on 2017/9/25.
 */
public interface FeedbackContract {
    interface Model extends BaseModel {


        Observable<BaseResult> commitfeedback(String user_token, String content);
        Observable<BaseResult> commitdocfeedback(String driver_token, String content);

    }

    interface View extends BaseView {


        void commitState(BaseResult result);


    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //获取数据
        public abstract void commitfeedback(boolean isdoc, String token, String content);



    }
}
