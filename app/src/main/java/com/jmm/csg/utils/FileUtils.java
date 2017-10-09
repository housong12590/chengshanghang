package com.jmm.csg.utils;

import java.io.File;

public class FileUtils {

    public static File createDirectory(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static void createDirectory(String... path) {
        for (String p : path) {
            File file = new File(p);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

//    public static File createDirectory(String parentPath, String directory) {
//        return createDirectory(parentPath + File.separator + directory);
//    }

    public static String getAppDirectoryPath(String directory) {
        if (SDCardUtils.isSDCardEnable()) {
            return SDCardUtils.getSDCardPath() + File.separator + directory;
        } else {
            return "";
        }
    }

    public static String getFileName() {
        return String.valueOf(System.currentTimeMillis()) + ".jpg";
    }

    public static Long getFileDirSize(File dir) {
        try {
            if (dir == null || !dir.isDirectory()) {
                return 0L;
            }
            long size = 0;
            for (File f : dir.listFiles()) {
                if (f.isFile()) {
                    size += f.length();
                } else if (f.isDirectory()) {
                    size += f.length();
                    size += getFileDirSize(f);
                }
            }
            return size;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
