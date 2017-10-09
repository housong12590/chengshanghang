package com.jmm.csg.imgload;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy {

    @Override
    public void loadImage(Context cxt, String url, ImageView iv) {
        ImageLoader build = new ImageLoader.Builder().build();
        Glide.with(cxt).load(url).asBitmap().placeholder(build.getPlaceHolder())
                .error(build.getErrorImg())
                .into(iv);
    }

    @Override
    public void loadImage(Context cxt, File file, ImageView iv) {
        ImageLoader build = new ImageLoader.Builder().build();
        Glide.with(cxt).load(file).placeholder(build.getPlaceHolder())
                .error(build.getErrorImg())
                .into(iv);
    }

    @Override
    public void loadImage(Context cxt, ImageLoader imageLoader) {
        if (imageLoader.getFile() != null) {
            Glide.with(cxt)
                    .load(imageLoader.getFile())
                    .placeholder(imageLoader.getPlaceHolder())
                    .error(imageLoader.getErrorImg())
                    .into(imageLoader.getImgView());
        } else {
            Glide.with(cxt)
                    .load(imageLoader.getUrl())
                    .asBitmap()
                    .placeholder(imageLoader.getPlaceHolder())
                    .diskCacheStrategy(imageLoader.isCache() ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                    .skipMemoryCache(!imageLoader.isCache())
                    .error(imageLoader.getErrorImg())
                    .into(imageLoader.getImgView());
        }
    }
}
