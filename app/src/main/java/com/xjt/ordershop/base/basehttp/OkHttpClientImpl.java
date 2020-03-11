package com.xjt.ordershop.base.basehttp;



import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by Corgi
 * on 2018/7/25.
 */
public class OkHttpClientImpl implements HttpClient{
    protected OkHttpClient.Builder builder;
    private static final int CONNECTION_TIME_OUT = 15000;
    private static final int READ_TIME_OUT = 15000;
    private boolean mDebug = false;
    private OkHttpClient mDefaultOkHttpClient;
    private OkHttpClient mDefaultTLSOkHttpClient;

    public OkHttpClientImpl(){
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECTION_TIME_OUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS);
        builder.retryOnConnectionFailure(true);
        builder.cache(null);
        addInterceptors();
        if (mDebug) {
            try {
                builder.proxy(new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved("127.0.0.1", 8888)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void addInterceptors() {

    }

    @Override
    public OkHttpClient.Builder getBuilder() {
        return builder;
    }

    @Override
    public OkHttpClient getDefaultOkHttpClient() {
        synchronized (this) {
            if (mDefaultOkHttpClient == null) {
                mDefaultOkHttpClient = builder.build();
            }
            return mDefaultOkHttpClient;
        }
    }

    @Override
    public OkHttpClient getDefaultTLSOkHttpClient() {
        synchronized (this) {
            if (mDefaultTLSOkHttpClient == null) {
                try {
                    SSLContext sc = SSLContext.getInstance("TLS");
                    sc.init(null, new TrustManager[]{new VideopaperTrustManager()}, new SecureRandom());
                    mDefaultTLSOkHttpClient = builder.sslSocketFactory(sc.getSocketFactory()).build();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
            }
            return mDefaultTLSOkHttpClient;
        }
    }
}
