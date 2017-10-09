package com.jmm.csg.pro.setting;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.bean.ShareModel;
import com.jmm.csg.widget.ShareDialog;
import com.jmm.csg.widget.TitleView;
import com.mob.commons.SHARESDK;

import butterknife.Bind;
import butterknife.OnClick;

public class TestActivity extends BaseActivity {

    @Bind(R.id.titleView) TitleView mTitleView;
    private ShareDialog dialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView() {
        SHARESDK.setAppKey("1dcbdb771f4ac");
        ShareModel model = new ShareModel("sdf", "sdfsdf", "www.baudi.com", "", null);
        dialog = new ShareDialog(this, model);
    }

    @Override
    public void initData() {

    }


    @OnClick(R.id.button)
    public void onClick() {
        dialog.show();
    }

}
