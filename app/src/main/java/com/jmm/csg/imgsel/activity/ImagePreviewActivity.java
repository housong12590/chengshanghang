package com.jmm.csg.imgsel.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.imgload.ImageLoader;
import com.jmm.csg.imgload.ImageLoaderUtils;

import butterknife.Bind;

public class ImagePreviewActivity extends BaseActivity {

    public static final String TRANSIT_PIC = "picture";
    @Bind(R.id.iv_picture) ImageView ivPicture;
    @Bind(R.id.iv_back) ImageView ivBack;
    private String path;

    @Override
    public void parseIntent(Intent intent) {
        path = intent.getStringExtra("path");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_preview;
    }

    @Override
    public void initView() {
        transparentStatusBar();
        ivBack.setOnClickListener(view -> finish());
//        ImageLoaderUtils.getInstance().loadImage(this, path, ivPicture);
        ImageLoaderUtils.getInstance().loadImage(this, new ImageLoader.Builder()
                .into(ivPicture)
                .placeHolder(R.drawable.defaultpic)
                .error(R.drawable.defaultpic)
                .isCache(false)
                .load(path)
                .build());
//        ViewCompat.setTransitionName(ivPicture, TRANSIT_PIC);
    }

    public static void start(Activity activity, View transitView, String path) {
        Intent intent = new Intent(activity, ImagePreviewActivity.class);
        intent.putExtra("path", path);
        activity.startActivity(intent);

//        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
//                .makeSceneTransitionAnimation(activity, transitView, TRANSIT_PIC);
//        try {
//            ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            activity.startActivity(intent);
//        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            onBackPressed();
//        }
//        return super.onTouchEvent(event);
//    }
}
