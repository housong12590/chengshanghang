package com.jmm.csg.bean;

/**
 * author：hs
 * date: 2017/6/14 0014 20:40
 */

public class UnionPayBean {
    private String result;
    private Data data;

    public String getResult() {
        return result;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private String tn;
        private String txnTime;
        private String payid;

        public String getTxnTime() {
            return txnTime;
        }

        public String getPayid() {
            return payid;
        }

        public String getTn() {
            return tn;
        }
    }

}
