package com.jmm.csg.pro.order.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jmm.csg.R;
import com.jmm.csg.base.adapter.BaseRVAdapter;
import com.jmm.csg.bean.Order;
import com.jmm.csg.bean.OrderStatus;
import com.jmm.csg.callback.OnListItemChildClickListener;
import com.jmm.csg.imgload.ImageLoaderUtils;

public class OrderAdapter extends BaseRVAdapter<Order.Entity> {

    private boolean isPer;
    private String mCheck_result;
    private String mCheck_status;

    public OrderAdapter(boolean isPer) {
        super(R.layout.item_order_layout);
        this.isPer = isPer;
    }

    @Override
    protected void convert(BaseViewHolder helper, Order.Entity item) {
        mCheck_result = item.getCheck_result();
        mCheck_status = item.getCheck_status();
        helper.setText(R.id.tv_order_code, item.getOrdersId());
//        TextView mTvStatus = helper.getView(R.id.tv_status);
//        String statusName = getStatusName(item.getOrderStatus());
//        SpannableString spannableString = new SpannableString(statusName);
//        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#AE1D08"));
//        if (spannableString.length() > 4 && !statusName.equals("等待买家付款")) {
//            spannableString.setSpan(colorSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        } else if (equalsState("3", "1")) {
//            spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        }
//        mTvStatus.setText(spannableString);
        helper.setText(R.id.tv_status,getStatusName(item.getOrderStatus()));
        helper.setText(R.id.tv_name, item.getPro_Name());
        helper.setText(R.id.tv_price, item.getPro_price());
        helper.setText(R.id.tv_count, "X" + item.getQuantity());
//        helper.setText(R.id.tv_charge, "¥ " + item.getShippingcharge());
        helper.setText(R.id.tv_total_price, "¥ " + item.getOrderspayprice());
        helper.setText(R.id.tv_pro_coding, item.getPro_Coding());
        helper.setText(R.id.tv_pro_specification, item.getPro_Specification());
        ImageView ivPicture = helper.getView(R.id.iv_picture);
        ImageLoaderUtils.getInstance().loadOSSImage(mContext, item.getPro_pic(), ivPicture);
        helper.addOnClickListener(R.id.ll_detail);
        LinearLayout statusLayout = helper.getView(R.id.ll_status);

        statusLayout.removeAllViews();
        OrderStatus status = item.getOrderStatus();
        if (!isPer) {
            if (status == OrderStatus.S) {
                statusLayout.setVisibility(View.VISIBLE);
                addView(helper, statusLayout, "查看物流", Style.gray);
            } else {
                statusLayout.setVisibility(View.GONE);
            }
            return;
        }
//        statusLayout.setVisibility(status == OrderStatus.C ? View.GONE : View.VISIBLE);
        switch (status) {
            case M:
                addView(helper, statusLayout, "取消订单", Style.gray);
                addView(helper, statusLayout, "付款", Style.red);
                break;
            case C:
                if (equalsState("0", "1") || equalsState("1", "1")) {
                    statusLayout.setVisibility(View.VISIBLE);
                    addView(helper, statusLayout, "取消退款", Style.red);
                } else {
                    statusLayout.setVisibility(View.GONE);
                }
                break;
            case S:
                if (equalsState("0", "1") || equalsState("1", "1")) {
                    addView(helper, statusLayout, "取消退款", Style.red);
                }
                addView(helper, statusLayout, "查看物流", Style.gray);
                addView(helper, statusLayout, "确认收货", Style.red);
                break;
            case F:
                addView(helper, statusLayout, "申请售后", Style.gray);
                addView(helper, statusLayout, "评价", Style.red);
                break;
            case X:
                addView(helper, statusLayout, "查看订单", Style.gray);
                break;
            case Q:
                addView(helper, statusLayout, "查看物流", Style.gray);
                addView(helper, statusLayout, "查看订单", Style.gray);
                break;
        }
    }

    private void addView(BaseViewHolder helper, ViewGroup parent, String text, Style style) {
        TextView tv = null;
        switch (style) {
            case red:
                tv = getRedTextView(parent);
                break;
            case gray:
                tv = getGrayTextView(parent);
                break;
            case small:
                tv = getSmallTextView(parent);
                break;
        }
        tv.setText(text);
        parent.addView(tv, parent.getChildCount());
        final TextView finalTv = tv;
        tv.setOnClickListener(v -> listener.onChildClick(finalTv, helper.getAdapterPosition()));
    }

    private OnListItemChildClickListener listener;

    public void setOnItemChildClickListener(OnListItemChildClickListener listener) {
        this.listener = listener;
    }

    private TextView getRedTextView(ViewGroup parent) {
        return (TextView) mLayoutInflater.inflate(R.layout.item_red_text_layout, parent, false);
    }

    private TextView getGrayTextView(ViewGroup parent) {
        return (TextView) mLayoutInflater.inflate(R.layout.item_gray_text_layout, parent, false);
    }

    private TextView getSmallTextView(ViewGroup parent) {
        return (TextView) mLayoutInflater.inflate(R.layout.item_small_text_layout, parent, false);
    }

    enum Style {
        red, gray, small
    }

    private SpannableString getStatusName(OrderStatus orderStatus) {
        switch (orderStatus) {
            case M:
                return new SpannableString("等待买家付款");
            case C:
//                if (equalsState("0", "1") || equalsState("1", "1")) {
//                    return "买家已付款 (审核中)";
//                } else if (equalsState("", "") || equalsState("0", "0")) {
//                    return "买家已付款";
//                } else if (equalsState("3", "1")) {
//                    return "买家已付款 (退款成功)";
//                } else if (equalsState("2", "1")) {
//                    return "买家已付款 (退款中)";
//                } else {
//                    return "买家已付款 (退款失败)";
//                }
                return disposeState("买家已付款");
            case S:
//                if (equalsState("0", "1") || equalsState("1", "1")) {
//                    return "已发货 (审核中)";
//                } else if (equalsState("", "") || equalsState("0", "0")) {
//                    return "已发货";
//                } else if (equalsState("3", "1")) {
//                    return "已发货 (退款成功)";
//                } else if (equalsState("2", "1")) {
//                    return "已发货 (退款中)";
//                } else {
//                    return "已发货 (退款失败)";
//                }
                return disposeState("已发货");
            case F:
                return new SpannableString("交易成功");
            case Q:
                return new SpannableString("已完成");
            case X:
                return disposeState("订单关闭");
        }
        return null;
    }

    private boolean equalsState(String status, String result) {
        return mCheck_status.equals(status) && mCheck_result.equals(result);
    }

    private SpannableString disposeState(String s) {
        int length = s.length();
        if (equalsState("0", "1") || equalsState("1", "1")) {
            s = s + " (审核中)";
        } else if (equalsState("", "") || equalsState("0", "0")) {
            s = s;
        } else if (equalsState("3", "1")) {
            s = "退款成功";
            length = 0;
        } else if (equalsState("2", "1")) {
            s = s + " (退款中)";
        } else {
            s = s + " (退款失败)";
        }
        SpannableString spannableString = new SpannableString(s);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#AE1D08"));
        spannableString.setSpan(colorSpan, length, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}

