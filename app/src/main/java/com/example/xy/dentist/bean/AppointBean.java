package com.example.xy.dentist.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by XY on 2017/10/23.
 */
public class AppointBean implements Serializable {
    public String id;
    public String user_id;
    public String doctor_id;
    public String clinic_id;
    public String date_id;
    public String year;
    public String month;
    public String day;
    public String log;
    public String create_time;
    public String status;
    public String comment;
    public String star;
    public String content;
    public String tooth;
    public List<String> tags;
    public String start_time;
    public String end_time;
    public String name;
    public String avatar;
    public String experience;
    public String skill;
    public String doctor_status;//1忙碌0空闲,
    public String phone;
    public String age;
}
