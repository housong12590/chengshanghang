package com.jmm.csg.pro.product.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jmm.csg.R;
import com.jmm.csg.imgload.ImageLoaderUtils;

/**
 * author：hs
 * date: 2017/6/23 0023 16:22
 */

public class ImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ImageAdapter() {
        super(R.layout.item_image1_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView ivImage = helper.getView(R.id.iv_image);
        ImageLoaderUtils.getInstance().loadOSSImage(mContext, item, ivImage);
    }
}
