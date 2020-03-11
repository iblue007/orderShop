package com.xjt.ordershop.base.basehttp;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.webkit.WebSettings;

import com.xjt.ordershop.config.AppConfig;
import com.xjt.ordershop.util.BaseConfigPreferences;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.UUID;


/**
 * Created by dingdj on 2016/5/13.
 */
public class HttpRequestParam {

    public static String DivideVersion;
    public static String SupPhone;
    public static String SupFirm;
    public static String IMEI;
    public static String IMSI;
    public static String CUID;
    public static final String MT = "4";
    public static final String ProtocolVersion_2 = NetLibUtil.utf8URLencode("2.0");
    public static final String ProtocolVersion_3 = NetLibUtil.utf8URLencode("3.0");
    public static final String ProtocolVersion_4 = NetLibUtil.utf8URLencode("4.0");
    public static final String LAUNCHER_REQUEST_KEY = "27B1F81F-1DD8-4F98-8D4B-6992828FB6E2";
    public static final String PO_REQUEST_KEY = "2B1F781F-1D8D-984F-D84B-9826E6928FB2";
    public static final String REQUEST_KEY = "58D1BAC3-3477-4870-9AD4-4879259652B7";

    public static String getUUId(Context context) {
        if (TextUtils.isEmpty(BaseConfigPreferences.getInstance(context).getGUID())) {
            UUID guid = UUID.randomUUID();
            BaseConfigPreferences.getInstance(context).setGUID(guid.toString());
            return guid.toString();
        } else {
            return BaseConfigPreferences.getInstance(context).getGUID();
        }
    }

    public static void headers(Context context, HashMap map) {
        try {
            map.put("Connection", "close");
            map.put("client-info", NetLibUtil.getVersionName(context) + ",Android," + Build.VERSION.RELEASE);
            map.put("client-guid", getUUId(context));
            map.put("client-api-level", Build.VERSION.SDK_INT + "");
            map.put("client-build-number", NetLibUtil.getVersionCode(context) + "");
            map.put("client-device-name", "");
            map.put("client-first-install-time", NetLibUtil.getFirstInstallTime(context) + "");
            map.put("client-last-update-time", NetLibUtil.getLastInstallTime(context) + "");
            map.put("client-readable-version", NetLibUtil.getVersionCode(context) + "." + NetLibUtil.getVersionName(context));
            map.put("client-system-name", "Android");
            map.put("client-emulator", NetLibUtil.isEmulator(context)+"");
            map.put("client-lng", AppConfig.POSISION_LNG + "");
            map.put("client-lat", AppConfig.POSISION_LAT + "");

            if (!TextUtils.isEmpty(Build.BRAND)) {
                map.put("client-brand", Build.BRAND + "");
            }


            if (!TextUtils.isEmpty(NetLibUtil.getPackageName(context))) {
                map.put("client-bundle-id", NetLibUtil.getPackageName(context));
            }
            if (!TextUtils.isEmpty(URLEncoder.encode(NetLibUtil.getOperator(context), "UTF-8"))) {
                map.put("client-carrier", URLEncoder.encode(NetLibUtil.getOperator(context), "UTF-8"));
            }

            if (!TextUtils.isEmpty(Build.BOARD)) {
                map.put("client-device-id", Build.BOARD);
            }


            if (!TextUtils.isEmpty(NetLibUtil.getInstanceId(context))) {
                map.put("client-instance-id", NetLibUtil.getInstanceId(context));
            }

            if(!TextUtils.isEmpty(NetLibUtil.getMacAddress(context))){
                map.put("client-mac-address", NetLibUtil.getMacAddress(context));
            }

            if(!TextUtils.isEmpty(Build.MANUFACTURER)){
                map.put("client-manufacturer", Build.MANUFACTURER);
            }
            if(!TextUtils.isEmpty(NetLibUtil.getPhoneNumber(context))){
                map.put("client-phone-number", NetLibUtil.getPhoneNumber(context));
            }

            if(!TextUtils.isEmpty(Build.SERIAL)){
                map.put("client-serial-number", Build.SERIAL);
            }

            if(!TextUtils.isEmpty(Build.VERSION.RELEASE)){
                map.put("client-system-version", Build.VERSION.RELEASE);
            }
            if(!TextUtils.isEmpty(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID))){
                map.put("client-unique-id", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
            }
            if(!TextUtils.isEmpty(NetLibUtil.getVersionName(context))){
                map.put("client-version", NetLibUtil.getVersionName(context));
            }

            if(!TextUtils.isEmpty(NetLibUtil.getIMEI(context))){
                map.put("client-imei", NetLibUtil.getIMEI(context));
                map.put("imei", NetLibUtil.getIMEI(context));
            }else{
                map.put("client-imei", HttpRequestParam.getUUId(context));
                map.put("imei", HttpRequestParam.getUUId(context));
            }

        } catch (Exception e) {

        }

    }


