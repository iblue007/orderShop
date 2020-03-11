package com.xjt.ordershop.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.xjt.ordershop.R;
import com.xjt.ordershop.aop.singleclick.SingleClick;
import com.xjt.ordershop.base.BaseFragment;
import com.xjt.ordershop.ui.AddressListActivity;
import com.xjt.ordershop.ui.AddressSetActivity;
import com.xjt.ordershop.ui.BannerListActivity;
import com.xjt.ordershop.ui.BannerSettingActivity;
import com.xjt.ordershop.ui.GoodInfoActivity;
import com.xjt.ordershop.ui.LoginMainActivity;
import com.xjt.ordershop.ui.SettingActivity;
import com.xjt.ordershop.util.BaseConfigPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by xuqunxing on  2020/3/5
 */
public class MyFragment extends BaseFragment {

    @BindView(R.id.my_setting_ip_rl)
    RelativeLayout ipRl;
    @BindView(R.id.my_setting_ip_tv)
    TextView ipTv;
    @BindView(R.id.my_setting_logout_tv)
    TextView loginTv;
    @BindView(R.id.my_setting_username_tv)
    TextView userNameTv;
    @BindView(R.id.my_setting_phone_tv)
    TextView phoneTv;
    @BindView(R.id.my_setting_username_rl)
    RelativeLayout userNameRl;
    @BindView(R.id.my_setting_banner_rl)
    RelativeLayout bannerRl;
    @BindView(R.id.my_setting_good_rl)
    RelativeLayout addGoodRl;
    @BindView(R.id.my_setting_phone_rl)
    RelativeLayout callPhoneRl;
    @BindView(R.id.my_setting_address_rl)
    RelativeLayout addressRl;
    @BindView(R.id.my_setting_address_tv)
    TextView addressTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        //绑定 ButterKnife
        ButterKnife.bind(this, view);
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true, 0.2f).init();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        String ipValue = BaseConfigPreferences.getInstance(getContext()).getValue(BaseConfigPreferences.APP_IP, "127.0.0.1");
        if (!TextUtils.isEmpty(ipValue)) {
            ipTv.setText(ipValue);
        }
        if (!TextUtils.isEmpty(BaseConfigPreferences.getInstance(getContext()).getLoginAccount())) {
            loginTv.setText("退出登录");
            userNameTv.setText(BaseConfigPreferences.getInstance(getContext()).getValue(BaseConfigPreferences.LOGIN_NAME));
            userNameRl.setVisibility(View.VISIBLE);
            int loginUserRole = BaseConfigPreferences.getInstance(getContext()).getLoginUserRole();
            if (loginUserRole == 1) {
                callPhoneRl.setVisibility(View.VISIBLE);
                bannerRl.setVisibility(View.GONE);
                addGoodRl.setVisibility(View.GONE);
            } else {
                bannerRl.setVisibility(View.VISIBLE);
                addGoodRl.setVisibility(View.VISIBLE);
                callPhoneRl.setVisibility(View.GONE);
            }
        } else {
            loginTv.setText("去登录");
            userNameRl.setVisibility(View.GONE);
            bannerRl.setVisibility(View.GONE);
            addGoodRl.setVisibility(View.GONE);
            callPhoneRl.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.my_setting_ip_rl, R.id.my_setting_logout_tv, R.id.my_setting_banner_rl, R.id.my_setting_good_rl, R.id.my_setting_phone_rl, R.id.my_setting_address_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.my_setting_ip_rl:
                Intent intent = new Intent(getContext(), SettingActivity.class);
                intent.putExtra(SettingActivity.VALUE_TYPE, 1);
                startActivity(intent);
                break;

            case R.id.my_setting_address_rl:
                startActivityForResult(new Intent(getContext(), AddressListActivity.class), 4000);
                break;
            case R.id.my_setting_logout_tv:
                String loginTvStr = loginTv.getText().toString();
                if (loginTvStr.equals("退出登录")) {
                    BaseConfigPreferences.getInstance(getContext()).setLoginAccount("");
                }
                startActivity(new Intent(getContext(), LoginMainActivity.class));
                break;

            case R.id.my_setting_banner_rl:
                Intent intent2 = new Intent(getContext(), BannerListActivity.class);
                startActivity(intent2);
                break;

            case R.id.my_setting_good_rl:
                Intent intent3 = new Intent(getContext(), GoodInfoActivity.class);
                startActivity(intent3);
                break;

            case R.id.my_setting_phone_rl:
                String phoneStr = phoneTv.getText().toString();
                if (!TextUtils.isEmpty(phoneStr)) {
                    callPhone(view, phoneStr);
                }
                break;
        }
    }

    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    @SingleClick
    public void callPhone(View view, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

}
