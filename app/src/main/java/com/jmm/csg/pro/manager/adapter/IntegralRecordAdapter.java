package com.jmm.csg.pro.manager.adapter;

import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jmm.csg.R;
import com.jmm.csg.base.adapter.BaseRVAdapter;
import com.jmm.csg.bean.IntegralRecord;
import com.jmm.csg.utils.TimeUtils;


public class IntegralRecordAdapter extends BaseRVAdapter<IntegralRecord.Data.Entity> {

    public IntegralRecordAdapter() {
        super(R.layout.item_integeral_record_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, IntegralRecord.Data.Entity item) {
        helper.setText(R.id.tv_integral, "积分扣除: " + item.getIntegralNum());
        helper.setText(R.id.tv_name, item.getProductName());
        helper.setText(R.id.tv_date, "兑换时间: " + TimeUtils.timeToString2(item.getCreateDate()));
        helper.setText(R.id.tv_phone, "手机号: " + item.getPhone());
        TextView tvProgress = helper.getView(R.id.tv_progress);
        String str = "";
        int color = 0;
        switch (item.getFlag()) {
            case "0"://充值中
                str = "努力充值中...";
                color = Color.parseColor("#8ECCCC");
                break;
            case "1"://充值成功
                str = "充值成功,请关注到账通知";
                color = mContext.getResources().getColor(R.color.btn_nor);
                break;
            case "2"://充值失败
                str = "充值失败,积分返还";
                color = mContext.getResources().getColor(R.color.red);
                break;
        }
        tvProgress.setText(" " + str);
        tvProgress.setTextColor(color);

    }
}
