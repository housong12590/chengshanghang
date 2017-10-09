package com.jmm.csg.http.log;


import okhttp3.internal.Platform;

@SuppressWarnings({"WeakerAccess", "unused"})
public interface Logger {

    void log(String tag, String message);

    Logger DEFAULT = new Logger() {
        @Override
        public void log(String tag, String message) {
            Platform.get().log(message);
        }
    };
}
