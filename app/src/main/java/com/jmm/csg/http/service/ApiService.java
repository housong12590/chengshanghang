package com.jmm.csg.http.service;


import com.jmm.csg.bean.AddressInfo;
import com.jmm.csg.bean.AppUpdate;
import com.jmm.csg.bean.Bank;
import com.jmm.csg.bean.BaseResp;
import com.jmm.csg.bean.Integral;
import com.jmm.csg.bean.IntegralDetails;
import com.jmm.csg.bean.IntegralProduct;
import com.jmm.csg.bean.IntegralRecord;
import com.jmm.csg.bean.LogisticsBean;
import com.jmm.csg.bean.ManagerCenter;
import com.jmm.csg.bean.Order;
import com.jmm.csg.bean.OrderDetail;
import com.jmm.csg.bean.PayResult;
import com.jmm.csg.bean.Product;
import com.jmm.csg.bean.ProductDetail;
import com.jmm.csg.bean.ProductReview;
import com.jmm.csg.bean.ROrderDetail;
import com.jmm.csg.bean.RealNameBean;
import com.jmm.csg.bean.RefundDetail;
import com.jmm.csg.bean.ReturnAddress;
import com.jmm.csg.bean.SubmitOrderBean;
import com.jmm.csg.bean.UnionPayBean;
import com.jmm.csg.bean.UserInfo;
import com.jmm.csg.bean.WeChatBean;
import com.jmm.csg.imgsel.bean.XiaoHua;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface ApiService {

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("accountManager/accountManagerLogin")
    Observable<UserInfo> login(@FieldMap Map<String, String> params);

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("accountManager/registerAccountManager")
    Observable<BaseResp> register(@FieldMap Map<String, String> params);

    /**
     * 忘记密码
     */
    @FormUrlEncoded
    @POST("appuser/forgetpwd")
    Observable<UserInfo> forgetpwd(@FieldMap Map<String, String> params);

    /**
     * 获取验证码
     */
    @FormUrlEncoded
    @POST("appuser/getVerificationCode")
    Observable<BaseResp> getVerificationCode(@FieldMap Map<String, String> params);

    /**
     * 总行的所有信息
     */
    @FormUrlEncoded
    @POST("accountManager/api/getBanks")
    Observable<Bank> getBanks(@FieldMap Map<String, String> params);

    /**
     * 根据 总行id 和 所属区域 查找所有分行
     */
    @FormUrlEncoded
    @POST("accountManager/api/getBranchBanks")
    Observable<List<Bank.BranchBank>> getBranchBanks(@FieldMap Map<String, String> params);

    /**
     * 客户经理实名认证
     */
    @FormUrlEncoded
    @POST("accountManager/api/realNameRequest")
    Observable<RealNameBean> realNameRequest(@FieldMap Map<String, String> params);

    /**
     * 客户经理中心相关信息、客户经理个人信息
     */
    @FormUrlEncoded
    @POST("accountManager/api/managerCenter")
    Observable<ManagerCenter> managerCenter(@FieldMap Map<String, String> params);

    /**
     * 客户经理任务列表
     */
    @FormUrlEncoded
    @POST("accountManager/api/missionList")
    Observable<List<Product>> missionList(@FieldMap Map<String, String> params);

    /**
     * 商品详情
     */
    @FormUrlEncoded
    @POST("accountManager/proDetail")
    Observable<ProductDetail> proDetail(@FieldMap Map<String, String> params);

    /**
     * 订单列表
     */
    @FormUrlEncoded
    @POST("accountManager/api/orderList")
    Observable<Order> orderList(@FieldMap Map<String, String> params);

    /**
     * 批量获取客户经理得简略信息
     */
    @FormUrlEncoded
    @POST("appProductOrder/getAllAccountManager")
    Observable<Object> getAllAccountManager(@FieldMap Map<String, String> params);

    /**
     * 提交未付款状态订单
     */
    @FormUrlEncoded
    @POST("appProductOrder/api/NewSubmitOrder")
    Observable<SubmitOrderBean> submitOrder(@FieldMap Map<String, String> params);

    /**
     * 根据订单号查找订单信息
     */
    @FormUrlEncoded
    @POST("appProductOrder/api/validateOrderId")
    Observable<Order.Entity> validateOrderId(@FieldMap Map<String, String> params);

    /**
     * 根据订单号，得到商品信息
     */
    @FormUrlEncoded
    @POST("appProductOrder/productByOrderId")
    Observable<Object> productByOrderId(@FieldMap Map<String, String> params);

    /**
     * 设置默认收货地址
     */
    @FormUrlEncoded
    @POST("appuser/setDefaultAddress")
    Observable<Object> setDefaultAddress(@FieldMap Map<String, String> params);

    /**
     * 修改订单状态
     */
    @FormUrlEncoded
    @POST("appuser/modifyorderstatus")
    Observable<BaseResp> changeOrderState(@FieldMap Map<String, String> params);

    /**
     * 支付订单
     */
    @FormUrlEncoded
    @POST("appProductOrder/api/payOrder")
    Observable<BaseResp> payOrder(@FieldMap Map<String, String> params);

    /**
     * 根据订单号查询订单项
     */
    @FormUrlEncoded
    @POST("appProductOrder/orderItemsByorderId")
    Observable<Object> orderItemsByorderId(@FieldMap Map<String, String> params);

    /**
     * 查询此订单是否可以退换货
     */
    @FormUrlEncoded
    @POST("appCustomerService/isCustomerService")
    Observable<Object> isCustomerService(@FieldMap Map<String, String> params);

    /**
     * 根据订单id得到商品数量
     */
    @FormUrlEncoded
    @POST("appCustomerService/api/getProductCountByorderId")
    Observable<Object> getProductCountByorderId(@FieldMap Map<String, String> params);

    /**
     * 得到可退换货的最大数量
     */
    @FormUrlEncoded
    @POST("appCustomerService/api/getRECountByorderId")
    Observable<BaseResp> getRECountByorderId(@FieldMap Map<String, String> params);

    /**
     * 插入退换货订单
     */
    @FormUrlEncoded
    @POST("accountManager/insertREorder")
    Observable<BaseResp> insertREorder(@FieldMap Map<String, String> params);

    /**
     * 通过退货订单id找到退货订单
     */
    @FormUrlEncoded
    @POST("appCustomerService/getREOrder")
    Observable<Object> getREOrder(@FieldMap Map<String, String> params);

    /**
     * 更新退货订单
     */
    @FormUrlEncoded
    @POST("appCustomerService/updateREOrder")
    Observable<Object> updateREOrder(@FieldMap Map<String, String> params);

    /**
     * 客户经理里审核售后拒绝后发送短信
     */
    @FormUrlEncoded
    @POST("appCustomerService/customerServiceFailSMS")
    Observable<Object> customerServiceFailSMS(@FieldMap Map<String, String> params);

    /**
     * 订单详情
     */
    @FormUrlEncoded
    @POST("appuser/getorderdetail")
    Observable<OrderDetail> getOrderDetail(@FieldMap Map<String, String> params);


    /**
     * 获取收货地址列表
     */
    @FormUrlEncoded
    @POST("appuser/api/getAddressList")
    Observable<AddressInfo> getAddressList(@FieldMap Map<String, String> params);

    /**
     * 添加/修改收货地址
     */
    @FormUrlEncoded
    @POST("appuser/api/savenewaddress")
    Observable<BaseResp> savaNewAddress(@FieldMap Map<String, String> params);

    /**
     * 删除收货地址
     */
    @FormUrlEncoded
    @POST("appuser/api/deleteAddress")
    Observable<BaseResp> deleteAddress(@FieldMap Map<String, String> params);

    /**
     * 获取自提地址
     */
    @FormUrlEncoded
    @POST("appProductOrder/api/getSelfMentionAddress")
    Observable<AddressInfo.ZtAddressEntity> getSelfMentionAddress(@FieldMap Map<String, String> params);

    /**
     * 获取物流信息
     */
    @FormUrlEncoded
    @POST("appProductOrder/api/logisticList")
    Observable<LogisticsBean> logisticList(@FieldMap Map<String, String> params);

    /**
     * 订单评论
     */
    @FormUrlEncoded
    @POST("appuser/createcomment")
    Observable<BaseResp> createComment(@FieldMap Map<String, String> params);

    /**
     * 退换货列表
     */
    @FormUrlEncoded
    @POST("accountManager/api/reorderList")
    Observable<Order> reOrderList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("appinfo/getAppInfoByNameBySys")
    Observable<AppUpdate> checkAppUpdate(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("cap/api/apppay")
    Observable<UnionPayBean> appPay(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("appProductOrder/api/appPayWx")
    Observable<WeChatBean> payWx(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("accountManager/api/modifyMgrPhone")
    Observable<BaseResp> modifyMgrPhone(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("appuser/checkLoginId")
    Observable<BaseResp> checkLoginId(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("appuser/api/getReturnAddress")
    Observable<ReturnAddress> getReturnAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("appuser/api/commitReturnDetail")
    Observable<BaseResp> commitReturnDetail(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("cap/api/orderQuery")
    Observable<PayResult> orderQuery(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("accountManager/api/verfyReturn")
    Observable<BaseResp> verfyReturn(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("accountManager/api/productList")
    Observable<List<Product>> productList(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("accountManager/api/getReturnGoods")
    Observable<ROrderDetail> getReturnGoods(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("accountManager/api/getAvailable")
    Observable<Integral> getAvailable(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("integal/api/getProductList")
    Observable<IntegralProduct> getProductList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("huaFei/api/huaFeiExchange")
    Observable<Integral> huaFeiExchange(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("integal/api/exchangeOilCard")
    Observable<Integral> exchangeOilCard(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("liuliang/api/liuLiangIntegalExchange")
    Observable<Integral> liuLiangIntegalExchange(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("integal/api/getExchangeRecord")
    Observable<IntegralRecord> getExchangeRecord(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("accountManager/api/getIntegralDetail")
    Observable<IntegralDetails> getIntegralDetail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("accountManager/api/shareSuccess")
    Observable<Object> shareSuccess(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("accountManager/getProDetailForApp")
    Observable<ProductDetail> getProDetailForApp(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("accountManager/getReviewForPublic")
    Observable<ProductReview> getReviewForPublic(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("refund/api/apply")
    Observable<BaseResp> applyRefun(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("refund/api/cancle")
    Observable<BaseResp> cancleRefun(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("refund/api/query")
    Observable<RefundDetail> queryRefun(@FieldMap Map<String, String> params);


    /**
     * 笑话
     *
     * @param params
     * @return
     */
    @Headers("Authorization:APPCODE 44a27dc40e2747ffb730e200fa4528c1")
    @GET("xiaohua/text")
    Observable<XiaoHua> xiaohua(@QueryMap Map<String, String> params);

}
