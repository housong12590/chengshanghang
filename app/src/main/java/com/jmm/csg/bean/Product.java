package com.jmm.csg.bean;


import android.text.TextUtils;

public class Product {

    private String proId;
    private String missionId;
    private String missionNum;
    private String specificationId;
    private String proPrice;
    private String proName;
    private String status;
    private String finishNum;
    private String coding;
    private String logo;
    private String proPic;
    private String proPicFilePath;
    private String productnum;
    private String bankNum;

    public String getBankNum() {
        return bankNum;
    }

    public String getProductnum() {
        return productnum;
    }

    public String getMissionId() {
        return missionId;
    }

    public String getProPicFilePath() {
        return proPicFilePath;
    }

    public void setProPicFilePath(String proPicFilePath) {
        this.proPicFilePath = proPicFilePath;
    }

    public String getProPic() {
        return proPic;
    }

    public String getLogo() {
        return logo;
    }

    public String getProId() {
        return proId;
    }

    public String getMissionNum() {
        return TextUtils.isEmpty(missionNum) ? "0" : missionNum;
    }

    public String getSpecificationId() {
        return specificationId;
    }

    public String getProPrice() {
        double price = Double.parseDouble(proPrice) / 100;
//        DecimalFormat df = new DecimalFormat("#.00");
//        String.valueOf(price);
        return String.format("%.2f", price);
//        return df.format(price);
    }

    public String getProName() {
        return proName;
    }

    public String getStatus() {
        return status;
    }

    public String getFinishNum() {
        return TextUtils.isEmpty(finishNum) ? "0" : finishNum;
    }

    public String getCoding() {
        return coding;
    }
}
