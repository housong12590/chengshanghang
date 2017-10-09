package com.jmm.csg.http.service;


import com.jmm.csg.bean.ManagerCenter;
import com.jmm.csg.bean.Bank;
import com.jmm.csg.bean.BaseResp;
import com.jmm.csg.bean.Order;
import com.jmm.csg.bean.Product;
import com.jmm.csg.bean.ProductDetail;
import com.jmm.csg.bean.UserInfo;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ManagerService {

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
    Observable<UserInfo> register(@FieldMap Map<String, String> params);

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
     * 客户经理实名认证
     */
    @FormUrlEncoded
    @POST("accountManager/api/realNameRequest")
    Observable<BaseResp> realNameRequest(@FieldMap Map<String, String> params);

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
}
