package com.jmm.csg.bean;

import android.graphics.Bitmap;

/**
 * Author: 30453
 * Date: 2016/8/8 12:43
 */
public class ShareModel {
    private String title;
    private String content;
    private String url;
    private String imageUrl;
    private String site;
    private String siteUrl;
    private String imagePath;
    private Bitmap imageBitmap;
    private String coding;

    public ShareModel(String title, String content, String url, String imagePath, Bitmap imageBitmap) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.imageUrl = imagePath;
        this.imageBitmap = imageBitmap;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ShareModel() {
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

