package com.jmm.csg.bean;


import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

public class ProductDetail {

    private List<Entity> product;
    private List<Logo> logo;
    private List<Imgs> proImgs; //商品图
    private List<Imgs> detImgs;//详情图
    private List<Imgs> certImgs; //证书图
//    private List<Review> review;
//    private List<ReviewCount> reviewCount;

//    public ReviewCount getReviewCount() {
//        if (reviewCount.size() != 0) {
//            return reviewCount.get(0);
//        }
//        return new ReviewCount();
//    }

//    public List<Review> getReview() {
//        return review;
//    }

    public List<Entity> getProduct() {
        return product;
    }

    public List<Logo> getLogo() {
        return logo;
    }

    public List<Imgs> getProImgs() {
        return proImgs;
    }

    public List<Imgs> getDetImgs() {
        return detImgs;
    }

    public List<Imgs> getCertImgs() {
        return certImgs;
    }

    public static class ReviewCount {
        private String reviewCount;
        private String P;
        private String B;

        public String getReviewCount() {
            return TextUtils.isEmpty(reviewCount) ? "0" : reviewCount;
        }

        public String getP() {
            return TextUtils.isEmpty(P) ? "0" : P;
        }

        public String getB() {
            return TextUtils.isEmpty(B) ? "0" : B;
        }
    }

//    public static class Review {
//        private String r_date;
//        private String r_status;
//        private String r_content;
//        private String r_specification_id;
//        private String specification;
//        private String product_id;
//        private String u_name;
//        private String reviewCount;
//        private String P;
//        private String B;
//        private String u_sfid;
//
//        public String getU_sfid() {
//            return u_sfid;
//        }
//
//        public String getR_date() {
//            return r_date;
//        }
//
//        public String getR_status() {
//            return r_status;
//        }
//
//        public String getR_content() {
//            return r_content;
//        }
//
//        public String getR_specification_id() {
//            return r_specification_id;
//        }
//
//        public String getSpecification() {
//            return specification;
//        }
//
//        public String getProduct_id() {
//            return product_id;
//        }
//
//        public String getU_name() {
//            return u_name;
//        }
//
//        public String getReviewCount() {
//            return reviewCount;
//        }
//
//        public String getP() {
//            return P;
//        }
//
//        public String getB() {
//            return B;
//        }
//    }

    public static class Logo {
        private String logo;

        public String getLogo() {
            return logo;
        }
    }

    public static class Imgs {
        private String pro_kind;
        private String pro_pic;

        public String getPro_kind() {
            return pro_kind;
        }

        public String getPro_pic() {
            return pro_pic;
        }
    }

    public static class Entity implements Serializable {
        private String id;
        private String pro_Name;
        private String pro_Coding;
        private String pro_kindCoding;
        private String pro_time;
        private String pro_status;
        private String pro_description;
        private String pro_pic;
        private String pro_detailpic1;
        private String pro_certpic1;
        private String pro_brand;
        private String pro_Specification;
        private String pro_Material;
        private String pro_SpecificationaCoding;
        private String pro_price;
        private String pro_Inventory;
        private String pro_Circulation;
        private String suName;

        public String getSuName() {
            return suName;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setPro_Name(String pro_Name) {
            this.pro_Name = pro_Name;
        }

        public void setPro_Coding(String pro_Coding) {
            this.pro_Coding = pro_Coding;
        }

        public void setPro_kindCoding(String pro_kindCoding) {
            this.pro_kindCoding = pro_kindCoding;
        }

        public void setPro_time(String pro_time) {
            this.pro_time = pro_time;
        }

        public void setPro_status(String pro_status) {
            this.pro_status = pro_status;
        }

        public void setPro_description(String pro_description) {
            this.pro_description = pro_description;
        }

        public void setPro_pic(String pro_pic) {
            this.pro_pic = pro_pic;
        }

        public void setPro_detailpic1(String pro_detailpic1) {
            this.pro_detailpic1 = pro_detailpic1;
        }

        public void setPro_certpic1(String pro_certpic1) {
            this.pro_certpic1 = pro_certpic1;
        }

        public void setPro_brand(String pro_brand) {
            this.pro_brand = pro_brand;
        }

        public void setPro_Specification(String pro_Specification) {
            this.pro_Specification = pro_Specification;
        }

        public void setPro_Material(String pro_Material) {
            this.pro_Material = pro_Material;
        }

        public void setPro_SpecificationaCoding(String pro_SpecificationaCoding) {
            this.pro_SpecificationaCoding = pro_SpecificationaCoding;
        }

        public void setPro_price(String pro_price) {
            this.pro_price = pro_price;
        }

        public void setPro_Inventory(String pro_Inventory) {
            this.pro_Inventory = pro_Inventory;
        }

        public void setPro_Circulation(String pro_Circulation) {
            this.pro_Circulation = pro_Circulation;
        }

        public String getId() {
            return id;
        }

        public String getPro_Name() {
            return pro_Name;
        }

        public String getPro_Coding() {
            return pro_Coding;
        }

        public String getPro_kindCoding() {
            return pro_kindCoding;
        }

        public String getPro_time() {
            return pro_time;
        }

        public String getPro_status() {
            return pro_status;
        }

        public String getPro_description() {
            return pro_description;
        }

        public String getPro_pic() {
            return pro_pic;
        }

        public String getPro_detailpic1() {
            return pro_detailpic1;
        }

        public String getPro_certpic1() {
            return pro_certpic1;
        }

        public String getPro_brand() {
            return pro_brand;
        }

        public String getPro_Specification() {
            return pro_Specification;
        }

        public String getPro_Material() {
            return pro_Material;
        }

        public String getPro_SpecificationaCoding() {
            return pro_SpecificationaCoding;
        }

        public String getPro_price() {
            double price = Double.parseDouble(pro_price) / 100;
            return String.format("%.2f", price);
//            return String.valueOf(Double.parseDouble(pro_price) / 100);
        }

        public String getPro_Inventory() {
            return pro_Inventory;

        }

        public String getPro_Circulation() {
            return pro_Circulation;
        }
    }
}
