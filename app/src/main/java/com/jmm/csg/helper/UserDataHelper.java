package com.jmm.csg.helper;

import android.content.SharedPreferences;

import com.jmm.csg.config.AppConfig;
import com.jmm.csg.bean.Account;
import com.jmm.csg.utils.SpUtils;

/**
 * author：hs
 * date: 2017/5/5 0005 15:08
 */
public class UserDataHelper {

    private static SharedPreferences getSharedPreferences() {
        return AppConfig.getSharedPreferences(AppDataHelper.getPhoneNumber());
    }

    public static String getUserName() {
        return SpUtils.get(getSharedPreferences(), "user_name", "");
    }

    public static void setUserName(String name) {
        SpUtils.put(getSharedPreferences(), "user_name", name);
    }

    public static void setGestureUnlock(boolean isOpen) {
        SpUtils.put(getSharedPreferences(), "gesture_unlock", isOpen);
    }

    public static boolean isGestureUnlock() {
        return SpUtils.get(getSharedPreferences(), "gesture_unlock", false);
    }

    public static void setGesturePassword(String password) {
        SpUtils.put(getSharedPreferences(), "gesture_pwd", password);
    }

    public static String getGesturePassword() {
        return SpUtils.get(getSharedPreferences(), "gesture_pwd", "");
    }

    public static String getBankLogo() {
        return SpUtils.get(getSharedPreferences(), "bank_logo", "");
    }

    public static void setBankLogo(String bankLogo) {
        SpUtils.put(getSharedPreferences(), "bank_logo", bankLogo);
    }

    public static void setRealNameStatus(String status) {
        SpUtils.put(getSharedPreferences(), "real_name_status", status);
    }

    public static String getRealNameStatus() {
        return SpUtils.get(getSharedPreferences(), "real_name_status", "0");
    }

    public static String getUserId() {
        return SpUtils.get(getSharedPreferences(), "user_id", "");
    }

    public static void setUserId(String userId) {
        SpUtils.put(getSharedPreferences(), "user_id", userId);
    }

    public static void setGestureUse(boolean use) {
        SpUtils.put(getSharedPreferences(), "gesture_use", use);
    }

    public static boolean isGestureUse() {
        return SpUtils.get(getSharedPreferences(), "gesture_use", false);
    }

    public static void setUserAvatar(String url) {
        SpUtils.put(getSharedPreferences(), "avatar_url", url);
    }

    public static String getUserAvatar() {
        return SpUtils.get(getSharedPreferences(), "avatar_url", "");
    }

    public static void setUserInfo(Account account) {
        setObject("userInfo", account);
    }

    public static Account getUserInfo() {
        return getObject("userInfo", Account.class);
    }

    public static <T> T getObject(String key, Class<T> clazz) {
        String json = SpUtils.get(getSharedPreferences(), key, "");
        return AppConfig.gson.fromJson(json, clazz);
    }

    public static <T> void setObject(String key, T t) {
        String json = AppConfig.gson.toJson(t);
        SpUtils.put(getSharedPreferences(), key, json);
    }
}
