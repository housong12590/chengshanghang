package com.jmm.csg.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

public class SystemUtils {

    public static void startTransitionActivity(Activity activity, Class<?> clazz, View transitView, String transitionName) {
        Intent intent = new Intent(activity, clazz);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, transitView, transitionName);
        try {
            ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            activity.startActivity(intent);
        }
    }

    public static void startTransitionActivity(Activity activity, Intent intent, View transitView, String transitionName) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, transitView, transitionName);
        try {
            ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            activity.startActivity(intent);
        }
    }

    public static void startCameraPhoto(Activity activity, String savePath, int requestCode) {
        RxPermissions.getInstance(activity).request(Manifest.permission.CAMERA).subscribe(aBoolean -> {
            if(!aBoolean){
                ToastUtils.showMsg("没有相机使用权限");
            }else{
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(savePath)));
                    activity.startActivityForResult(intent, requestCode);
                } else {
                    Toast.makeText(activity, "该手机没有相机", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void startDial(Activity context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
//        context.startActivityForResult(intent, 100);
        context.startActivity(intent);
    }
}
