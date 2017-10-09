//package com.jmm.csg.pro.product.fragment;
//
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.jmm.csg.R;
//import com.jmm.csg.base.fragment.BaseFragment;
//import com.jmm.csg.bean.LoadStatus;
//import com.jmm.csg.bean.ProductReview;
//import com.jmm.csg.helper.UserDataHelper;
//import com.jmm.csg.http.BaseSubscriber;
//import com.jmm.csg.http.HttpModule;
//import com.jmm.csg.http.HttpParams;
//import com.jmm.csg.pro.product.adapter.EvaluateAdapter;
//import com.jmm.csg.widget.LoadingAndRetryManager;
//import com.jmm.csg.widget.SpaceItemDecoration;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import butterknife.Bind;
//import in.srain.cube.views.ptr.PtrFrameLayout;
//
//public class EvaluateFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, BaseQuickAdapter.RequestLoadMoreListener {
//
//    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
//    @Bind(R.id.ptrFrameLayout) PtrFrameLayout ptrFrameLayout;
//    @Bind(R.id.rb_all_evaluate) RadioButton mTvAllEvaluate;
//    @Bind(R.id.rb_p_evaluate) RadioButton mTvPEvaluate;
//    @Bind(R.id.rb_b_evaluate) RadioButton mTvBEvaluate;
//    @Bind(R.id.radioGroup) RadioGroup radioGroup;
//    private EvaluateAdapter adapter;
//    private String productId;
//    private String evaluateStatus = null;
//    private LoadingAndRetryManager layoutManager;
//    private List<ProductReview.DataBean.ReviewBean> reviewData;
//    private List<ProductReview.DataBean.ReviewBean> tempList = new ArrayList<>();
//    private int currentPage = 1;
//    private String type;
//
//    @Override
//    protected void parseIntent(Bundle bundle) {
//        productId = bundle.getString("productId");
//        type = bundle.getString("type");
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_evaluate;
//    }
//
//
//    @Override
//    protected void initView() {
//        layoutManager = getLoadingLayoutManager(ptrFrameLayout);
//        bindRefreshView(ptrFrameLayout);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerView.addItemDecoration(new SpaceItemDecoration(5));
//        adapter = new EvaluateAdapter(Collections.emptyList());
//        adapter.setOnLoadMoreListener(this);
//        mRecyclerView.setAdapter(adapter);
//        radioGroup.setOnCheckedChangeListener(this);
//        mTvAllEvaluate.setChecked(true);
//    }
//
//    @Override
//    protected void requestData(LoadStatus status) {
//        String userId = UserDataHelper.getUserId();
//        currentPage = status == LoadStatus.MORE ? ++currentPage : 1;
//        HttpModule.getReviewForPublic(HttpParams.getReviewForPublic(userId, productId, type, String.valueOf(currentPage), "10"))
//                .subscribe(new BaseSubscriber<ProductReview>() {
//                    @Override
//                    public void onStart() {
//                        if (status == LoadStatus.LOADING) {
//                            layoutManager.showLoading();
//                        }
//                    }
//
//                    @Override
//                    public void onNext(ProductReview productReview) {
//                        reviewData = productReview.getData().getReview();
//                        if (status == LoadStatus.LOADING) {
//                            if (reviewData.size() == 0) {
//                                layoutManager.showEmpty();
//                                return;
//                            } else {
//                                layoutManager.showContent();
//                            }
//                        }
//                        ProductReview.DataBean.ReviewCountBean reviewCount = productReview.getData().getReviewCount();
//                        mTvAllEvaluate.setText("全部评价: " + reviewCount.getReviewCount());
//                        mTvPEvaluate.setText("好评: " + reviewCount.getP());
//                        mTvBEvaluate.setText("差评: " + reviewCount.getB());
//                        adapter.setNewData(getReviewData(evaluateStatus));
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                        if (status == LoadStatus.REFRESH) {
//                            ptrFrameLayout.refreshComplete();
//                        }
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        if (status == LoadStatus.REFRESH) {
//                            ptrFrameLayout.refreshComplete();
//                        }
//                    }
//                });
//    }
//
//
//    private List<ProductReview.DataBean.ReviewBean> getReviewData(String status) {
//        tempList.clear();
//        if (TextUtils.isEmpty(status)) {
//            return reviewData;
//        }
//        for (ProductReview.DataBean.ReviewBean review : reviewData) {
//            if (status.equals(review.getR_status())) {
//                tempList.add(review);
//            }
//        }
//        return tempList;
//    }
//
//
//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        if (reviewData == null) {
//            return;
//        }
//        List<ProductReview.DataBean.ReviewBean> list = new ArrayList<>();
//        switch (checkedId) {
//            case R.id.rb_all_evaluate:
//                list = getReviewData(null);
//                evaluateStatus = null;
//                break;
//            case R.id.rb_p_evaluate:
//                list = getReviewData("P");
//                evaluateStatus = "P";
//                break;
//            case R.id.rb_b_evaluate:
//                list = getReviewData("B");
//                evaluateStatus = "B";
//                break;
//        }
//        adapter.setNewData(list);
//    }
//
//    @Override
//    public void onLoadMoreRequested() {
//        requestData(LoadStatus.MORE);
//    }
//}
