package com.jmm.csg.bean;

import android.text.TextUtils;

/**
 * author：hs
 * date: 2017/6/13 0013 15:56
 */

public class ReturnAddress {

    public Entity returnAddress;

    public Entity getReturnAddress() {
        return returnAddress == null ? new Entity() : returnAddress;
    }

    public static class Entity {
        private String CONTACTS;
        private String PHONE_NUM;
        private String MODIFER_NAME;
        private String CREATOR_ID;
        private String CREATE_DATE;
        private String AREA;
        private String CITY;
        private String MODIFY_DATE;
        private String ADDRESS;
        private String CREATOR_NAME;
        private String PROVINCE;
        private String MODIFER_ID;
        private String id;

        public String getName() {
            return CONTACTS;
        }

        public String getAddress() {
            if (TextUtils.isEmpty(PROVINCE)) {
                return null;
            }
            return PROVINCE + CITY + AREA;
        }

        public String getPhoneNum() {
            return PHONE_NUM;
        }
    }
}
