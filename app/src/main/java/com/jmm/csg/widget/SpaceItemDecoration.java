package com.jmm.csg.widget;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpaceItemDecoration(int value) {
        this.space = dp2px(value);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) manager;
            gridLayoutManager(outRect, parent.getChildAdapterPosition(view), layoutManager);
        } else if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
            linearLayoutManager(outRect, parent.getChildAdapterPosition(view), layoutManager);
        }
    }

    private void gridLayoutManager(Rect rect, int position, GridLayoutManager layoutManager) {
        int spanCount = layoutManager.getSpanCount();
        int orientation = layoutManager.getOrientation();
        if (orientation == GridLayoutManager.VERTICAL) {
            if (position >= spanCount) {//第一行不加top间距
                rect.top = space;
            }
            if (position % spanCount < spanCount - 1) { //最右边不加right间距
                rect.right = space / 2;
            }
            if (position % spanCount != 0) { //最左边不加left间距
                rect.left = space / 2;
            }
        } else if (orientation == GridLayoutManager.HORIZONTAL) {
            if (position >= spanCount) {
                rect.left = space;
            }
            if (position % spanCount < spanCount - 1) {
                rect.bottom = space / 2;
            }
            if (position % spanCount != 0) {
                rect.top = space / 2;
            }
        }
    }

    private void linearLayoutManager(Rect rect, int position, LinearLayoutManager layoutManager) {
        int orientation = layoutManager.getOrientation();
        if (orientation == LinearLayoutManager.HORIZONTAL) { //纵向不加left
            if (position != 0) {
                rect.left = space;
            }
        } else if (orientation == LinearLayoutManager.VERTICAL) {//垂直不加top
            if (position != 0) {
                rect.top = space;
            }
        }
    }

    public int dp2px(int space) {
        return (int) (Resources.getSystem().getDisplayMetrics().density * space);
    }
}
