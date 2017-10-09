package com.jmm.csg.pro.manager.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jmm.csg.config.AppConfig;
import com.jmm.csg.Constant;
import com.jmm.csg.R;
import com.jmm.csg.base.activity.XActivity;
import com.jmm.csg.bean.Bank;
import com.jmm.csg.bean.UploadBean;
import com.jmm.csg.callback.OnResultListener;
import com.jmm.csg.helper.AppDataHelper;
import com.jmm.csg.helper.DialogHelper;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.BaseSubscriber;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.imgload.ImageLoaderUtils;
import com.jmm.csg.imgsel.activity.ImageSelActivity;
import com.jmm.csg.imgsel.bean.ImageFolderBean;
import com.jmm.csg.pro.contract.UserCenterContract;
import com.jmm.csg.pro.presenter.UserCenterPresenter;
import com.jmm.csg.utils.FileUtils;
import com.jmm.csg.utils.IdcardValidator;
import com.jmm.csg.utils.OSSUtils;
import com.jmm.csg.utils.SystemUtils;
import com.jmm.csg.utils.ToastUtils;
import com.jmm.csg.widget.CitySelectDialog;
import com.jmm.csg.widget.ListDialog;
import com.jmm.csg.widget.TitleView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 实名认证
 */
public class RealNameAuthActivity extends XActivity<UserCenterPresenter> implements UserCenterContract.V {

    private static final int IMAGE_SELECT_CODE = 100;
    private static final int IMAGE_ZOOM_CODE = 101;
    private static final int IMAGE_FRONT_CODE = 103;
    private static final int IMAGE_BACK_CODE = 104;
    private static final int IMAGE_CAMERA_CODE = 105;
    @Bind(R.id.titleView)
    TitleView mTitleView;
    @Bind(R.id.tv_area)
    TextView mTvArea;
    @Bind(R.id.iv_avatar)
    ImageView mIvAvatar;
    @Bind(R.id.iv_card_front)
    ImageView mIvCardFront;
    @Bind(R.id.iv_card_back)
    ImageView mIvCardBack;
    @Bind(R.id.tv_commit)
    TextView mTvSubmit;
    @Bind(R.id.et_name)
    EditText mEtName;
    @Bind(R.id.et_phone_num)
    EditText mEtPhoneNum;
    @Bind(R.id.et_card_num)
    EditText mEtCardNum;
    @Bind(R.id.et_job_Num)
    EditText mEvJobNum;
    @Bind(R.id.tv_bank)
    TextView mTvBank;
    @Bind(R.id.tv_branch)
    TextView mTvBranch;

    private String imagePath;
    private Bank.BankEntity bankEntity;
    private Bank.BranchBank branchBank;
    private String address;
    private String cardFrontPath;
    private String cardBackPath;
    private String picPath;
    private ListDialog<Bank.BankEntity> bankDialog;
    private ListDialog<Bank.BranchBank> branchBankDialog;
    private int flag;

    @Override
    public int getLayoutId() {
        return R.layout.activity_real_name_auth;
    }

    @Override
    public void initView() {
//        mTvJobNum.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        setTextGravity(mTvBank, mTvBranch, mTvArea);
        mEtPhoneNum.setEnabled(false);
        mEtPhoneNum.setText(AppDataHelper.getPhoneNumber());
    }

    @Override
    public UserCenterPresenter newPresenter() {
        return new UserCenterPresenter();
    }

    @Override
    public void onSuccess() {
        startActivity(new Intent(RealNameAuthActivity.this, MainActivity.class));
        UserDataHelper.setRealNameStatus(Constant.REALNAME_ING);
        finish();
    }

