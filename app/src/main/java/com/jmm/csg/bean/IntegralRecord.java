package com.jmm.csg.bean;


import java.util.List;

public class IntegralRecord {

    private String code;
    private Data data;

    public String getCode() {
        return code;
    }

    public Data getData() {
        return data;
    }


    public static class Data {
        private List<Entity> list;
        private Pager pager;

        public List<Entity> getList() {
            return list;
        }

        public Pager getPager() {
            return pager;
        }

        public static class Pager {
            private String pageNo;
            private String pageSize;
            private String totalPage;

            public String getPageNo() {
                return pageNo;
            }

            public String getPageSize() {
                return pageSize;
            }

            public String getTotalPage() {
                return totalPage;
            }
        }

        public static class Entity {
            private String cardId;
            private String id;
            private String cardName;
            private String phone;
            private String flag;
            private String integralNum;
            private String createDate;
            private String productName;
            private String orderId;

            public String getCardId() {
                return cardId;
            }

            public String getId() {
                return id;
            }

            public String getCardName() {
                return cardName;
            }

            public String getPhone() {
                return phone;
            }

            public String getFlag() {
                return flag;
            }

            public String getIntegralNum() {
                return integralNum;
            }

            public String getCreateDate() {
                return createDate;
            }

            public String getProductName() {
                return productName;
            }

            public String getOrderId() {
                return orderId;
            }
        }
    }
}