    public static void addCommmonPostRequestValue(Context context, HashMap<String, String> paramsMap) {
        // paramsMap.put("Accept-Encoding", "gzip, deflate");
        paramsMap.put("accept", "*/*");
        paramsMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        paramsMap.put("User-Agent", getUserAgent(context));
        headers(context, paramsMap);
    }

    public static void addCommmonPostRequestValueNoImei(Context context, HashMap<String, String> paramsMap) {
        // paramsMap.put("Accept-Encoding", "gzip, deflate");
        paramsMap.put("accept", "*/*");
        paramsMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        paramsMap.put("User-Agent", getUserAgent(context));
        headers(context, paramsMap);
    }

    public static void addCommmonGetRequestValue(Context context, HashMap<String, String> paramsMap) {
        // paramsMap.put("Accept-Encoding", "gzip, deflate");
        paramsMap.put("accept", "*/*");
        paramsMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        paramsMap.put("User-Agent", getUserAgent(context));
        paramsMap.put("content-type", "application/x-www-form-urlencoded");
        headers(context, paramsMap);
    }

    public static String getUserAgent(Context context) {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String parsePostData(String... values) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            if (i % 2 == 0) {
                if (i == 0) {
                    stringBuffer.append(value);
                } else {
                    stringBuffer.append("&" + value);
                }
            } else {
                stringBuffer.append("=" + value);
            }
        }
        return stringBuffer.toString();
    }

    public static String parsePostData(LinkedHashMap linkedHashMap) {
        StringBuffer stringBuffer = new StringBuffer();
        if (linkedHashMap != null & linkedHashMap.size() > 0) {
            int i = 0;
            Set<String> keyset = linkedHashMap.keySet();
            for (Iterator<String> it = keyset.iterator(); it.hasNext(); ) {
                String key = it.next();
                String value = (String) linkedHashMap.get(key);
                if (i > 0) {
                    stringBuffer.append("&" + key + "=" + value);
                } else {
                    stringBuffer.append(key + "=" + value);
                }
                i++;
            }
        }
        return stringBuffer.toString();
    }

    public static String parsePostDataTest(LinkedHashMap linkedHashMap) {
        StringBuffer stringBuffer = new StringBuffer();
        if (linkedHashMap != null & linkedHashMap.size() > 0) {
            int i = 0;
            Set<String> keyset = linkedHashMap.keySet();
            for (Iterator<String> it = keyset.iterator(); it.hasNext(); ) {
                String key = it.next();
                String value = (String) linkedHashMap.get(key);
                if (i > 0) {
                    stringBuffer.append(";" + key + "=" + value);
                } else {
                    stringBuffer.append(key + "=" + value);
                }
                i++;
            }
        }
        return stringBuffer.toString();
    }
