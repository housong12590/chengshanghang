package com.jmm.csg.pro.service;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jmm.csg.config.AppConfig;
import com.jmm.csg.R;
import com.jmm.csg.base.activity.XActivity;
import com.jmm.csg.bean.UploadBean;
import com.jmm.csg.helper.DialogHelper;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.imgload.ImageLoaderUtils;
import com.jmm.csg.imgsel.activity.ImagePreviewActivity;
import com.jmm.csg.imgsel.activity.ImageSelActivity;
import com.jmm.csg.imgsel.bean.ImageFolderBean;
import com.jmm.csg.pro.contract.AfterSaleContract;
import com.jmm.csg.pro.presenter.AfterSalePresenter;
import com.jmm.csg.pro.setting.WebViewActivity;
import com.jmm.csg.utils.FileUtils;
import com.jmm.csg.utils.OSSUtils;
import com.jmm.csg.utils.SystemUtils;
import com.jmm.csg.utils.ToastUtils;
import com.jmm.csg.widget.ActionSheet;
import com.jmm.csg.widget.ContainsEmojiEditText;
import com.jmm.csg.widget.CustomLayout;
import com.jmm.csg.widget.TitleView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 退换货
 */
public class AfterSaleActivity extends XActivity<AfterSalePresenter> implements AfterSaleContract.V {

    private static final int REQUEST_IMAGE_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;
    @Bind(R.id.titleView) TitleView mTitleView;
    @Bind(R.id.tv_commit) TextView mTvSubmit;
    @Bind(R.id.iv_reduce) ImageView mTvReduce;
    @Bind(R.id.tv_count) TextView mTvCount;
    @Bind(R.id.iv_add) ImageView mTvAdd;
    @Bind(R.id.customLayout) CustomLayout mCustomLayout;
    @Bind(R.id.radioGroup) RadioGroup mRadioGroup;
    @Bind(R.id.rb_quality) RadioButton mRbQuality;
    @Bind(R.id.et_remark)
    ContainsEmojiEditText mEtRemark;
    @Bind(R.id.tv_desc) TextView mTvDesc;
    private String imagePath;
    private String orderId;
    private String type;
    private String address;
    private int quantity;

    @Override
    public void parseIntent(Intent intent) {
        orderId = intent.getStringExtra("orderId");
        address = intent.getStringExtra("address");
        type = intent.getStringExtra("type");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_after_sale;
    }

    @Override
    public AfterSalePresenter newPresenter() {
        return new AfterSalePresenter();
    }

    @Override
    public void initView() {
        mTitleView.setOnLeftImgClickListener(view -> finish());
        mTitleView.setTitle(type.equals("return") ? "退货" : "换货");
        mTvDesc.setText(type.equals("R") ? "退货数量" : "换货数量");
        mRbQuality.setChecked(true);
        mCustomLayout.setInitList(new CustomLayout.InitListener() {
            @Override
            public void addClick() {
                DialogHelper.getActionDialog(AfterSaleActivity.this,
                        getSupportFragmentManager(),
                        getActionListener(), "相机", "从相册选取").show();
            }

            @Override
            public void itemClick(View view, int position) {
                ImageFolderBean item = ((List<ImageFolderBean>) mCustomLayout.getData()).get(position);
                ImagePreviewActivity.start(AfterSaleActivity.this, view, item.path);

            }

            @Override
            public View getView(Object val, int position) {
                ImageFolderBean item = (ImageFolderBean) val;
                View view = View.inflate(AfterSaleActivity.this, R.layout.item_picture_layout, null);
                ImageView ivPicture = (ImageView) view.findViewById(R.id.iv_picture);
                ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
                System.out.println(item.path);
                ImageLoaderUtils.getInstance().loadImage(AfterSaleActivity.this, item.path, ivPicture);
                ivDelete.setOnClickListener(v -> mCustomLayout.removeItem(mCustomLayout.getPosition(view)));
                return view;
            }
        });
    }

    @Override
    public void initData() {
        getP().getRECountByOrderId(orderId);
    }

    @Override
    public void reQuantity(String quantity) {
//        this.quantity = Integer.parseInt(quantity);
        mTvCount.setText(quantity);
    }

    @Override
    public void onSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    //, R.id.iv_add, R.id.iv_reduce
    @OnClick({R.id.tv_commit, R.id.tv_cancel, R.id.tv_notice})
    public void onClick(View view) {
//        int count = Integer.parseInt(mTvCount.getText().toString());
        switch (view.getId()) {
            case R.id.tv_commit:
                commit();
                break;
//            case R.id.iv_add:
//                if (count >= quantity) {
//                    ToastUtils.showMsg("退换货数量不能大于购买数量");
//                    return;
//                }
//                count++;
//                break;
//            case R.id.iv_reduce:
//                if (count <= 1) {
//                    return;
//                }
//                count--;
//                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_notice://退货须知
                WebViewActivity.start(this, "退货须知", AppConfig.EXCHANGE_URL);
                break;
        }
//        mTvCount.setText(String.valueOf(count));
    }

    private void commit() {
        String userId = UserDataHelper.getUserId();
        RadioButton rb = (RadioButton) findViewById(mRadioGroup.getCheckedRadioButtonId());
        String reason = rb.getText().toString();
        String count = mTvCount.getText().toString();
        if (TextUtils.isEmpty(count)) {
            ToastUtils.showMsg("退货数量为空");
            return;
        }
        String remark = mEtRemark.getText().toString();
        if (TextUtils.isEmpty(remark)) {
            ToastUtils.showMsg("请填写问题描述");
            return;
        }
        List<ImageFolderBean> data = mCustomLayout.getData();
        if (data.size() == 0) {
            ToastUtils.showMsg("请选择图片");
            return;
        }
        List<UploadBean> images = new ArrayList<>();
        List<String> tempList = new ArrayList<>();
        for (ImageFolderBean o : data) {
            String path = OSSUtils.getCustomerService(o.path);
            tempList.add(path);
            images.add(new UploadBean(o.path, path));
        }
        //R退 C换
        Map<String, String> params = HttpParams.insertREorder(orderId, userId, reason, count, remark,
                type, address, tempList);
        getP().insertREOrder(params, images);
    }

    private ActionSheet.ActionSheetListener getActionListener() {
        return (actionSheet, index) -> {
            imagePath = AppConfig.DEFAULT_IMAGE_PATH + File.separator + FileUtils.getFileName();
            switch (index) {
                case 0:
                    SystemUtils.startCameraPhoto(AfterSaleActivity.this, imagePath, CAMERA_REQUEST_CODE);
                    break;
                case 1:
                    RxPermissions.getInstance(this).request(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(aBoolean -> {
                                if (aBoolean) {
                                    int itemCount = mCustomLayout.getItemCount();
                                    ImageSelActivity.start(AfterSaleActivity.this, false, 5 - itemCount, REQUEST_IMAGE_CODE);
                                } else {
                                    ToastUtils.showMsg("没有存储权限,请去授权中心开启");
                                }
                            });
                    break;
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CODE:
                    List<ImageFolderBean> list = (List<ImageFolderBean>) data.getSerializableExtra("list");
                    mCustomLayout.setImageList(list);
                    break;
                case CAMERA_REQUEST_CODE:
                    ImageFolderBean bean = new ImageFolderBean();
                    bean.path = imagePath;
                    mCustomLayout.setImageItem(bean);
                    break;
            }
        }
    }
}
