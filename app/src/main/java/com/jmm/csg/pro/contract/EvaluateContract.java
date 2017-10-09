package com.jmm.csg.pro.contract;


import java.util.Map;

public interface EvaluateContract {

    interface V extends BaseContract.View {
        void onSuccess();
    }

    interface P extends BaseContract.Presenter<V> {

        void commitComment(Map<String, String> params);
    }
}
