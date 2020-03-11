package com.xjt.ordershop.util;

import android.text.TextUtils;

/**
 * Create by xuqunxing on  2020/1/21
 */
public class ApiUrlManger {
    public static String BaseUrl = "http://localhost:8080/orderShop";
    public static String API_USER_INSERT = "/user/insert";
    public static String API_USER_LOGIN = "/user/userLogin";
    public static String API_USER_LIST = "/user/userList";
    public static String API_USER_UPDATE_ADDRESS = "/user/udpateAddress";
    public static String API_USER_BY_ID = "/user/selectUserById";

    public static String API_GOOD_ADD = "/good/insertGood";
    public static String API_GOOD_LIST = "/good/goodList";
    public static String API_GOOD_UPDATE = "/good/udpate";
    public static String API_GOOD_LIST_BY_KEY = "/good/goodListByKey";
    public static String API_GOOD_LIST_BY_ID = "/good/selectUserById";
    public static String API_GOOD_LIST_BY_CID = "/good/goodListByCategoryId";

    public static String API_CATEGORY_LIST = "/category/categoryList";
    public static String API_CATEGORY_ADD = "/category/insertCategory";

    public static String API_BANNER_ADD = "/banner/insertBanenr";
    public static String API_BANNER_LIST = "/banner/bannerList";
    public static String API_BANNER_REMOVE_ALL = "/banner/deleteAllBanner";


    public static String API_ORDER_LIST = "/order/orderList";
    public static String API_ORDER_DELETE_BY_ID = "/order/deleteOrderById";
    public static String API_ORDER_INSERT = "/order/insertOrder";
    public static String API_ORDER_UPDATE = "/order/udpateOrder";
    public static String API_ORDER_BY_ID = "/order/getOrderById";

    public static String API_ADDRESS_LIST = "/address/addressList";
    public static String API_ADDRESS_DELETE_BY_ID = "/address/deleteAddressById";
    public static String API_ADDRESS_INSERT = "/address/insertAddress";
    public static String API_ADDRESS_UPDATE = "/address/updateAddress";
    public static String API_ADDRESS_UPDATE_STATE = "/address/updateAddressState";

    public static String appendUrl(String url) {
        return BaseUrl + url;
    }

    public static void setBaseUrl(String ip) {
        if (!TextUtils.isEmpty(ip)) {
            BaseUrl = "http://" + ip + ":8080/orderShop";
        } else {
            BaseUrl = "http://localhost:8080/orderShop";
        }
        LogUtils.e("======", "======BaseUrl:" + BaseUrl);
    }
}