    @OnClick({R.id.tv_area, R.id.iv_avatar, R.id.iv_card_front, R.id.iv_card_back, R.id.tv_commit
            , R.id.tv_job_Num, R.id.ll_bank, R.id.ll_branch})
    public void onClick(View view) {
        imagePath = AppConfig.DEFAULT_IMAGE_PATH + File.separator + FileUtils.getFileName();
        switch (view.getId()) {
            case R.id.tv_area:
                showCityDialog();
                break;
            case R.id.tv_commit:
                commitRealName();
                break;
            case R.id.iv_avatar:
//                ImageSelActivity.start(this, IMAGE_SELECT_CODE);
                showActionDialog(imagePath, IMAGE_SELECT_CODE);
                flag = 0;
                break;
            case R.id.iv_card_front:
//                ImageSelActivity.start(this, IMAGE_FRONT_CODE);
                showActionDialog(imagePath, IMAGE_FRONT_CODE);
                flag = 1;
                break;
            case R.id.iv_card_back:
//                ImageSelActivity.start(this, IMAGE_BACK_CODE);
                showActionDialog(imagePath, IMAGE_BACK_CODE);
                flag = 2;
                break;
            case R.id.tv_job_Num:

                break;
            case R.id.ll_bank:
                getBanks();
                break;
            case R.id.ll_branch:
                getBranchBanks();
                break;
        }
    }

