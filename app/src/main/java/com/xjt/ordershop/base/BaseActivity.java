package com.xjt.ordershop.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.xjt.ordershop.callback.RequestPermissionCallBack;
import com.xjt.ordershop.util.LogUtils;
import com.xjt.ordershop.util.RequestPermissionUtil;

/**
 * Create by xuqunxing on  2020/3/2
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.e("======", "======requestCode:" + requestCode + "----grantResults:" + grantResults.toString());
        if (requestCode == RequestPermissionUtil.mRequestCode) {
            RequestPermissionUtil.requestPermissions(this, false, permissions, new RequestPermissionCallBack() {
                @Override
                public void granted() {
                    requestPermissionTest();
                }

                @Override
                public void denied() {

                }
            });
        }
    }

    protected void requestPermissionTest() {
    }
}
