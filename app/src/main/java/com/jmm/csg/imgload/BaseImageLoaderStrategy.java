package com.jmm.csg.imgload;


import android.content.Context;
import android.widget.ImageView;

import java.io.File;

public interface BaseImageLoaderStrategy {

    void loadImage(Context cxt, String url, ImageView iv);

    void loadImage(Context cxt, File file, ImageView iv);

    void loadImage(Context cxt, ImageLoader imageLoader);
}
