package com.xjt.ordershop.config;

public class AppConfig {
    /**sentry 测试地址======*/
    public  static  final String SENTRY_DSN="https://25fc497fd25946e4bad42b967f18511b:a15b59a9d1514d8abc3f2e2ab88de952@sentry.dongpin.cn/25";
    /**sentry正式地址======*/
//      public  static  final String SENTRY_DSN="https://352f43a418554b1b9b7b93d1397af1e7:dc31633275284e85a95082b45d414ec3@sentry.dongpin.cn/27";


    /**友盟测试=======*/
    public  static  final  String UM_APPID="5c11d81ff1f556ad390000bf";
    /**友盟正式=======*/
//    public  static  final  String UM_APPID="5cc599e40cafb2a297000d9e";


    /**微信授权以及分享相关 正式跟测试是同一个*/
    public static final String WX_APPID = "wxc9813ce066b9d6d3";
    public static final String WX_APPSECRET  = "59977a188c3abf58063e5111febe0fbc";


    public static final String WX_LOGIN_STATE = "wx_login_duzun";
    public static final String WX_LOGIN_BIND_STATE = "wx_bind_duzun";
    public static final String WECHAT_LOGIN = "com.ionicframework.dpshop573861.wechat.dpzxlogin";
    public static final String WECHAT_BIND = "com.ionicframework.dpshop573861.wechat.dpzxbind";


    private static final String LoginApp = "com.zhxh.login.LoginApp";
    private static final String ShareApp = "com.zhxh.login.ShareApp";
    public static final String NO_PRICE = "暂无报价";
    public static final String SELL_OUT = "已售罄";
    public static final String NO_SELL = "暂不销售";
    public static final String SELL_OUT_BUHUO = "售罄,补货中";
    public static final String SELL_OUT_QIANGGUAN = "抢光了,恢复原价";
    public static final String NO_STOCK = "库存不足";
    public static final int ROWS = 20;
    public static final String WECHAT_PAY_ACTION = "com.ionicframework.dpshop573861.wechat.pay";
    public static final String REFESH_EVALUATE_ACTION = "com.ionicframework.dpshop573861.refesh.evaluate";
    public static final String FROM_TYPE_ORDER_MY_ORDER = "OrderListFragment";
    public static final String FROM_TYPE_ORDER_DETAIL = "OrderDetailActivity";
    public static final int EVALUATE_REQUEST_CODE = 1000;
    public static final int EVALUATE_SUCCESS_REQUEST_CODE = 1001;
    public static final int AFTER_SALE_REQUEST_CODE = 1002;
    public static final int AFTER_SALE_REQUEST_CODE_SUCCESS = 1003;
    public static final int REFRESH_COMFIRM_CODE_SUCCESS = 1004;
    public static final int GO_AFTER_SALE = 1005;
    public static final int FROM_TYPE_COMFIRM_DETAIL_PAGE = 1006;
    public static final int POINT_CHANGE_CODE = 1007;
    public static final int GO_ORDER_LIST = 1008;
    public static final int GO_PAY_ACTIVITY = 1009;
    public static final int FROME_BANNER = 0xb1;
    public static final int FROME_PUSH = 0xb2;
    public static final int FROM_LIUYAN = 0xb3;
    public static final int FROM_LIUYAN_SECCESS = 0xb4;
    public static final int FROM_SMS = 0xb5;
    public static double POSISION_LNG = 0;
    public static double POSISION_LAT = 0;
    public static boolean isCheckAll = false;
    public static int STOCK_THRESHOLD = 0;
    public static String pay_order_detail = "orderDetail";
    public static String pay_order_List = "orderList";
    public static String pay_wallet = "pay_wallet";
    public static String pay_comfirm = "pay_comfirm";


    public static String[] moduleApps = {
            LoginApp,
            ShareApp
    };
}
