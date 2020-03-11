package com.xjt.ordershop.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.ordershop.base.basehttp.HttpCommon;
import com.xjt.ordershop.base.basehttp.HttpRequestParam;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.base.basehttp.ServerResultHeader;
import com.xjt.ordershop.ui.AddressListActivity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Create by xuqunxing on  2020/1/21
 */
public class NetApiUtil {

    public static final ServerResult<CommonResultMessage> addAddress(String name, String phone, String address, String addressDetail) {
        int userId = BaseConfigPreferences.getInstance(Global.getContext()).getLoginUserId();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("phone", phone);
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("addressDetail", addressDetail);
        jsonObject.addProperty("state", 0);
        String httpStr = jsonObject.toString();
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_ADDRESS_INSERT), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultPostBody(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> updateAddress(int id, String name, String phone, String address, String addressDetail, int state, String addressDate) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("phone", phone);
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("addressDetail", addressDetail);
        String httpStr = jsonObject.toString();
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_ADDRESS_UPDATE), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultPostBody(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> updateAddressState(int id, int state) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("state", state);
        String httpStr = jsonObject.toString();
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_ADDRESS_UPDATE_STATE), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultPostBody(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> getAddressList() {
        int userId = BaseConfigPreferences.getInstance(Global.getContext()).getLoginUserId();
        String httpStr = "";
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_ADDRESS_LIST + "?userId=" + userId), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultGetJson(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> deleteAddressById(int addressId) {
        String httpStr = "";
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_ADDRESS_DELETE_BY_ID + "?addressId=" + addressId),
                null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultGetJson(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> getOrderById(int orderId) {
        String httpStr = "";
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_ORDER_BY_ID + "?orderId=" + orderId),
                null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultGetJson(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> getOrderList() {
        String httpStr = "";
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_ORDER_LIST), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultGetJson(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> addOrder(int goodId, int categoryId, int goodState, String goodName, String goodDetail, String goodDiscount,
                                                                   String goodPrice, String goodPic, String buyerName, String buyerphone, String buyerAddress) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("goodId", goodId);
        jsonObject.addProperty("categoryId", categoryId);
        jsonObject.addProperty("goodState", goodState);
        jsonObject.addProperty("goodName", goodName);
        jsonObject.addProperty("goodDetail", goodDetail);
        jsonObject.addProperty("goodDiscount", goodDiscount);
        jsonObject.addProperty("goodPrice", goodPrice);
        jsonObject.addProperty("goodPic", goodPic);
        jsonObject.addProperty("buyerName", buyerName);
        jsonObject.addProperty("buyerphone", buyerphone);
        jsonObject.addProperty("buyerAddress", buyerAddress);
        String httpStr = jsonObject.toString();
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_ORDER_INSERT), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultPostBody(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> udpateOrder(int id, int goodId, int categoryId, int goodState, String goodName, String goodDetail, String goodDiscount, String goodPrice, String goodPic) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("goodId", goodId);
        jsonObject.addProperty("categoryId", categoryId);
        jsonObject.addProperty("goodState", goodState);
        jsonObject.addProperty("goodName", goodName);
        jsonObject.addProperty("goodDetail", goodDetail);
        jsonObject.addProperty("goodDiscount", goodDiscount);
        jsonObject.addProperty("goodPrice", goodPrice);
        jsonObject.addProperty("goodPic", goodPic);
        String httpStr = jsonObject.toString();
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_ORDER_UPDATE), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultPostBody(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> deleteOrderById(int orderId) {
        String httpStr = "";
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_ORDER_DELETE_BY_ID + "?orderId=" + orderId),
                null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultGetJson(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> getCategoryById(int categoryId) {
        String httpStr = "";
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_GOOD_LIST_BY_CID + "?categoryId=" + categoryId),
                null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultGetJson(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    //新增商品
    public static final ServerResult<CommonResultMessage> addBanner(int bType, int categoryId, int goodId, String picStr, String name) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("bType", bType);
        jsonObject.addProperty("categoryId", categoryId);
        jsonObject.addProperty("goodId", goodId);
        jsonObject.addProperty("picStr", picStr);
        jsonObject.addProperty("name", name);
        String httpStr = jsonObject.toString();
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_BANNER_ADD), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultPostBody(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    //新增商品
    public static final ServerResult<CommonResultMessage> addGood(String goodName, String goodDetail, String goodDiscount, String goodPrice, String goodPic, int categoryId, String categoryName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("goodName", goodName);
        jsonObject.addProperty("goodDetail", goodDetail);
        jsonObject.addProperty("goodDiscount", goodDiscount);
        jsonObject.addProperty("goodPrice", goodPrice);
        jsonObject.addProperty("goodPic", goodPic);
        jsonObject.addProperty("categoryId", categoryId);
        jsonObject.addProperty("categoryName", categoryName);
        String httpStr = jsonObject.toString();
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_GOOD_ADD), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultPostBody(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> updateGood(int goodId, String goodName, String goodDetail, String goodDiscount, String goodPrice, String goodPic, int categoryId, String categoryName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", goodId);
        jsonObject.addProperty("goodName", goodName);
        jsonObject.addProperty("goodDetail", goodDetail);
        jsonObject.addProperty("goodDiscount", goodDiscount);
        jsonObject.addProperty("goodPrice", goodPrice);
        jsonObject.addProperty("goodPic", goodPic);
        jsonObject.addProperty("categoryId", categoryId);
        jsonObject.addProperty("categoryName", categoryName);
        String httpStr = jsonObject.toString();
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_GOOD_UPDATE), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultPostBody(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> removeAllBanner() {
        String httpStr = "";
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_BANNER_REMOVE_ALL), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultGetJson(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> getBannerList() {
        String httpStr = "";
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_BANNER_LIST), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultGetJson(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> getCategoryList() {
        String httpStr = "";
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_CATEGORY_LIST), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultGetJson(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> getGoodList(int rows, int page) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("rows", rows);
        jsonObject.addProperty("page", page);
        String httpStr = jsonObject.toString();
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_GOOD_LIST + "?row=" + rows + "&page=" + page),
                null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultGetJson(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }


    public static final ServerResult<CommonResultMessage> getGoodListByKey(String key) {
        String httpStr = "";
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_GOOD_LIST_BY_KEY + "?key=" + key),
                null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultGetJson(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    //新增用户
    public static final ServerResult<CommonResultMessage> getUserList() {
        String httpStr = "";
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_GOOD_LIST), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultGetJson(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> getGoodListByID(int goodId) {
        String httpStr = "";
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_GOOD_LIST_BY_ID + "?goodId=" + goodId),
                null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultGetJson(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> getUserById(int userId) {
        String httpStr = "";
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_USER_BY_ID + "?id=" + userId),
                null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultGetJson(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> postUserLogin(String mobile, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile", mobile);
        jsonObject.addProperty("password", password);
        String httpStr = jsonObject.toString();
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_USER_LOGIN), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultPostBody(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    //新增用户
    public static final ServerResult<CommonResultMessage> postUserInsert(String userName, String mobile, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userName", userName);
        jsonObject.addProperty("mobile", mobile);
        jsonObject.addProperty("password", password);
        String httpStr = jsonObject.toString();
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_USER_INSERT), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultPostBody(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

    public static final ServerResult<CommonResultMessage> updateUserAddress(int id, String address, String addressDetail) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("addressDetail", addressDetail);
        String httpStr = jsonObject.toString();
        HashMap<String, String> paramsMap = new HashMap<>();
        HttpRequestParam.addCommmonPostRequestValue(Global.getApplicationContext(), paramsMap);
        HttpCommon httpCommon = new HttpCommon(ApiUrlManger.appendUrl(ApiUrlManger.API_USER_UPDATE_ADDRESS), null);
        ServerResultHeader csResult = httpCommon.getResponseAsCsResultPostBody(paramsMap, httpStr);
        ServerResult<CommonResultMessage> resTagList = new ServerResult<CommonResultMessage>();
        if (csResult != null) {
            String responseStr = csResult.getResponseJson();
            resTagList.setCsResult(csResult);
            if (!TextUtils.isEmpty(responseStr)) {
                try {
                    CommonResultMessage userRegisterCompleteBean = new Gson().fromJson(responseStr, CommonResultMessage.class);
                    resTagList.setResultBean(userRegisterCompleteBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resTagList;
    }

}
