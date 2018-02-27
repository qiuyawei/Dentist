package com.example.xy.dentist.contract;

import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.RecruitBean;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by XY on 2017/9/11.
 */
public interface EvaluateContract {
    interface Model extends BaseModel {


        Observable<BaseResult<BaseListResult<RecruitBean,String>>> recruit(String doctor_token,String page, String limit, String distict_id, String time_desc);
        Observable<BaseResult<BaseListResult<String,RecruitBean>>> recruit_info(String doctor_token, String id);


    }

    interface View extends BaseView {


        void setRecruitListData(List<RecruitBean> bean, String message);
        void setRecruit_infoData(RecruitBean bean, String message);


    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //获取数据
        public abstract void recruit(String doctor_token,String page, String limit, String distict_id, String time_desc);
        public abstract void recruit_info(String doctor_token, String id);


    }
}
