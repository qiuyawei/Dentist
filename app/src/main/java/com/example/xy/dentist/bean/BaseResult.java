package com.example.xy.dentist.bean;

import java.io.Serializable;

/**
 * Created by XY on 2017/9/1.
 */
public class BaseResult<T> implements Serializable {

    public T data;
    public String url;
    public String desc;
    public String response;
    public String message;
    public int code;
    public String createTime;
    public String status;
    public String msg;


}
