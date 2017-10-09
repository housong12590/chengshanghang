package com.jmm.csg.pro.product.activity;

import android.content.Intent;
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
import com.jmm.csg.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * 二维码预览界面
 */
public class QRCodePreviewActivity extends BaseActivity {

    public static final String TRANSIT_PIC = "picture";

    @Bind(R.id.iv_image) ImageView mIvImage;
    private Map<String, String> map = new HashMap<>();
    private String imagePath;

    @Override
    public void parseIntent(Intent intent) {
        map.put("amId", intent.getStringExtra("accountManagerId"));
        map.put("coding", intent.getStringExtra("coding"));
        imagePath = intent.getStringExtra("proPic");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_qrcode_preview;
    }


    @Override
    public void initView() {
        setStatusBarBackgroundAndColor();
        String s = StringUtils.mapToString(map);
        String url = AppConfig.BASE_WEB_URL + s;
        Bitmap bitmap = null;
        if (imagePath != null) {
            bitmap = BitmapFactory.decodeFile(imagePath);
        }
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }

//        if (TextUtils.isEmpty(imagePath)) {
//            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
//        } else {
//            bitmap = BitmapFactory.decodeFile(imagePath);
//        }
        try {
            bitmap = ImageUtils.centerSquareScaleBitmap(bitmap, 150);
            bitmap = ImageUtils.createCode(url, bitmap);
            mIvImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        ViewCompat.setTransitionName(mIvImage, TRANSIT_PIC);
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            onBackPressed();
        }
        return super.onTouchEvent(event);
    }
}
