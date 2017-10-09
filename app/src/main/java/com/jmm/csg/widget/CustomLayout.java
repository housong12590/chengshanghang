package com.jmm.csg.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.jmm.csg.R;
import com.jmm.csg.imgsel.bean.ImageFolderBean;
import com.jmm.csg.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;


public class CustomLayout extends FlowLayout {

    private int width;
    private int column = 4;
    private int maxCount = 5;
    public ViewGroup.LayoutParams params;
    private View addView;
    private List<Object> list = new ArrayList<>();

    public CustomLayout(Context context) {
        this(context, null);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        addView = inflate(getContext(), R.layout.item_add_image_layout, null);
        int width = DensityUtils.getScreenWidth(getContext());
//        this.width = (width / ++column);
        this.width = (width - DensityUtils.dip2px(getContext(), 80)) / column;
        params = new ViewGroup.LayoutParams(this.width, this.width);
        addView.setLayoutParams(params);
        addView(addView);
        addView.setOnClickListener(addClickListener);
    }

    public void setColumnNum(int column) {
        this.column = column;
        removeView(addView);
        init();
    }

    public void setMaxCount(int count) {
        this.maxCount = count;
    }

    private View.OnClickListener addClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (initListener != null) {
                initListener.addClick();
            }
        }
    };

    public interface InitListener<T> {
        void addClick();

        void itemClick(View view, int position);

        View getView(T val, int position);
    }

    private InitListener initListener;

    public void setInitList(InitListener listener) {
        this.initListener = listener;
    }

    public void setImageList(List<ImageFolderBean> list) {
        for (Object obj : list) {
            setImageItem((ImageFolderBean) obj);
        }
    }

    public int getItemCount() {
        return addView.getParent() == null ? getChildCount() : getChildCount() - 1;
    }

    public void setImageItem(ImageFolderBean val) {
        if (initListener == null)
            return;
        list.add(val);
        int position = list.size() - 1;
        View view = initListener.getView(val, list.size() - 1);
        addView(view, getChildCount() - 1, params);
        view.setOnClickListener(listener);
        if (getChildCount() == maxCount + 1) {
            removeView(addView);
        }
    }

    public int getPosition(View rootView) {
        return indexOfChild(rootView);
    }

    public void removeItem(int position) {
        View view = getChildAt(position);
        list.remove(position);
        removeView(view);
    }

    public <T> T getData() {
        return (T) list;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (initListener != null) {
                initListener.itemClick(v, indexOfChild(v));
            }
        }
    };

    @Override
    public void removeView(View view) {
        if (addView.getParent() == null) {
            addView(addView);
        }
        super.removeView(view);
    }
}
