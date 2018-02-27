package com.example.xy.dentist.contract;

import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.InfoBean;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import rx.Observable;

/**
 * Created by XY on 2017/10/12.
 */
public interface UserContract {
    interface Model extends BaseModel {



        Observable<BaseResult<InfoBean>> getMyInfo(String user_token);
        Observable<BaseResult<InfoBean>> doctor_getMyInfo(String doctor_token);
        Observable<BaseResult> updateInfo(String doctor_token, String introduce, String skill, String resume,String experice);
    }

    interface View extends BaseView {



        void setMyInfo(BaseResult<InfoBean> data);
        void setDocInfo(BaseResult<InfoBean> data);
        void  updateInfo(BaseResult result,String type,String content);
    }

    abstract static class Presenter extends BasePresenter<View, Model> {



        public abstract void getMyInfo(String user_token);
        public abstract void getDoctor_MyInfo(String doctor_token);

        public abstract void updateInfo(String doctor_token, String introduce, String skill, String resume,String experice);
    }
}
