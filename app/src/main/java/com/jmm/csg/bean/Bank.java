package com.jmm.csg.bean;

import java.util.List;

public class Bank {

    private List<BankEntity> banks;

    public List<BankEntity> getBanks() {
        return banks;
    }

    /**
     * 总行
     */
    public static class BankEntity {
        private String id;
        private String logo;
        private String bank_name;
        private String property;
        private String linkman;
        private String telephone;

        public String getId() {
            return id;
        }

        public String getLogo() {
            return logo;
        }

        public String getBank_name() {
            return bank_name;
        }

        public String getProperty() {
            return property;
        }

        public String getLinkman() {
            return linkman;
        }

        public String getTelephone() {
            return telephone;
        }
    }

    /**
     * 支行
     */
    public static class BranchBank {
        private String id;
        private String branch_name;
        private String address_province;
        private String address_city;
        private String address_district;
        private String point;
        private String point_number;
        private String grade;
        private String adm_name;
        private String phone;

        public String getId() {
            return id;
        }

        public String getBranch_name() {
            return branch_name;
        }

        public String getAddress_province() {
            return address_province;
        }

        public String getAddress_city() {
            return address_city;
        }

        public String getAddress_district() {
            return address_district;
        }

        public String getPoint() {
            return point;
        }

        public String getPoint_number() {
            return point_number;
        }

        public String getGrade() {
            return grade;
        }

        public String getAdm_name() {
            return adm_name;
        }

        public String getPhone() {
            return phone;
        }
    }
}
