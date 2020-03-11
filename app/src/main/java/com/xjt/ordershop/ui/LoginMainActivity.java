package com.xjt.ordershop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.baselib.bean.Good;
import com.xjt.baselib.bean.User;
import com.xjt.ordershop.R;
import com.xjt.ordershop.aop.singleclick.SingleClick;
import com.xjt.ordershop.base.BaseActivity;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.util.BaseConfigPreferences;
import com.xjt.ordershop.util.MessageUtils;
import com.xjt.ordershop.util.NetApiUtil;
import com.xjt.ordershop.util.ThreadUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginMainActivity extends BaseActivity {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.common_back_rl)
    RelativeLayout backRl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.login_login_tv)
    TextView loginTv;
    @BindView(R.id.login_login_phone_et)
    EditText phoneEt;
    @BindView(R.id.login_login_pwd_et)
    EditText pwdEt;

    @BindView(R.id.login_login_register_tv)
    TextView loginRegisterTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        commonTitleTv.setText("登录");
        ImmersionBar.with(this).statusBarView(statusBarView)
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    @OnClick({R.id.common_back_rl, R.id.login_login_register_tv, R.id.login_login_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_rl:
                finish();
                break;

            case R.id.login_login_register_tv:
                Intent intent = new Intent(LoginMainActivity.this, SettingActivity.class);
                intent.putExtra(SettingActivity.VALUE_TYPE, 2);
                startActivity(intent);
                break;

            case R.id.login_login_tv:
                String phoneStr = phoneEt.getText().toString();
                String pwdStr = pwdEt.getText().toString();
                if (!TextUtils.isEmpty(phoneStr) && !TextUtils.isEmpty(pwdStr)) {
                    userLogin(view, phoneStr, pwdStr);
                } else {
                    MessageUtils.show(LoginMainActivity.this, "参数不能为空");
                }
                break;

        }
    }

    @SingleClick
    private void userLogin(View view, String phoneStr, String pwdStr) {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                ServerResult<CommonResultMessage> commonResultMessageServerResult = NetApiUtil.postUserLogin(phoneStr, pwdStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (commonResultMessageServerResult != null && commonResultMessageServerResult.getResultBean() != null) {
                            CommonResultMessage resultBean = commonResultMessageServerResult.getResultBean();
                            if (resultBean.isSuccess()) {
                                User user = new Gson().fromJson(resultBean.getMessage(), User.class);
                                MessageUtils.show(LoginMainActivity.this, "登录成功");
                                BaseConfigPreferences.getInstance(LoginMainActivity.this).setLoginAccount(phoneStr);
                                BaseConfigPreferences.getInstance(LoginMainActivity.this).setLoginUserRole(user.getRole());
                                BaseConfigPreferences.getInstance(LoginMainActivity.this).setValue(BaseConfigPreferences.LOGIN_NAME, user.getUserName());
                                BaseConfigPreferences.getInstance(LoginMainActivity.this).setLoginUserId(user.getId());
                                if (!TextUtils.isEmpty(user.getAddress()) && !TextUtils.isEmpty(user.getAddressDetail())) {
                                    BaseConfigPreferences.getInstance(LoginMainActivity.this).setLoginAddress(user.getAddress() + " " + user.getAddressDetail());
                                } else {
                                    BaseConfigPreferences.getInstance(LoginMainActivity.this).setLoginAddress("");
                                }
                                finish();
                            } else {
                                MessageUtils.show(LoginMainActivity.this, resultBean.getMessage());
                            }
                        } else {
                            MessageUtils.show(LoginMainActivity.this, "程序出错，请稍后再试！");
                        }
                    }
                });
            }
        });
    }

}
