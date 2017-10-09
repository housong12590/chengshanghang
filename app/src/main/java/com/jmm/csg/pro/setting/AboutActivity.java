package com.jmm.csg.pro.setting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.jmm.csg.config.AppConfig;
import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.helper.AppDataHelper;
import com.jmm.csg.utils.ImageUtils;
import com.jmm.csg.utils.SystemUtils;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @Bind(R.id.titleView) TitleView titleView;
    @Bind(R.id.tv_versions) TextView tvVersions;
    @Bind(R.id.tv_update_text) TextView tvUpdateText;
    @Bind(R.id.iv_share) ImageView ivShare;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        titleView.setOnLeftImgClickListener(v -> finish());
        createQRCode();
        String versionName = SystemUtils.getAppVersionName(this);
        int versionCode = SystemUtils.getAppVersionCode(this);
        String str = "已是最新版";
        String appUpdateVersions = AppDataHelper.getAppUpdateVersions();
        try {
            if (Integer.parseInt(appUpdateVersions) > versionCode) {
                str = "有新版本";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvUpdateText.setText(str);
        tvVersions.setText("v " + versionName);
    }

    @OnClick({R.id.ll_update, R.id.tv_score, R.id.ll_hotLine, R.id.iv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_update://检查更新
                AppConfig.appCheckUpdate(this, true);
                break;
            case R.id.tv_score://去评分
                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intentpf = new Intent(Intent.ACTION_VIEW, uri);
                    intentpf.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentpf);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_hotLine://客户热线
                SystemUtils.startDial(this, "4000406755");
                break;
            case R.id.iv_share:
                Intent intent = new Intent(this, ShareQRCodePreviewActivity.class);
                SystemUtils.startTransitionActivity(this, intent, view, ShareQRCodePreviewActivity.TRANSIT_PIC);
                break;
        }
    }

    private void createQRCode() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        try {
            bitmap = ImageUtils.centerSquareScaleBitmap(bitmap, 150);
            bitmap = ImageUtils.createCode(AppConfig.APP_SHARE_URL, bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        ivShare.setImageBitmap(bitmap);
    }
}
