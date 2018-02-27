package com.example.xy.dentist.bean.eventbus;

/**
 * Created by XY on 2017/11/2.
 */
public class UpdateDocInfo {
    public UpdateDocInfo(boolean isSuccess) {
        this.isSuccess = isSuccess;

    }

    public boolean isSuccess;
}
