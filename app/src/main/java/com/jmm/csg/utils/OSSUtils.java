package com.jmm.csg.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.jmm.csg.BaseApplication;
import com.jmm.csg.bean.UploadBean;
import com.jmm.csg.callback.OSSCallback;
import com.jmm.csg.config.AppConfig;
import com.jmm.csg.config.OSSConfig;

import java.io.File;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class OSSUtils {

    private static OSSConfig config = AppConfig.ROUTE_CONFIG.getOSSConfig();
    private static final int IMAGE_SIZE = 200 * 1024;//图片压缩指定大小
    private static String endpoint = config.getEndpoint();
    private static String bucketName = config.getBucketName();

    private static String ACCOUNT_PATH = "accountManager/";
    public static String BASE_URL = "http://" + bucketName + "." + endpoint + "/";
//    https://csh-pic-001.oss-cn-shanghai.aliyuncs.com/accountManager/
//    http://csh-test-001.oss-cn-shanghai.aliyuncs.com/


    private static OSSClient configOSS(Context context) {
        String accessKeyId = config.getAccessKeyId();
        String accessKeySecret = config.getAccessKeySecret();

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000);
        conf.setSocketTimeout(15 * 1000);
        conf.setMaxConcurrentRequest(5);
        conf.setMaxErrorRetry(2);
        return new OSSClient(context, endpoint, new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret), conf);
    }

    public static OSSAsyncTask<PutObjectResult> upLoadFile(String objKey, String filePath, OSSCallback callback) {
        byte[] bytes = ImageUtils.compressImage(ImageUtils.getSmallBitmap(BaseApplication.getContext(), filePath), IMAGE_SIZE);
        PutObjectRequest put = new PutObjectRequest(bucketName, objKey, bytes);
        put.setProgressCallback((putObjectRequest, currentSize, totalSize) -> callback.onProgress(currentSize, totalSize));
        return configOSS(BaseApplication.getContext()).asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                Log.e("OSS", "图片上传完成--->" + put.getObjectKey());
                callback.onSuccess(putObjectResult.getETag());
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                e1.printStackTrace();
                e.printStackTrace();
                callback.onFailure();
            }
        });
    }


    public static Observable<String> uploadImages(List<UploadBean> list) {
        return Observable.from(list)
                .filter(uploadBean -> new File(uploadBean.path).exists())
                .flatMap(uploadBean -> Observable.create(new Observable.OnSubscribe<UploadBean>() {
                    @Override
                    public void call(Subscriber<? super UploadBean> subscriber) {
                        Bitmap bitmap = ImageUtils.getSmallBitmap(BaseApplication.getContext(), uploadBean.path);
                        uploadBean.bytes = ImageUtils.compressImage(bitmap, IMAGE_SIZE);
                        subscriber.onNext(uploadBean);
                        subscriber.onCompleted();
                    }
                }))
                .flatMap(uploadBean -> Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        PutObjectRequest put = new PutObjectRequest(bucketName, uploadBean.name, uploadBean.bytes);
                        configOSS(BaseApplication.getContext()).asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                            @Override
                            public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                                Log.e("ossUpload", "图片上传成功--->" + uploadBean.name);
                                subscriber.onNext(uploadBean.name);
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                                subscriber.onError(e);
                            }
                        });
                    }
                })).compose(RxUtils.rxSchedulerHelper());

    }


    public static String getCardPhotoFileName(String cardNum, boolean isFront) {
        return ACCOUNT_PATH + cardNum + "_" + (isFront ? "front" : "back") + ".jpg";
    }

    public static String getAvatarLoadFileName(String cardNum) {
        return ACCOUNT_PATH + cardNum + ".jpg";
    }

    public synchronized static String getCustomerService(String path) {
        String CUSTOMER_SERVICE = "customerService/";
        return CUSTOMER_SERVICE + StringUtils.md5Encode(path + String.valueOf(System.currentTimeMillis())) + ".jpg";
    }
}
