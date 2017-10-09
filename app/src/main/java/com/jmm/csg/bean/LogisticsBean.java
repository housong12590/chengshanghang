package com.jmm.csg.bean;

import android.text.TextUtils;

import java.util.List;

public class LogisticsBean {

    private String message;
    private String nu;
    private String ischeck;
    private String condition;
    private String com;
    private String status;
    private String state;
    private List<Entity> data;

    public String getMessage() {
        return message;
    }

    public String getNu() {
        return nu;
    }

    public String getIscheck() {
        return ischeck;
    }

    public String getCondition() {
        return condition;
    }

    public String getCom() {
        return com;
    }

    public String getStatus() {
        return status;
    }

    public String getState() {
        return TextUtils.isEmpty(state) ? "暂无" : state;
    }

    public List<Entity> getData() {
        return data;
    }

    public static class Entity {
        private String time;
        private String ftime;
        private String context;

        public String getTime() {
            return time;
        }

        public String getFtime() {
            return ftime;
        }

        public String getContext() {
            return context;
        }
    }
}
