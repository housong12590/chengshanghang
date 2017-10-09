package com.jmm.csg.bean;

/**
 * author：hs
 * date: 2017/5/16 0016 14:22
 */
public class AppUpdate {

    private Entity data;

    public Entity getData() {
        return data;
    }

    public void setData(Entity data) {
        this.data = data;
    }

    public static class Entity {
        private String apkContent;
        private String apkVersionsCode;
        private String apkVersionsName;
        private String appname;
        private String apkIsConstaintUpdate;
        private String apkUrl;
        private String cREATE_DATE;
        private String id;
        private String createDate;


        public String getApkContent() {
            return apkContent;
        }

        public void setApkContent(String apkContent) {
            this.apkContent = apkContent;
        }

        public String getApkVersionsCode() {
            return apkVersionsCode;
        }

        public void setApkVersionsCode(String apkVersionsCode) {
            this.apkVersionsCode = apkVersionsCode;
        }

        public String getApkVersionsName() {
            return apkVersionsName;
        }

        public void setApkVersionsName(String apkVersionsName) {
            this.apkVersionsName = apkVersionsName;
        }

        public String getAppname() {
            return appname;
        }

        public void setAppname(String appname) {
            this.appname = appname;
        }

        public String getApkIsConstaintUpdate() {
            return apkIsConstaintUpdate;
        }

        public void setApkIsConstaintUpdate(String apkIsConstaintUpdate) {
            this.apkIsConstaintUpdate = apkIsConstaintUpdate;
        }

        public String getApkUrl() {
            return apkUrl;
        }

        public void setApkUrl(String apkUrl) {
            this.apkUrl = apkUrl;
        }

        public String getcREATE_DATE() {
            return cREATE_DATE;
        }

        public void setcREATE_DATE(String cREATE_DATE) {
            this.cREATE_DATE = cREATE_DATE;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }



}
