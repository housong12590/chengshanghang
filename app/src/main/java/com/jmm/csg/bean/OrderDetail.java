package com.jmm.csg.bean;


import com.jmm.csg.utils.StringUtils;

public class OrderDetail {

    private Entity orderdetail;

    public Entity getOrderdetail() {
        return orderdetail;
    }

    public static class Entity {
        private String logo;
        private String phonenumber;
        private String shippingcharge;
        private String ACCOUNTMANAGERNAME;
        private String ACCOUNTMANAGERMOBILE;
        private String orderitemsid;
        private String consignee;
        private String ordesstatus;
        private String orderspayprice;
        private String JOBNUMBER;
        private String id;
        private String createtime;
        private String pay_time;
        private String check_result;//退款结果
        private String check_status;//退款状态
        private String pro_price;
        private String pro_Specification;
        private String pro_Name;
        private String address;
        private String ACCOUNTMANAGERPIC;
        private String quantity;
        private String belongBank;
        private String managerid;
        private String pro_Coding;
        private String ordersId;
        private String pro_pic;
        private String remark;

        public String getRemark() {
            return remark == null ? "" : remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getPro_pic() {
            return pro_pic;
        }

        public String getLogo() {
            return logo;
        }

        public String getPhonenumber() {
            return phonenumber;
        }

        public String getShippingcharge() {
            return shippingcharge;
        }

        public String getACCOUNTMANAGERNAME() {
            return ACCOUNTMANAGERNAME;
        }

        public String getACCOUNTMANAGERMOBILE() {
            return ACCOUNTMANAGERMOBILE;
        }

        public String getOrderitemsid() {
            return orderitemsid;
        }

        public String getConsignee() {
            return consignee;
        }

        public String getOrdesstatus() {
            return ordesstatus;
        }

        public String getOrderspayprice() {
            double price = Double.parseDouble(orderspayprice) / 100;
            return String.format("%.2f", price);
//            return String.valueOf(Double.parseDouble(orderspayprice) / 100);
        }

        public String getJOBNUMBER() {
            return JOBNUMBER;
        }

        public String getId() {
            return id;
        }

        public String getCreatetime() {
            return createtime;
        }

        public String getPaytime() {
            return pay_time;
        }

        public String getPro_price() {
            double price = Double.parseDouble(pro_price) / 100;
            return String.format("%.2f", price);
//            return String.valueOf(Double.parseDouble(pro_price) / 100);
        }

        public String getPro_Specification() {
            return pro_Specification;
        }

        public String getPro_Name() {
            return pro_Name;
        }

        public String getAddress() {
            return StringUtils.replaceBlank(address);
        }

        public String getACCOUNTMANAGERPIC() {
            return ACCOUNTMANAGERPIC;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getBelongBank() {
            return belongBank;
        }

        public String getManagerid() {
            return managerid;
        }

        public String getPro_Coding() {
            return pro_Coding;
        }

        public String getOrdersId() {
            return ordersId;
        }
        public String getCheck_result() {
            return check_result == null ? "" : check_result;
        }

        public void setCheck_result(String check_result) {
            this.check_result = check_result;
        }

        public String getCheck_status() {
            return check_status == null ? "" : check_status;
        }

        public void setCheck_status(String check_status) {
            this.check_status = check_status;
        }

    }
}
