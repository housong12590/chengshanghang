package com.jmm.csg.bean;

public enum CityStatus {

    PROVINCE(0), //省
    CITY(1), // 市
    AREA(2); // 区


    private int status;

    CityStatus(int code) {
        this.status = code;
    }

    public int getStatus() {
        return status;
    }

}
