package com.jmm.csg.pro.manager.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jmm.csg.R;
import com.jmm.csg.base.adapter.BaseRVAdapter;
import com.jmm.csg.base.fragment.BaseRVFragment;
import com.jmm.csg.bean.IntegralRecord;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.pro.manager.adapter.IntegralRecordAdapter;
import com.jmm.csg.utils.RxUtils;
import com.jmm.csg.widget.SpaceItemDecoration;

import java.util.List;

import rx.Observable;


public class ExchangeRecordFragment extends BaseRVFragment<IntegralRecord.Data.Entity> {

    private String type;
    private String pageCount;

    @Override
    protected void parseIntent(Bundle bundle) {
        type = bundle.getString("type");
    }

    @Override
    protected void initView() {
        super.initView();
        View emptyView = View.inflate(getActivity(), R.layout.integeral_empty_layout, null);
        mAdapter.setEmptyView(emptyView);
    }

    @Override
    protected String getCurrentPage() {
        return pageCount;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new SpaceItemDecoration(1);
    }

    @Override
    protected Observable<List<IntegralRecord.Data.Entity>> getApi(String currNum) {
        return HttpModule.getExchangeRecord(HttpParams.getExchangeRecord(UserDataHelper.getUserId(),
                type, currNum, String.valueOf(PAGE_SIZE)))
                .compose(RxUtils.rxSchedulerHelper())
                .doOnNext(integralRecord -> pageCount = integralRecord.getData().getPager().getTotalPage())
                .map(integralRecord -> integralRecord.getData().getList());
    }

    @Override
    protected BaseRVAdapter<IntegralRecord.Data.Entity> getRecyclerViewAdapter() {
        return new IntegralRecordAdapter();
    }

    @Override
    protected void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
