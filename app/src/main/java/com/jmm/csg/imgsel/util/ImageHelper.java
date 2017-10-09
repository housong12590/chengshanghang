package com.jmm.csg.imgsel.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import com.jmm.csg.imgsel.bean.ImageFolderBean;
import com.jmm.csg.imgsel.core.ImageSelectObservable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ImageHelper {

    public static Observable<List<ImageFolderBean>> loadLocalFolderContainsImage(final Context context){
       return Observable.create(new Observable.OnSubscribe<List<ImageFolderBean>>() {
            @Override
            public void call(Subscriber<? super List<ImageFolderBean>> subscriber) {
                ArrayList<ImageFolderBean> imageFolders = new ArrayList<>();
                String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, "COUNT(1) AS count"};
                String selection = "0==0) GROUP BY (" + MediaStore.Images.Media.BUCKET_ID;
                String sortOrder = MediaStore.Images.Media.DATE_MODIFIED;
                Cursor cursor;
                cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, selection, null, sortOrder);
                if (cursor == null || !cursor.moveToFirst()) {
                    return;
                }
                int columnPath = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                int columnId = cursor.getColumnIndex(MediaStore.Images.Media._ID);

                int columnFileName = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                int columnCount = cursor.getColumnIndex("count");
                do {
                    ImageFolderBean folderBean = new ImageFolderBean();
                    folderBean.path = cursor.getString(columnPath);
                    folderBean._id = cursor.getInt(columnId);
                    folderBean.pisNum = cursor.getInt(columnCount);

                    String bucketName = cursor.getString(columnFileName);
                    folderBean.fileName = bucketName;
                    if (!Environment.getExternalStorageDirectory().getPath().contains(bucketName)) {
                        imageFolders.add(0, folderBean);
                    }
                } while (cursor.moveToNext());
                subscriber.onNext(imageFolders);
                cursor.close();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<List<ImageFolderBean>> queryFolderPicture(final Context context, final String folderPath){
        return Observable.create(new Observable.OnSubscribe<List<ImageFolderBean>>() {
            @Override
            public void call(Subscriber<? super List<ImageFolderBean>> subscriber) {
                ArrayList<ImageFolderBean> list = new ArrayList<>();
                String[] columns = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
                String whereclause = MediaStore.Images.ImageColumns.DATA + " like'" + folderPath + "/%'";
                Cursor cursor = null;
                List<ImageFolderBean> selects = ImageSelectObservable.getInstance().getSelectImages();
                cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, whereclause, null, null);
                if(cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst()){
                    return;
                }
                do {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                    int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));

                    ImageFolderBean photoItem = new ImageFolderBean();
                    photoItem.path = path;
                    photoItem._id = id;
                    for (int index = 0, len = selects.size(); index < len; index ++) {
                        if (selects.get(index).path.equals(photoItem.path)) {
                            photoItem.selectPosition = selects.get(index).selectPosition;
                            selects.remove(index);
                            selects.add(photoItem);
                            break;
                        }
                    }
                    list.add(0, photoItem);
                } while (cursor.moveToNext());
                cursor.close();
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<List<ImageFolderBean>> queryAllPicture(final Context context){
        return Observable.create(new Observable.OnSubscribe<List<ImageFolderBean>>() {
            @Override
            public void call(Subscriber<? super List<ImageFolderBean>> subscriber) {
                ContentResolver resolver = context.getContentResolver();
                Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{"_data"}, null, null, null);
                ArrayList<ImageFolderBean> pictures = new ArrayList<>();
                if (cursor == null)
                    return;
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    ImageFolderBean bean = new ImageFolderBean();
                    if (!new File(path).exists())
                        continue;
                    bean.path = path;
                    pictures.add(bean);
                }
                cursor.close();
                subscriber.onNext(pictures);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
