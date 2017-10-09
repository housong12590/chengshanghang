package com.jmm.csg.pro.manager.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmm.csg.Constant;
import com.jmm.csg.R;
import com.jmm.csg.base.fragment.BaseFragment;
import com.jmm.csg.bean.Account;
import com.jmm.csg.bean.ManagerCenter;
import com.jmm.csg.helper.DialogHelper;
import com.jmm.csg.helper.UserDataHelper;
import com.jmm.csg.http.BaseSubscriber;
import com.jmm.csg.http.HttpModule;
import com.jmm.csg.http.HttpParams;
import com.jmm.csg.imgload.ImageLoaderUtils;
import com.jmm.csg.pro.manager.activity.IntegralExchangeActivity;
import com.jmm.csg.pro.manager.activity.LoginActivity;
import com.jmm.csg.pro.manager.activity.RealNameAuthActivity;
import com.jmm.csg.pro.manager.activity.UserCenterActivity;
import com.jmm.csg.pro.order.activity.OrderListActivity;
import com.jmm.csg.pro.order.activity.SelectAddressActivity;
import com.jmm.csg.pro.setting.CreateGestureActivity;
import com.jmm.csg.pro.setting.SettingActivity;
import com.jmm.csg.utils.SystemUtils;
import com.jmm.csg.widget.CommonDialog;
import com.jmm.csg.widget.SingleButtonDialog;
import com.jmm.csg.widget.TipLayout;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscription;


/**
 * 我的
 */

public class MineFragment extends BaseFragment {

