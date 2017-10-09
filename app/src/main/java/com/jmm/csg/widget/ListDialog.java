package com.jmm.csg.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jmm.csg.R;
import com.jmm.csg.bean.Bank;
import com.jmm.csg.callback.OnResultListener;

import java.util.Collections;
import java.util.List;

public class ListDialog<T> extends BaseDialog {

    private RecyclerView recyclerView;
    private TextView tvTitle;
    private ProgressBar progressBar;
    private TextView tvError;
    private ListAdapter adapter;
    private TextView tvEmpty;

    public ListDialog(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_list_layout;
    }

    @Override
    protected void initView() {
        findViewById(R.id.iv_back).setOnClickListener(v -> dismiss());
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvError = (TextView) findViewById(R.id.tv_error);
        tvEmpty = (TextView) findViewById(R.id.tv_empty);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        adapter = new ListAdapter(Collections.emptyList());
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
    }

    public void showLoading() {
        tvError.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
    }

    public void showError() {
        tvError.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
    }

    public void showContent(List<T> list) {
        if(list.size() == 0){
            showEmpty();
            return;
        }
        adapter.setNewData(list);
        recyclerView.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
    }

    public void showEmpty() {
        recyclerView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    public ListDialog setTitle(String text) {
        tvTitle.setText(text);
        return this;
    }

    private OnResultListener<T> resultListener;

    public void setOnResultListener(OnResultListener resultListener) {
        this.resultListener = resultListener;
    }

    private class OnRecyclerItemClickListener extends OnItemClickListener {

        @Override
        public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            if (resultListener != null) {
                resultListener.onResult(position, (T) adapter.getData().get(position));
            }
            dismiss();
        }
    }


    public class ListAdapter extends BaseQuickAdapter<T, BaseViewHolder> {

        public ListAdapter(List<T> data) {
            super(R.layout.item_text_layout, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, T item) {
            if (item instanceof Bank.BankEntity) {
                helper.setText(R.id.text, ((Bank.BankEntity) item).getBank_name());
            } else if (item instanceof Bank.BranchBank) {
                helper.setText(R.id.text, ((Bank.BranchBank) item).getBranch_name());
            }
        }
    }
}
