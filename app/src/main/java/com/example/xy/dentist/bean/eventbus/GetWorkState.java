package com.example.xy.dentist.bean.eventbus;

/**
 * Created by XY on 2017/11/10.
 */
public class GetWorkState {
    public GetWorkState(String state) {//预约单状态，0新预约1待确认2进行中3已完成-1已取消
        this.state = state;

    }

    public String state;
}
