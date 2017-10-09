package com.jmm.csg.bean;

public class UserInfo extends BaseResp {

    private String userid;

    private String phoneNum;

    private String password;

    private String realnamestatus;

    public String getRealNameStatus() {
        return realnamestatus;
    }

    public void setRealNameStatus(String realnamestatus) {
        this.realnamestatus = realnamestatus;
    }

    public String getUserId() {
        return userid;
    }

    public void setUserId(String userid) {
        this.userid = userid;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
