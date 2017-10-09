package com.jmm.csg.utils;


import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {

    public static void close(Object obj) {
        try {
            if (obj instanceof InputStream) {
                ((InputStream) obj).close();
            } else if (obj instanceof OutputStream) {
                ((OutputStream) obj).close();
            }else{
                Log.e("IOUtils","stream close error...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