    public void showActionDialog(String picPath, int requestCode) {
        DialogHelper.getActionDialog(this, getSupportFragmentManager(), (actionSheet, index) -> {
            switch (index) {
                case 0:
                    SystemUtils.startCameraPhoto(RealNameAuthActivity.this, picPath, IMAGE_CAMERA_CODE);
                    break;
                case 1:
                    RxPermissions.getInstance(RealNameAuthActivity.this).request(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(aBoolean -> {
                                if (aBoolean) {
                                    ImageSelActivity.start(RealNameAuthActivity.this, requestCode);
                                } else {
                                    ToastUtils.showMsg("没有存储权限,请去授权中心开启");
                                }
                            });
                    break;
            }
        }, "相机", "从相册选取").show();
    }

    private void showCityDialog() {
        CitySelectDialog dialog = new CitySelectDialog(this);
        dialog.setonResultListener(val -> {
            address = val;
            String s = val.replaceAll("/", "");
            mTvArea.setText(s);
            branchBank = null;
            mTvBranch.setText("");
        });
        dialog.show();
    }

    private void getBanks() {
        if (bankDialog == null) {
            bankDialog = new ListDialog<>(this).setTitle("选择银行");
        }
        bankDialog.setOnResultListener(new OnResultListener<Bank.BankEntity>() {
            @Override
            public void onResult(int position, Bank.BankEntity val) {
                bankEntity = val;
                branchBank = null;
                mTvBranch.setText("");
                mTvBank.setText(val.getBank_name());
//                address = null;
//                mTvArea.setText("");
            }
        });
        bankDialog.show();
        bankDialog.showLoading();
        getP().getBanks().subscribe(new BaseSubscriber<Bank>() {
            @Override
            public void onNext(Bank bank) {
                bankDialog.showContent(bank.getBanks());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                bankDialog.showError();
            }
        });
    }

    private void getBranchBanks() {
        if (bankEntity == null) {
            ToastUtils.showMsg("请先选择所属银行");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtils.showMsg("请先选择区域");
            return;
        }
        if (branchBankDialog == null) {
            branchBankDialog = new ListDialog<>(this).setTitle("选择支行");
        }
        branchBankDialog.setOnResultListener(new OnResultListener<Bank.BranchBank>() {
            @Override
            public void onResult(int position, Bank.BranchBank val) {
                branchBank = val;
                mTvBranch.setText(val.getBranch_name());
            }
        });
        branchBankDialog.show();
        String[] split = address.split("/");
        branchBankDialog.showLoading();
        getP().getBranchBanks(bankEntity.getId(), split[1], split[2], split[3])
                .subscribe(new BaseSubscriber<List<Bank.BranchBank>>() {
                    @Override
                    public void onNext(List<Bank.BranchBank> branchBanks) {
                        branchBankDialog.showContent(branchBanks);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        branchBankDialog.showError();
                    }
                });
    }

    private void setTextGravity(TextView... tv) {
        for (TextView aTv : tv) {
            RxTextView.textChanges(aTv).subscribe(s -> aTv.setGravity((s.length() > 0
                    ? Gravity.LEFT : Gravity.RIGHT) | Gravity.CENTER_VERTICAL));
        }
    }


    private void commitRealName() {
        String name = mEtName.getText().toString().trim();
        String phone = mEtPhoneNum.getText().toString().trim();
//        String tempJobNum = mTvJobNum.getText().toString();
//        String jobNum = tempJobNum.substring(tempJobNum.indexOf(":") + 1, tempJobNum.length());
        String jobNum = mEvJobNum.getText().toString().trim();
        String cardNum = mEtCardNum.getText().toString().trim();
        if (TextUtils.isEmpty(picPath)) {
            ToastUtils.showMsg("请选择头像");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showMsg("请输入姓名");
            return;
        }
        String tempAddress = null;
        if (!TextUtils.isEmpty(address)) {
            tempAddress = this.address.replaceAll("/", "");
        }
        if (bankEntity == null) {
            ToastUtils.showMsg("请选择银行");
            return;
        }
        if (branchBank == null) {
            ToastUtils.showMsg("请选择支行");
            return;
        }
        if (TextUtils.isEmpty(jobNum)) {
            ToastUtils.showMsg("请输入工号");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showMsg("请输入手机号码");
            return;
        }
//        if (!cardNum.matches(Constant.REGEX_ID_CARD18)) {
//            ToastUtils.showMsg("身份证号不正确");
//            return;
//        }
        if (!new IdcardValidator().isValidatedAllIdcard(cardNum)) {
            ToastUtils.showMsg("身份证号不正确");
            return;
        }
        if (TextUtils.isEmpty(cardFrontPath) || TextUtils.isEmpty(cardBackPath)) {
            ToastUtils.showMsg("请上传身份证图片");
            return;
        }
        String cardFrontName = OSSUtils.getCardPhotoFileName(cardNum, true);
        String cardBackName = OSSUtils.getCardPhotoFileName(cardNum, false);
        String avatarName = OSSUtils.getAvatarLoadFileName(cardNum);
        List<UploadBean> list = new ArrayList<>();
        list.add(new UploadBean(picPath, avatarName));
        list.add(new UploadBean(cardFrontPath, cardFrontName));
        list.add(new UploadBean(cardBackPath, cardBackName));
        String cardFront = OSSUtils.getCardPhotoFileName(cardNum, true);
        String cardBack = OSSUtils.getCardPhotoFileName(cardNum, false);
        String userId = UserDataHelper.getUserId();
        Map<String, String> params = HttpParams.realNameRequest(userId, name, tempAddress, phone, jobNum,
                cardNum, avatarName, cardFront, cardBack, bankEntity.getId(), branchBank.getId());
        getP().commitRealName(params, list);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<ImageFolderBean> list;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_CAMERA_CODE:
                    switch (flag) {
                        case 0:
                            startPhotoZoom(Uri.fromFile(new File(imagePath)));
                            break;
                        case 1:
                            cardFrontPath = imagePath;
                            ImageLoaderUtils.getInstance().loadImage(this, cardFrontPath, mIvCardFront);
                            break;
                        case 2:
                            cardBackPath = imagePath;
                            ImageLoaderUtils.getInstance().loadImage(this, cardBackPath, mIvCardBack);
                            break;
                    }
                    break;
                case IMAGE_SELECT_CODE:
                    list = (List<ImageFolderBean>) data.getSerializableExtra("list");
                    startPhotoZoom(Uri.fromFile(new File(list.get(0).path)));
                    break;
                case IMAGE_ZOOM_CODE:
                    picPath = imagePath;
                    Glide.with(this).load(new File(imagePath)).into(mIvAvatar);
                    break;
                case IMAGE_FRONT_CODE:
                    list = (List<ImageFolderBean>) data.getSerializableExtra("list");
                    cardFrontPath = list.get(0).path;
                    ImageLoaderUtils.getInstance().loadImage(this, cardFrontPath, mIvCardFront);
                    break;
                case IMAGE_BACK_CODE:
                    list = (List<ImageFolderBean>) data.getSerializableExtra("list");
                    cardBackPath = list.get(0).path;
                    ImageLoaderUtils.getInstance().loadImage(this, cardBackPath, mIvCardBack);
                    break;
            }
        }
    }

    private void startPhotoZoom(Uri uri) {
        File file = new File(imagePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, IMAGE_ZOOM_CODE);
    }

    @Override
    public void onBackPressed() {
        DialogHelper.getMessageDialog(this, "是否退出实名认证？", (dialog, which) -> {
//            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }).show();
    }
}
