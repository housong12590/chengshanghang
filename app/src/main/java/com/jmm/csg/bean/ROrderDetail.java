package com.jmm.csg.bean;

/**
 * author：hs
 * date: 2017/6/23 0023 16:48
 */

public class ROrderDetail {

    private String returnprice;
    private String reason;
    private String createtime;
    private String quantity;
    private String flag;
    private String productid;
    private String remark;
    private String managerId;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private String image6;
    private String image7;
    private String ordersId;
    private String CREATE_DATE;
    private String addressinfo;
    private String tkstatus;
    private String orderitemsId;
    private String id;
    private String payprice;
    private String productprice;
    private String memberId;
    private String status;
    private String expressNo;
    private String cheeckremark;

    public String getCheeckremark() {
        return cheeckremark == null ? "" : cheeckremark;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public String getReturnprice() {
        return returnprice;
    }

    public String getReason() {
        return reason;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getFlag() {
        return flag;
    }

    public String getProductid() {
        return productid;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public String getManagerId() {
        return managerId;
    }

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getImage3() {
        return image3;
    }

    public String getImage4() {
        return image4;
    }

    public String getImage5() {
        return image5;
    }

    public String getImage6() {
        return image6;
    }

    public String getImage7() {
        return image7;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public String getCREATE_DATE() {
        return CREATE_DATE;
    }

    public String getAddressinfo() {
        return addressinfo;
    }

    public String getTkstatus() {
        return tkstatus;
    }

    public String getOrderitemsId() {
        return orderitemsId;
    }

    public String getId() {
        return id;
    }

    public String getPayprice() {
        return payprice;
    }

    public String getProductprice() {
        return productprice;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getStatus() {
        return status;
    }

}
