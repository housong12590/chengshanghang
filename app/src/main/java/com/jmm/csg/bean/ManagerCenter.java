package com.jmm.csg.bean;


import android.text.TextUtils;

public class ManagerCenter {


    private Entity info;
    private Account manager;
    private Integral monthAccount;
    private Integral account;

    public Integral getMonthAccount() {
        return monthAccount == null ? new Integral() : monthAccount;
    }

    public void setMonthAccount(Integral monthAccount) {
        this.monthAccount = monthAccount;
    }

    public Integral getAccount() {
        return account == null ? new Integral() : account;
    }

    public void setAccount(Integral account) {
        this.account = account;
    }

    public static class Integral {
        private String available;
        private String integral;

        public String getAvailable() {
            return TextUtils.isEmpty(available) ? "0" : available;
        }

        public void setAvailable(String available) {
            this.available = available;
        }

        public String getIntegral() {
            return TextUtils.isEmpty(integral) ? "0" : integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }
    }

    public Entity getInfo() {
        return info == null ? new Entity() : info;
    }

    public Account getManager() {
        return manager == null ? new Account() : manager;
    }

    public class Entity {
        private String customerService;
        private String pendReceipt;
        private String finish;
        private String pendEvaluate;
        private String performanceRank;
        private String pendPay;
        private String pendDelivery;
        private String logo;

        public String getLogo() {
            return logo;
        }

        /**
         * 售后订单数量
         */
        public String getCustomerService() {
            return customerService;
        }

        /**
         * 待收货订单数量
         */
        public String getPendReceipt() {
            return pendReceipt;
        }

        /**
         * 已完成订单数量
         */
        public String getFinish() {
            return finish;
        }

        /**
         * 待评价订单数量
         */
        public String getPendEvaluate() {
            return pendEvaluate;
        }

        /**
         * 业绩排名
         */
        public String getPerformanceRank() {
            return performanceRank;
        }

        /**
         * 待付款订单数量
         */
        public String getPendPay() {
            return pendPay;
        }

        /**
         * 代发货订单数量
         */
        public String getPendDelivery() {
            return pendDelivery;
        }
    }
}
