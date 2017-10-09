package com.jmm.csg.imgload;

import android.content.Context;
import android.widget.ImageView;

import com.jmm.csg.R;
import com.jmm.csg.utils.OSSUtils;

import java.io.File;

public class ImageLoaderUtils {

    private static ImageLoaderUtils mInstance;
    private BaseImageLoaderStrategy strategy;

    private ImageLoaderUtils() {
        strategy = new GlideImageLoaderStrategy();
    }

    public static ImageLoaderUtils getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoaderUtils.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderUtils();
                }
            }
        }
        return mInstance;
    }


    public void loadImage(Context cxt, ImageLoader imageLoader) {
        strategy.loadImage(cxt, imageLoader);
    }

    public void loadImage(Context cxt, String url, ImageView iv) {
        strategy.loadImage(cxt, url, iv);
    }

    public void loadImage(Context cxt, File file, ImageView iv) {
        strategy.loadImage(cxt, file, iv);
    }

    public void loadOSSImage(Context cxt, String path, ImageView iv) {
        loadImage(cxt, new ImageLoader.Builder()
                .load(OSSUtils.BASE_URL + path)
                .placeHolder(R.drawable.defaultpic)
                .error(R.drawable.defaultpic)
                .into(iv)
                .build());
    }

    public void loadOSSAvatar(Context cxt, String path, ImageView iv) {
        loadImage(cxt, new ImageLoader.Builder()
                .load(OSSUtils.BASE_URL + path)
                .placeHolder(R.drawable.icon_defavatar)
                .error(R.drawable.icon_defavatar)
                .isCache(false)
                .into(iv)
                .build());
    }
}
