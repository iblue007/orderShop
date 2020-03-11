package com.xjt.ordershop.base.basehttp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: </br>
 * Author: cxy
 * Date: 2017/3/3.
 */

public class NetLibUtil {



    /**获取手机号*/
    public static String getPhoneNumber(Context ctx) {
        if (ctx == null)
            return null;
        String imei =null;
        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getLine1Number();
            if (imei == null || "".equals(imei))
                return "91";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imei;
    }





    public static String getMacAddress(Context context) {
        String mac = "";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacDefault(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mac = getMacAddress();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mac = getMacFromHardware();
        }
        return mac;
    }

    /**
     * Android 6.0（包括） - Android 7.0（不包括）
     * @return
     */
    private static String getMacAddress() {
        String WifiAddress =null;
        try {
            WifiAddress = new BufferedReader(new FileReader(new File("/sys/class/net/wlan0/address"))).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return WifiAddress;
    }


    /**
     * Android  6.0 之前（不包括6.0）
     * 必须的权限  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     * @param context
     * @return
     */
    private static String getMacDefault(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }

        WifiManager wifi = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) {
            return mac;
        }
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
        }
        if (info == null) {
            return "";
        }
        mac = info.getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }
        return mac;
    }

    /**
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET" />
     * @return
     */
    private static String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }







    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getInstanceId(Context context) {
        return  "";
    }


    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized long getFirstInstallTime(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.firstInstallTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized long getLastInstallTime(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.lastUpdateTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 获取当前的运营商
     *
     * @param context
     * @return 运营商名字
     */
    public static String getOperator(Context context) {
            try {
                String ProvidersName = "";
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String IMSI = telephonyManager.getSubscriberId();
                if (IMSI != null) {
                    if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                        ProvidersName = "中国移动";
                    } else if (IMSI.startsWith("46001")  || IMSI.startsWith("46006")) {
                        ProvidersName = "中国联通";
                    } else if (IMSI.startsWith("46003")) {
                        ProvidersName = "中国电信";
                    }
                    return ProvidersName;
                } else {
                    return "没有获取到sim卡信息";
                }
            }catch (Exception e){

            }
            return  "";
    }



    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }






    /**
     * 取得IMEI号
     *
     * @param ctx
     * @return
     */
    public static String getIMEI(Context ctx) {
        if (ctx == null)
            return null;
        String imei =null;
        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
            if (imei == null || "".equals(imei))
                return HttpRequestParam.getUUId(ctx);//如果为空，返回guid
        } catch (Exception e) {
            e.printStackTrace();
            return HttpRequestParam.getUUId(ctx);//如果为空，返回guid
        }
        Log.i("===imei",imei);
        return imei;
    }


















    public static String getIMSI(Context ctx) {
        if (ctx == null)
            return "91";

        String imsi = "91";
        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            imsi = tm.getSubscriberId();
            if (imsi == null || "".equals(imsi))
                return "91";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imsi;
    }

    /**
     * 通过反射的方法，获取CUID
     *
     * @param ctx
     */
//    public static String getCUID(Context ctx) {
//        if (null == ctx)
//            return "";
//
////        if (Build.VERSION.SDK_INT >= 23) {
////            if (Settings.System.canWrite(ctx)) {
////
////            } else {
////                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
////                intent.setData(Uri.parse("package:" + Global.getPkgName()));
////                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                ctx.startActivity(intent);
////            }
////        }
//
//        return NdAnalytics.getCUID(ctx);
//    }

    /**
     * 获取versionName
     *
     * @param context
     * @return String
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageInfo packageinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            versionName = packageinfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }


    public static boolean isZh(Context context) {
        Locale lo;
        if (null == context) {
            return true;
        } else {
            lo = context.getResources().getConfiguration().locale;
        }
        if (lo.getLanguage().equals("zh"))
            return true;
        return false;
    }

    public static String utf8URLencode(String url) {
        StringBuffer result = new StringBuffer();
        if (url != null)
            for (int i = 0; i < url.length(); i++) {
                char c = url.charAt(i);
                if ((c >= 0) && (c <= 255)) {
                    result.append(c);
                } else {
                    byte[] b = new byte[0];
                    try {
                        b = Character.toString(c).getBytes("utf-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int j = 0; j < b.length; j++) {
                        int k = b[j];
                        if (k < 0)
                            k += 256;
                        result.append("%" + Integer.toHexString(k).toUpperCase());
                    }
                }
            }
        return result.toString();
    }

    /**
     * <br>Description: 替换非法字符(去除非中文、字母、数字、空格、下划线、"-"的字符)
     * <br>Author:caizp
     * <br>Date:2016年10月13日下午3:11:21
     */
    public static String replaceIllegalCharacter(String source, String replaceChar) {
        if (TextUtils.isEmpty(replaceChar)) {
            return source;
        }
        if (TextUtils.isEmpty(source)) {
            return "";
        }
        String regex = "[^\\sa-zA-Z0-9\u4e00-\u9fa5_-]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        String result = matcher.replaceAll(replaceChar);
        return result;
    }


    public static String getBuildMode() {
        return Build.MODEL;
    }

    public static String getBuildVersion() {
        return Build.VERSION.RELEASE;
    }


    /**
     * 功能描述：判断当前设备是否为模拟器
     * 参数：
     */
    /**
     * 判断当前设备是否是模拟器。如果返回TRUE，则当前是模拟器，不是返回FALSE
     *
     * @param context
     * @return
     */
    public static boolean isEmulator(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (imei != null && imei.equals("000000000000000")) {
                return true;
            }
            return (Build.MODEL.equals("sdk"))
                    || (Build.MODEL.equals("google_sdk"));
        } catch (Exception e) {

        }
        return false;
    }

}
