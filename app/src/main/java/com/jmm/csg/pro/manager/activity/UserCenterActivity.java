package com.jmm.csg.pro.manager.activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmm.csg.config.AppConfig;
import com.jmm.csg.R;
import com.jmm.csg.base.activity.XActivity;
import com.jmm.csg.bean.Account;
import com.jmm.csg.bean.UploadBean;
import com.jmm.csg.helper.DialogHelper;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.BaseSubscriber;
import com.jmm.csg.imgload.ImageLoaderUtils;
import com.jmm.csg.imgsel.activity.ImagePreviewActivity;
import com.jmm.csg.imgsel.activity.ImageSelActivity;
import com.jmm.csg.imgsel.bean.ImageFolderBean;
import com.jmm.csg.pro.contract.UserCenterContract;
import com.jmm.csg.pro.presenter.UserCenterPresenter;
import com.jmm.csg.utils.FileUtils;
import com.jmm.csg.utils.OSSUtils;
import com.jmm.csg.utils.SystemUtils;
import com.jmm.csg.widget.ActionSheet;
import com.jmm.csg.widget.TitleView;

import java.io.File;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 个人信息
 */

public class UserCenterActivity extends XActivity<UserCenterPresenter> implements UserCenterContract.V {

    private static final int IMAGE_SELECT_CODE = 100;
    private static final int IMAGE_ZOOM_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 103;
    private String imagePath;

    @Bind(R.id.iv_avatar)
    ImageView mIvAvatar;
    @Bind(R.id.titleView)
    TitleView mTitleView;
    @Bind(R.id.tv_area)
    TextView mTvArea;
    @Bind(R.id.tv_bank)
    TextView mTvBank;
    @Bind(R.id.tv_branch)
    TextView mTvBranch;
    @Bind(R.id.tv_phone_num)
    TextView mTvPhoneNum;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_job_Num)
    TextView mTvJobNum;
    @Bind(R.id.iv_card_front)
    ImageView mIvCardFront;
    @Bind(R.id.iv_card_back)
    ImageView mIvCardBack;
    @Bind(R.id.tv_change_avatar)
    TextView tvChangeAvatar;
    private Account account;
    private String cardFrontPath;
    private String cardBackPath;

    @Override
    public void parseIntent(Intent intent) {
        account = (Account) intent.getSerializableExtra("account");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_center;
    }

    @Override
    public UserCenterPresenter newPresenter() {
        return new UserCenterPresenter();
    }

    @Override
    public void initView() {
        mTitleView.setOnLeftImgClickListener(view -> finish());
        if (account == null) {
            return;
        }
        mTvBank.setText(account.getBankName());
        mTvPhoneNum.setText(account.getPhoneNumber());
        mTvName.setText(account.getName());
        mTvBranch.setText(account.getBranchName());
        mTvArea.setText(account.getArea());
        mTvJobNum.setText(account.getJobNumber());
        ImageLoaderUtils.getInstance().loadOSSAvatar(this, account.getAccountManagerPic(), mIvAvatar);
        String idCard = account.getIdcard();
        String flag = account.getFlag();
        if (null != flag && flag.equals("batch")) {
            mIvCardFront.setVisibility(View.GONE);
            mIvCardBack.setVisibility(View.GONE);
        } else {
            cardFrontPath = OSSUtils.getCardPhotoFileName(idCard, true);
            cardBackPath = OSSUtils.getCardPhotoFileName(idCard, false);
            ImageLoaderUtils.getInstance().loadOSSImage(this, cardFrontPath, mIvCardFront);
            ImageLoaderUtils.getInstance().loadOSSImage(this, cardBackPath, mIvCardBack);
        }
    }

    @Override
    public void onSuccess() {

    }

    @OnClick({R.id.iv_avatar, R.id.iv_card_back, R.id.iv_card_front, R.id.tv_commit, R.id.tv_change_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar:
                ImagePreviewActivity.start(this, mIvAvatar, OSSUtils.BASE_URL + account.getAccountManagerPic());
                break;
            case R.id.iv_card_front://身份证正面
                ImagePreviewActivity.start(this, mIvCardFront, OSSUtils.BASE_URL + cardFrontPath);
                break;
            case R.id.iv_card_back://身份证反面
                ImagePreviewActivity.start(this, mIvCardBack, OSSUtils.BASE_URL + cardBackPath);
                break;
            case R.id.tv_commit://重新提交审核
                String phone = mTvPhoneNum.getText().toString();

                if (account == null && account.getPhoneNumber().equals(phone)) {
                    return;
                }
                getP().modifyMgrPhone(UserDataHelper.getUserId(), phone);
                break;
            case R.id.tv_change_avatar:
                DialogHelper.getActionDialog(UserCenterActivity.this,
                        getSupportFragmentManager(),
                        getActionListener(), "相机", "从相册选取").show();
                break;
        }
    }


    private ActionSheet.ActionSheetListener getActionListener() {
        return (actionSheet, index) -> {
            imagePath = AppConfig.DEFAULT_IMAGE_PATH + File.separator + FileUtils.getFileName();
            switch (index) {
                case 0:
                    SystemUtils.startCameraPhoto(UserCenterActivity.this, imagePath, CAMERA_REQUEST_CODE);
                    break;
                case 1:
                    ImageSelActivity.start(this, IMAGE_SELECT_CODE);
                    break;
            }
        };
    }

    boolean isChangeAvatar;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    startPhotoZoom(Uri.fromFile(new File(imagePath)));
                    break;
                case IMAGE_SELECT_CODE:
                    List<ImageFolderBean> list = (List<ImageFolderBean>) data.getSerializableExtra("list");
                    startPhotoZoom(Uri.fromFile(new File(list.get(0).path)));
                    break;
                case IMAGE_ZOOM_CODE:
                    UploadBean bean = new UploadBean(imagePath, account.getAccountManagerPic());
                    OSSUtils.uploadImages(Collections.singletonList(bean)).subscribe(new BaseSubscriber<String>() {
                        @Override
                        public void onNext(String s) {
                            ImageLoaderUtils.getInstance().loadImage(UserCenterActivity.this, imagePath, mIvAvatar);
                            isChangeAvatar = true;
                        }
                    });
                    break;
            }
        }
    }

    @Override
    public void finish() {
        if (isChangeAvatar) {
            setResult(RESULT_OK);
        }
        super.finish();
    }

    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imagePath)));
        startActivityForResult(intent, IMAGE_ZOOM_CODE);
    }
}
