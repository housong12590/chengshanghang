package com.jmm.csg.pro.order.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jmm.csg.Constant;
import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.bean.AddressInfo;
import com.jmm.csg.bean.BaseResp;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.http.ProgressSubscriber;
import com.jmm.csg.utils.ToastUtils;
import com.jmm.csg.widget.CitySelectDialog;
import com.jmm.csg.widget.ContainsEmojiEditText;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

import static com.jmm.csg.R.id.cb_primary;

public class NewAddressActivity extends BaseActivity {

    @Bind(R.id.titleView) TitleView mTitleView;
    @Bind(R.id.tv_address) TextView mTvAddress;
    @Bind(R.id.et_consignee) EditText mEtConsignee;
    @Bind(R.id.et_phone_num) EditText mEtPhoneNum;
    @Bind(R.id.tv_save) TextView mTvSave;
    @Bind(R.id.et_detail_address) ContainsEmojiEditText mEtDetailAddress;
    @Bind(cb_primary) CheckBox mCbPrimary;

    private String tempAddress;
    private AddressInfo.Entity entity;
    private String addressId = "";
    private boolean isFromOrder;
    private boolean isShowDef;

    @Override
    public void parseIntent(Intent intent) {
        entity = (AddressInfo.Entity) intent.getSerializableExtra("addressInfo");
        isFromOrder = intent.getBooleanExtra("isFromOrder", false);
        isShowDef = intent.getBooleanExtra("isShowDef", false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_address;
    }

    @Override
    public void initView() {
        mTitleView.setOnLeftImgClickListener(view -> finish());
        Observable<CharSequence> obsConsignee = RxTextView.textChanges(mEtConsignee);
        Observable<CharSequence> obsPhoneNum = RxTextView.textChanges(mEtPhoneNum);
        Observable<CharSequence> obsAddress = RxTextView.textChanges(mTvAddress);
        Observable<CharSequence> obsDetailAddress = RxTextView.textChanges(mEtDetailAddress);
        Observable.combineLatest(obsConsignee, obsPhoneNum, obsAddress, obsDetailAddress,
                (s1, s2, s3, s4) -> s1.length() != 0 && s2.length() != 0 && s3.length() != 0 && s4.length() != 0)
                .subscribe(aBoolean -> mTvSave.setEnabled(aBoolean));
        mTvSave.setText(isFromOrder ? "保存并使用" : "保存");
//        mCbPrimary.setVisibility(isFromOrder ? View.INVISIBLE : isShowDef ? View.VISIBLE : View.INVISIBLE);

        if (entity == null) {
            return;
        }
        mTitleView.setTitle("编辑收货地址");
        tempAddress = "/" + entity.getProvince() + "/" + entity.getCity() + "/" + entity.getDistrict();
        mTvAddress.setText(tempAddress.replaceAll("/", ""));
        mEtConsignee.setText(entity.getConsignee());
        mEtPhoneNum.setText(entity.getPhoneNumber());
        mCbPrimary.setChecked(entity.getIsPrimary().equals("1"));
        mEtDetailAddress.setText(entity.getAddress());
        addressId = entity.getAddressId();

    }


    @OnClick(R.id.tv_save)
    public void commit() {
        String consignee = mEtConsignee.getText().toString().trim();
        String phoneNum = mEtPhoneNum.getText().toString().trim();
        String detailAddress = mEtDetailAddress.getText().toString().trim();
        if (!phoneNum.matches(Constant.PHONE_CHECK2)) {
            ToastUtils.showMsg("请输入正确的电话号码");
            return;
        }
        if (checkParams(consignee, phoneNum, tempAddress, detailAddress)) {
            String isPrimary = mCbPrimary.isChecked() ? "1" : "0";
            String[] split = tempAddress.split("/");

            HttpModule.saveNewAddress(HttpParams.saveNewAddress(UserDataHelper.getUserId(), addressId, consignee,
                    phoneNum, isPrimary, detailAddress, split[1], split[2], split[3]))
                    .subscribe(new ProgressSubscriber<BaseResp>(this) {
                        @Override
                        public void onNext(BaseResp resp) {
                            if (resp.getStatus().equals("0")) {
                                AddressInfo.Entity entity = new AddressInfo.Entity(split[3], phoneNum, split[1],
                                        detailAddress, consignee, split[2]);
                                entity.setADDRESS_ID(addressId);
                                Intent intent = new Intent();
                                intent.putExtra("addressInfo", entity);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    });
        }
    }

    @OnClick(R.id.tv_address)
    public void selectAddress() {
        CitySelectDialog dialog = new CitySelectDialog(this);
        dialog.setonResultListener(str -> {
            tempAddress = str;
            mTvAddress.setText(str.replaceAll("/", ""));
        });
        dialog.show();
    }


    private boolean checkParams(String consignee, String phoneNum, String newAddress, String detailAddress) {
        if (TextUtils.isEmpty(consignee)) {
            ToastUtils.showMsg("收货人不能为空");
            return false;
        }
        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtils.showMsg("联系电话不能为空");
            return false;
        }
        if (TextUtils.isEmpty(newAddress)) {
            ToastUtils.showMsg("所在地区不能为空");
            return false;
        }
        if (TextUtils.isEmpty(detailAddress)) {
            ToastUtils.showMsg("详细地址不能为空");
            return false;
        }
        return true;
    }
}
