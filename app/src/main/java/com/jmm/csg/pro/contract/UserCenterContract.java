package com.jmm.csg.pro.contract;

import com.jmm.csg.bean.Bank;
import com.jmm.csg.bean.UploadBean;

import java.util.List;
import java.util.Map;

import rx.Observable;

public interface UserCenterContract {

    interface V extends BaseContract.View {
        void onSuccess();
    }

    interface P extends BaseContract.Presenter<V> {

        void changeAvatar();

        Observable<Bank> getBanks();

        Observable<List<Bank.BranchBank>> getBranchBanks(String bankId, String province, String city, String district);

        void commitRealName(Map<String, String> params, List<UploadBean> list);

        void modifyMgrPhone(String id,String phone);

    }
}
