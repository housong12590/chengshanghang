package com.jmm.csg.pro.order.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jmm.csg.R;
import com.jmm.csg.bean.LogisticsBean;
import com.jmm.csg.helper.DialogHelper;
import com.jmm.csg.utils.DensityUtils;
import com.jmm.csg.utils.StringUtils;
import com.jmm.csg.utils.SystemUtils;

import java.util.List;


public class LogisticsAdapter extends BaseQuickAdapter<LogisticsBean.Entity, BaseViewHolder> {

    private FragmentManager fm;
    private AppCompatActivity act;

    public LogisticsAdapter(AppCompatActivity act) {
        super(R.layout.item_logistics_layout);
        this.fm = act.getSupportFragmentManager();
        this.act = act;
    }

    @Override
    protected void convert(BaseViewHolder helper, LogisticsBean.Entity item) {
        View bottomLineView = helper.getView(R.id.bottomLineView);
        View topLineView = helper.getView(R.id.topLineView);
        View dotView = helper.getView(R.id.dotView);
        TextView tvText = helper.getView(R.id.tv_text);
        TextView tvTime = helper.getView(R.id.tv_time);
        topLineView.setVisibility(isFirstItem(helper) ? View.INVISIBLE : View.VISIBLE);
        bottomLineView.setVisibility(isLastItem(helper) ? View.INVISIBLE : View.VISIBLE);
        dotView.setBackgroundResource(isFirstItem(helper) ? R.drawable.shape_oval_blue_bg : R.drawable.shape_oval_gray_bg);
        tvText.setText(item.getContext());
//        String text = item.getContext();
        tvTime.setText(item.getTime());
        if (isFirstItem(helper)) {
            ViewGroup.LayoutParams params = dotView.getLayoutParams();
            params.width = DensityUtils.dip2px(mContext, 15);
            params.height = DensityUtils.dip2px(mContext, 15);
            dotView.setLayoutParams(params);
            tvText.setTextColor(mContext.getResources().getColor(R.color.btn_set));
        } else {
            tvText.setTextColor(mContext.getResources().getColor(R.color.gray_66));
            ViewGroup.LayoutParams params = dotView.getLayoutParams();
            params.width = DensityUtils.dip2px(mContext, 10);
            params.height = DensityUtils.dip2px(mContext, 10);
            dotView.setLayoutParams(params);
        }
//        if (helper.getLayoutPosition() == 1) {
//            text = "我的电话号码是 ";
//            tvText.setText(text);
//        }

        List<String> phoneNumbers = StringUtils.getPhoneNumbers(item.getContext());
        for (String phoneNumber : phoneNumbers) {
            int start = item.getContext().indexOf(phoneNumber);
            int end = start + phoneNumber.length();
            PhoneNumClick clickableSpan = new PhoneNumClick(phoneNumber);
            if (clickableSpan != null) {
                setClickableSpanForTextView(tvText, clickableSpan, item.getContext(), start, end);
            }
        }
    }

    private boolean isLastItem(BaseViewHolder helper) {
        return helper.getLayoutPosition() == getData().size() - 1;
    }

    private boolean isFirstItem(BaseViewHolder helper) {
        return helper.getLayoutPosition() == 0;
    }

    private void setClickableSpanForTextView(TextView tv, ClickableSpan clickableSpan, String text, int start, int end) {
        SpannableString sp = new SpannableString(text);
        sp.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(sp);
        tv.setLinkTextColor(mContext.getResources().getColor(R.color.text_set));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setFocusable(false);
        tv.setClickable(false);
        tv.setLongClickable(false);
    }

    private class PhoneNumClick extends ClickableSpan {

        String text;

        public PhoneNumClick(String text) {
            this.text = text;
        }

        @Override
        public void onClick(View widget) {
            DialogHelper.getActionDialog(mContext, fm,
                    (actionSheet, index) -> SystemUtils.startDial(act, text), text).show();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }
}
