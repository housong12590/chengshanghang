package com.jmm.csg.bean;

import java.io.Serializable;

public class Account implements Serializable {

    private String phonenumber;
    private String accountmanagerpic;
    private String status;
    private String idcard;
    private String bankName;
    private String belongtobank; //所属总行
    private String idcardpic;
    private String id;
    private String accountmanagername;
    private String jobnumber;
    private String area;
    private String branchName;
    private String subbranch;
    private String flag;
    private String isintegral;

    public String getIsintegral() {
        return isintegral == null ? "0" : isintegral;
    }

    public String getFlag() {
        return flag;
    }

    public String getPhoneNumber() {
        return phonenumber;
    }

    public String getAccountManagerPic() {
        return accountmanagerpic;
    }

    public String getStatus() {
        return status;
    }

    public String getIdcard() {
        return idcard;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBelongtobank() {
        return belongtobank;
    }

    public String getCardPic() {
        return idcardpic;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return accountmanagername == null ? "" : accountmanagername;
    }

    public String getJobNumber() {
        return jobnumber;
    }

    public String getArea() {
        return area;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getSubbranch() {
        return subbranch;
    }
}
