package com.jmm.csg.pro.product.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jmm.csg.R;
import com.jmm.csg.base.adapter.BaseRVAdapter;
import com.jmm.csg.base.fragment.BaseRVFragment;
import com.jmm.csg.bean.ProductReview;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.pro.product.adapter.EvaluateAdapter2;
import com.jmm.csg.widget.SpaceItemDecoration;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class EvaluateFragment2 extends BaseRVFragment<ProductReview.DataBean.ReviewBean> {


    private String productId;
    private String type;

    @Override
    protected void parseIntent(Bundle bundle) {
        super.parseIntent(bundle);
        productId = bundle.getString("productId");
        type = bundle.getString("type");
    }

    @Override
    protected void initView() {
        super.initView();
        mAdapter.setEmptyView(R.layout.loader_base_empty);
        setBackgroundColor(getResources().getColor(R.color.layout_bg));
    }

    @Override
    protected String getCurrentPage() {
        return null;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new SpaceItemDecoration(6);
    }

    @Override
    protected Observable<List<ProductReview.DataBean.ReviewBean>> getApi(String currNum) {
        String userId = UserDataHelper.getUserId();
        return HttpModule.getReviewForPublic(HttpParams.getReviewForPublic(userId, productId, type,
                currNum, String.valueOf(PAGE_SIZE)))
                .doOnNext(new Action1<ProductReview>() {
                    @Override
                    public void call(ProductReview productReview) {
                        ProductReview.DataBean.ReviewCountBean reviewCount = productReview.getData().getReviewCount();
                        EvaluateCategoryFragment fragment = (EvaluateCategoryFragment) getParentFragment();
                        fragment.refreshEvaluateCount(reviewCount, type);
                    }
                })
                .flatMap(new Func1<ProductReview, Observable<List<ProductReview.DataBean.ReviewBean>>>() {
                    @Override
                    public Observable<List<ProductReview.DataBean.ReviewBean>> call(ProductReview productReview) {
                        return Observable.just(productReview.getData().getReview());
                    }
                });
    }


    @Override
    protected BaseRVAdapter<ProductReview.DataBean.ReviewBean> getRecyclerViewAdapter() {
        return new EvaluateAdapter2();
    }

    @Override
    protected void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
