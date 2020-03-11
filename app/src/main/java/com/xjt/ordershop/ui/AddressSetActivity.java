package com.xjt.ordershop.ui;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.xjt.baselib.bean.Address;
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.ordershop.R;
import com.xjt.ordershop.addresspickerview.AddressBean;
import com.xjt.ordershop.addresspickerview.AreaPickerView;
import com.xjt.ordershop.aop.singleclick.SingleClick;
import com.xjt.ordershop.base.BaseActivity;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.util.BaseConfigPreferences;
import com.xjt.ordershop.util.Global;
import com.xjt.ordershop.util.MessageUtils;
import com.xjt.ordershop.util.NetApiUtil;
import com.xjt.ordershop.util.ThreadUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by xuqunxing on  2020/3/10
 */
public class AddressSetActivity extends BaseActivity {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.common_back_rl)
    RelativeLayout backRl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.my_setting_address_rl)
    RelativeLayout addressRl;
    @BindView(R.id.activity_address_tv)
    TextView addressTv;
    @BindView(R.id.activity_address_detail_tv)
    TextView addressDetailTv;
    @BindView(R.id.activity_address_name_tv)
    TextView nameTv;
    @BindView(R.id.activity_address_phone_tv)
    TextView phoneTv;
    @BindView(R.id.activity_address_submit_tv)
    TextView submitTv;

    private AreaPickerView areaPickerView;
    private List<AddressBean> addressBeans;
    private int[] i;
    public static String KEY_EDIT = "key_edit";
    public static String KEY_ADDRESS = "key_address";
    private boolean isEdit = false;
    private Address address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        ImmersionBar.with(this).statusBarView(statusBarView)
                .statusBarDarkFont(true, 0.2f)
                .init();
        isEdit = getIntent().getBooleanExtra(KEY_EDIT, false);
        address = (Address) getIntent().getSerializableExtra(KEY_ADDRESS);
        if (isEdit) {
            commonTitleTv.setText("地址修改");
            if (address != null) {
                nameTv.setText(address.getName());
                phoneTv.setText(address.getPhone());
                addressTv.setText(address.getAddress());
                addressDetailTv.setText(address.getAddressDetail());
            }
            submitTv.setText("确认修改");
        } else {
            commonTitleTv.setText("地址选择");
            submitTv.setText("确认保存");
        }
        Gson gson = new Gson();
        addressBeans = gson.fromJson(getCityJson(), new TypeToken<List<AddressBean>>() {
        }.getType());

        areaPickerView = new AreaPickerView(this, R.style.Dialog, addressBeans);
        areaPickerView.setAreaPickerViewCallback(new AreaPickerView.AreaPickerViewCallback() {
            @Override
            public void callback(int... value) {
                i = value;
                if (value.length == 3)
                    addressTv.setText(addressBeans.get(value[0]).getLabel() + "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getLabel() + "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getLabel());
                else
                    addressTv.setText(addressBeans.get(value[0]).getLabel() + "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getLabel());
            }
        });
    }

    @OnClick({R.id.common_back_rl, R.id.my_setting_address_rl, R.id.activity_address_submit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_rl:
                finish();
                break;
            case R.id.my_setting_address_rl:
                areaPickerView.setSelect(i);
                areaPickerView.show();
                break;
            case R.id.activity_address_submit_tv:
                addAddress(view);
                break;
        }
    }

    @SingleClick
    private void addAddress(View view) {
        String detailAddressStr = addressDetailTv.getText().toString();
        String addressStr = addressTv.getText().toString();
        String nameStr = nameTv.getText().toString();
        String phoneStr = phoneTv.getText().toString();
        if (TextUtils.isEmpty(detailAddressStr) || TextUtils.isEmpty(addressStr) || TextUtils.isEmpty(nameStr) || TextUtils.isEmpty(phoneStr)) {
            MessageUtils.show(AddressSetActivity.this, "地址信息不能为空");
            return;
        }
        if (isEdit) {
            ThreadUtil.executeMore(new Runnable() {
                @Override
                public void run() {
                    ServerResult<CommonResultMessage> commonResultMessageServerResult = NetApiUtil.updateAddress(address.getId(), nameStr, phoneStr, addressStr, detailAddressStr, address.getState(), address.getAddressDate());
                    Global.runInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (commonResultMessageServerResult != null && commonResultMessageServerResult.getResultBean() != null) {
                                CommonResultMessage resultBean = commonResultMessageServerResult.getResultBean();
                                if (resultBean.isSuccess()) {
                                    String message = resultBean.getMessage();
                                    MessageUtils.show(AddressSetActivity.this, message);
                                }
                                finish();
                            } else {
                                MessageUtils.show(AddressSetActivity.this, "程序出错，请稍后再试！");
                            }
                        }
                    });
                }
            });
        } else {
            ThreadUtil.executeMore(new Runnable() {
                @Override
                public void run() {
                    ServerResult<CommonResultMessage> commonResultMessageServerResult = NetApiUtil.addAddress(nameStr, phoneStr, addressStr, detailAddressStr);
                    Global.runInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (commonResultMessageServerResult != null && commonResultMessageServerResult.getResultBean() != null) {
                                CommonResultMessage resultBean = commonResultMessageServerResult.getResultBean();
                                if (resultBean.isSuccess()) {
                                    String message = resultBean.getMessage();
                                    MessageUtils.show(AddressSetActivity.this, message);
                                }
                                finish();
                            } else {
                                MessageUtils.show(AddressSetActivity.this, "程序出错，请稍后再试！");
                            }
                        }
                    });
                }
            });
        }
    }

    private String getCityJson() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = this.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open("region.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