//
//    /**
//     * 添加桌面接口通用参数
//     *
//     * @param paramsMap
//     * @param jsonParams
//     */
//    public static void addGlobalLauncherRequestValue(HashMap<String, String> paramsMap, String jsonParams, String sessionId) {
//        Context ctx = Global.getApplicationContext();
//        if (paramsMap == null)
//            return;
//        if (jsonParams == null) {
//            jsonParams = "";
//        }
//        try {
//            if (null == DivideVersion)
//                DivideVersion = NetLibUtil.utf8URLencode(NetLibUtil.getDivideVersion(ctx));
//            if (null == SupPhone)
//                SupPhone = Des2.encodeHeader(Des2.HEADER_DES_KEY, Des2.HEADER_DESIV, NetLibUtil.utf8URLencode(NetLibUtil.getBuildMode()).getBytes());
//            if (null == SupFirm)
//                SupFirm = Des2.encodeHeader(Des2.HEADER_DES_KEY, Des2.HEADER_DESIV, NetLibUtil.utf8URLencode(NetLibUtil.getBuildVersion()).getBytes());
//            if (null == IMEI)
//                IMEI = Des2.encodeHeader(Des2.HEADER_DES_KEY, Des2.HEADER_DESIV, NetLibUtil.utf8URLencode(NetLibUtil.getIMEI(ctx)).getBytes());
//            if (null == IMSI)
//                IMSI = Des2.encodeHeader(Des2.HEADER_DES_KEY, Des2.HEADER_DESIV, NetLibUtil.utf8URLencode(NetLibUtil.getIMSI(ctx)).getBytes());
//            if (null == CUID)
//                CUID = URLEncoder.encode(NetLibUtil.getCUID(ctx), "UTF-8");
//
//            CUID = TextUtils.isEmpty(CUID) ? IMEI : CUID;
//            sessionId = sessionId == null ? "" : sessionId;
//
//            paramsMap.put("PID", BaseConfig.APPID + "");
//            paramsMap.put("MT", MT);
//            paramsMap.put("DivideVersion", DivideVersion);
//            paramsMap.put("SupPhone", SupPhone);
//            paramsMap.put("SupFirm", SupFirm);
//            paramsMap.put("IMEI", IMEI);
//            paramsMap.put("IMSI", IMSI);
//            paramsMap.put("SessionId", sessionId);
//            paramsMap.put("CUID", CUID);//通用用户唯一标识 NdAnalytics.getCUID(ctx)
//            paramsMap.put("ProtocolVersion", ProtocolVersion_3);
//            paramsMap.put("EncryptType", "100");
////            paramsMap.put("EnableStatus", "1");//值为1，表示支持H265格式
//            String Sign = DigestUtil.md5Hex(BaseConfig.APPID + MT + DivideVersion + SupPhone + SupFirm + IMEI + IMSI + sessionId + CUID + ProtocolVersion_3 + jsonParams + LAUNCHER_REQUEST_KEY);
//            paramsMap.put("Sign", Sign);
//            paramsMap.put("EnableStatus", getEnableStatus(ctx));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static String getEnableStatus(Context context){
//        int enableStatus = 0;
//        boolean enableH265 = false;// 1:开启 0:未开启
//        boolean enableWallpaperService = VideoWallpaperTool.hasApplyLiveWallpaperBefore(context); // 2:开启 0:未开启
//        if (enableH265) {
//            enableStatus += 1;
//        }
//        if (enableWallpaperService) {
//            enableStatus += 2;
//        }
//        return String.valueOf(enableStatus);
//    }
//
//    /**
//     * 添加PO接口通用参数
//     *
//     * @param paramsMap
//     * @param jsonParams
//     */
//    public static void addGlobalPoRequestValue(HashMap<String, String> paramsMap, String jsonParams, String sessionId) {
//        Context ctx = Global.getApplicationContext();
//        if (paramsMap == null)
//            return;
//        if (jsonParams == null) {
//            jsonParams = "";
//        }
//        try {
//            if (null == DivideVersion)
//                DivideVersion = NetLibUtil.utf8URLencode(NetLibUtil.getDivideVersion(ctx));
//            if (null == SupPhone)
//                SupPhone = Des2.encodeHeader(Des2.HEADER_DES_KEY, Des2.HEADER_DESIV, NetLibUtil.utf8URLencode(NetLibUtil.getBuildMode()).getBytes());
//            if (null == SupFirm)
//                SupFirm = Des2.encodeHeader(Des2.HEADER_DES_KEY, Des2.HEADER_DESIV, NetLibUtil.utf8URLencode(NetLibUtil.getBuildVersion()).getBytes());
//            if (null == IMEI)
//                IMEI = Des2.encodeHeader(Des2.HEADER_DES_KEY, Des2.HEADER_DESIV, NetLibUtil.utf8URLencode(NetLibUtil.getIMEI(ctx)).getBytes());
//            if (null == IMSI)
//                IMSI = Des2.encodeHeader(Des2.HEADER_DES_KEY, Des2.HEADER_DESIV, NetLibUtil.utf8URLencode(NetLibUtil.getIMSI(ctx)).getBytes());
//            if (null == CUID)
//                CUID = URLEncoder.encode(NetLibUtil.getCUID(ctx), "UTF-8");
//
//            CUID = TextUtils.isEmpty(CUID) ? IMEI : CUID;
//            sessionId = sessionId == null ? "" : sessionId;
//            String resolution = ScreenUtil.getCurrentScreenWidth(ctx) + "x" + ScreenUtil.getCurrentScreenHeight(ctx);
//
//            paramsMap.put("PID", BaseConfig.PID);
//            paramsMap.put("MT", MT);
//            paramsMap.put("DivideVersion", DivideVersion);
//            paramsMap.put("SupPhone", SupPhone);
//            paramsMap.put("SupFirm", SupFirm);
//            paramsMap.put("IMEI", IMEI);
//            paramsMap.put("IMSI", IMSI);
//            paramsMap.put("SessionId", sessionId);
//            paramsMap.put("CUID", CUID);//通用用户唯一标识 NdAnalytics.getCUID(ctx)
//            paramsMap.put("ProtocolVersion", ProtocolVersion_3);
//            paramsMap.put("Resolution",resolution);
//            String Sign = DigestUtil.md5Hex(BaseConfig.APPID + MT + DivideVersion + SupPhone + SupFirm + IMEI + IMSI + sessionId + CUID + resolution + ProtocolVersion_3 + jsonParams + PO_REQUEST_KEY);
//            paramsMap.put("Sign", Sign);
//            paramsMap.put("ChannelID", ChannelUtil.getChannel(ctx));
//            paramsMap.put("EncryptType", "100");
//            paramsMap.put("EnableStatus",getEnableStatus(ctx));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static class UserSession {
//        public String appName;
//        public int appId;
//        public String nickName;
//        public String loginUin;
//        public String sessionId;
//    }

}
