package com.jmm.csg.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.jmm.csg.config.AppConfig;
import com.jmm.csg.utils.ToastUtils;

import java.io.File;


public class ApkDownService extends Service {

    private DownloadManager dm;
    private long downloadId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void start(Context context, String apkUrl) {
        Intent intent = new Intent(context, ApkDownService.class);
        intent.putExtra("apkUrl", apkUrl);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String apkUrl = intent.getStringExtra("apkUrl");
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        startDownload(apkUrl);
        return super.onStartCommand(intent, flags, startId);
    }


    private void startDownload(String apkUrl) {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (!apkUrl.startsWith("http")) {
            Toast.makeText(ApkDownService.this, "不是一个正确的网址", Toast.LENGTH_SHORT).show();
            return;
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        request.setMimeType("application/vnd.android.package-archive");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getFileName(apkUrl));
        downloadId = dm.enqueue(request);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId == id) {
                Uri uri = dm.getUriForDownloadedFile(downloadId);

                installApk(uri, null,context);
            }
            stopSelf();
        }
    };

    //http://oa5jgcq4p.bkt.clouddn.com/%E5%9F%8E%E5%95%86%E8%B4%AD.apk

    private String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

//    private void installApk(File file, Context context) {
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
////        intent.setData(Uri.fromFile(file));
//        System.out.println(file.getAbsolutePath());
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        context.startActivity(intent);
//    }

    private void installApk(Uri uri, File file, Context context) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction("android.intent.action.VIEW");
            if (Build.VERSION.SDK_INT >= 24) {
//                Uri apkUri = FileProvider.getUriForFile(context, "com.jmm.csg.provider", file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                context.startActivity(intent);
            } else {
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                context.startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(AppConfig.APP_SHARE_URL);
            ToastUtils.showMsg("安装失败，应用下载链接已复制到剪切板，如需更新请前往手动下载安装");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
