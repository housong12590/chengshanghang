package com.jmm.csg.http;

import android.util.Log;

import com.google.gson.stream.MalformedJsonException;
import com.jmm.csg.utils.ToastUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ConnectException || e instanceof UnknownHostException) {
            ToastUtils.showMsg("请检查当前的网络环境！");
        } else if (e instanceof SocketTimeoutException) {
            ToastUtils.showMsg("网络超时");
        } else if (e instanceof HttpException) {
            ToastUtils.showMsg("服务器繁忙,请稍候再试!");
        } else if (e instanceof HttpApiException) {
            ToastUtils.showMsg("未知错误");
        }
        if (e instanceof MalformedJsonException) {
            Log.e("MalformedJsonException", "--------------->>>> JSON解析异常");
        }
        e.printStackTrace();
    }

}
