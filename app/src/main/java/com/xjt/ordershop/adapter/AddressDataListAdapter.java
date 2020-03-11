package com.xjt.ordershop.adapter;


import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xjt.baselib.bean.Address;
import com.xjt.baselib.bean.BannerBean;
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.ordershop.R;
import com.xjt.ordershop.aop.singleclick.SingleClick;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.callback.OnObjectCallback;
import com.xjt.ordershop.ui.AddressListActivity;
import com.xjt.ordershop.ui.LoginMainActivity;
import com.xjt.ordershop.util.BaseConfigPreferences;
import com.xjt.ordershop.util.Global;
import com.xjt.ordershop.util.MessageUtils;
import com.xjt.ordershop.util.NetApiUtil;
import com.xjt.ordershop.util.ThreadUtil;

import java.util.List;

/**
 * Create by xuqunxing on  2019/8/26
 */
public class AddressDataListAdapter extends BaseAdapter<Address, BaseViewHolder> {

    public AddressDataListAdapter(int layoutResId, @Nullable List<Address> data) {
        super(R.layout.address_data_item, data);
    }

    public AddressDataListAdapter(@Nullable List<Address> data) {
        super(R.layout.address_data_item, data);
    }

    private boolean noShowState = false;
    private OnObjectCallback onObjectCallback;

    public AddressDataListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Address address, int i) {
        TextView titleTv = baseViewHolder.getView(R.id.home_item_title_tv);
        TextView stateTv = baseViewHolder.getView(R.id.home_item_state_tv);
        TextView phoneTv = baseViewHolder.getView(R.id.home_item_phone_tv);
        TextView addressTv = baseViewHolder.getView(R.id.home_item_address_tv);

        titleTv.setText(address.getName());
        phoneTv.setText(address.getPhone());
        addressTv.setText(address.getAddress() + " " + address.getAddressDetail());
        if (!noShowState) {
            stateTv.setVisibility(View.VISIBLE);
            if (address.getState() == 0) {
                stateTv.setText("设为默认地址");
                stateTv.setTextColor(Color.parseColor("#ff0000"));
            } else {
                stateTv.setText("默认地址");
                stateTv.setTextColor(Color.parseColor("#999999"));
            }
            stateTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (address.getState() == 0) {
                        updateAddress(v, address);
                    }
                }
            });
        }else {
            stateTv.setVisibility(View.GONE);
        }
    }

    @SingleClick
    private void updateAddress(View v, Address address) {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                ServerResult<CommonResultMessage> commonResultMessageServerResult = NetApiUtil.updateAddressState(address.getId(), (address.getState() + 1));
                int loginUserId = BaseConfigPreferences.getInstance(mContext).getLoginUserId();
                NetApiUtil.updateUserAddress(loginUserId, address.getAddress(), address.getAddressDetail());
                Global.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (commonResultMessageServerResult != null && commonResultMessageServerResult.getResultBean() != null) {
                            CommonResultMessage resultBean = commonResultMessageServerResult.getResultBean();
                            if (resultBean.isSuccess()) {
                                String message = resultBean.getMessage();
                                MessageUtils.show(mContext, message);
                            }
                            if (!TextUtils.isEmpty(address.getAddress()) && !TextUtils.isEmpty(address.getAddressDetail())) {
                                BaseConfigPreferences.getInstance(mContext).setLoginAddress(address.getAddress() + " " + address.getAddressDetail());
                            } else {
                                BaseConfigPreferences.getInstance(mContext).setLoginAddress("");
                            }
                            if (onObjectCallback != null) {
                                onObjectCallback.onClickCallBack("");
                            }
                        } else {
                            MessageUtils.show(mContext, "程序出错，请稍后再试！");
                        }
                    }
                });
            }
        });
    }

    public void setCallBack(OnObjectCallback onObjectCallback1) {
        onObjectCallback = onObjectCallback1;
    }

    public void setNoShowState(boolean noShowState1) {
        noShowState = noShowState1;
    }
}
