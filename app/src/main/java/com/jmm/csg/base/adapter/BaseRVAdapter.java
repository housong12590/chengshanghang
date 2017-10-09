package com.jmm.csg.base.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public abstract class BaseRVAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public BaseRVAdapter(int layoutResId) {
        super(layoutResId);
    }

    public BaseRVAdapter(List<T> data) {
        super(data);
    }

}