    @Bind(R.id.tip_fk)
    TipLayout tipFk;
    @Bind(R.id.tip_fh)
    TipLayout tipFh;
    @Bind(R.id.tip_sh)
    TipLayout tipSh;
    @Bind(R.id.tip_pj)
    TipLayout tipPj;
    @Bind(R.id.tip_th)
    TipLayout tipTh;
    @Bind(R.id.titleView)
    TitleView titleView;
    @Bind(R.id.ll_sell_order)
    LinearLayout llOrder;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_manage)
    TextView tvManage;
    @Bind(R.id.iv_avatar)
    ImageView mIvAvatar;
    @Bind(R.id.tv_performance)
    TextView mTvPerformance;
    @Bind(R.id.tv_finish_count)
    TextView mTvFinishCount;
    @Bind(R.id.tv_job_Num)
    TextView mTvJobNum;
    @Bind(R.id.tv_new_integral)
    TextView tvNewIntegral;
    @Bind(R.id.tv_total_integral)
    TextView tvTotalIntegral;
    @Bind(R.id.ll_integral) View llIntegral;
    @Bind(R.id.ll_newIntegral) View llNewIntegral;
    @Bind(R.id.ll_integralDivider) View llIntegralDivider;
    private Account account;
    private Subscription subscribe;
    private SingleButtonDialog dialog;
    private CommonDialog.Builder cmDialog;
    private boolean isRealName;
    private boolean isLoad;
    private boolean isChangeAvatar;
    private String flag;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        titleView.setOnRightImgClickListener(view -> {
                    Intent intent = new Intent(getActivity(), SettingActivity.class);
                    intent.putExtra("account", account);
                    startActivityForResult(intent, 100);
                }
        );
    }

    @Override
    protected void initData() {
        isRealName = !UserDataHelper.getRealNameStatus().equals(Constant.REALNAME_NO);
//        checkRealName(UserDataHelper.getRealNameStatus());
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (isRealName) {
        managerCenter();
//        }
    }

    private void managerCenter() {
        subscribe = HttpModule.managerCenter(HttpParams.managerCenter(UserDataHelper.getUserId()))
                .subscribe(new BaseSubscriber<ManagerCenter>() {
                    @Override
                    public void onNext(ManagerCenter managerCenter) {
//                        UserDataHelper.setUserInfo(account);
                        UserDataHelper.setUserName(managerCenter.getManager().getName());
                        UserDataHelper.setUserAvatar(managerCenter.getManager().getAccountManagerPic());
                        UserDataHelper.setBankLogo(managerCenter.getInfo().getLogo());
                        UserDataHelper.setRealNameStatus(managerCenter.getManager().getStatus());
                        setUIData(managerCenter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isLoad) {
                            super.onError(e);
                        }
                    }
                });
    }


    private void setUIData(ManagerCenter data) {
        ManagerCenter.Entity dataInfo = data.getInfo();
        String isIntegral = data.getManager().getIsintegral();
        if (isIntegral.equals("0")) {
            llIntegral.setVisibility(View.VISIBLE);
            llNewIntegral.setVisibility(View.VISIBLE);
            llIntegralDivider.setVisibility(View.VISIBLE);
        } else {
            llIntegral.setVisibility(View.INVISIBLE);
            llNewIntegral.setVisibility(View.GONE);
            llIntegralDivider.setVisibility(View.GONE);
        }

        tipFk.setTip(dataInfo.getPendPay());
        tipFh.setTip(dataInfo.getPendDelivery());
        tipSh.setTip(dataInfo.getPendReceipt());
        tipPj.setTip(dataInfo.getPendEvaluate());
        tipTh.setTip(dataInfo.getCustomerService());
        mTvPerformance.setText(dataInfo.getPerformanceRank());
        mTvFinishCount.setText(dataInfo.getFinish());
        account = data.getManager();
        mTvName.setText(account.getBankName());
        mTvJobNum.setText(account.getName());
        tvNewIntegral.setText(data.getMonthAccount().getIntegral());
        tvTotalIntegral.setText(data.getAccount().getAvailable());
        tvTotalIntegral.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        if (!isLoad || isChangeAvatar) {
            ImageLoaderUtils.getInstance().loadOSSAvatar(getActivity(), account.getAccountManagerPic(), mIvAvatar);
            isChangeAvatar = false;
            isLoad = true;
        }
        checkRealName(data.getManager().getStatus());
    }

    public void checkRealName(String status) {
        if (TextUtils.isEmpty(status)) {
            status = UserDataHelper.getRealNameStatus();
        }
        cmDialog = DialogHelper.getDialog(getActivity())
                .setTitle("温馨提示")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setPositiveButtonText("返回登录", (dialogInterface, i) -> {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                });
        switch (status) {
            case Constant.REALNAME_NO: // 未实名
                cmDialog.setNegativeButtonText("去认证", (dialogInterface, i) ->
                        startActivity(new Intent(getActivity(), RealNameAuthActivity.class)))
                        .setMessage("你还没有进行实名认证,请前去实名认证!");
                break;
            case Constant.REALNAME_ING://待审核
                cmDialog.setNegativeButtonText("重新认证", (dialogInterface, i) ->
                        startActivity(new Intent(getActivity(), RealNameAuthActivity.class)))
                        .setMessage("实名认证正在审核中...");
                break;
            case Constant.REALNAME_YES://通过
                if (!UserDataHelper.isGestureUse()) {
                    Intent intent = new Intent(getActivity(), CreateGestureActivity.class);
                    intent.putExtra("isHasSkip", true);
                    startActivity(intent);
                    UserDataHelper.setGestureUse(true);
                }
                if (cmDialog != null) {
                    cmDialog.dismiss();
                }
                return;
            case Constant.REALNAME_NO_PASS://未通过
                cmDialog.setNegativeButtonText("重新认证", (dialogInterfaced, i) ->
                        startActivity(new Intent(getActivity(), RealNameAuthActivity.class)))
                        .setMessage("请重新提交实名认证!");
                break;
            case Constant.REALNAME_RETURN://驳回
                cmDialog.setNegativeButtonText("重新认证", (dialogInterface, i) ->
                        startActivity(new Intent(getActivity(), RealNameAuthActivity.class)))
                        .setMessage("你的实名认证被驳回,请再次提交实名认证!");
                break;
        }
//        cmDialog.setDismiss(false);
        cmDialog.show();
    }

    @OnClick({R.id.ll_sell_order, R.id.ll_layout, R.id.tip_fk, R.id.tip_fh, R.id.tip_sh
            , R.id.tip_pj, R.id.tip_th, R.id.ll_address, R.id.ll_pers_order, R.id.iv_avatar
            , R.id.ll_service, R.id.ll_integral})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_sell_order://销售订单
                intent = new Intent(getActivity(), OrderListActivity.class);
                intent.putExtra("isPer", false);
                startActivity(intent);
                break;
            case R.id.ll_pers_order://个人订单
                intent = new Intent(getActivity(), OrderListActivity.class);
                intent.putExtra("isPer", true);
                startActivity(intent);
                break;
            case R.id.ll_address://收货地址
                startActivity(new Intent(getActivity(), SelectAddressActivity.class));
                break;
            case R.id.tip_fk:
                startOrderPage(1);//待付款
                break;
            case R.id.tip_fh:
                startOrderPage(2);//待发货
                break;
            case R.id.tip_sh:
                startOrderPage(3);//待收货
                break;
            case R.id.tip_pj:
                startOrderPage(4);//待评价
                break;
            case R.id.tip_th:
                startOrderPage(6);//退换货
                break;
            case R.id.ll_layout://个人信息
            case R.id.iv_avatar://个人资料
                if (account == null) {
                    return;
                }
                intent = new Intent(getActivity(), UserCenterActivity.class);
                intent.putExtra("account", account);
                startActivityForResult(intent, 100);
                break;
            case R.id.ll_service:
                SystemUtils.startDial(getActivity(), "4000406755");
                break;
            case R.id.ll_integral:
                startActivity(new Intent(getActivity(), IntegralExchangeActivity.class));
//                String url = AppConfig.SCORE_URL + UserDataHelper.getUserId();
//                WebViewActivity.start(getActivity(), "积分兑换", url);
//                LogUtils.d("url", url);
                break;
        }
    }

    /**
     *
     */
    private void startOrderPage(int page) {
        Intent intent = new Intent(getActivity(), OrderListActivity.class);
        intent.putExtra("page", page);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscribe != null) {
            if (!subscribe.isUnsubscribed()) {
                subscribe.unsubscribe();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            isChangeAvatar = true;
        }
    }
}
