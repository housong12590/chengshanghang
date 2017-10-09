package com.jmm.csg.pro.order.activity;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.XActivity;
import com.jmm.csg.bean.AddressInfo;
import com.jmm.csg.bean.ProductDetail;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.imgload.ImageLoaderUtils;
import com.jmm.csg.pro.contract.CommitOrderContract;
import com.jmm.csg.pro.presenter.CommitOrderPresenter;
import com.jmm.csg.utils.ToastUtils;
import com.jmm.csg.widget.TitleView;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.OnClick;

public class CommitOrderActivity extends XActivity<CommitOrderPresenter> implements CommitOrderContract.V {

    private static final int SELECT_ADDRESS_CODE = 100;
    private static final int INVOICE_INFO_CDOE = 101;

    @Bind(R.id.titleView)
    TitleView mTitleView;
    @Bind(R.id.iv_add)
    ImageView mIvAdd;
    @Bind(R.id.tv_count)
    TextView mEtCount;
    @Bind(R.id.iv_subtract)
    ImageView mIvSubtract;
    @Bind(R.id.ll_address)
    LinearLayout mLlAddress;
    @Bind(R.id.tv_total)
    TextView mTvTotalPrice;
    @Bind(R.id.tv_price)
    TextView mTvPrice;
    @Bind(R.id.tv_commit_order)
    TextView mTvSubmitOrder;
    @Bind(R.id.tv_address)
    TextView mTvAddress;
    @Bind(R.id.tv_consignee)
    TextView mTvConsignee;
    @Bind(R.id.tv_phone_num)
    TextView mTvPhoneNum;
    @Bind(R.id.tv_invoice_info)
    TextView mTvInvoiceInfo;
    @Bind(R.id.et_remark)
    EditText mEtRemark;
    @Bind(R.id.tv_pro_name)
    TextView mTvProName;
    @Bind(R.id.tv_manage_name)
    TextView mTvManageName;
    @Bind(R.id.iv_pro_pic)
    ImageView mIvProPic;
    private String productId;
    private String missionId;
    private AddressInfo.Entity addressEntity;
    private String titleType = "P"; //P 个人,S 企业
    private String invoiceType = "N"; //P 纸质 ,E电子 , N不开发票
    private String pickWay = "S"; //A自提 S 配送
    private String invoiceInfo = "";
    private String taxIdentNum = "";
    private ProductDetail.Entity product;
    private int bankNum = 0;
    private String flag;

    @Override
    public void parseIntent(Intent intent) {
        productId = intent.getStringExtra("productId");
        missionId = intent.getStringExtra("missionId");
        flag = intent.getStringExtra("flag");
        product = (ProductDetail.Entity) intent.getSerializableExtra("product");
    }

