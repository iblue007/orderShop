package com.xjt.ordershop.intercepter;

import android.util.Log;

import com.xjt.ordershop.base.basehttp.ServerResultInterceptor;

/**
 * 网络请求返回码统一监听类
 * Create by xuqunxing on  2019/4/2
 */
public class NetConnectionIntercepter extends ServerResultInterceptor {
    @Override
    public void onIntercept(String message, int code) {
        Log.e("======", "======NetIntercepter-code:" + code);
        if (code == 10013) {
            try {
//                BaseConfigPreferences.getInstance(Global.getContext()).setUserToken("");
//                BaseConfigPreferences.getInstance(Global.getContext()).setAppAuthState(9);
//                Intent intent = new Intent(Global.getContext(), OfflineNotificationActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra(OfflineNotificationActivity.KEY_MESSAGE, message);
//                Global.getContext().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (code == 10033) {
            try {
//                BaseConfigPreferences.getInstance(Global.getContext()).setUserToken("");
//                BaseConfigPreferences.getInstance(Global.getContext()).setAppAuthState(9);
//                Intent intent = new Intent(Global.getContext(), ForbiddenTipActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra(ForbiddenTipActivity.KEY_MESSAGE, message);
//                Global.getContext().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
