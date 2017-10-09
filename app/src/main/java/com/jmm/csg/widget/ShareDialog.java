package com.jmm.csg.widget;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jmm.csg.R;
import com.jmm.csg.bean.ShareModel;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.BaseSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.utils.ToastUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qq.QQClientNotExistException;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * author：hs
 * date: 2017/5/12 0012 14:47
 */
public class ShareDialog extends Dialog implements View.OnClickListener, PlatformActionListener {

    private int width;
    private ShareModel shareModel;

    public ShareDialog(Context context, ShareModel model) {
        super(context, R.style.comment_dialog_animation);
        this.shareModel = model;
        ShareSDK.initSDK(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_share);
        findViewById(R.id.qq).setOnClickListener(this);
        findViewById(R.id.qzone).setOnClickListener(this);
        findViewById(R.id.friendster).setOnClickListener(this);
        findViewById(R.id.microblog).setOnClickListener(this);
        findViewById(R.id.wechat).setOnClickListener(this);
        findViewById(R.id.copylink).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(v -> dismiss());

        setCanceledOnTouchOutside(true);

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = width;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.comment_dialog_animation);
    }

    @Override
    public void onClick(View v) {
        String shareName = null;
        switch (v.getId()) {
            case R.id.wechat:
                shareName = Wechat.NAME;
                break;
            case R.id.friendster:
                shareName = WechatMoments.NAME;
                break;
            case R.id.qq:
                shareName = QQ.NAME;
                break;
            case R.id.qzone:
                shareName = QZone.NAME;
                break;
            case R.id.microblog:
                shareName = SinaWeibo.NAME;
                break;
            case R.id.copylink://复制链接
                ClipboardManager cmb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(shareModel.getUrl());
                ToastUtils.showMsg("复制成功");
                dismiss();
                return;
        }
        share(shareName);
        dismiss();
    }

    private void share(String shareName) {
        Platform.ShareParams params = new Platform.ShareParams();
        params.setUrl(shareModel.getUrl());
        params.setTitle(shareModel.getTitle());
        params.setTitleUrl(shareModel.getUrl());
        params.setText(shareModel.getContent());
        params.setImageUrl(shareModel.getImageUrl());
        params.setShareType(Platform.SHARE_WEBPAGE);
        Platform platform = ShareSDK.getPlatform(getContext(), shareName);
        platform.setPlatformActionListener(this);
        platform.share(params);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        System.out.println("分享成功");
        HttpModule.shareSuccess(HttpParams.shareSuccess(UserDataHelper.getUserId(), shareModel.getCoding()))
                .subscribe(new BaseSubscriber<Object>() {
                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) { //此处回调是在子线程中
        System.out.println(platform.getName());
        Observable.just("").observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    System.out.println(Thread.currentThread().getName());
                    if (throwable instanceof WechatClientNotExistException) {
                        ToastUtils.showMsg("没有安装微信客户端");
                    } else if (throwable instanceof QQClientNotExistException) {
                        ToastUtils.showMsg("没有安装QQ客户端");
                    }
                });
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        System.out.println("分享取消");
    }
}
