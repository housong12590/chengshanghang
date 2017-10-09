package com.jmm.csg.callback;

public interface OSSCallback {

    void onProgress(long currentSize,long totalSize);

    void onSuccess(String id);

    void onFailure();
}
