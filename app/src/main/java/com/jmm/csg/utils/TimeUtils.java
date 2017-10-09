package com.jmm.csg.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeUtils {

    public static String timeToString(String time) {
        long parseLong = 1;
        if (!TextUtils.isEmpty(time) && TextUtils.isDigitsOnly(time)) {
            parseLong = Long.parseLong(time) * 1000;
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(parseLong));
        }
        return time;
    }

    public static String timeToString2(String time) {
        long parseLong = 1;
        if (!TextUtils.isEmpty(time) && TextUtils.isDigitsOnly(time)) {
            parseLong = Long.parseLong(time) * 1000;
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(parseLong));
        }
        return time;
    }

    public static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }
}
