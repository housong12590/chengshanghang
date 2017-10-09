package com.jmm.csg.pro.order.adapter;

import android.text.TextUtils;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jmm.csg.R;
import com.jmm.csg.bean.AddressInfo;
import com.jmm.csg.utils.StringUtils;

import java.util.List;


public class AddressAdapter extends BaseQuickAdapter<AddressInfo.Entity, BaseViewHolder> {

    public AddressAdapter(List<AddressInfo.Entity> data) {
        super(R.layout.item_address_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressInfo.Entity item) {
//        ImageView defAddress = helper.getView(R.id.cb_def_address);
//        defAddress.setImageResource(R.drawable.icon_check_nor);
        RadioButton rbDefAddress = helper.getView(R.id.rb_def_address);
        boolean isPrimary = item.getIsPrimary().equals("1");
        rbDefAddress.setSelected(isPrimary);
//        ivDefAddress.setImageResource(isPrimary ? R.drawable.icon_check_set : R.drawable.icon_check_nor);
        helper.setText(R.id.tv_consignee, item.getConsignee());
        helper.setText(R.id.tv_phone_num, item.getPhoneNumber());
        String city = item.getCity();
        if (TextUtils.isEmpty(city)) {
            city = "";
        }
        String address = StringUtils.replaceBlank(item.getProvince() + city + item.getDistrict() + item.getAddress());
        helper.setText(R.id.tv_address, "收货地址 : " + address);

        helper.addOnClickListener(R.id.rb_def_address)
                .addOnClickListener(R.id.tv_edit)
                .addOnClickListener(R.id.tv_delete);
    }
}
