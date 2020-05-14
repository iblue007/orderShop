package com.xjt.ordershop.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.ordershop.R;
import com.xjt.ordershop.aop.singleclick.SingleClick;
import com.xjt.ordershop.base.BaseActivity;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.util.ApiUrlManger;
import com.xjt.ordershop.util.BaseConfigPreferences;
import com.xjt.ordershop.util.MessageUtils;
import com.xjt.ordershop.util.NetApiUtil;
import com.xjt.ordershop.util.ThreadUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by xuqunxing on  2020/3/3
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.activity_setting_ip_et)
    EditText settingIpEt;
    @BindView(R.id.activity_setting_password_et)
    EditText passwordEt;
    @BindView(R.id.activity_setting_password2_et)
    EditText passwordEt2;
    @BindView(R.id.activity_setting_mobile_et)
    EditText mobileEt;
    @BindView(R.id.activity_setting_name_et)
    EditText nameEt;
    @BindView(R.id.activity_setting_submit_tv)
    TextView submitTv;
    @BindView(R.id.common_back_rl)
    RelativeLayout backRl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.common_toolbar_rl)
    RelativeLayout commonToolbarRl;
    public static String VALUE_TYPE = "value_type";
    private int fromType = 0;//1：设置ip 2：添加用户 3: 设置餐厅地址

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        fromType = getIntent().getIntExtra(VALUE_TYPE, 0);
        if (fromType == 1) {
            String ipValue = BaseConfigPreferences.getInstance(SettingActivity.this).getValue(BaseConfigPreferences.APP_IP, "127.0.0.1");
            if (!TextUtils.isEmpty(ipValue)) {
                settingIpEt.setText(ipValue);
            }
            commonTitleTv.setText("修改ip地址");
            settingIpEt.setVisibility(View.VISIBLE);
            passwordEt.setVisibility(View.GONE);
            passwordEt2.setVisibility(View.GONE);
            mobileEt.setVisibility(View.GONE);
            nameEt.setVisibility(View.GONE);
        } else if (fromType == 2) {
            commonTitleTv.setText("添加用户");
            settingIpEt.setVisibility(View.GONE);
            passwordEt.setVisibility(View.VISIBLE);
            passwordEt2.setVisibility(View.VISIBLE);
            mobileEt.setVisibility(View.VISIBLE);
            nameEt.setVisibility(View.VISIBLE);
        } else if (fromType == 3) {
            commonTitleTv.setText("设置餐厅地址");
            settingIpEt.setHint("请输入餐厅地址");
            settingIpEt.setVisibility(View.VISIBLE);
            passwordEt.setVisibility(View.GONE);
            passwordEt2.setVisibility(View.GONE);
            mobileEt.setVisibility(View.GONE);
            nameEt.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.activity_setting_submit_tv, R.id.common_back_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_rl:
                finish();
                break;
            case R.id.activity_setting_submit_tv:
                doSubmit(view);
                break;

        }
    }

    @SingleClick
    private void doSubmit(View view) {
        if (fromType == 1) {
            String ipString = settingIpEt.getText().toString();
            if (!TextUtils.isEmpty(ipString)) {
                BaseConfigPreferences.getInstance(SettingActivity.this).setValue(BaseConfigPreferences.APP_IP, ipString);
                settingIpEt.setText(ipString);
                MessageUtils.show(SettingActivity.this, "保存成功");
                ApiUrlManger.setBaseUrl(ipString);
            }
            finish();
        } else if (fromType == 2) {
            final String phone = mobileEt.getText().toString();
            final String userName = nameEt.getText().toString();
            final String password = passwordEt.getText().toString();
            final String password2 = passwordEt2.getText().toString();
            if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(password2) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(userName) && password.equals(password2)) {
                ThreadUtil.executeMore(new Runnable() {
                    @Override
                    public void run() {
                        ServerResult<CommonResultMessage> goodsCookBookDetail = NetApiUtil.postUserInsert(userName, phone, password);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (goodsCookBookDetail != null && goodsCookBookDetail.getResultBean() != null) {
                                    CommonResultMessage resultBean = goodsCookBookDetail.getResultBean();
                                    String message = resultBean.getMessage();
                                    resultBean.isSuccess();
                                    MessageUtils.show(SettingActivity.this, message);
                                    finish();
                                } else {
                                    MessageUtils.show(SettingActivity.this, "程序出错，请稍后再试！");
                                }
                            }
                        });
                    }
                });
            } else {
                MessageUtils.show(SettingActivity.this, "输入的内容不合法");
            }
        }
        if (fromType == 3) {
            String addressStr = settingIpEt.getText().toString();
            if (!TextUtils.isEmpty(addressStr)) {
                BaseConfigPreferences.getInstance(SettingActivity.this).setValue(BaseConfigPreferences.LOGIN_SHOP_ADDRESS, addressStr);
                MessageUtils.show(SettingActivity.this, "保存成功");
            }
            finish();
        }
    }

}
