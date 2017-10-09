package com.jmm.csg.imgsel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jmm.csg.R;
import com.jmm.csg.imgsel.adapter.ImageFolderAdapter;
import com.jmm.csg.imgsel.bean.ImageFolderBean;
import com.jmm.csg.imgsel.listener.OnRecyclerViewClickListener;
import com.jmm.csg.imgsel.util.ImageHelper;
import com.jmm.csg.widget.DividerItemDecoration;
import com.jmm.csg.widget.TitleView;

import java.util.ArrayList;
import java.util.List;

public class FolderListActivity extends AppCompatActivity implements OnRecyclerViewClickListener {

    private ImageFolderAdapter adapter;
    private List<ImageFolderBean> folderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_list);
        folderList = new ArrayList<>();
        initView();
        initData();
    }

    private void initView() {
        TitleView titleView = (TitleView) findViewById(R.id.titleView);
        titleView.setOnRightTextClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setHasFixedSize(true);
        adapter = new ImageFolderAdapter(this, folderList);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(this);
    }

    private void initData() {
        ImageHelper.loadLocalFolderContainsImage(this).subscribe(imageFolderBeen -> {
            folderList.clear();
            folderList.addAll(imageFolderBeen);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        ImageFolderBean item = folderList.get(position);
        Intent intent = new Intent();
        intent.putExtra("data", item.getFolderPath());
        intent.putExtra("title", item.fileName);
        setResult(RESULT_OK, intent);
        finish();
    }
}
