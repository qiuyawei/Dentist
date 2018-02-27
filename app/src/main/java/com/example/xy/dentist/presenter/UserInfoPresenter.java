package com.example.xy.dentist.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.PicBean;
import com.example.xy.dentist.contract.UserInfoContract;
import com.jaydenxiao.common.commonutils.ToastUitl;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * Created by XY on 2017/11/1.
 */
public class UserInfoPresenter  extends UserInfoContract.Presenter{
    private String type;

    @Override
    public void updateInfo(boolean isdoc, String user_token, final String name, final String avatar, final String age) {
        if(!isdoc){
            mRxManage.add(mModel.updateInfo(user_token, name, avatar, age).subscribe(new Subscriber<BaseResult>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
                }

                @Override
                public void onNext(BaseResult result) {
                    switch (result.code){
                        case 200:
                            if(!TextUtils.isEmpty(name)){
                                type="0";
                            }else if(!TextUtils.isEmpty(avatar)){
                                type="1";
                            }else if(!TextUtils.isEmpty(age)){
                                type="2";
                            }
                            mView.changeInfoState(result,  type);
                            break;
                        case 400:
                            ToastUitl.showShort(result.message);
                            break;

                    }

                }
            }));
        }else{
            mRxManage.add(mModel.updateDocInfo(user_token, name, avatar, age).subscribe(new Subscriber<BaseResult>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
                }

                @Override
                public void onNext(BaseResult result) {
                    switch (result.code){
                        case 200:
                            if(!TextUtils.isEmpty(name)){
                                type="0";
                            }else if(!TextUtils.isEmpty(avatar)){
                                type="1";
                            }else if(!TextUtils.isEmpty(age)){
                                type="2";
                            }
                            mView.changeInfoState(result,  type);
                            break;
                        case 400:
                            ToastUitl.showShort(result.message);
                            break;

                    }

                }
            }));
        }

    }

    @Override
    public void updateAvatar(Map<String, RequestBody> map) {
        mRxManage.add(mModel.updateAvatar(map).subscribe(new Subscriber<BaseResult<PicBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(BaseResult<PicBean>result) {
                switch (result.code) {
                    case 200:
                        mView.updateAvatarState(result);
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }

            }
        }));
    }

    @Override
    public void updateDoctorAvatar(Map<String, RequestBody> map) {
        mRxManage.add(mModel.updateDoctorAvatar(map).subscribe(new Subscriber<BaseResult<PicBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(BaseResult<PicBean>result) {
                Log.i("TAG","resultDocCode="+result.code);
                Log.i("TAG","resultDocUrl="+result.url);

                switch (result.code) {
                    case 200:
                        mView.updateAvatarState(result);
                        break;
                    case 400:
                        ToastUitl.showShort(result.message);
                        break;

                }

            }
        }));
    }


}
