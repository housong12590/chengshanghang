package com.jmm.csg.http;

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
import com.jmm.csg.config.AppConfig;
import com.jmm.csg.http.log.Level;
import com.jmm.csg.http.log.LoggingInterceptor;
import com.jmm.csg.http.service.ApiService;
import com.jmm.csg.imgsel.bean.XiaoHua;
import com.jmm.csg.parser.BaseConverterFactory;
import com.jmm.csg.utils.RxUtils;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;

public class HttpModule {

    public static final String BASE_URL = AppConfig.ROUTE_CONFIG.getBaseHttpUrl() + "csh/";

    private static ApiService newHttp() {

        LoggingInterceptor loggingInterceptor = new LoggingInterceptor.Builder()
                .loggable(true)
                .setLevel(AppConfig.ROUTE_CONFIG.isPrintLog() ? Level.BASIC : Level.NONE)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new SignatureInterceptor())
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(30 * 1000, TimeUnit.MILLISECONDS)
//                .sslSocketFactory(createSSLSocketFactory())
//                .hostnameVerifier((s, sslSession) -> true)
                .build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(BaseConverterFactory.create())
                .build().create(ApiService.class);
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }


    public static Observable<UserInfo> login(Map<String, String> params) {
        return newHttp().login(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<BaseResp> register(Map<String, String> params) {
        return newHttp().register(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<BaseResp> getVerificationCode(Map<String, String> params) {
        return newHttp().getVerificationCode(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<BaseResp> forgetpwd(Map<String, String> params) {
        return newHttp().forgetpwd(params).compose(RxUtils.rxSchedulerHelper());
    }

    /**
     * 总行的所有信息
     */
    public static Observable<Bank> getBanks(Map<String, String> params) {
        return newHttp().getBanks(params).compose(RxUtils.rxSchedulerHelper());
    }

    /**
     * 根据 总行id 和 所属区域 查找所有分行
     */
    public static Observable<List<Bank.BranchBank>> getBranchBanks(Map<String, String> params) {
        return newHttp().getBranchBanks(params).compose(RxUtils.rxSchedulerHelper());
    }

    /**
     * 客户经理实名认证
     */
    public static Observable<RealNameBean> realNameRequest(Map<String, String> params) {
        return newHttp().realNameRequest(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<ManagerCenter> managerCenter(Map<String, String> params) {
        return newHttp().managerCenter(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<Order> orderList(Map<String, String> params) {
        return newHttp().orderList(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<Object> setDefaultAddress(Map<String, String> params) {
        return newHttp().setDefaultAddress(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<BaseResp> changeOrderState(Map<String, String> params) {
        return newHttp().changeOrderState(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<Object> getProductCountByorderId(Map<String, String> params) {
        return newHttp().getProductCountByorderId(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<List<Product>> missionList(Map<String, String> params) {
        return newHttp().missionList(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<Order.Entity> validateOrderId(Map<String, String> params) {
        return newHttp().validateOrderId(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<ProductDetail> proDetail(Map<String, String> params) {
        return newHttp().proDetail(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<SubmitOrderBean> submitOrder(Map<String, String> params) {
        return newHttp().submitOrder(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<OrderDetail> getOrderDetail(Map<String, String> params) {
        return newHttp().getOrderDetail(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<AddressInfo> getAddressList(Map<String, String> params) {
        return newHttp().getAddressList(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<BaseResp> saveNewAddress(Map<String, String> params) {
        return newHttp().savaNewAddress(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<BaseResp> deleteAddress(Map<String, String> params) {
        return newHttp().deleteAddress(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<AddressInfo.ZtAddressEntity> getSelfMentionAddress(Map<String, String> params) {
        return newHttp().getSelfMentionAddress(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<BaseResp> insertREorder(Map<String, String> params) {
        return newHttp().insertREorder(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<BaseResp> getRECountByOrderId(Map<String, String> params) {
        return newHttp().getRECountByorderId(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<LogisticsBean> logisticLis(Map<String, String> params) {
        return newHttp().logisticList(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<BaseResp> createComment(Map<String, String> params) {
        return newHttp().createComment(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<Order> reOrderList(Map<String, String> params) {
        return newHttp().reOrderList(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<BaseResp> payOrder(Map<String, String> params) {
        return newHttp().payOrder(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<AppUpdate> checkAppUpdate(Map<String, String> params) {
        return newHttp().checkAppUpdate(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<UnionPayBean> appPay(Map<String, String> params) {
        return newHttp().appPay(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<WeChatBean> payWx(Map<String, String> params) {
        return newHttp().payWx(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<BaseResp> modifyMgrPhone(Map<String, String> params) {
        return newHttp().modifyMgrPhone(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<BaseResp> checkLoginId(Map<String, String> parmas) {
        return newHttp().checkLoginId(parmas).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<XiaoHua> xiaohua(Map<String, String> params) {
        return newHttp().xiaohua(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<ReturnAddress> getReturnAddress(Map<String, String> params) {
        return newHttp().getReturnAddress(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<PayResult> orderQuery(Map<String, String> params) {
        return newHttp().orderQuery(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<BaseResp> verfyReturn(Map<String, String> params) {
        return newHttp().verfyReturn(params).compose(RxUtils.rxSchedulerHelper());
    }


    public static Observable<BaseResp> commitReturnDetail(Map<String, String> params) {
        return newHttp().commitReturnDetail(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<List<Product>> productList(Map<String, String> params) {
        return newHttp().productList(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<ROrderDetail> getReturnGoods(Map<String, String> params) {
        return newHttp().getReturnGoods(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<Integral> getAvailable(Map<String, String> params) {
        return newHttp().getAvailable(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<IntegralProduct> getProductList(Map<String, String> params) {
        return newHttp().getProductList(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<Integral> huaFeiExchange(Map<String, String> params) {
        return newHttp().huaFeiExchange(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<Integral> exchangeOilCard(Map<String, String> params) {
        return newHttp().exchangeOilCard(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<Integral> liuLiangIntegalExchange(Map<String, String> params) {
        return newHttp().liuLiangIntegalExchange(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<IntegralRecord> getExchangeRecord(Map<String, String> params) {
        return newHttp().getExchangeRecord(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<IntegralDetails> getIntegralDetail(Map<String, String> params) {
        return newHttp().getIntegralDetail(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<Object> shareSuccess(Map<String, String> params) {
        return newHttp().shareSuccess(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<ProductDetail> getProDetailForApp(Map<String, String> params) {
        return newHttp().getProDetailForApp(params).compose(RxUtils.rxSchedulerHelper());
    }

    public static Observable<ProductReview> getReviewForPublic(Map<String, String> params) {
        return newHttp().getReviewForPublic(params).compose(RxUtils.rxSchedulerHelper());
    }

    // 申请退款
    public static Observable<BaseResp> applyRefun(Map<String, String> params) {
        return newHttp().applyRefun(params).compose(RxUtils.rxSchedulerHelper());
    }

    // 取消退款
    public static Observable<BaseResp> cancleRefun(Map<String, String> params) {
        return newHttp().cancleRefun(params).compose(RxUtils.rxSchedulerHelper());
    }

    // 查看退款详情
    public static Observable<RefundDetail> queryRefun(Map<String, String> params) {
        return newHttp().queryRefun(params).compose(RxUtils.rxSchedulerHelper());
    }

}
