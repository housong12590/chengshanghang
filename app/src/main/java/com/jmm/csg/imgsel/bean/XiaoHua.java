package com.jmm.csg.imgsel.bean;


import java.util.List;

public class XiaoHua {

    public String status;
    public String msg;
    public Result result;


    public static class Result {
        public String total;
        public int pagenum;
        public String pagesize;
        public List<Bean> list;
    }

    public static class Bean {
        public String content;
        public String addtime;
        public String url;
    }

}
