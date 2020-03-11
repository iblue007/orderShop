package com.xjt.ordershop.config;

import android.os.Environment;

/**
 * Create by xuqunxing on  2019/5/14
 */
public class BaseConfig {

    public static final String BASE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ordershop/";
    //    public static final String MAIN_BANNER_BASE_DIR = BASE_DIR + "banner/";
    public static final String MAIN_PHOTO_BASE_DIR = BASE_DIR + "photo/";
    public static final String MAIN_QRCODE = BASE_DIR + "qrcode/";
//    public static final String MAIN_CRASH = BASE_DIR + "crash/";
}
