package com.jmm.csg.config;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;

import com.google.gson.Gson;
import com.jmm.csg.R;
import com.jmm.csg.bean.AppUpdate;
import com.jmm.csg.helper.AppDataHelper;
import com.jmm.csg.helper.DialogHelper;
import com.jmm.csg.http.BaseSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.service.ApkDownService;
import com.jmm.csg.utils.SDCardUtils;
import com.jmm.csg.utils.SystemUtils;
import com.jmm.csg.utils.ToastUtils;
import com.jmm.csg.widget.CommonDialog;
import com.jmm.csg.widget.VisibilityLayout;

import java.io.File;

import rx.Subscription;

public class AppConfig {

    public static final RouteConfig ROUTE_CONFIG = RouteConfig.TEST;

    private static final String BASE_URL = ROUTE_CONFIG.getBaseHttpUrl();

    public static final String BASE_WEB_URL = BASE_URL + "cshweb/accountManager/toProDetail?";

    public static final String BASE_SHARE_URL = BASE_URL + "cshweb/accountManager/toSharePic?";

    public static final String REGISTER_URL = BASE_URL + "cshweb/accountManager/accountmanagerregisterview";

    public static final String EXCHANGE_URL = BASE_URL + "cshweb/static/staticpage/exchange.html";

    public static final String APP_SHARE_URL = BASE_URL + "csh/appShare/downloadApp";

    private static Context mContext;

    public static Gson gson = new Gson();

    public static final String APP_ROOT_DIRECTORY = SDCardUtils.getSDCardPath()
            + File.separator
            + "51jmm" + File.separator + "uoogou";

    public static final String DEFAULT_IMAGE_PATH = APP_ROOT_DIRECTORY
            + File.separator
            + "image";

    public static final String DEFAULT_FILE_PATH = APP_ROOT_DIRECTORY
            + File.separator
            + "file";

    public static final String ENCODE_KEY = "com.jmm.csh";

    public static void init(Context context) {
        AppConfig.mContext = context;
        initAppDirectory();
        VisibilityLayout.EMPTY_LAYOUT_ID = R.layout.loader_base_empty;
        VisibilityLayout.LOAD_LAYOUT_ID = R.layout.loader_base_loading;
        VisibilityLayout.ERROR_LAYOUT_ID = R.layout.loader_base_retry;
    }


    private static void initAppDirectory() {

    }


    public static SharedPreferences getSharedPreferences(String prefName) {
        if (mContext == null) {
            throw new NullPointerException("appConfig mContext null...");
        }
        return mContext.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    public static Subscription appCheckUpdate(Context context, boolean flag) {
        return HttpModule.checkAppUpdate(HttpParams.appCheckUpdata())
                .subscribe(new BaseSubscriber<AppUpdate>() {
                    @Override
                    public void onNext(AppUpdate resp) {
                        AppUpdate.Entity entity = resp.getData();
                        String versionsCode = String.valueOf(SystemUtils.getAppVersionCode(context));
                        AppDataHelper.setAppVersionsCode(entity.getApkVersionsCode());
                        String appSkipVersion = AppDataHelper.getAppSkipVersion();
                        boolean isConstaintUpdate = entity.getApkIsConstaintUpdate().equals("1");
//                        if (!entity.getVersionsCode().equals(versionsCode)) { //判断当前版本是否为最新版本
                        try {
                            if (Integer.parseInt(entity.getApkVersionsCode()) > Integer.parseInt(versionsCode)) { //判断当前版本是否为最新版本
                                if (appSkipVersion.equals(entity.getApkVersionsCode()) && !flag) { //判断该版本是否为跳过版本,并且不是自动检测更新
                                    return;
                                }
                                String msg = entity.getApkContent();
//                            String apkUrl = "http://oa5jgcq4p.bkt.clouddn.com/apk/csh.apk";
                                CommonDialog.Builder dialog = DialogHelper.getDialog(context).setTitle("更新提示") // 弹出更新Dialog
                                        .setMessage(msg).setMessageGravity(Gravity.LEFT)
                                        .setCanceledOnTouchOutside(false)
                                        .setNegativeButtonText("更新", (dialog1, which) -> {
                                            ToastUtils.showMsg("正在下载...");
                                            ApkDownService.start(context, entity.getApkUrl()); // 开启服务去下载APK
                                        });
                                if (isConstaintUpdate) {// 是否强制更新
                                    dialog.setCancelable(false)//屏蔽系统返回键
                                            .setPositiveButtonText("取消", (dialog1, which) -> { // 不可以跳过,必须更新,否则结束应用
                                                ((Activity) context).finish();
                                            });
                                } else {
                                    dialog.setPositiveButtonText(flag ? "取消" : "跳过", (dialog1, which) -> {// 可以不更新,可跳过
                                        if (!flag) {
                                            AppDataHelper.setSkipVersion(entity.getApkVersionsCode()); // 记录跳过的版本号
                                        }
                                    });
                                }
                                dialog.show();
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
