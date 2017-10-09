package com.jmm.csg.utils;

import android.os.Environment;

public class SDCardUtils {

    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

}
