//package com.jmm.csg.pro.product.adapter;
//
//import android.widget.ImageView;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.jmm.csg.Constant;
//import com.jmm.csg.R;
//import com.jmm.csg.bean.ProductReview;
//import com.jmm.csg.imgload.ImageLoaderUtils;
//
//import java.util.List;
//
//public class EvaluateAdapter extends BaseQuickAdapter<ProductReview.DataBean.ReviewBean, BaseViewHolder> {
//
//    public EvaluateAdapter(List<ProductReview.DataBean.ReviewBean> data) {
//        super(R.layout.item_evaluate_layout, data);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, ProductReview.DataBean.ReviewBean item) {
//        helper.setText(R.id.tv_content, item.getR_content());
//        helper.setText(R.id.tv_name, getSimpleName(item.getU_name()) + "(" + (item.getR_status().equals("P") ? "好评" : "差评") + ")");
//        helper.setText(R.id.tv_create_time, item.getR_date());
//        helper.setText(R.id.tv_specification, "规格 : " + item.getSpecification());
//        ImageView avatar = helper.getView(R.id.iv_avatar);
//        ImageLoaderUtils.getInstance().loadOSSAvatar(mContext, item.getU_sfid(), avatar);
//    }
//
//    private String getSimpleName(String text) {
//        if (text.matches(Constant.PHONE_CHECK)) {
//            String start = text.substring(0, 3);
//            String end = text.substring(text.length() - 4, text.length());
//            return start + "****" + end;
//        } else {
//            return text;
//        }
//
//        //        String tempStr = text.substring(1, text.length() - 1);
//
//    }
//}
