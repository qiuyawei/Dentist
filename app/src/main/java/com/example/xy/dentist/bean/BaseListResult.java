package com.example.xy.dentist.bean;

import java.util.List;

/**
 * Created by XY on 2017/10/23.
 */
public class BaseListResult<T,P> {
    public  List<T> list;
    public  P info;
    public  List<T> data;
    public  int code;
    public  String message;
    public  List<String> date_ids;

    public  String status;
}
