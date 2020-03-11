package com.xjt.ordershop.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.xjt.ordershop.callback.OnClickItemCallBack;
import com.xjt.ordershop.callback.RequestPermissionCallBack;
import com.xjt.ordershop.widget.PermissionTipDialog;

/**
 * Create by xuqunxing on  2020/3/2
 */
public class RequestPermissionUtil {

    public static final int mRequestCode = 1024;

    /**
     * 发起权限请求
     *
     * @param context
     * @param permissions
     * @param mRequestPermissionCallBack
     */
    public static void requestPermissions(final Context context, boolean forceRequest, final String[] permissions,
                                          RequestPermissionCallBack mRequestPermissionCallBack) {
        StringBuilder permissionNames = new StringBuilder();
        for (String s : permissions) {
            permissionNames = permissionNames.append(s + "\r\n");
        }
        //如果所有权限都已授权，则直接返回授权成功,只要有一项未授权，则发起权限请求
        boolean isAllGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                isAllGranted = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    LogUtils.e("======", "======qq2222222222222222222222222222222222");
                    PermissionTipDialog permissionTipDialog = new PermissionTipDialog(context);
                    permissionTipDialog.show();
                    permissionTipDialog.setOnClickItemCallBack(new OnClickItemCallBack() {
                        @Override
                        public void onClickCallBack(String... value) {
                            ActivityCompat.requestPermissions(((Activity) context), permissions, mRequestCode);
                        }
                    });
                } else {
                    if (forceRequest) {
                        ActivityCompat.requestPermissions(((Activity) context), permissions, mRequestCode);
                    }
                }
                break;
            }
        }
        if (mRequestPermissionCallBack != null) {
            if (isAllGranted) {
                mRequestPermissionCallBack.granted();
            } else {
                mRequestPermissionCallBack.denied();
            }
        }
        return;
    }


}
