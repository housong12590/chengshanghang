package com.jmm.csg.imgsel.core;


import com.jmm.csg.imgsel.bean.ImageFolderBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;


public class ImageSelectObservable extends Observable {

    public static Object imgSelectObj = new Object();

    private static ImageSelectObservable sObserver;

    private List<ImageFolderBean> mFolderAllImages;

    private List<ImageFolderBean> mSelectImages;

    private ImageSelectObservable() {
        mFolderAllImages = new ArrayList<>();
        mSelectImages = new ArrayList<>();
    }

    public static ImageSelectObservable getInstance() {

        if (sObserver == null) {
            synchronized (ImageSelectObservable.class) {
                if (sObserver == null) {
                    sObserver = new ImageSelectObservable();
                }
            }
        }
        return sObserver;
    }

    public void addFolderImagesAndClearBefore(Collection<? extends ImageFolderBean> list) {
        mFolderAllImages.clear();
        if (list != null) {
            mFolderAllImages.addAll(list);
        }
    }

    public void addSelectImagesAndClearBefore(Collection<? extends ImageFolderBean> list) {
        mSelectImages.clear();
        if (list != null) {
            mSelectImages.addAll(list);
        }
    }

    public List<ImageFolderBean> getFolderAllImages() {
        return mFolderAllImages;
    }

    public List<ImageFolderBean> getSelectImages() {
        return mSelectImages;
    }

    /**
     * 通知图片选择已改变
     */
    public void updateImageSelectChanged () {
        setChanged();
        notifyObservers(imgSelectObj);

    }

    public void clearFolderImages () {
        mFolderAllImages.clear();
    }

    public void clearSelectImages() {
        mSelectImages.clear();
    }
}
