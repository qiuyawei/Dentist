package com.example.xy.dentist.bean.eventbus;

/**
 * 申请提现成功 更新我的积分和个人中心的积分值
 * Created by Administrator on 2016/10/18.
 */

public class UpdateInfo {

    public UpdateInfo(boolean isSuccess) {
        this.isSuccess = isSuccess;

    }

    public boolean isSuccess;


}
