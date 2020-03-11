package com.xjt.ordershop;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xjt.ordershop.base.basehttp.AppOkHttpClientImpl;
import com.xjt.ordershop.base.basehttp.HttpCommon;
import com.xjt.ordershop.config.BaseConfig;
import com.xjt.ordershop.util.ApiUrlManger;
import com.xjt.ordershop.util.BaseConfigPreferences;
import com.xjt.ordershop.util.DisplayOptionUtil;
import com.xjt.ordershop.util.Global;
import com.xjt.ordershop.util.LogUtils;

import java.io.File;

/**
 * Create by xuqunxing on  2019/8/8
 */
public class MainApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //因为引用的包过多，实现多包问题
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            Global.setContext(this);
            Global.setHandler(new Handler());
            HttpCommon.initClient(new AppOkHttpClientImpl());
            createDefaultDir();
            initWXAPI();
            initBaseUrl();
            initImageLoader(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBaseUrl() {
        String ipValue = BaseConfigPreferences.getInstance(this).getValue(BaseConfigPreferences.APP_IP,"127.0.0.1");
        LogUtils.e("======", "======ip:" + ipValue);
        if (!TextUtils.isEmpty(ipValue)) {
            ApiUrlManger.setBaseUrl(ipValue);
        }
    }

    private void initWXAPI() {
        // 三个参数分别是上下文、应用的appId、是否检查签名（默认为false）
//        IWXAPI mWxApi = WXAPIFactory.createWXAPI(this, "wx3b512ee41a1250a9", true);
//        // 注册
//        mWxApi.registerApp("wx3b512ee41a1250a9");
    }


    /**
     * 初始化基础目录
     */
    public void createDefaultDir() {
        final String baseDir = BaseConfig.BASE_DIR;
        final String basePhotoDir = BaseConfig.MAIN_PHOTO_BASE_DIR;
        final String baseQrCodeDir = BaseConfig.MAIN_QRCODE;
        File dir = new File(baseDir);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }

        File dir2 = new File(basePhotoDir);
        if (!dir2.isDirectory()) {
            dir2.mkdirs();
        }

        File dir3 = new File(baseQrCodeDir);
        if (!dir3.isDirectory()) {
            dir3.mkdirs();
        }
        // FileUtil.createDir(NativeHelper.getResourceDir(false));
    }

    /**
     * 初始化imageloader
     */
    public void initImageLoader(Context context) {
        try {
            ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                    .defaultDisplayImageOptions(DisplayOptionUtil.DEFAULT_OPTIONS)
                    .build();
            ImageLoader.getInstance().init(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
