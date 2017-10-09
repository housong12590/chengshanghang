package com.jmm.csg.http;


import android.text.TextUtils;

import com.jmm.csg.BaseApplication;
import com.jmm.csg.R;
import com.jmm.csg.bean.UploadBean;
import com.jmm.csg.utils.StringUtils;
import com.jmm.csg.utils.TimeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpParams {

//    private static final String KEY = "jmm1441658802";

    private static Map<String, String> getMap() {
        return new HashMap<>();
    }

    public static Map<String, String> signature() {
        return signature(new HashMap<>());
    }

    public static Map<String, String> signature(Map<String, String> params) {
        String timestamp = TimeUtils.getTimestamp();
//        params.put("scret", StringUtils.getSignature(KEY, timestamp));
        Map<String, String> tempMap;
        tempMap = params;
//        params.put("scret", StringUtils.getSignature(params, timestamp));
        String base64 = StringUtils.getBase64(StringUtils.getJson(params));


        String key = getString(R.string.czlyj);
        params.put("scret", StringUtils.getSignature(key, timestamp, base64));

        params.put("context", base64);
        params.put("time", timestamp);
        return params;
    }

    private static String getString(int resId) {
        return BaseApplication.getContext().getString(resId);
    }

    /**
     * 获取支行
     */
    public static Map<String, String> getBranchBanks(String bankId,
                                                     String province,
                                                     String city,
                                                     String district) {
        Map<String, String> params = getMap();
        params.put("bankId", bankId);
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        return signature(params);
    }

    /**
     * 实名认证
     */
    public static Map<String, String> realNameRequest(String id, String name, String area, String phone,
                                                      String jobnum, String idCardNumber, String picPath,
                                                      String idCardPicPath, String idCardPicPath2,
                                                      String bank, String branch) {
        Map<String, String> params = getMap();
        params.put("id", id);
        params.put("name", name);
        params.put("area", area);
        params.put("phone", phone);
        params.put("jobnum", jobnum);
        params.put("idCardNumber", idCardNumber);
        params.put("picPath", picPath);
        params.put("idCardPicPath", idCardPicPath);
        params.put("idCardPicPath2", idCardPicPath2);
        params.put("bank", bank);
        params.put("branch", branch);
        return signature(params);
    }

    /**
     * 登录
     */
    public static Map<String, String> login(String account, String logonPassword) {
        Map<String, String> params = getMap();
        params.put("account", account);
        params.put("logonPassword", logonPassword);
        return signature(params);
    }


    /**
     * 注册
     */
    public static Map<String, String> register(String mobileNumber, String password, String smsCode) {
        Map<String, String> params = getMap();
        params.put("mobileNumber", mobileNumber);
        params.put("password", password);
        params.put("smsCode", smsCode);
        return signature(params);
    }

    /**
     * 忘记密码
     */
    public static Map<String, String> forgetpwd(String mobileNumber, String resetPassword, String smsCode) {
        Map<String, String> params = getMap();
        params.put("mobileNumber", mobileNumber);
        params.put("resetPassword", resetPassword);
        params.put("smsCode", smsCode);
        return signature(params);
    }

    /**
     * 获取验证码
     * smsflag reg注册 fwd忘记密码
     */
    public static Map<String, String> getVerificationCode(String phone, String smsflag) {
        Map<String, String> params = getMap();
        params.put("phone", phone);
        params.put("smsflag", smsflag);
        return signature(params);
    }

    /**
     * 获取客户经理数据
     */
    public static Map<String, String> managerCenter(String userId) {
        Map<String, String> params = getMap();
        params.put("id", userId);
        return signature(params);
    }

    /**
     * 订单列表
     */
    public static Map<String, String> orderList(String accountManagerId, String userId, String orderStatus,
                                                String pageNo, String pageSize) {
        Map<String, String> params = getMap();

        if (accountManagerId != null) {
            params.put("accountManagerId", accountManagerId);
        }
        if (userId != null) {
            params.put("userid", userId);
        }
        if (orderStatus != null) {
            params.put("orderStatus", orderStatus);
        }
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        return signature(params);
    }

    /**
     * 设置默认地址
     */
    public static Map<String, String> setDefaultAddress(String addressId, String userId) {
        Map<String, String> params = getMap();
        params.put("ADDRESS_ID", addressId);
        params.put("USERID", userId);
        return signature(params);
    }

    /**
     * 更改订单状态
     */
    public static Map<String, String> changeOrderState(String orderId, String userId, String ordesStatus) {
        Map<String, String> params = getMap();
        params.put("ordersid", orderId);
        params.put("userid", userId);
        params.put("ordesstatus", ordesStatus);
        return signature(params);
    }

    /**
     * 根据订单id得到商品数量
     */
    public static Map<String, String> getProductCountByorderId(String orderId) {
        Map<String, String> params = getMap();
        params.put("orderId", orderId);
        return signature(params);
    }

    /**
     * 得到可退换货的最大数量
     */
    public static Map<String, String> getRECountByorderId(String orderId) {
        Map<String, String> params = getMap();
        params.put("orderId", orderId);
        return signature(params);
    }

    /**
     * 根据订单号查找订单信息
     */
    public static Map<String, String> validateOrderId(String orderId) {
        Map<String, String> params = getMap();
        params.put("orderId", orderId);
        return signature(params);
    }

    /**
     * 客户经理任务列表
     */
    public static Map<String, String> missionList(String id, String product) {
        Map<String, String> params = getMap();
        params.put("id", id);
        if (product != null) {
            params.put("product", product);
        }
        return signature(params);
    }

    /**
     * 商品列表
     */
    public static Map<String, String> productList(String id) {
        Map<String, String> params = getMap();
        params.put("id", id);
        return signature(params);
    }

    /**
     * 商品详情
     *
     * @deprecated use {@link #getProDetailForApp(String, String)}}
     */
    public static Map<String, String> proDetail(String productId, String id) {
        Map<String, String> params = getMap();
        params.put("productId", productId);
        params.put("id", id);
        return signature(params);
    }

    /**
     * 商品详情
     */
    public static Map<String, String> getProDetailForApp(String managerId, String productId) {
        Map<String, String> params = getMap();
        params.put("managerId", managerId);
        params.put("productId", productId);
        return signature(params);
    }

    /**
     * 商品评价
     */
    public static Map<String, String> getReviewForPublic(String managerId, String productId, String type,
                                                         String pageNo, String pageSize) {
        Map<String, String> params = getMap();
        params.put("managerId", managerId);
        if (type != null) {
            params.put("type", type);
        }
        params.put("productId", productId);
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        return signature(params);
    }

    /**
     * 提交未付款状态订单
     */
    public static Map<String, String> submitOrder(String accountManagerId, String productId,
                                                  String addressInfo, String pickWay, String count,
                                                  String userId, String remark, String InvoiceInfo,
                                                  String invoiceType, String titleType, String missionId,
                                                  String companyName, String taxIdentNum) {
        Map<String, String> params = getMap();
        params.put("accountManagerId", accountManagerId);
        params.put("productId", productId);
        params.put("addressInfo", addressInfo);
        params.put("pickWay", pickWay);//A自提,S供应商发货
        params.put("count", count);
        params.put("userId", userId);
        params.put("remark", remark);
        params.put("InvoiceInfo", InvoiceInfo);
        params.put("invoiceType", invoiceType);//P是纸质,E是电子,N不开发票
        params.put("titleType", titleType); // P是个人,S是企业
        if (TextUtils.isEmpty(missionId)) {
            missionId = "";
        }
        params.put("companyName", companyName);
        params.put("taxIdentNum", taxIdentNum);
        params.put("missionId", missionId); //客户经理任务ID
        return signature(params);
    }

    /**
     * 获取订单详情
     */
    public static Map<String, String> getOrderDetail(String accountManagerId, String ordersId) {
        Map<String, String> params = getMap();
        params.put("accountManagerId", accountManagerId);
        params.put("ordersId", ordersId);
        return signature(params);
    }

    /**
     * 获取收货地址列表
     */
    public static Map<String, String> getAddressList(String userId) {
        Map<String, String> params = getMap();
        params.put("USER_ID", userId);
        return signature(params);
    }

    /**
     * 添加/修改收货地址
     */
    public static Map<String, String> saveNewAddress(String userid, String addressid, String consignee,
                                                     String phoneNumber, String isprimary,
                                                     String address, String province,
                                                     String city, String district) {

        Map<String, String> params = getMap();
        params.put("userid", userid);
        params.put("addressid", addressid);
        params.put("consignee", consignee);
        params.put("phoneNumber", phoneNumber);
        params.put("isprimary", isprimary);
        params.put("address", address);
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        return signature(params);
    }

    /**
     * 获取自提地址
     */
    public static Map<String, String> getSelfMentionAddress(String accountManagerId) {
        Map<String, String> params = getMap();
        params.put("accountManagerId", accountManagerId);
        return signature(params);
    }

    /**
     * 删除收货地址
     */
    public static Map<String, String> deleteAddress(String addressId, String userId) {
        Map<String, String> params = getMap();
        params.put("ADDRESS_ID", addressId);
        params.put("USERID", userId);
        return signature(params);
    }

    /**
     * 退换货申请
     */
    public static Map<String, String> insertREorder(String orderId, String userId, String reason, String count,
                                                    String remark, String type, String addressInfo,
                                                    List<String> images) {
        Map<String, String> params = getMap();
        params.put("orderId", orderId);
        params.put("userId", userId);
        params.put("reason", reason);
        params.put("count", count);
        params.put("type", type);
        if (!TextUtils.isEmpty(remark)) {
            params.put("remark", remark);
        }
        params.put("addressInfo", addressInfo);
        if (images != null) {
            for (int i = 0; i < images.size(); i++) {
                params.put("image" + (i + 1), images.get(i));
            }
        }
        return signature(params);
    }

    /**
     * 物流信息
     */
    public static Map<String, String> logisticLis(String ordersId) {
        Map<String, String> params = getMap();
        params.put("ordersId", ordersId);
        return signature(params);
    }

    /**
     * 订单评价
     *
     * @param productrade        商品好差评
     * @param accountmanagerrade 客户经理好差评
     * @param pcomment           商品评价
     * @param acomment           客户经理评价
     * @param ordersid           订单ID
     * @param orderitemsid       订单XXID
     * @param productid          商品ID
     * @param managerid          客户经理评价
     * @param userid             用户ID
     */
    public static Map<String, String> commitComment(String productrade, String accountmanagerrade,
                                                    String pcomment, String acomment, String ordersid,
                                                    String orderitemsid, String productid,
                                                    String managerid, String userid, String status) {
        Map<String, String> params = getMap();
        if (!TextUtils.isEmpty(productrade)) {
            params.put("productrade", productrade);
        }
        if (!TextUtils.isEmpty(accountmanagerrade)) {
            params.put("accountmanagerrade", accountmanagerrade);
        }
        if (!TextUtils.isEmpty(pcomment)) {
            params.put("pcomment", pcomment);
        }
        if (!TextUtils.isEmpty(acomment)) {
            params.put("acomment", acomment);
        }
        params.put("ordersid", ordersid);
        params.put("orderitemsid", orderitemsid);
        params.put("productid", productid);
        params.put("managerid", managerid);
        params.put("userid", userid);
//        params.put("status", status);
        return signature(params);
    }

    /**
     * 退换货订单列表
     */
    public static Map<String, String> reOrderList(String userId, String accountManagerId, String pageNo, String pageSize) {
        Map<String, String> params = getMap();
        if (userId != null) {
            params.put("userid", userId);
        }
        if (accountManagerId != null) {
            params.put("accountManagerId", accountManagerId);
        }
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        return signature(params);
    }

    /**
     * 支付订单
     */
    public static Map<String, String> payOrder(String userId, String orderId) {
        Map<String, String> params = getMap();
        params.put("orderId", orderId);
        params.put("userId", userId);
        params.put("appType", "Android");
        return signature(params);
    }

    public static Map<String, String> appCheckUpdata() {
        Map<String, String> params = getMap();
        params.put("appname", "jmm");
        params.put("sys", "android");
        return signature(params);
    }

    /**
     * 银联支付
     */
    public static Map<String, String> appPay(String orderId) {
        Map<String, String> params = getMap();
//        params.put("merId", merId);
        params.put("orderId", orderId);
//        params.put("txnAmt", txnAmt);
        return signature(params);
    }

    /**
     * 微信支付接口
     */
    public static Map<String, String> payWx(String orderId) {
        Map<String, String> params = getMap();
        params.put("orderId", orderId);
        params.put("appType", "Android");
        return signature(params);
    }

    /**
     * 客户经理修改手机号
     */
    public static Map<String, String> modifyMgrPhone(String id, String phone) {
        Map<String, String> params = getMap();
        params.put("id", id);
        params.put("phone", phone);
        return signature(params);
    }

    /**
     * 检查该账号是否被注册
     */
    public static Map<String, String> checkLoginId(String loginId) {
        Map<String, String> params = getMap();
        params.put("logonid", loginId);
        return signature(params);
    }

    /**
     * 获取退换货收货地址
     */
    public static Map<String, String> getReturnAddress(String orderItemsId) {
        Map<String, String> params = getMap();
        params.put("orderItemsId", orderItemsId);
        return signature(params);
    }

    /**
     * 提交退换货订单号
     */
    public static Map<String, String> commitReturnDetail(String ordersId, String expressName,
                                                         String expressNo, List<UploadBean> images) {
        Map<String, String> params = getMap();
        params.put("ordersId", ordersId);
        params.put("expressName", expressName);
        params.put("expressNo", expressNo);
        for (int i = 0; i < images.size(); i++) {
            params.put("image" + (6 + i), images.get(i).name);
        }
        return signature(params);
    }

    /**
     * 查询订单支付结果
     */
    public static Map<String, String> orderQuery(String txnTime, String payId) {
        Map<String, String> params = getMap();
        params.put("payid", payId);
        params.put("txnTime", txnTime);
        return signature(params);
    }

    /**
     * 客户经理审核退款
     */
    public static Map<String, String> verfyReturn(String orderId, String flag, String status, String checkremark) {
        Map<String, String> params = getMap();
        params.put("orderid", orderId);
        if (!TextUtils.isEmpty(flag)) {
            params.put("flag", flag);
        }
        params.put("status", status);
        params.put("checkremark", checkremark);
        return signature(params);
    }

    /**
     * 退货订单详情
     */
    public static Map<String, String> getReturnGoods(String orderId) {
        Map<String, String> params = getMap();
        params.put("orderId", orderId);
        return signature(params);
    }

    /**
     * 获取库存数
     */
    public static Map<String, String> productList(String id, String coding) {
        Map<String, String> params = getMap();
        params.put("id", id);
        params.put("coding", coding);
        return signature(params);
    }

    public static Map<String, String> xiaohua(String pagenum, String pagesize, String sort) {
        Map<String, String> params = getMap();
        params.put("pagenum", pagenum);
        params.put("pagesize", pagesize);
        params.put("sort", sort);
        return signature(params);
    }

    /**
     * 获取可用积分
     */
    public static Map<String, String> getAvailable(String userId) {
        Map<String, String> params = getMap();
        params.put("user_id", userId);
        return signature(params);
    }

    /**
     * 兑换商品列表 1.话费2.流量 3加油卡
     */
    public static Map<String, String> getProductList(String productType) {
        Map<String, String> params = getMap();
        params.put("productType", productType);
        return signature(params);
    }

    /**
     * 话费/流量兑换
     */
    public static Map<String, String> exchange(String pid, String userId, String phone) {
        Map<String, String> params = getMap();
        params.put("pid", pid);
        params.put("userId", userId);
        params.put("phone", phone);
        return signature(params);
    }

    /**
     * 加油卡兑换
     */
    public static Map<String, String> exchangeOilCard(String userId, String proId, String game_userid,
                                                      String gasCardTel, String gasCardName) {
        Map<String, String> params = getMap();
        params.put("userId", userId);
        params.put("proId", proId);
        params.put("game_userid", game_userid);
        params.put("gasCardTel", gasCardTel);
        params.put("gasCardName", gasCardName);
        return signature(params);
    }

    /**
     * 积分商品兑换记录
     */
    public static Map<String, String> getExchangeRecord(String userId, String productType, String pageNo, String pageSize) {
        Map<String, String> params = getMap();
        params.put("userId", userId);
        params.put("productType", productType);
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        return signature(params);
    }

    /**
     * 积分明细
     */
    public static Map<String, String> getIntegralDetail(String userId, String pageNo, String pageSize) {
        Map<String, String> params = getMap();
        params.put("userId", userId);
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        return signature(params);
    }


    /**
     * 分享成功后调用,用于统计
     */
    public static Map<String, String> shareSuccess(String amId, String coding) {
        Map<String, String> params = getMap();
        params.put("amId", amId);
        params.put("coding", coding);
        return signature(params);
    }

    /**
     * 申请退款
     */
    public static Map<String, String> applyRefun(String userId, String orderId,String refundReason) {
        Map<String, String> params = getMap();
        params.put("user_id", userId);
        params.put("order_id", orderId);
        params.put("refund_reason", refundReason);
        return signature(params);
    }

    /**
     * 取消退款
     */
    public static Map<String, String> cancleRefun(String userId, String orderId) {
        Map<String, String> params = getMap();
        params.put("user_id", userId);
        params.put("order_id", orderId);
        return signature(params);
    }

    /**
     * 查询退款
     */
    public static Map<String, String> queryRefun(String userId, String orderId) {
        Map<String, String> params = getMap();
        params.put("user_id", userId);
        params.put("order_id", orderId);
        return signature(params);
    }
}
