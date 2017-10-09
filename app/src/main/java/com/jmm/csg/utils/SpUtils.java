package com.jmm.csg.utils;

import android.content.SharedPreferences;

public class SpUtils {

    public static void put(SharedPreferences sp, String key, Object obj) {
        SharedPreferences.Editor edit = sp.edit();
        if (obj instanceof String) {

            edit.putString(key, (String) obj);
        } else if (obj instanceof Integer) {
            edit.putInt(key, (Integer) obj);
        } else if (obj instanceof Boolean) {
            edit.putBoolean(key, (Boolean) obj);
        } else if (obj instanceof Long) {
            edit.putLong(key, (Long) obj);
        } else if (obj instanceof Float) {
            edit.putFloat(key, (Float) obj);
        }
        edit.apply();
    }

    public static int get(SharedPreferences sp, String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public static boolean get(SharedPreferences sp, String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public static String get(SharedPreferences sp, String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public static long get(SharedPreferences sp, String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    public static float get(SharedPreferences sp, String key, float defValue) {
        return sp.getFloat(key, defValue);
    }

}
