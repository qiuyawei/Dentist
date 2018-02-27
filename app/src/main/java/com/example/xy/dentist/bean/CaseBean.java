package com.example.xy.dentist.bean;

/**
 * Created by XY on 2017/10/13.
 */
public class CaseBean {
    public String name;
    public String id;
    public boolean isCheck;
    public  int pos;

    @Override
    public String toString() {
        return "CaseBean{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", isCheck=" + isCheck +
                ", pos=" + pos +
                '}';
    }
}
