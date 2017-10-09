package com.jmm.csg.rxbus;

public @interface Subscribe {

    int code() default -1;
    ThreadMode threadMode() default ThreadMode.CURRENT_THREAD;
    boolean sticky() default false;
}
