package com.jmm.csg;


public final class Constant {

    public static final String PWD_CHECK = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";

    public static final String PHONE_CHECK = "((1[0-9]))\\d{9}$";

    public static final String PHONE_CHECK2 = "^((1[\\d]{10})|0[\\d]{2,3}-?[ ]?[\\d]{7,8})";

    public static final String VERIFY_CODE_CHECK = "\\d{6}";

    public static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";

    public static final int SETUP_GESTURE_PWD = 1001;

    public static final int REFRESH_ORDER_STATUS = 1002;

    public static final int REFRESH_MAIN_DATA = 1003;

    public static final int PAY_SUCCESS = 2000;

    public static final int NON_PAYMENT = 2001;

    /**
     * 未审核
     */
    public static final String REALNAME_NO = "0";
    /**
     * 审核中
     */
    public static final String REALNAME_ING = "1";
    /**
     * 审核通过
     */
    public static final String REALNAME_YES = "2";
    /**
     * 审核未通过
     */
    public static final String REALNAME_NO_PASS = "3";
    /**
     * 审核被驳回
     */
    public static final String REALNAME_RETURN = "4";

    public static final String INTEGRAL_COST = "1"; // 话费兑换

    public static final String INTEGRAL_FLOW = "2"; // 流量兑换

    public static final String INTEGRAL_CARD = "3"; // 加油卡充值

}
