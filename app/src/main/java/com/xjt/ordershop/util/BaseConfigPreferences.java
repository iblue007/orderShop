package com.xjt.ordershop.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Create by xuqunxing on  2020/2/29
 */
public class BaseConfigPreferences {
    public static final String NAME = "aopmaster";
    private static SharedPreferences baseSP;
    private static BaseConfigPreferences baseConfig;

    protected BaseConfigPreferences(Context context) {
        baseSP = context.getSharedPreferences(NAME, 4);
    }

    public static BaseConfigPreferences getInstance(Context context) {
        if (null == baseConfig) {
            baseConfig = new BaseConfigPreferences(context);
        }

        return baseConfig;
    }

    public static final String LOGIN_ACCOUNT = "login_account";
    public static final String APP_GUID = "app_dpzx_guid";
    public static final String APP_IP = "app_ip";//当前电脑的ip
    public static final String APP_PAY_PASSWORD = "app_pay_password";//当前电脑的ip
    public static final String LOGIN_NAME = "login_name";
    public static final String LOGIN_ADDRESS = "login_address";
    public static final String LOGIN_USER_ROLE = "login_user_role";
    public static final String LOGIN_USER_ID = "login_user_id";
    public static final String LOGIN_SHOP_ADDRESS = "login_shop_address";


    public void setLoginUserId(int userId) {
        baseSP.edit().putInt(LOGIN_USER_ID, userId).commit();
    }

    public int getLoginUserId() {
        return baseSP.getInt(LOGIN_USER_ID, 0);
    }

    public void setLoginUserRole(int count) {
        baseSP.edit().putInt(LOGIN_USER_ROLE, count).commit();
    }

    public int getLoginUserRole() {
        return baseSP.getInt(LOGIN_USER_ROLE, 1);
    }

    public void setValue(String key, String value) {
        baseSP.edit().putString(key, value).commit();
    }

    public String getValue(String key) {
        return baseSP.getString(key, "");
    }

    public String getValue(String key, String value) {
        return baseSP.getString(key, value);
    }

    public void setLoginAddress(String count) {
        baseSP.edit().putString(LOGIN_ADDRESS, count).commit();
    }

    public String getLoginAddress() {
        return baseSP.getString(LOGIN_ADDRESS, (String) null);
    }

    public void setLoginAccount(String count) {
        baseSP.edit().putString(LOGIN_ACCOUNT, count).commit();
    }

    public String getLoginAccount() {
        return baseSP.getString(LOGIN_ACCOUNT, (String) null);
    }

    public void setGUID(String guid) {
        baseSP.edit().putString(APP_GUID, guid).commit();
    }

    public String getGUID() {
        return baseSP.getString(APP_GUID, null);
    }

}
