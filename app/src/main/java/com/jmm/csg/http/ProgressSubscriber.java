package com.jmm.csg.http;

import android.content.Context;

import com.jmm.csg.http.dialog.ProgressCancelListener;
import com.jmm.csg.http.dialog.ProgressDialogHandler;
import com.jmm.csg.utils.ThreadUtils;

public abstract class ProgressSubscriber<T> extends BaseSubscriber<T> implements ProgressCancelListener {


    private final ProgressDialogHandler dialog;

    public ProgressSubscriber(Context context) {
        dialog = new ProgressDialogHandler(context, this, true);
    }

    private void showProgressDialog() {
        dialog.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
    }

    private void dismissProgressDialog() {
        dialog.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
    }

    @Override
    public void onStart() {
        if(ThreadUtils.isMainThread()){
            showProgressDialog();
        }
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        e.printStackTrace();
        dismissProgressDialog();
    }

    @Override
    public void onCancelProgress() {
        if (!isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
