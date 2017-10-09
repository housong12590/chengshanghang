package com.jmm.csg.pro.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.bean.AddressInfo;
import com.jmm.csg.bean.BaseResp;
import com.jmm.csg.helper.DialogHelper;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.BaseSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.http.ProgressSubscriber;
import com.jmm.csg.pro.order.adapter.AddressAdapter;
import com.jmm.csg.utils.ToastUtils;
import com.jmm.csg.widget.SpaceItemDecoration;
import com.jmm.csg.widget.TitleView;
import com.jmm.csg.widget.VisibilityLayout;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscription;

public class SelectAddressActivity extends BaseActivity {

    @Bind(R.id.titleView) TitleView mTitleView;
    @Bind(R.id.content) LinearLayout mContentView;
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    private boolean isChanged;
    private AddressInfo.Entity addressInfo;
    private AddressAdapter mAdapter;
    private static final int NEW_ADDRESS_CODE = 101;
    private boolean isSelect;
    private VisibilityLayout visibilityLayout;
    private Subscription subscribe;
    private boolean isLoad, needRefresh;
    private String addressId;

    @Override
    public void parseIntent(Intent intent) {
        isSelect = intent.getBooleanExtra("isSelect", false);
        addressId = intent.getStringExtra("addressId");
    }

    public static void start(Object activityAndFragment, boolean isSelect, int requestCode, String addressId) {
        Intent intent = new Intent();
        intent.putExtra("isSelect", isSelect);
        intent.putExtra("addressId", addressId);
        if (activityAndFragment instanceof Activity) {
            Activity activity = (Activity) activityAndFragment;
            intent.setClass(activity, SelectAddressActivity.class);
            activity.startActivityForResult(intent, requestCode);
        } else if (activityAndFragment instanceof Fragment) {
            Fragment fragment = (Fragment) activityAndFragment;
            intent.setClass(fragment.getActivity(), SelectAddressActivity.class);
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_address;
    }

    @Override
    public void initView() {
        visibilityLayout = new VisibilityLayout(mContentView);
        mTitleView.setOnLeftImgClickListener(view -> finish());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(6));
        mRecyclerView.addOnItemTouchListener(itemListener);

        mAdapter = new AddressAdapter(Collections.emptyList());
        mRecyclerView.setAdapter(mAdapter);
        visibilityLayout.getErrorView().findViewById(R.id.id_btn_retry).setOnClickListener(v -> initData());
    }

    @Override
    public void initData() {
        subscribe = HttpModule.getAddressList(HttpParams.getAddressList(UserDataHelper.getUserId()))
                .subscribe(new BaseSubscriber<AddressInfo>() {
                    @Override
                    public void onNext(AddressInfo addressInfo) {
                        List<AddressInfo.Entity> address = addressInfo.getAddress();
                        AddressInfo.Entity tempEntity = null;
                        for (int i = 0; i < address.size(); i++) {//把默认地址提至第一个
                            AddressInfo.Entity entity = address.get(i);
                            if (entity.getIsPrimary().equals("1")) {
                                tempEntity = entity;
                                address.remove(entity);
                                break;
                            }
                        }
                        if (tempEntity != null) {
                            address.add(0, tempEntity);
                        } else if (address.size() != 0) {
                            address.get(0).setISPRIMARY("1");
                        }
                        mAdapter.setNewData(address);
                        isLoad = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        visibilityLayout.showErrorView();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        visibilityLayout.showContentView();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        if (!isLoad) {
                            visibilityLayout.showLoadingView();
                        }
                    }
                });
    }

    @OnClick({R.id.tv_new_address})
    public void onClick(View view) {
        if (mAdapter.getData().size() >= 20) {
            DialogHelper.getSingleButtonDialog(this, "提示", "添加地址已达上限20条，请删除或修改不需要的地址", "知道了", null).show();
        } else {
            Intent intent = new Intent(this, NewAddressActivity.class);
            intent.putExtra("isShowDef", mAdapter.getData().size() > 0);//是否已经有地址,如果已经有地址,则显示设置默认地址
            startActivityForResult(intent, NEW_ADDRESS_CODE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            initData();
            if (needRefresh) {
                addressInfo = (AddressInfo.Entity) data.getSerializableExtra("addressInfo");
            }
        }
    }

    private void deleteAddress(String addressId) {
        String userId = UserDataHelper.getUserId();
        HttpModule.deleteAddress(HttpParams.deleteAddress(addressId, userId))
                .subscribe(new ProgressSubscriber<BaseResp>(this) {
                    @Override
                    public void onNext(BaseResp resp) {
                        initData();
                    }
                });
    }

    private void setDefaultAddress(String addressId) {
        String userId = UserDataHelper.getUserId();
        HttpModule.setDefaultAddress(HttpParams.setDefaultAddress(addressId, userId))
                .subscribe(new ProgressSubscriber<Object>(this) {
                    @Override
                    public void onNext(Object o) {
                        List<AddressInfo.Entity> list = mAdapter.getData();
                        for (int i = 0; i < list.size(); i++) {
                            AddressInfo.Entity entity = list.get(i);
                            if (entity.getAddressId().equals(addressId)) {
                                entity.setISPRIMARY("1");
                            } else {
                                entity.setISPRIMARY("0");
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        ToastUtils.showMsg("设置默认地址成功");
                    }
                });
    }

    private SimpleClickListener itemListener = new SimpleClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            if (!isSelect) {
                return;
            }
            addressInfo = (AddressInfo.Entity) adapter.getData().get(position);
            finish();
        }

        @Override
        public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

        }

        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            AddressInfo.Entity item = (AddressInfo.Entity) adapter.getData().get(position);
            switch (view.getId()) {
                case R.id.rb_def_address:
                    RadioButton rb = (RadioButton) view;
                    if (rb.isSelected()) {
                        return;
                    }
                    setDefaultAddress(item.getAddressId());
                    break;
                case R.id.tv_edit:
                    Intent intent = new Intent(SelectAddressActivity.this, NewAddressActivity.class);
                    intent.putExtra("addressInfo", item);
                    intent.putExtra("isShowDef", mAdapter.getData().size() > 1);//是否是第一个地址
                    startActivityForResult(intent, NEW_ADDRESS_CODE);
                    needRefresh = item.getAddressId().equals(addressId);
                    break;
                case R.id.tv_delete:
                    DialogHelper.getMessageDialog(SelectAddressActivity.this, "您确定要删除该地址吗?", (dialog, which) -> {
                        deleteAddress(item.getAddressId());
                        isChanged = true;
                    }).show();
                    break;
            }
        }

        @Override
        public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

        }
    };


    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("addressInfo", addressInfo);
        intent.putExtra("isChanged", isChanged);
        setResult(RESULT_OK, intent);
        super.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }
}
