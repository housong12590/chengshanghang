package com.jmm.csg.callback;

public interface OnResultListener<T> {

    void onResult(int position, T val);
}
