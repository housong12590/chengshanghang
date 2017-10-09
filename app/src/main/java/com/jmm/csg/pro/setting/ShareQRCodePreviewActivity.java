package com.jmm.csg.pro.setting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.jmm.csg.config.AppConfig;
import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.utils.ImageUtils;

import butterknife.Bind;

public class ShareQRCodePreviewActivity extends BaseActivity {

    public static final String TRANSIT_PIC = "picture";
    @Bind(R.id.iv_image) ImageView mIvImage;

    @Override
    public int getLayoutId() {
        transparentStatusBar();
        return R.layout.activity_share_qrcode_preview;
    }

    @Override
    public void initView() {
        createQRCode();
    }


    private void createQRCode() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        try {
            bitmap = ImageUtils.centerSquareScaleBitmap(bitmap, 150);
            bitmap = ImageUtils.createCode(AppConfig.APP_SHARE_URL, bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        mIvImage.setImageBitmap(bitmap);
        ViewCompat.setTransitionName(mIvImage, TRANSIT_PIC);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            onBackPressed();
        }
        return super.onTouchEvent(event);
    }
}
