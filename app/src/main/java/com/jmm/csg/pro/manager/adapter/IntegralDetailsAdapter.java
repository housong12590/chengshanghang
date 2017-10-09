package com.jmm.csg.pro.manager.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jmm.csg.R;
import com.jmm.csg.base.adapter.BaseRVAdapter;
import com.jmm.csg.bean.IntegralDetails;
import com.jmm.csg.utils.TimeUtils;


public class IntegralDetailsAdapter extends BaseRVAdapter<IntegralDetails.Data.Entity> {

    public IntegralDetailsAdapter() {
        super(R.layout.item_integeral_details_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, IntegralDetails.Data.Entity item) {
        View rootView = helper.getView(R.id.root);
        if (helper.getLayoutPosition() % 2 == 0) {
            rootView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            rootView.setBackgroundColor(mContext.getResources().getColor(R.color.layout_bg));
        }
        String name = "";
        String integral = "";
        int color = 0;
        switch (item.getCHANNEL()) {
            case "1":
                integral = "+" + item.getINTEGRAL_NUM();
                name = "销售成功,奖励积分";
                color = getColor(R.color.gray_66);
                break;
            case "2":
                integral = "+" + item.getINTEGRAL_NUM();
                name = "兑换失败,返还积分";
                color = getColor(R.color.gray_66);
                break;
            case "-1":
                integral = item.getINTEGRAL_NUM();
                name = "申请退货,扣除积分";
                color = getColor(R.color.red);
                break;
            case "-2":
                integral = item.getINTEGRAL_NUM();
                name = "兑换成功,扣除积分";
                color = getColor(R.color.red);
                break;
        }

        helper.setText(R.id.tv_name, name);
        TextView tvIntegral = helper.getView(R.id.tv_integral);
        tvIntegral.setText(integral);
        tvIntegral.setTextColor(color);
//        helper.setText(R.id.tv_integral, integral);
        helper.setText(R.id.tv_date, TimeUtils.timeToString2(item.getCREATE_DATE()));
    }

    private int getColor(int resId) {
        return mContext.getResources().getColor(resId);
    }
}
