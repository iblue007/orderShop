package com.xjt.ordershop.ui;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.baselib.bean.Good;
import com.xjt.ordershop.R;
import com.xjt.ordershop.adapter.CommonDataListAdapter;
import com.xjt.ordershop.adapter.WrapWrongLinearLayoutManger;
import com.xjt.ordershop.base.BaseActivity;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.util.MessageUtils;
import com.xjt.ordershop.util.NetApiUtil;
import com.xjt.ordershop.util.RequestPermissionUtil;
import com.xjt.ordershop.util.ThreadUtil;

import java.util.List;

public class TestActivity extends BaseActivity {

    //    @BindView(R.id.app_user_list_rv)
    RecyclerView appHomeSaleRv;
    private CommonDataListAdapter commonDataListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final EditText phoneEt = findViewById(R.id.login_phone_et);
        final EditText userNameEt = findViewById(R.id.login_username_et);
        final EditText emainEt = findViewById(R.id.login_email_et);
        final Button goodBt = findViewById(R.id.login_good_tv);
        final Button settingTv = findViewById(R.id.login_setting_tv);
        appHomeSaleRv = findViewById(R.id.app_user_list_rv);
        appHomeSaleRv.setLayoutManager(new WrapWrongLinearLayoutManger(TestActivity.this));
        commonDataListAdapter = new CommonDataListAdapter(null);
        appHomeSaleRv.setAdapter(commonDataListAdapter);
        goodBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this, GoodInfoActivity.class));
            }
        });
        settingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this, SettingActivity.class));
            }
        });
        findViewById(R.id.login_login_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = phoneEt.getText().toString();
                final String userName = userNameEt.getText().toString();
                final String emain = emainEt.getText().toString();
                ThreadUtil.executeMore(new Runnable() {
                    @Override
                    public void run() {
                        ServerResult<CommonResultMessage> goodsCookBookDetail = NetApiUtil.postUserInsert(userName, phone, emain);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (goodsCookBookDetail != null && goodsCookBookDetail.getResultBean() != null) {
                                    CommonResultMessage resultBean = goodsCookBookDetail.getResultBean();
                                    String message = resultBean.getMessage();
                                    resultBean.isSuccess();
                                    MessageUtils.show(TestActivity.this, message);
                                } else {
                                    MessageUtils.show(TestActivity.this, "程序出错，请稍后再试！");
                                }
                            }
                        });
                    }
                });
            }
        });
        findViewById(R.id.login_user_list_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadUtil.executeMore(new Runnable() {
                    @Override
                    public void run() {
                        ServerResult<CommonResultMessage> goodsCookBookDetail = NetApiUtil.getUserList();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (goodsCookBookDetail != null && goodsCookBookDetail.getResultBean() != null) {
                                    CommonResultMessage resultBean = goodsCookBookDetail.getResultBean();
                                    if (resultBean.isSuccess()) {
                                        String message = resultBean.getMessage();
                                        if (!TextUtils.isEmpty(message)) {
                                            try {
                                                List<Good> list = new Gson().fromJson(message, new TypeToken<List<Good>>() {
                                                }.getType());
                                                if (list != null && list.size() > 0) {

                                                    commonDataListAdapter.setNewData(list);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } else {
                                    MessageUtils.show(TestActivity.this, "程序出错，请稍后再试！");
                                }
                            }
                        });
                    }
                });
            }
        });
    }

}
