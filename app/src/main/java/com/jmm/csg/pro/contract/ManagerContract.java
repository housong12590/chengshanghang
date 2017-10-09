package com.jmm.csg.pro.contract;

import com.jmm.csg.bean.ManagerCenter;

public interface ManagerContract {

    interface V extends BaseContract.View {
        void getManagerInfoSuccess(ManagerCenter managerCenter);
    }

    interface P extends BaseContract.Presenter<V> {

        void managerCenter(String userId);
    }
}
