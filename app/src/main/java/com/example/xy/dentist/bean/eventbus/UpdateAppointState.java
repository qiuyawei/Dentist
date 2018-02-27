package com.example.xy.dentist.bean.eventbus;

/**
 * Created by XY on 2017/11/6.
 */
public class UpdateAppointState {

    public UpdateAppointState(int state) {//1进行中2待评价3已完成
        this.state = state;

    }

    public int state;
}
