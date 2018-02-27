package com.example.xy.dentist.model;


import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.ShopBean;
import com.example.xy.dentist.contract.ShopContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XY on 2017/9/11.
 */
public class ShopModel implements  ShopContract.Model {



    @Override
    public Observable<BaseResult<BaseListResult<ShopBean, String>>> shop(String doctor_token, String page, String limit) {
        return Api.getDefault(HostType.NETEASE_DRIVER).shop(doctor_token, page, limit).map(new Func1<BaseResult<BaseListResult<ShopBean, String>>, BaseResult<BaseListResult<ShopBean, String>>>() {
            @Override
            public BaseResult<BaseListResult<ShopBean, String>> call(BaseResult<BaseListResult<ShopBean, String>> baseListResultBaseResult) {
                return baseListResultBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<BaseListResult<ShopBean, String>>>io_main());
    }

    @Override
    public Observable<BaseResult<BaseListResult<String, ShopBean>>> shop_info(String doctor_token, String id) {
        return Api.getDefault(HostType.NETEASE_DRIVER).shop_info(doctor_token, id).map(new Func1<BaseResult<BaseListResult<String, ShopBean>>, BaseResult<BaseListResult<String, ShopBean>>>() {
            @Override
            public BaseResult<BaseListResult<String, ShopBean>> call(BaseResult<BaseListResult<String, ShopBean>> baseListResultBaseResult) {
                return baseListResultBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<BaseListResult<String, ShopBean>>>io_main());
    }
}
