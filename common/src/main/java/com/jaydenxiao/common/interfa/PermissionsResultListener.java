package com.jaydenxiao.common.interfa;

/**
 * Created by XY on 2017/9/12.
 */
public interface PermissionsResultListener {
    //成功
    void onSuccessful(int[] grantResults);

    //失败
    void onFailure();
}
