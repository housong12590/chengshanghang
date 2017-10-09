package com.jmm.csg.pro.product.adapter;

import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jmm.csg.R;
import com.jmm.csg.base.adapter.BaseRVAdapter;
import com.jmm.csg.bean.Product;
import com.jmm.csg.imgload.ImageLoaderUtils;
import com.jmm.csg.utils.OSSUtils;

import java.io.File;
import java.util.List;

public class MissionListAdapter extends BaseRVAdapter<Product> {

    public MissionListAdapter() {
        super(R.layout.item_mission_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, Product item) {
        helper.addOnClickListener(R.id.iv_encode);
//        Double price = Double.parseDouble(item.getProPrice()) / 100;
        helper.setText(R.id.tv_price, "¥" + item.getProPrice());
        helper.setText(R.id.tv_pro_name, item.getProName());
        helper.setText(R.id.tv_progress, item.getFinishNum() + "/" + item.getMissionNum());
        helper.setText(R.id.tv_pro_status, item.getStatus().equals("0") ? "现货" : "预售");
        ProgressBar progressBar = helper.getView(R.id.progressBar);
        progressBar.setMax(Integer.parseInt(item.getMissionNum()));
        progressBar.setProgress(Integer.parseInt(item.getFinishNum()));
        ImageView ivProPic = helper.getView(R.id.iv_pro_pic);
//                ImageLoaderUtils.getInstance().loadOSSImage(mContext, item.getProPic(), ivProPic);
        Glide.with(mContext).load(OSSUtils.BASE_URL + item.getProPic()).downloadOnly(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                ImageLoaderUtils.getInstance().loadImage(mContext, resource.getAbsolutePath(), ivProPic);
                item.setProPicFilePath(resource.getAbsolutePath());
            }
        });
    }

    @Override
    public void setNewData(List<Product> data) {
        super.setNewData(data);
    }
}
