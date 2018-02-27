package com.example.xy.dentist.bean;

/**
 * Created by XY on 2017/9/1.
 */
public class BaseBean {
    public String appointUserid;
    public String createTime;
    public String icon;
    private String takeTimes;
    private String goodjobCount;
    private String replyCount;
    public  String pictures;
    private String type;
    private String content;
    public  String nickName;

    public String getAppointUserid() {
        return appointUserid;
    }

    public void setAppointUserid(String appointUserid) {
        this.appointUserid = appointUserid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTakeTimes() {
        return takeTimes;
    }

    public void setTakeTimes(String takeTimes) {
        this.takeTimes = takeTimes;
    }

    public String getGoodjobCount() {
        return goodjobCount;
    }

    public void setGoodjobCount(String goodjobCount) {
        this.goodjobCount = goodjobCount;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
