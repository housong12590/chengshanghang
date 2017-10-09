package com.jmm.csg.bean;

/**
 * author：hs
 * date: 2017/5/19 0019 10:41
 */
public class PayResultBean {
    private String orderId;
    private String proName;
    private String proSpecification;
    private String price;

    public PayResultBean() {
    }

    public PayResultBean(String orderId, String proName, String proSpecification, String price) {
        this.orderId = orderId;
        this.proName = proName;
        this.proSpecification = proSpecification;
        this.price = price;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProSpecification() {
        return proSpecification;
    }

    public void setProSpecification(String proSpecification) {
        this.proSpecification = proSpecification;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
