package com.jmm.csg.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jmm.csg.R;
import com.jmm.csg.bean.ChineseCities;
import com.jmm.csg.bean.CityStatus;
import com.jmm.csg.utils.DensityUtils;
import com.jmm.csg.utils.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 城市选择对话框
 */
public class CitySelectDialog extends Dialog {


    private int width;
    private int height;
    private List<String> tempList = new ArrayList<>();

    private RecyclerView recyclerView;
    private TextView tvProvince;
    private TextView tvCity;
    private TextView tvArea;
    private LinearLayoutManager recyclerLayoutManager;
    private List<ChineseCities> chineseCities;
    private CityAdapter adapter;
    private int lastPosition;

    private CityStatus status;
    private String result = "";


    public CitySelectDialog(Context context) {
        super(context, R.style.comment_dialog);
        width = DensityUtils.getScreenWidth(context);
        height = DensityUtils.getScreenHeight(context);
        initDialogParams();
        initData();
        initView();
    }

    private void initData() {
        InputStream is = getContext().getResources().openRawResource(R.raw.cities);
        String cityJson = StringUtils.getJsonFromLocal(is);
        chineseCities = new Gson().fromJson(cityJson, new TypeToken<List<ChineseCities>>() {
        }.getType());
    }

    private void initDialogParams() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_city_select1);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = width;
        lp.height = height / 2;
        window.setAttributes(lp);
    }


    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(view -> backToUp());
        findViewById(R.id.iv_close).setOnClickListener(view -> dismiss());
        tvProvince = (TextView) findViewById(R.id.tv_province);
        tvArea = (TextView) findViewById(R.id.tv_area);
        tvCity = (TextView) findViewById(R.id.tv_city);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.addOnItemTouchListener(listener);
        for (ChineseCities cities : chineseCities) {
            tempList.add(cities.getName());
        }
        adapter = new CityAdapter(tempList);
        recyclerView.setAdapter(adapter);
        status = CityStatus.PROVINCE;
    }

    private void backToUp() {
        tempList.clear();
        switch (status) {
            case PROVINCE:
                dismiss();
                return;
            case CITY:
                tvProvince.setText("");
                tvCity.setText("选择区域");
                for (ChineseCities cities : chineseCities) {
                    tempList.add(cities.getName());
                }
                status = CityStatus.PROVINCE;
                break;
            case AREA:
                tvCity.setText("");
                for (ChineseCities.City city : chineseCities.get(lastPosition).getCity()) {
                    tempList.add(city.getName());
                }
                status = CityStatus.CITY;
                break;
        }
        result = result.substring(0, result.lastIndexOf("/"));
        adapter.setNewData(tempList);
        moveToPosition(recyclerLayoutManager, recyclerView, 0);
    }


    private class CityAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public CityAdapter(List<String> data) {
            super(R.layout.item_text_layout, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.text, item);
        }
    }


    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            if (tempList.size() != 0) {
                result += "/" + tempList.get(position);
                TextView tv = status == CityStatus.PROVINCE ? tvProvince : status == CityStatus.CITY ? tvCity : tvArea;
                tv.setText(tempList.get(position));
            }
            tempList.clear();
            switch (status) {
                case PROVINCE:
                    ChineseCities cities = CitySelectDialog.this.chineseCities.get(position);
                    for (ChineseCities.City city : cities.getCity()) {
                        tempList.add(city.getName());
                    }
                    status = CityStatus.CITY;
                    tvCity.setText("");
                    lastPosition = position;
                    break;
                case CITY:
                    ChineseCities.City city = chineseCities.get(lastPosition).getCity().get(position);
                    tempList.addAll(city.getArea());
                    status = CityStatus.AREA;
                    break;
                default:
                    if (resultListener != null) {
                        resultListener.onResult(result);
                    }
                    dismiss();
                    break;
            }
            moveToPosition(recyclerLayoutManager, recyclerView, 0);
            CitySelectDialog.this.adapter.setNewData(tempList);
        }
    };

    @Override
    public void dismiss() {
        status = CityStatus.PROVINCE;
        lastPosition = 0;
        result = "";
        super.dismiss();
    }

    public void moveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }
    }

    private onResultListener resultListener;

    public void setonResultListener(onResultListener listener) {
        this.resultListener = listener;
    }

    public interface onResultListener {
        void onResult(String val);
    }
}
