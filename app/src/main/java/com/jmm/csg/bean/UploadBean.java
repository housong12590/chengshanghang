package com.jmm.csg.bean;


public class UploadBean {

    public String path;
    public String name;
    public byte[] bytes;

    public UploadBean() {
    }

    public UploadBean(String path, String name) {
        this.path = path;
        this.name = name;
    }
}
