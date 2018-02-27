package com.example.xy.dentist.contract;

import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.ClinicBean;
import com.example.xy.dentist.bean.DoctorDetailbean;
import com.example.xy.dentist.bean.UserInfo;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by XY on 2017/11/2.
 */
public interface QueryDetailContract {
    interface Model extends BaseModel {


        Observable<BaseResult<BaseListResult<UserInfo,ClinicBean>>> getInfoData(String id, String latitude,String longitude);
        Observable<BaseResult<BaseListResult<String,DoctorDetailbean>>> doctorInfo(String id);
        Observable<BaseResult> appointment(String user_token,String clinic_id, String doctor_id,String type);
    }

    interface View extends BaseView {


        void setInfoData(ClinicBean info,List<UserInfo> bean, String message);
        void setDicData(DoctorDetailbean info,String message);
        void setAppoint(BaseResult result, String status ,String message);

    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //获取数据
        public abstract void getInfoData(String id, String latitude,String longitude);


        public abstract void doctorInfo(String id);

        public abstract void appointment(String status, String user_token, String clinic_id, String doctor_id,String type);
    }
}
