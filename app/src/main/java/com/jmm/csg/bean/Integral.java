package com.jmm.csg.bean;


public class Integral {

    private String code;
    private String message;
    private String data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data == null ? "0" : data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
