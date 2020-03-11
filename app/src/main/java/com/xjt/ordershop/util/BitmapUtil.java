package com.xjt.ordershop.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Create by xuqunxing on  2020/3/2
 */
public class BitmapUtil {

    public static String encodeBitmap(String imgPath) {
        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // 压缩图片
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byte_arr = stream.toByteArray();
        // Base64图片转码为String
        String encodedString = Base64.encodeToString(byte_arr, 0);
        return encodedString;
    }
}
