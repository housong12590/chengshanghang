package com.jmm.csg.pro.manager.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jmm.csg.R;
import com.jmm.csg.base.adapter.BaseRVAdapter;
import com.jmm.csg.base.fragment.BaseRVFragment;
import com.jmm.csg.bean.IntegralDetails;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.pro.manager.adapter.IntegralDetailsAdapter;
import com.jmm.csg.utils.RxUtils;

import java.util.List;

import rx.Observable;


public class IntegralDetailsFragment extends BaseRVFragment<IntegralDetails.Data.Entity> {

    private String pageCount;

    @Override
    protected String getCurrentPage() {
        return pageCount;
    }

    @Override
    protected void initView() {
        super.initView();
        setUserVisibleHint(true);
        View emptyView = View.inflate(getActivity(), R.layout.integeral_empty_layout, null);
        TextView tvText = (TextView) emptyView.findViewById(R.id.tv_text);
        tvText.setText("您还没有任何积分记录");
        mAdapter.setEmptyView(emptyView);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Override
    protected Observable<List<IntegralDetails.Data.Entity>> getApi(String currNum) {
        return HttpModule.getIntegralDetail(HttpParams.getIntegralDetail(UserDataHelper.getUserId()
                , currNum, String.valueOf(PAGE_SIZE)))
                .compose(RxUtils.rxSchedulerHelper())
                .doOnNext(integralDetails -> pageCount = integralDetails.getData().getPager().getTotalPage())
                .map(integralDetails -> integralDetails.getData().getList());
    }

    @Override
    protected BaseRVAdapter<IntegralDetails.Data.Entity> getRecyclerViewAdapter() {
        return new IntegralDetailsAdapter();
    }

    @Override
    protected void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
