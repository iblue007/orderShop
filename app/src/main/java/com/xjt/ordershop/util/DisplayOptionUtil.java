package com.xjt.ordershop.util;

import android.graphics.Bitmap.Config;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xjt.ordershop.R;

/**
 * Create by xuqunxing on  2019/3/14
 */
public class DisplayOptionUtil {

    public static final DisplayImageOptions DEFAULT_OPTIONS = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    public static final DisplayImageOptions VIDEO_UNIT_ITEM_OPTIONS = new DisplayImageOptions.Builder()
            .bitmapConfig(Config.ARGB_8888)
            .cacheOnDisk(true)
            .cacheInMemory(true)
            .showImageOnLoading(R.drawable.ic_loading_placehold_light)
            .showImageForEmptyUri(R.drawable.ic_loading_placehold_light)
            .showImageOnFail(R.drawable.ic_loading_placehold_light)
            .considerExifParams(true)
            .build();

    public static final DisplayImageOptions VIDEO_ROUND_ICON_OPTIONS = new DisplayImageOptions.Builder()
            .bitmapConfig(Config.ARGB_8888)
            .cacheOnDisk(true)
            .cacheInMemory(true)
            .showImageOnLoading(R.drawable.ic_loading_placehold_light)
            .showImageForEmptyUri(R.drawable.ic_loading_placehold_light)
            .showImageOnFail(R.drawable.ic_loading_placehold_light)
            .displayer(new RoundedBitmapDisplayer(ScreenUtil.dip2px(Global.getContext(), 1000), 0))
            .considerExifParams(true)
            .build();

    public static final DisplayImageOptions VIDEO_ROUNDED_OPTIONS = new DisplayImageOptions.Builder()
            .bitmapConfig(Config.ARGB_8888)
            .cacheOnDisk(true)
            .cacheInMemory(true)
            .showImageOnLoading(R.drawable.ic_loading_placehold_light)
            .showImageForEmptyUri(R.drawable.ic_loading_placehold_light)
            .showImageOnFail(R.drawable.ic_loading_placehold_light)
            .displayer(new RoundedBitmapDisplayer(ScreenUtil.dip2px(Global.getContext(), 10), 0))
            .considerExifParams(true)
            .build();
}
