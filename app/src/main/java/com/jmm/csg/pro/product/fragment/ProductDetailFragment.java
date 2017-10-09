package com.jmm.csg.pro.product.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jmm.csg.R;
import com.jmm.csg.base.fragment.BaseFragment;
import com.jmm.csg.bean.ProductDetail;
import com.jmm.csg.bean.ShareModel;
import com.jmm.csg.config.AppConfig;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.http.ProgressSubscriber;
import com.jmm.csg.imgload.ImageLoaderUtils;
import com.jmm.csg.pro.order.activity.CommitOrderActivity;
import com.jmm.csg.pro.product.activity.QRCodePreviewActivity;
import com.jmm.csg.utils.DensityUtils;
import com.jmm.csg.utils.OSSUtils;
import com.jmm.csg.utils.StringUtils;
import com.jmm.csg.utils.SystemUtils;
import com.jmm.csg.widget.AutoViewPager;
import com.jmm.csg.widget.ShareDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class ProductDetailFragment extends BaseFragment implements AutoViewPager.OnItemClickListener {

    @Bind(R.id.tv_QRCode)
    TextView mTvQRCode;
    @Bind(R.id.tv_empty)
    TextView mTvEmpty;
    @Bind(R.id.viewPager)
    AutoViewPager viewPager;
    @Bind(R.id.tv_pro_name)
    TextView mTvProName;
    @Bind(R.id.tv_pro_desc)
    TextView mTvProDesc;
    @Bind(R.id.tv_price)
    TextView mTvPrice;
    @Bind(R.id.tv_pro_specification)
    TextView mTvProSpecification;
    @Bind(R.id.tv_pro_specification_id)
    TextView mTvProSpecificationId;
    @Bind(R.id.tv_pro_circulation)
    TextView mTvProCirculation;
    @Bind(R.id.tv_pro_band)
    TextView mTvProBand;
    @Bind(R.id.tv_pro_cast)
    TextView mTvProCast;
    @Bind(R.id.iv_bank)
    ImageView mIvBank;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.tv_progress)
    TextView mTvprogress;
    @Bind(R.id.v_progress)
    View vProgress;
    @Bind(R.id.ll_progress)
    View llProgress;
    @Bind(R.id.tv_as)
    TextView mTvas;
    @Bind(R.id.rl_as)
    LinearLayout mRlas;
    @Bind(R.id.tv_provider)
    TextView tvProvider;
    @Bind(R.id.ll_logo) View mLlLogo;
    private String productId;
    private String proPic;
    private String missionId;
    private ProductDetail detail;
    private String missionNum;
    private String finishNum;
    private String productnum;
    private String flag;

    @Override
    protected void parseIntent(Bundle bundle) {
        productId = bundle.getString("productId");
        proPic = bundle.getString("proPic");
        missionId = bundle.getString("missionId");
        missionNum = bundle.getString("missionNum");
        finishNum = bundle.getString("finishNum");
        productnum = bundle.getString("productnum");
        flag = bundle.getString("flag");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_commodity_detail;
    }

    @Override
    protected void initView() {
        int screenWidth = DensityUtils.getScreenWidth(getActivity());
        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
        layoutParams.height = screenWidth;
        viewPager.setLayoutParams(layoutParams);
        if (flag.equals("product")) {
            llProgress.setVisibility(View.GONE);
            mTvas.setText(productnum);

        } else if (flag.equals("mission")) {
            mRlas.setVisibility(View.GONE);
            mTvprogress.setText(finishNum + "/" + missionNum);
            mProgressBar.setMax(Integer.parseInt(missionNum));
            mProgressBar.setProgress(Integer.parseInt(finishNum));
        }
    }

    @OnClick({R.id.tv_close_account, R.id.tv_QRCode, R.id.tv_share})
    public void onClick(View view) {
        Intent intent;
        if (detail == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_close_account:
                List<ProductDetail.Entity> product = detail.getProduct();
                String proImage = null;
                if (product != null) {
                    if (product.size() != 0) {
                        List<ProductDetail.Imgs> proImgs = detail.getProImgs();
                        if (proImgs.size() != 0) {
                            ProductDetail.Imgs imgs = proImgs.get(0);
                            proImage = imgs.getPro_pic();
                        }
                        ProductDetail.Entity entity = product.get(0);
                        entity.setPro_pic(proImage);
                        intent = new Intent(getActivity(), CommitOrderActivity.class);
                        intent.putExtra("productId", productId);
                        intent.putExtra("missionId", missionId);
                        intent.putExtra("product", entity);
                        intent.putExtra("flag", flag);
                        startActivityForResult(intent, 1001);
                    }
                }
                break;
            case R.id.tv_QRCode:
                intent = new Intent(getActivity(), QRCodePreviewActivity.class);
                intent.putExtra("coding", detail.getProduct().get(0).getPro_Coding());
                intent.putExtra("productId", productId);
                intent.putExtra("accountManagerId", UserDataHelper.getUserId());
                intent.putExtra("missionId", missionId);
                intent.putExtra("proPic", proPic);
                SystemUtils.startTransitionActivity(getActivity(), intent,
                        mTvEmpty, QRCodePreviewActivity.TRANSIT_PIC);
                break;
            case R.id.tv_share://分享
                share();
                break;
        }
    }

    private void share() {
        ShareModel shareModel = new ShareModel();
        List<ProductDetail.Entity> product = detail.getProduct();
        ProductDetail.Entity entity = product.get(0);
        String proName = entity.getPro_Name();
        String proContent = StringUtils.replaceBlank(entity.getPro_description());
        shareModel.setTitle(proName);
        shareModel.setContent(proContent);
        if (detail.getProImgs().size() == 0) {
            return;
        }
        shareModel.setImageUrl(OSSUtils.BASE_URL + detail.getProImgs().get(0).getPro_pic());
        Map<String, String> map = new HashMap<>();
        map.put("amId", UserDataHelper.getUserId());
        map.put("coding", entity.getPro_Coding());
        String s = StringUtils.mapToString(map);
        String url = AppConfig.BASE_SHARE_URL + s;
        shareModel.setUrl(url);
        String proCoding = entity.getPro_Coding();
        shareModel.setCoding(proCoding);
        new ShareDialog(getActivity(), shareModel).show();
    }

    @Override
    protected void initData() {
        String userId = UserDataHelper.getUserId();

        HttpModule.getProDetailForApp(HttpParams.getProDetailForApp(userId, productId))
                .subscribe(new ProgressSubscriber<ProductDetail>(getActivity()) {
                    @Override
                    public void onNext(ProductDetail detail) {
                        ProductDetailFragment.this.detail = detail;
                        setUiData(detail);
                    }
                });
//        HttpModule.proDetail(HttpParams.proDetail(productId, userId))
//                .subscribe(new ProgressSubscriber<ProductDetail>(getActivity()) {
//                    @Override
//                    public void onNext(ProductDetail detail) {
//                        ProductDetailFragment.this.detail = detail;
//                        setUiData(detail);
//                    }
//                });
    }

    private void setUiData(ProductDetail detail) {
        List<ProductDetail.Imgs> proImgs = detail.getProImgs();
        List<String> imageList = new ArrayList<>();
        for (ProductDetail.Imgs proImg : proImgs) {
            imageList.add(OSSUtils.BASE_URL + proImg.getPro_pic());
        }
        List<ProductDetail.Entity> entityList = detail.getProduct();
        ProductDetail.Entity product = entityList.get(0);
        if (product == null) {
            return;
        }
        viewPager.setImages(imageList);
        viewPager.setOnItemClickListener(this);
        mTvProName.setText(product.getPro_Name());
        tvProvider.setText(product.getSuName());
        mTvProDesc.setText(StringUtils.replaceBlank(product.getPro_description()));
        mTvPrice.setText(product.getPro_price());
        mTvProSpecification.setText(product.getPro_Specification());
        mTvProSpecificationId.setText(product.getPro_Coding());
        mTvProCirculation.setText(product.getPro_Circulation());
        mTvProBand.setText(product.getPro_brand());
        mTvProCast.setText("null");
        if (detail.getLogo().size() != 0) {
            String logo = detail.getLogo().get(0).getLogo();
            ImageLoaderUtils.getInstance().loadOSSImage(getActivity(), logo, mIvBank);
        } else {
            mLlLogo.setVisibility(View.GONE);
        }
    }

    @Override
    public void itemClick(View view, Object o, int position) {
//        ImagePreviewActivity.start(getActivity(), view, (String) o);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish();
        }
    }

}
