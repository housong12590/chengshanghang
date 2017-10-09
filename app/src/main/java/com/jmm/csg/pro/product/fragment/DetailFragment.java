package com.jmm.csg.pro.product.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jmm.csg.R;
import com.jmm.csg.base.fragment.BaseFragment;
import com.jmm.csg.bean.LoadStatus;
import com.jmm.csg.bean.ProductDetail;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.BaseSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.utils.OSSUtils;
import com.jmm.csg.widget.VisibilityLayout;

import java.util.List;

import butterknife.Bind;

public class DetailFragment extends BaseFragment {

    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    private String productId;
    private VisibilityLayout visibilityLayout;

    @Override
    protected void parseIntent(Bundle bundle) {
        productId = bundle.getString("productId");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail;
    }

    @Override
    protected void requestData(LoadStatus status) {
        HttpModule.proDetail(HttpParams.proDetail(productId, UserDataHelper.getUserId()))
                .subscribe(new BaseSubscriber<ProductDetail>() {
                    @Override
                    public void onNext(ProductDetail detail) {
                        List<ProductDetail.Imgs> detImgs = detail.getDetImgs();
                        if (detImgs.size() == 0) {
                            visibilityLayout.showEmptyView();
                        } else {
                            adapter.setNewData(detImgs);
                            visibilityLayout.showContentView();
                        }
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        visibilityLayout.showLoadingView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        visibilityLayout.showErrorView();
                    }
                });
    }

    @Override
    protected void initView() {
        visibilityLayout = new VisibilityLayout(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private BaseQuickAdapter adapter = new BaseQuickAdapter<ProductDetail.Imgs, BaseViewHolder>(R.layout.item_detail_image) {
        @Override
        protected void convert(BaseViewHolder helper, ProductDetail.Imgs item) {
            ImageView ivImage = helper.getView(R.id.iv_image);
//            ImageLoaderUtils.getInstance().loadOSSImage(mContext, item.getPro_pic(), ivImage);
            Glide.with(DetailFragment.this).load(OSSUtils.BASE_URL + item.getPro_pic())
                    .placeholder(R.drawable.defaultpic)
                    .error(R.drawable.defaultpic).into(ivImage);
//            Glide.with(getActivity()).load(OSSUtils.BASE_URL + item.getPro_pic())
//                    .into(new SimpleTarget<GlideDrawable>() {
//                        @Override
//                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                            int height = resource.getIntrinsicHeight();
//                            int width = resource.getIntrinsicWidth();
//                            ViewGroup.LayoutParams params = ivImage.getLayoutParams();
//                            params.height = height;
////                            params.width = width;
//                            ivImage.setLayoutParams(params);
//                            ivImage.setImageDrawable(resource);
//                        }
//                    });
        }
    };

}
