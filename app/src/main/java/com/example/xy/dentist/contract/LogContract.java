package com.example.xy.dentist.contract;

import com.example.xy.dentist.bean.AppointBean;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.TimeSetBean;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by XY on 2017/11/3.
 */
public interface LogContract {
    interface Model extends BaseModel {


        Observable<BaseResult<BaseListResult<String,AppointBean>>> doctor_getUserInfo(String doctor_token,String id);
        Observable<BaseResult<BaseListResult<TimeSetBean,String>>> tags();
        Observable<BaseResult> doctor_judge( String doctor_token, String id,  String content,String tooth, String tags);


    }

    interface View extends BaseView {


        void setData(AppointBean bean, String message);
        void setTag(List<TimeSetBean> bean);
        void set_judge(BaseResult bean);

    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //获取数据
        public abstract void doctor_getUserInfo(String doctor_token,String id);
        public abstract void tags();
        public abstract void doctor_judge( String doctor_token, String id,  String content,String tooth, String tags);

    }
}
