package com.jmm.csg.helper;

import android.content.SharedPreferences;

import com.jmm.csg.config.AppConfig;
import com.jmm.csg.utils.SpUtils;

/**
 * author：hs
 * date: 2017/5/5 0005 14:38
 */
public class AppDataHelper {

    private static final String APP_PREF_FILE_NAME = "app_data";
    private static final String LOGIN_ONLINE = "login_online";

    private static SharedPreferences getSharedPreferences() {
        return AppConfig.getSharedPreferences(APP_PREF_FILE_NAME);
    }

    public static void setFirstUse(boolean firstUse) {
        SpUtils.put(getSharedPreferences(), "firstUse", firstUse);
    }

    public static boolean isFirstUse() {
        return SpUtils.get(getSharedPreferences(), "firstUse", true);
    }

    public static void setLoginOnline(boolean online) {
        SpUtils.put(getSharedPreferences(), LOGIN_ONLINE, online);
    }

    public static boolean isLoginOnline() {
        return SpUtils.get(getSharedPreferences(), LOGIN_ONLINE, false);
    }

    public static void setPhoneNumber(String phoneNumber) {
        SpUtils.put(getSharedPreferences(), "phoneNumber", phoneNumber);
    }

    public static String getPhoneNumber() {
        return SpUtils.get(getSharedPreferences(), "phoneNumber", "");
    }

    public static void setAppVersionsCode(String code) {
        SpUtils.put(getSharedPreferences(), "versionsCode", code);
    }

    public static String getAppUpdateVersions() {
        return SpUtils.get(getSharedPreferences(), "versionsCode", "1");
    }

    public static void setSkipVersion(String code) {
        SpUtils.put(getSharedPreferences(), "skipVersion", code);
    }

    public static String getAppSkipVersion() {
        return SpUtils.get(getSharedPreferences(), "skipVersion", "");
    }

}
