package com.example.xy.dentist.contract;

import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.PicBean;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by XY on 2017/11/1.
 */
public interface UserInfoContract {
    interface Model extends BaseModel {


        Observable<BaseResult> updateInfo(String user_token,String name, String avatar, String age);
        Observable<BaseResult<PicBean>> updateAvatar(Map<String, RequestBody> map);
        Observable<BaseResult<PicBean>> updateDoctorAvatar(Map<String, RequestBody> map);

        Observable<BaseResult> updateDocInfo(String doctor_token,String name, String avatar, String age);

    }
//    updateDoctorAvatar
    interface View extends BaseView {


        void changeInfoState(BaseResult result,String type);
        void updateAvatarState(BaseResult<PicBean> result);
        void updateDoctorAvatarState(BaseResult<PicBean> result);

//        void changeDocInfoState(BaseResult result,String type);

    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //获取数据


        public abstract void updateInfo(boolean isdoc, String user_token, String name, String avatar, String age);

        public abstract void updateAvatar(Map<String, RequestBody> map);
        public abstract void updateDoctorAvatar(Map<String, RequestBody> map);

//         public abstract void updateDocInfo(String doctor_token,String name, String avatar, String age);

    }
}
