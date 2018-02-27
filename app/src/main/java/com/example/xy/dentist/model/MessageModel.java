package com.example.xy.dentist.model;

import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.Meassagebean;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.Meassagebean;
import com.example.xy.dentist.bean.ShopBean;
import com.example.xy.dentist.contract.AppointmentContract;
import com.example.xy.dentist.contract.MessageContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/11/1.
 */
public class MessageModel implements MessageContract.Model {
    @Override
    public Observable<BaseResult<BaseListResult<Meassagebean, String>>> getListDatas(String uid, String states) {
        return Api.getDefault(HostType.NETEASE_DRIVER).getMessagesss(uid,states).map(new Func1<BaseResult<BaseListResult<Meassagebean, String>>, BaseResult<BaseListResult<Meassagebean, String>>>() {
            @Override
            public BaseResult<BaseListResult<Meassagebean, String>> call(BaseResult<BaseListResult<Meassagebean, String>> baseListResultBaseResult) {
                return baseListResultBaseResult;
            }
        });
    }
  /*  @Override
    public Observable<BaseResult<BaseListResult<Meassagebean, String>>> getMessage(String uid, String states) {
        return Api.getDefault(HostType.NETEASE_DRIVER).getMessage(uid, states).map(new Func1<BaseResult<BaseListResult<Meassagebean, String>>, BaseResult<BaseListResult<Meassagebean, String>>>() {
            @Override
            public BaseResult<BaseListResult<Meassagebean, String>> call(BaseResult<BaseListResult<Meassagebean, String>> baseListResultBaseResult) {
                return baseListResultBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<BaseListResult<Meassagebean, String>>>io_main());
    
    }*/

   /* @Override
    public Observable<BaseResult<BaseListResult<Meassagebean,String>>> getListDatas(String uid, String states) {
        return Api.getDefault(HostType.NETEASE_DRIVER).getMessagesss(uid, states).map(new Func1<BaseResult<BaseListResult<Meassagebean, String>>, BaseResult<BaseListResult<Meassagebean, String>>>() {
            @Override
            public BaseResult<BaseListResult<Meassagebean, String>> call(BaseResult<BaseListResult<Meassagebean, String>> baseListResultBaseResult) {
                return baseListResultBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<BaseListResult<Meassagebean, String>>>io_main());*/


     /*   @Override
        public Observable<BaseResult<BaseListResult<ShopBean, String>>> shop(String doctor_token, String page, String limit) {
            return Api.getDefault(HostType.NETEASE_DRIVER).shop(doctor_token, page, limit).map(new Func1<BaseResult<BaseListResult<ShopBean, String>>, BaseResult<BaseListResult<ShopBean, String>>>() {
                @Override
                public BaseResult<BaseListResult<ShopBean, String>> call(BaseResult<BaseListResult<ShopBean, String>> baseListResultBaseResult) {
                    return baseListResultBaseResult;
                }
            }).compose(RxSchedulers.<BaseResult<BaseListResult<ShopBean, String>>>io_main());
        }*/

}
