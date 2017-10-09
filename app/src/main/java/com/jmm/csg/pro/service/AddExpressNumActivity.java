package com.jmm.csg.pro.service;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.XActivity;
import com.jmm.csg.bean.ReturnAddress;
import com.jmm.csg.config.AppConfig;
import com.jmm.csg.helper.DialogHelper;
import com.jmm.csg.imgload.ImageLoaderUtils;
import com.jmm.csg.imgsel.activity.ImagePreviewActivity;
import com.jmm.csg.imgsel.activity.ImageSelActivity;
import com.jmm.csg.imgsel.bean.ImageFolderBean;
import com.jmm.csg.pro.contract.ReOrderContract;
import com.jmm.csg.pro.presenter.ReOrderPresenter;
import com.jmm.csg.utils.FileUtils;
import com.jmm.csg.utils.SystemUtils;
import com.jmm.csg.utils.ToastUtils;
import com.jmm.csg.widget.ActionSheet;
import com.jmm.csg.widget.CustomLayout;
import com.jmm.csg.widget.TitleView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 填写快递单号
 */
public class AddExpressNumActivity extends XActivity<ReOrderPresenter> implements ReOrderContract.V {

    private static final int REQUEST_IMAGE_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;
    private String imagePath;
    @Bind(R.id.titleView) TitleView mTitleView;
    @Bind(R.id.tv_name) TextView tvName;
    @Bind(R.id.tv_phone) TextView tvPhone;
    @Bind(R.id.tv_address) TextView tvAddress;
    @Bind(R.id.spinner) Spinner spinner;
    @Bind(R.id.ll_add_logistics) LinearLayout llAddLogistics;
    @Bind(R.id.v_logistics) View vLogistics;
    @Bind(R.id.et_logistics) EditText etLogistics;
    @Bind(R.id.et_logistics_num) EditText etLogisticsNum;
    @Bind(R.id.customLayout) CustomLayout customLayout;
    @Bind(R.id.ll_content) LinearLayout llContent;
    @Bind(R.id.ll_empty) LinearLayout llEmpty;
    private String orderItemsId;
    private String selectName = "";
    private String orderId;

    @Override
    public void parseIntent(Intent intent) {
        orderItemsId = intent.getStringExtra("orderItemsId");
        orderId = intent.getStringExtra("orderId");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_express_num;
    }


    @Override
    public void initView() {
        mTitleView.setOnLeftImgClickListener(v -> onBackPressed());
        List<String> items = Arrays.asList("顺丰快递", "圆通快递", "申通快递", "韵达快递", "EMS", "其它快递");
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                llAddLogistics.setVisibility(i == items.size() - 1 ? View.VISIBLE : View.GONE);
                vLogistics.setVisibility(i == items.size() - 1 ? View.VISIBLE : View.GONE);
                selectName = items.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        customLayout.setMaxCount(2);
        customLayout.setColumnNum(3);
        customLayout.setInitList(new CustomLayout.InitListener() {
            @Override
            public void addClick() {
                DialogHelper.getActionDialog(AddExpressNumActivity.this,
                        getSupportFragmentManager(),
                        getActionListener(), "相机", "从相册选取").show();
            }

            @Override
            public void itemClick(View view, int position) {
                ImageFolderBean item = ((List<ImageFolderBean>) customLayout.getData()).get(position);
                ImagePreviewActivity.start(AddExpressNumActivity.this, view, item.path);

            }

            @Override
            public View getView(Object val, int position) {
                ImageFolderBean item = (ImageFolderBean) val;
                View view = View.inflate(AddExpressNumActivity.this, R.layout.item_picture_layout, null);
                ImageView ivPicture = (ImageView) view.findViewById(R.id.iv_picture);
                ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
                System.out.println(item.path);
                ImageLoaderUtils.getInstance().loadImage(AddExpressNumActivity.this, item.path, ivPicture);
                ivDelete.setOnClickListener(v -> customLayout.removeItem(customLayout.getPosition(view)));
                return view;
            }
        });
    }


    @Override
    public ReOrderPresenter newPresenter() {
        return new ReOrderPresenter();
    }

    @Override
    public void initData() {
        super.initData();
        getP().getReturnAddress(orderItemsId);
    }

    @Override
    public void getReturnAddressSuccess(ReturnAddress bean) {
        ReturnAddress.Entity entity = bean.getReturnAddress();
        if (TextUtils.isEmpty(entity.getAddress())) {
            llEmpty.setVisibility(View.VISIBLE);
            llContent.setVisibility(View.GONE);
            return;
        }
        tvName.setText(entity.getName());
        tvPhone.setText(entity.getPhoneNum());
        tvAddress.setText(entity.getAddress());
    }

    @OnClick(R.id.tv_commit)
    public void onClick() {
        if (selectName.equals("其它快递")) {
            selectName = etLogistics.getText().toString().trim();
        }
        if (TextUtils.isEmpty(selectName)) {
            ToastUtils.showMsg("请选择快递公司");
            return;
        }
        String logisticsNum = etLogisticsNum.getText().toString().trim();
        if (TextUtils.isEmpty(logisticsNum)) {
            ToastUtils.showMsg("请填写快递单号");
            return;
        }
        List<String> tempList = new ArrayList<>();
        List<ImageFolderBean> data = customLayout.getData();
        if (data.size() == 0) {
            ToastUtils.showMsg("请选择图片");
            return;
        }
        for (ImageFolderBean bean : data) {
            tempList.add(bean.path);
        }
        getP().commitReturnDetail(orderId, selectName, logisticsNum, tempList);
    }

    @OnClick(R.id.bt_back)
    public void onBack() {
        finish();
    }

    @Override
    public void submitSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    private ActionSheet.ActionSheetListener getActionListener() {
        return (actionSheet, index) -> {
            imagePath = AppConfig.DEFAULT_IMAGE_PATH + File.separator + FileUtils.getFileName();
            switch (index) {
                case 0:
                    SystemUtils.startCameraPhoto(AddExpressNumActivity.this, imagePath, CAMERA_REQUEST_CODE);
                    break;
                case 1:
                    RxPermissions.getInstance(this).request(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(aBoolean -> {
                                if (aBoolean) {
                                    int itemCount = customLayout.getItemCount();
                                    ImageSelActivity.start(AddExpressNumActivity.this, false, 2 - itemCount, REQUEST_IMAGE_CODE);
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
                    customLayout.setImageList(list);
                    break;
                case CAMERA_REQUEST_CODE:
                    ImageFolderBean bean = new ImageFolderBean();
                    bean.path = imagePath;
                    customLayout.setImageItem(bean);
                    break;
            }
        }
    }

}
