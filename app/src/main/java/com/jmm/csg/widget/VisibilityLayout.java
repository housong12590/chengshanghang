package com.jmm.csg.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class VisibilityLayout extends FrameLayout {

    private View mLoadingView;
    private View mErrorView;
    private View mContentView;
    private View mEmptyView;
    public static int LOAD_LAYOUT_ID = NO_ID;
    public static int ERROR_LAYOUT_ID = NO_ID;
    public static int EMPTY_LAYOUT_ID = NO_ID;

    public VisibilityLayout(View view) {
        super(view.getContext());
        mContentView = view;
        ViewGroup parent = (ViewGroup) view.getParent();
        int childCount = parent.getChildCount();
        int index = 0;
        for (int i = 0; i < childCount; i++) { // 找到contentView在父容器所在的位置
            if (parent.getChildAt(i) == view) {
                index = i;
            }
        }
        parent.removeView(view); // 把原先contentView移除
        ViewGroup.LayoutParams params = view.getLayoutParams();
        parent.addView(this, index, params); // 再把自己创建的view添加到原来的位置

        addView(view);
        view.setVisibility(GONE);
        if (mErrorView == null) {
            mErrorView = inflate(ERROR_LAYOUT_ID);
        }
        addView(mErrorView);
        mErrorView.setVisibility(GONE);
        if (mEmptyView == null) {
            mEmptyView = inflate(EMPTY_LAYOUT_ID);
        }
        addView(mEmptyView);
        mEmptyView.setVisibility(GONE);
        if (mLoadingView == null) {
            mLoadingView = inflate(LOAD_LAYOUT_ID);
        }
        addView(mLoadingView);
        mLoadingView.setVisibility(GONE);
    }

    public VisibilityLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VisibilityLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void showView(View view) {
        if (view == null) {
            return;
        }
        mLoadingView.setVisibility(view == mLoadingView ? VISIBLE : GONE);
        mErrorView.setVisibility(view == mErrorView ? VISIBLE : GONE);
        mContentView.setVisibility(view == mContentView ? VISIBLE : GONE);
        mEmptyView.setVisibility(view == mEmptyView ? VISIBLE : GONE);
    }

    public void showErrorView() {
        Log.e(this.getClass().getSimpleName(), "showErrorView");
        showView(mErrorView);
    }

    public void showContentView() {
        Log.e(this.getClass().getSimpleName(), "showContentView");
        showView(mContentView);
    }

    public void showEmptyView() {
        Log.e(this.getClass().getSimpleName(), "showEmptyView");
        showView(mEmptyView);
    }

    public void showLoadingView() {
        Log.e(this.getClass().getSimpleName(), "showLoadingView");
        showView(mLoadingView);
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public View getErrorView() {
        return mErrorView;
    }

    public View getContentView() {
        return mContentView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setLoadingView(View mLoadingView) {
        removeView(this.mLoadingView);
        addView(mLoadingView);
        mLoadingView.setVisibility(GONE);
        this.mLoadingView = mLoadingView;
    }

    public void setEmptyView(View mEmptyView) {
        removeView(this.mEmptyView);
        addView(mEmptyView);
        mEmptyView.setVisibility(GONE);
        this.mEmptyView = mEmptyView;
    }

    public void setContentView(View mContentView) {
        removeView(this.mContentView);
        addView(mContentView);
        mContentView.setVisibility(GONE);
        this.mContentView = mContentView;
    }

    public void setErrorView(View mErrorView) {
        removeView(this.mErrorView);
        addView(mErrorView);
        mErrorView.setVisibility(GONE);
        this.mErrorView = mErrorView;
    }

    public void setLoadingView(int layoutId) {
        setLoadingView(inflate(layoutId));
    }

    public void setErrorView(int layoutId) {
        setErrorView(inflate(layoutId));
    }

    public void EmptyView(int layoutId) {
        setEmptyView(inflate(layoutId));
    }

    private View inflate(int layoutId) {
        return inflate(getContext(), layoutId, null);
    }
}
