package com.example.xy.dentist.bean.eventbus;

/**
 * Created by XY on 2017/10/11.
 */
public class Notify {
    public Notify(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess;
}
