package com.jmm.csg.pro.product.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jmm.csg.Constant;
import com.jmm.csg.R;
import com.jmm.csg.base.adapter.BaseRVAdapter;
import com.jmm.csg.base.fragment.BaseRVFragment;
import com.jmm.csg.bean.LoadStatus;
import com.jmm.csg.bean.Product;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.pro.manager.activity.MainActivity;
import com.jmm.csg.pro.order.activity.OrderListActivity;
import com.jmm.csg.pro.product.activity.ProductDetailActivity;
import com.jmm.csg.pro.product.activity.QRCodePreviewActivity;
import com.jmm.csg.pro.product.adapter.ProductListAdapter;
import com.jmm.csg.utils.SystemUtils;
import com.jmm.csg.widget.SpaceItemDecoration;

import java.util.List;

import rx.Observable;

public class AllProductListFragment extends BaseRVFragment<Product> {

//    private ImageView ivLogo;
//    private View headerView;

    @Override
    protected String getCurrentPage() {
        return null;
    }

    @Override
    protected Observable<List<Product>> getApi(String currNum) {
        return HttpModule.productList(HttpParams.productList(UserDataHelper.getUserId()))
                .doOnNext(products -> { // 把返回的第一条logo去掉
                    if (products.size() != 0) {
                        Product product = products.remove(0);
                        ProductsFragment parentFragment = (ProductsFragment) getParentFragment();
                        parentFragment.setBankLogo(product.getLogo());
//                        if (TextUtils.isEmpty(product.getLogo())) {//如果没有logo则把header移除
//                            mAdapter.removeHeaderView(headerView);
//                        } else {
//                            UserDataHelper.setBankLogo(product.getLogo());
//                            ImageLoaderUtils.getInstance().loadOSSImage(getActivity(), product.getLogo(), ivLogo);
//                        }
                    }
                });
    }

    @Override
    protected void initView() {
//        headerView = View.inflate(getActivity(), R.layout.item_bank_logo_layout, null);
//        ivLogo = (ImageView) headerView.findViewById(R.id.iv_logo);
        super.initView();
        mAdapter.setEmptyView(R.layout.loader_base_empty);
    }

    @Override
    protected BaseRVAdapter<Product> getRecyclerViewAdapter() {
        ProductListAdapter adapter = new ProductListAdapter();
//        adapter.addHeaderView(headerView);
        return adapter;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new SpaceItemDecoration(8);
    }

    @Override
    public boolean hasLoadMore() {
        return false;
    }

    @Override
    protected void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Product item = (Product) adapter.getData().get(position);
        if (!TextUtils.isEmpty(item.getLogo())) {
            return;
        }
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra("productId", item.getProId());
        intent.putExtra("proPic", item.getProPicFilePath());
        System.out.println(item.getProId());
        intent.putExtra("missionId", item.getMissionId());
        intent.putExtra("finishNum", item.getFinishNum());
        intent.putExtra("missionNum", item.getMissionNum());
        intent.putExtra("productName",item.getProName());
        intent.putExtra("productnum",item.getProductnum());
        intent.putExtra("flag", "product");
//        intent.putExtra("accountM",item.get)
        startActivityForResult(intent, 1001);
    }

    @Override
    protected void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_encode:
                Product product = (Product) adapter.getData().get(position);
                Intent intent = new Intent(getActivity(), QRCodePreviewActivity.class);
                intent.putExtra("productId", product.getProId());
                intent.putExtra("missionId", product.getMissionId());
                intent.putExtra("accountManagerId", UserDataHelper.getUserId());
                intent.putExtra("proPic", product.getProPicFilePath());
                intent.putExtra("coding", product.getCoding());
                SystemUtils.startTransitionActivity(getActivity(), intent,
                        view, QRCodePreviewActivity.TRANSIT_PIC);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            requestData(LoadStatus.REFRESH);
            int flag = data.getIntExtra("flag", 0);
            MainActivity activity = (MainActivity) getActivity();
            activity.setViewPagerCurrentItem(2);
            Intent intent = new Intent(getActivity(), OrderListActivity.class);
            int page = flag == Constant.PAY_SUCCESS ? 2 : 1;
            intent.putExtra("page", page);
            intent.putExtra("isPer", true);
            startActivity(intent);
        }
    }
}
