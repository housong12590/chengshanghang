package com.jmm.csg.bean;


import android.text.TextUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class AddressInfo {

    private List<Entity> address;
    private List<Entity> selfAddress;

    public List<Entity> getAddress() {

        return address.isEmpty() ? Collections.emptyList() : address;
    }

    public List<Entity> getSelfAddress() {
        return selfAddress.isEmpty() ? Collections.emptyList() : selfAddress;
    }

    public static class Entity implements Serializable {
        private String ADDRESS_ID;
        private String ISPRIMARY; //1为默认地址
        private String DISTRICT;
        private String PHONENUMBER;
        private String PROVINCE;
        private String ADDRESS;
        private String CONSIGNEE;
        private String USERID;
        private String CITY;
        private String pickWay;

        public Entity() {
        }

        public Entity(String district, String PHONENUMBER, String province, String ADDRESS, String CONSIGNEE, String CITY) {
            this.DISTRICT = district;
            this.PHONENUMBER = PHONENUMBER;
            this.PROVINCE = province;
            this.ADDRESS = ADDRESS;
            this.CONSIGNEE = CONSIGNEE;
            this.CITY = CITY;
        }

        public void setISPRIMARY(String ISPRIMARY) {
            this.ISPRIMARY = ISPRIMARY;
        }

        public String getPickWay() {
            return pickWay;
        }

        public void setCity(String CITY) {
            this.CITY = CITY;
        }

        public void setConsignee(String CONSIGNEE) {
            this.CONSIGNEE = CONSIGNEE;
        }

        public void setAddress(String ADDRESS) {
            this.ADDRESS = ADDRESS;
        }

        public void setProvince(String PROVINCE) {
            this.PROVINCE = PROVINCE;
        }

        public void setPhonenumber(String PHONENUMBER) {
            this.PHONENUMBER = PHONENUMBER;
        }

        public void setDISTRICT(String DISTRICT) {
            this.DISTRICT = DISTRICT;
        }

        public String getCity() {
            if (TextUtils.isEmpty(CITY)) {
                CITY = "";
            }
            return CITY;
        }

        public String getAddressId() {
            if(TextUtils.isEmpty(ADDRESS_ID)){
                ADDRESS_ID = "";
            }
            return ADDRESS_ID;
        }

        public void setADDRESS_ID(String ADDRESS_ID) {
            this.ADDRESS_ID = ADDRESS_ID;
        }

        public String getIsPrimary() {
            return ISPRIMARY;
        }

        public String getDistrict() {
            return DISTRICT;
        }

        public String getPhoneNumber() {
            return PHONENUMBER;
        }

        public String getProvince() {
            return PROVINCE;
        }

        public String getAddress() {
            return ADDRESS;
        }

        public String getConsignee() {
            return CONSIGNEE;
        }

        public String getUserId() {
            return USERID;
        }

        public String getConsigneeAddress() {
            return PROVINCE + getCity() + DISTRICT + ADDRESS;
        }
    }

    public static class ZtAddressEntity implements Serializable {
        private String id;
        private String phone;
        private String province;
        private String branchName;
        private String bankName;
        private String district;
        private String city;
        private String accountManagerName;

        public String getId() {
            return id;
        }

        public String getPhone() {
            return phone;
        }

        public String getProvince() {
            return province;
        }

        public String getBranchName() {
            return branchName;
        }

        public String getBankName() {
            return bankName;
        }

        public String getDistrict() {
            return district;
        }

        public String getCity() {
            return city;
        }

        public String getAccountManagerName() {
            return accountManagerName;
        }
    }

}