    @Override
    public int getLayoutId() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_commit_order;
    }


    @Override
    public void initView() {
        mTitleView.setOnLeftImgClickListener(view -> finish());
        mTvProName.setText(product.getPro_Name());
        mTvPrice.setText(product.getPro_price());
        mTvTotalPrice.setText(product.getPro_price());
        mTvManageName.setText(UserDataHelper.getUserName());
        mTvConsignee.getPaint().setFakeBoldText(true);
        mTvPhoneNum.getPaint().setFakeBoldText(true);
        ImageLoaderUtils.getInstance().loadOSSImage(this, product.getPro_pic(), mIvProPic);
    }


    @Override
    public CommitOrderPresenter newPresenter() {
        return new CommitOrderPresenter();
    }


    @Override
    public void initData() {
        String userId = UserDataHelper.getUserId();
        getP().getDefaultAddress(userId);
        if (flag.equals("product")) {
            getP().productList(userId, product.getPro_Coding());
        } else if (flag.equals("mission")) {
            getP().missionList(userId, product.getPro_Coding());
        }
    }

    @Override
    public void defaultAddress(AddressInfo.Entity entity) {
        addressEntity = entity;
        if (entity != null) {
            mTvConsignee.setVisibility(View.VISIBLE);
            mTvPhoneNum.setVisibility(View.VISIBLE);
            mTvConsignee.setText("收货人: " + entity.getConsignee());
            mTvAddress.setText(entity.getConsigneeAddress());
            mTvPhoneNum.setText(entity.getPhoneNumber());
        } else {
            mTvConsignee.setVisibility(View.GONE);
            mTvPhoneNum.setVisibility(View.GONE);
            mTvAddress.setText("您的收货地址为空,点此添加收货地址");
        }
    }

    @Override
    public void commitOrderSuccess(String orderId) {
        String price = mTvTotalPrice.getText().toString();
//        RxBus.getDefault().post(Constant.REFRESH_MAIN_DATA, LoadStatus.REFRESH);
        SelectPayActivity.start(this, orderId, product.getPro_Name(), product.getPro_Specification(), price, 1001);
    }

    @Override
    public void productListSuccess(String bankNum) {
        this.bankNum = Integer.parseInt(bankNum);
    }

    @Override
    public void missionListSuccess(String bankNum) {
        this.bankNum = Integer.parseInt(bankNum);
    }


    @OnClick({R.id.ll_address, R.id.ll_invoice, R.id.tv_commit_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_address:
                if (addressEntity == null) {
                    startActivityForResult(new Intent(this, NewAddressActivity.class)
                            .putExtra("isFromOrder", true), SELECT_ADDRESS_CODE);
                } else {
                    String addressId = addressEntity.getAddressId();
                    SelectAddressActivity.start(this, true, SELECT_ADDRESS_CODE, addressId);
                }
                break;
            case R.id.ll_invoice:
                Intent intent = new Intent(this, InvoiceInfoActivity.class);
                intent.putExtra("content", invoiceInfo);
                intent.putExtra("taxIdentNum", taxIdentNum);
                intent.putExtra("titleType", titleType);
                intent.putExtra("invoiceType", invoiceType);
                startActivityForResult(intent, INVOICE_INFO_CDOE);
                break;
            case R.id.tv_commit_order:
                if (addressEntity == null) {
                    ToastUtils.showMsg("请选择收货地址");
                    return;
                }
                String userId = UserDataHelper.getUserId();
                String count = mEtCount.getText().toString();
                if (Integer.parseInt(count) > bankNum) {
                    ToastUtils.showMsg("库存数量不足");
                    return;
                }
                String remark = mEtRemark.getText().toString();
                String addressInfo = addressEntity.getConsignee() + "-" + addressEntity.getPhoneNumber()
                        + "-" + addressEntity.getConsigneeAddress();
                getP().commitOrder(userId, productId, addressInfo, pickWay, count, userId, remark,
                        "", invoiceType, titleType, missionId, invoiceInfo, taxIdentNum.toUpperCase());
                break;
        }
    }

    @OnClick({R.id.iv_subtract, R.id.iv_add})
    public void function(View view) {
        int count = Integer.parseInt(mEtCount.getText().toString());
        switch (view.getId()) {
            case R.id.iv_subtract:
                if (count <= 1) {
                    return;
                }
                count--;
                break;
            case R.id.iv_add:
                count++;
                if (count > bankNum) {
                    ToastUtils.showMsg("库存数量不足");
                    return;
                }
                break;
        }
        mEtCount.setText(String.valueOf(count));
        Double price = Double.parseDouble(mTvPrice.getText().toString());
        BigDecimal b = new BigDecimal(price * count);
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        String format = String.format("%.2f", f1);
        mTvTotalPrice.setText(format);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_ADDRESS_CODE:
                    AddressInfo.Entity entity = (AddressInfo.Entity) data.getSerializableExtra("addressInfo");
                    if (entity != null) {
                        defaultAddress(entity);
                        addressEntity = entity;
                    }
                    if (data.getBooleanExtra("isChanged", false)) {
                        getP().getDefaultAddress(UserDataHelper.getUserId());
                    }
                    break;
                case INVOICE_INFO_CDOE:
                    titleType = data.getStringExtra("titleType");
                    invoiceType = data.getStringExtra("invoiceType");
                    invoiceInfo = data.getStringExtra("companyName");
                    taxIdentNum = data.getStringExtra("taxIdentNum");
//                    String s = (invoiceType.equals("P") ? "纸质" : "电子") + " - " + (titleType.equals("P") ? "个人" : "企业");
                    String s = invoiceType.equals("N") ? "不开发票" : "明细 - " + (titleType.equals("P") ? "个人" : invoiceInfo);
                    if (invoiceType.equals("N")) {
                        titleType = "";
                        invoiceInfo = "";
                    }
                    mTvInvoiceInfo.setText(s);
                    break;
                case 1001:
                    setResult(RESULT_OK, data);
                    int flag = data.getIntExtra("flag", -1);
                    System.out.println("flag--->" + flag);
                    finish();
                    break;
            }
        }
    }
}
