package com.xjt.ordershop.base.basehttp;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求头拦截器
 *
 * Created by Corgi
 * on 2018/7/23.
 */
public class HttpCommonInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request.Builder newBuilder = oldRequest.newBuilder();
        newBuilder.addHeader("Language", getLocal());
        newBuilder.addHeader("Country", getCountryCode());
        Log.e("pdw", "http interceptor：");
        return chain.proceed(newBuilder.build());
    }

    private String getLocal() {
        String lan = Locale.getDefault().getLanguage();
        String country = Locale.getDefault().getCountry();
        if (TextUtils.isEmpty(lan) || TextUtils.isEmpty(country)) {
            return "";
        }
        return lan + "_" + country;
    }

    public static String getCountryCode() {
//        Location location = LocationManager.get().getLocInfo();
//        if (location != null && !TextUtils.isEmpty(location.countryCode)) {
//            Log.e("pdw", "country cdoe:" + location.countryCode);
//            return location.countryCode;
//        }
        Locale locale = Locale.getDefault();
        return locale.getCountry();
    }
}
