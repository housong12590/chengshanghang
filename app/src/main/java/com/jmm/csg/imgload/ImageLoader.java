package com.jmm.csg.imgload;

import android.widget.ImageView;

import com.jmm.csg.R;

import java.io.File;

public class ImageLoader {

    private File file;
    private int errorImg;
    private String url;
    private int placeHolder;
    private ImageView imgView;
    private boolean isCache;

    private ImageLoader() {

    }

    private ImageLoader(Builder builder) {
        this.url = builder.url;
        this.placeHolder = builder.placeHolder;
        this.errorImg = builder.errorImg;
        this.imgView = builder.imgView;
        this.file = builder.file;
        this.isCache = builder.isCache;
    }

    public File getFile() {
        return file;
    }

    public int getErrorImg() {
        return errorImg;
    }

    public String getUrl() {
        return url;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public ImageView getImgView() {
        return imgView;
    }

    public boolean isCache(){
        return isCache;
    }

    public static class Builder {

        private int placeHolder;
        private ImageView imgView;
        private String url;
        private int errorImg;
        private File file;
        private boolean isCache;

        public Builder() {
            placeHolder = R.drawable.defaultpic; //设置默认的图片,不用每次都写
            errorImg = R.drawable.defaultpic;
            isCache = true; //默认缓存
        }

        public Builder error(int resId) {
            this.errorImg = resId;
            return this;
        }

        public Builder placeHolder(int resId) {
            this.placeHolder = resId;
            return this;
        }

        public Builder load(String url) {
            this.url = url;
            return this;
        }

        public Builder load(File file) {
            this.file = file;
            return this;
        }

        public Builder into(ImageView iv) {
            this.imgView = iv;
            return this;
        }

        public ImageLoader build() {
            return new ImageLoader(this);
        }

        public Builder isCache(boolean isCache){
            this.isCache = isCache;
            return this;
        }
    }

}
