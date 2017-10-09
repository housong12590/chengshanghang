package com.jmm.csg.pro.order.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jmm.csg.R;
import com.jmm.csg.base.adapter.BaseRVAdapter;
import com.jmm.csg.bean.Order;
import com.jmm.csg.callback.OnListItemChildClickListener;
import com.jmm.csg.imgload.ImageLoaderUtils;

public class ReOrderAdapter extends BaseRVAdapter<Order.Entity> {

    private boolean isPer;

    public ReOrderAdapter(boolean isPer) {
        super(R.layout.item_order_layout);
        this.isPer = isPer;
    }

    /**
     * 用户提交退款申请：
     * status：0，flag:1
     * 用户取消：
     * status：0，flag:0
     * 客户经理审核同意：
     * status：1，flag:1
     * 客户经理审核拒绝：
     * status：1，flag:0
     * 平台审核同意：
     * status：2，flag:1
     * 平台审核拒绝：
     * status：2，flag:0
     * 用户填写快递单号：
     * status：3，flag:1
     * 供应商确认收货：
     * status：4，flag:1
     * 供应商拒绝收货：
     * status：4，flag:0
     * 平台确认退款：
     * status：5，flag:1
     * 平台拒绝退款：
     * status：5，flag:0
     * 退款成功：
     * status：6，flag:1
     */
    @Override
    protected void convert(BaseViewHolder helper, Order.Entity item) {
        helper.setText(R.id.tv_order_code, item.getOrdersId());
//        helper.setText(R.id.tv_status, getStatusName(item.getOrderStatus()));
        helper.setText(R.id.tv_name, item.getPro_Name());
        helper.setText(R.id.tv_price, item.getPro_price());
        helper.setText(R.id.tv_count, "X" + item.getQuantity());
//        helper.setText(R.id.tv_charge, "¥ " + item.getShippingcharge());
        helper.setText(R.id.tv_total_price, "¥ " + String.format("%.2f", (Double.parseDouble(item.getPro_price())
                * Integer.parseInt(item.getQuantity()))));
//        helper.setText(R.id.tv_total_price, "¥" + item.getOrderspayprice());
        helper.setText(R.id.tv_pro_coding, item.getPro_Coding());
        helper.setText(R.id.tv_pro_specification, item.getPro_Specification());
        ImageView ivPicture = helper.getView(R.id.iv_picture);
        ImageLoaderUtils.getInstance().loadOSSImage(mContext, item.getPro_pic(), ivPicture);

        helper.addOnClickListener(R.id.ll_detail);
        LinearLayout statusLayout = helper.getView(R.id.ll_status);
        String statusName = "";
        statusLayout.removeAllViews();
        String status = item.getStatus();
        String flag = item.getFlag();
        if (status.equals("0")) {
            if (flag.equals("0")) {//用户取消,退款申请
                statusName = "取消退货";
            } else if (flag.equals("1")) { //客户经理审核
                statusName = isPer ? "待审核" : "待客户经理审核";
                if (isPer) {
                    addView(helper, statusLayout, "取消退货", Style.red);
                } else {
                    addView(helper, statusLayout, "去审核", Style.red);
                }
            }
        } else if (status.equals("1")) {
            if (flag.equals("0")) { // 客户经理审核不通过
                statusName = isPer ? "审核不通过" : "审核不通过";
                if (isPer) {
                    addView(helper, statusLayout, "再次申请", Style.gray);
                    addView(helper, statusLayout, "取消退货", Style.red);
                }
            } else if (flag.equals("1")) { // 待平台审核
                statusName = isPer ? "待审核" : "待平台审核";
                if (isPer) {
                    addView(helper, statusLayout, "取消退货", Style.red);
                }
            }
        } else if (status.equals("2")) {
            if (flag.equals("0")) { //平台审核不通过
                statusName = isPer ? "审核不通过" : "平台审核不通过";
                if (isPer) {
                    addView(helper, statusLayout, "再次申请", Style.red);
                }
            } else if (flag.equals("1")) { //平台审核通过,用户待发货/上传快递单号
                statusName = "待退货";
                if (isPer) {
                    addView(helper, statusLayout, "上传快递单号", Style.border);
                    addView(helper, statusLayout, "取消退货", Style.red);
                }
            }
        } else if (status.equals("3") && flag.equals("1")) { // 用户已发货
            statusName = "待商家收货";
        } else if (status.equals("4")) {
            if (flag.equals("0")) { // 供应商拒绝收货
                statusName = "退货退款失败";
                if (isPer) {
                    addView(helper, statusLayout, "联系客服", Style.red);
                }
            } else if (flag.equals("1")) { //供应商确认收货, 待退款状态
                statusName = "待退款";
            }
        } else if (status.equals("5")) {
            if (flag.equals("0")) { //平台拒绝退款,退款失败
                if (isPer) {
                    addView(helper, statusLayout, "联系客服", Style.red);
                }
                statusName = "退货退款失败";
            } else if (flag.equals("1")) {//平台同意退款
                statusName = "退款中";
            }
        } else if (status.equals("6") && flag.equals("1")) {//退款成功
            statusName = "退货退款成功";
        }


        statusLayout.setVisibility(statusLayout.getChildCount() == 0 ? View.GONE : View.VISIBLE);

        helper.setText(R.id.tv_status, statusName);
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
            case border:
                tv = getBorderTextView(parent);
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

    private TextView getBorderTextView(ViewGroup parent) {
        return (TextView) mLayoutInflater.inflate(R.layout.item_border_text_layout, parent, false);
    }

    enum Style {
        red, gray, small, border
    }
}

