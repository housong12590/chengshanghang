package com.jmm.csg.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jmm.csg.R;
import com.jmm.csg.bean.LoadStatus;
import com.jmm.csg.rxbus.RxBus;
import com.jmm.csg.widget.LoadingAndRetryManager;
import com.jmm.csg.widget.OnLoadingAndRetryListener;

import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public abstract class BaseFragment extends Fragment {

    private boolean isCreated ;
    private boolean isVisibleToUser;
    private boolean hasLoaded ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            parseIntent(getArguments());
        }
        View view = View.inflate(getActivity(), getLayoutId(), null);
        ButterKnife.bind(this, view);
        RxBus.getDefault().register(this);
        onCreate();
        initView();
        initData();
//        requestData(LoadStatus.LOADING);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isCreated = true;
        delayLoad();
    }

    public void onCreate(){

    }

    protected void parseIntent(Bundle bundle) {

    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected void initData() {

    }

    protected void requestData(LoadStatus status) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
        delayLoad();
    }

    private void delayLoad() {
        if (isVisibleToUser && isCreated && !hasLoaded) {
            requestData(LoadStatus.LOADING);
            hasLoaded = true;
        }
    }

    protected void bindRefreshView(PtrFrameLayout val) {
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getActivity());
        val.setHeaderView(header);
        val.addPtrUIHandler(header);
        val.setPtrHandler(getRefreshListener());
    }

    private PtrHandler getRefreshListener() {
        return new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestData(LoadStatus.REFRESH);
            }
        };
    }

    protected LoadingAndRetryManager getLoadingLayoutManager(Object val) {
        return new LoadingAndRetryManager(val, listener);
    }


    private OnLoadingAndRetryListener listener = new OnLoadingAndRetryListener() {
        @Override
        public void setRetryEvent(View retryView) {
            retryView.findViewById(R.id.id_btn_retry).setOnClickListener(v -> requestData(LoadStatus.LOADING));
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hasLoaded = false;
        isCreated = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        RxBus.getDefault().unRegister(this);
    }
}
