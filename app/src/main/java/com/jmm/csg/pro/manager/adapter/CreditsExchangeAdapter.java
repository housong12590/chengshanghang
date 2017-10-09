package com.jmm.csg.pro.manager.adapter;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jmm.csg.R;
import com.jmm.csg.bean.IntegralItem;

import java.util.List;

public class CreditsExchangeAdapter extends BaseQuickAdapter<IntegralItem, BaseViewHolder> {

    public CreditsExchangeAdapter(List<IntegralItem> data) {
        super(R.layout.item_integeral_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IntegralItem item) {
        TextView tv = helper.getView(R.id.tv_text);

        Drawable top = mContext.getResources().getDrawable(item.getIcon());
        tv.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        tv.setText(item.getText());
    }
}
