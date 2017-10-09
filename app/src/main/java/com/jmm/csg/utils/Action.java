package com.jmm.csg.utils;

/**
 * Author: housong12590
 * Date: 2017/4/8 10:48
 */
public class Action {

    private String code;
    private Object data;

    public String getCode() {
        return code;
    }

    public Action setCode(String code) {
        this.code = code;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Action setData(Object data) {
        this.data = data;
        return this;
    }
}
