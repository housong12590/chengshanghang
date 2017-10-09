package com.jmm.csg.imgsel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.jmm.csg.R;
import com.jmm.csg.imgsel.adapter.ImageSelAdapter;
import com.jmm.csg.imgsel.bean.ImageFolderBean;
import com.jmm.csg.imgsel.core.ImageSelectObservable;
import com.jmm.csg.imgsel.listener.OnRecyclerViewClickListener;
import com.jmm.csg.imgsel.util.ImageHelper;
import com.jmm.csg.widget.TitleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ImageSelActivity extends AppCompatActivity implements OnRecyclerViewClickListener, Observer {

    private static final int REQUEST_PREVIEW_PHOTO = 100;
    private static final int REQUEST_FOLDER_PHOTO = 101;
    private static final int DEFAULT_MAX_NUM = 9;

    public static int sMaxPicNum = DEFAULT_MAX_NUM;

    private static final int COLUMN = 3;
    private ImageSelAdapter adapter;
    private boolean isSingleImage;
    private String folderPath;
    private TitleView titleView;

    private void parseIntent() {
        Intent intent = getIntent();
        folderPath = intent.getStringExtra("data");
        isSingleImage = intent.getBooleanExtra("single", false);
        sMaxPicNum = intent.getIntExtra("maxCount", DEFAULT_MAX_NUM);
        ImageSelectObservable.getInstance().addSelectImagesAndClearBefore((ArrayList<ImageFolderBean>) getIntent().getSerializableExtra("list"));
    }

    public static void start(Activity activity, boolean singleSelect, int maxCount, int requestCode) {
        Intent intent = new Intent(activity, ImageSelActivity.class);
        intent.putExtra("single", singleSelect);
        intent.putExtra("maxCount", maxCount);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Activity activity, int requestCode) {
        start(activity, true, 1, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_sel);
        ImageSelectObservable.getInstance().addObserver(this);
        parseIntent();
        initView();
        initData();
    }

    private void initView() {
        titleView = (TitleView) findViewById(R.id.titleView);
        titleView.setOnLeftTextAndImageListener(view -> {
            startActivityForResult(new Intent(ImageSelActivity.this, FolderListActivity.class), REQUEST_FOLDER_PHOTO);
//            overridePendingTransition(R.anim.in_from_right,0);
        });
        titleView.setOnRightTextClickListener(view -> titleRightTextListener());
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, COLUMN));
        adapter = new ImageSelAdapter(this, ImageSelectObservable.getInstance().getFolderAllImages(), isSingleImage, sMaxPicNum);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnClickListener(this);
    }

    private void initData() {
        (TextUtils.isEmpty(folderPath) ? ImageHelper.queryAllPicture(this) : ImageHelper.queryFolderPicture(this, folderPath))
                .subscribe(imageFolderBeen -> {
                    ImageSelectObservable.getInstance().addFolderImagesAndClearBefore(imageFolderBeen);
                    adapter.notifyDataSetChanged();
                });
    }


    public void titleRightTextListener() {
        List<ImageFolderBean> selectImages = ImageSelectObservable.getInstance().getSelectImages();
        if (selectImages.size() != 0) {
            returnResult();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PREVIEW_PHOTO:
                    adapter.notifyDataSetChanged();
                    break;
                case REQUEST_FOLDER_PHOTO:
                    folderPath = data.getStringExtra("data");
                    String title = data.getStringExtra("title");
                    titleView.setTitle(title);
                    initData();
                    break;
            }
        } else {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position == -1) {
            List<ImageFolderBean> images = ImageSelectObservable.getInstance().getSelectImages();
            titleView.setRightText(images.size() == 0 ? "取消" : getString(R.string.image_max_count,images.size(),sMaxPicNum));
            return;
        }
        if (isSingleImage) {
            returnResult();
        } else {
            ImageFolderBean folderBean = ImageSelectObservable.getInstance().getFolderAllImages().get(position);
            ImagePreviewActivity.start(this, view, folderBean.path);
        }
    }

    public void returnResult() {
        Intent intent = new Intent();
        ArrayList<ImageFolderBean> list = new ArrayList<>();
        list.addAll(ImageSelectObservable.getInstance().getSelectImages());
        intent.putExtra("list", list);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void update(Observable o, Object arg) {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageSelectObservable.getInstance().clearSelectImages();
        ImageSelectObservable.getInstance().clearFolderImages();
    }
}
