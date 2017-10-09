package com.jmm.csg.bean;


import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private List<Entity> orderList;
    private List<Entity> returnOrderList;
    private String pageCount;

    public List<Entity> getReturnOrderList() {
        if (returnOrderList == null) {
            returnOrderList = new ArrayList<>();
        }
        return returnOrderList;
    }

    public List<Entity> getOrderList() {
        if (orderList == null) {
            orderList = new ArrayList<>();
        }
        return orderList;
    }

    public String getPageCount() {
        return pageCount;
    }

    public static class Entity {
        private String phonenumber; //电话号码
        private String shippingcharge; //运费
        private String ACCOUNTMANAGERNAME; //客户经理名字
        private String ACCOUNTMANAGERMOBILE; //客户经理手机号码
        private String orderitemsid; //订单项id
        private String consignee; //收货人
        private String ordesstatus; //订单状态
        private String orderspayprice;//订单支付价格
        private String JOBNUMBER; //工号
        private String id;//用户id
        private String createtime; //下单时间
        private String pro_price; //商品价格
        private String pro_Specification;//规格
        private String pro_Name;//商品名字
        private String address; //地址
        private String ACCOUNTMANAGERPIC; // 个人头像
        private String quantity; // 数量
        private String belongBank;//总行的id
        private String managerid; //客户经理id
        private String pro_Coding; //规格编码
        private String ordersId; //订单ID
        private String remark; // 评论
        private String check_result;//退款结果
        private String check_status;//退款状态
        private String lastupdatetime;
        private String addressInfo;
        private String memberId;
        private String ordersprice;
        private String REorderId;
        private String status;
        private String payprice;
        private String flag;
        private String checkstatus;
        private String pro_pic;

        public String getPro_pic() {
            return pro_pic;
        }

        public String getRemark() {
            return remark;
        }

        public String getLastupdatetime() {
            return lastupdatetime;
        }

        public String getAddressInfo() {
            return addressInfo;
        }

        public String getMemberId() {
            return memberId;
        }

        public String getOrdersprice() {
            return ordersprice;
        }

        public String getPhonenumber() {
            return phonenumber;
        }

        public String getShippingcharge() {
//            return shippingcharge;
            try {
                float charge = Float.parseFloat(shippingcharge);
                return String.format("%.2f", charge);
            } catch (Exception e) {
                return "0.00";
            }
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


        public OrderStatus getOrderStatus() {
            OrderStatus status = OrderStatus.A;
            switch (ordesstatus) {
                case "M":
                    status = OrderStatus.M;
                    break;
                case "C":
                    status = OrderStatus.C;
                    break;
                case "S":
                    status = OrderStatus.S;
                    break;
                case "X":
                    status = OrderStatus.X;
                    break;
                case "F":
                    status = OrderStatus.F;
                    break;
                case "Q":
                    status = OrderStatus.Q;
                    break;
            }
            return status;
        }

        public String getOrderspayprice() {
            if (TextUtils.isEmpty(orderspayprice)) {
                return "";
            }
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

        public String getPro_price() {
            double price = Double.parseDouble(pro_price) / 100;
            return String.format("%.2f", price);
        }

        public String getPro_Specification() {
            return pro_Specification;
        }

        public String getPro_Name() {
            return pro_Name;
        }

        public String getAddress() {
            return address;
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

        public String getREorderId() {
            return REorderId;
        }

        public String getStatus() {
            return status;
        }

        public String getPayprice() {
            return payprice;
        }

        public String getFlag() {
            return flag == null ? "" : flag;
        }

        public String getCheckstatus() {
            return checkstatus;
        }
    }
}
