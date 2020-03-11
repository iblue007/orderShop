package com.xjt.ordershop.util;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

/**
 * Created by solo on 2018/1/9.
 * Banner 框架的图片加载器
 */

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
//        Glide.with(context).load(path).into(imageView);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage((String) path, imageView);
    }
}
