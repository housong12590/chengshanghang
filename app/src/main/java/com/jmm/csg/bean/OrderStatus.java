package com.jmm.csg.bean;


import java.io.Serializable;

public enum OrderStatus implements Serializable {
    A("A"),//所有订单
    M("M"),//待付款
    C("C"),//待发货
    S("S"),//待收货
    F("F"),//待评价
    X("X"),//已取消
    Q("Q");//已完成

    String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
