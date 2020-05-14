package com.xjt.ordershop.adapter;


import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xjt.baselib.R;
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.baselib.bean.Good;
import com.xjt.baselib.bean.Order;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.ui.GoodDetailActivity;
import com.xjt.ordershop.util.BaseConfigPreferences;
import com.xjt.ordershop.util.CommonUtil;
import com.xjt.ordershop.util.Global;
import com.xjt.ordershop.util.LogUtils;
import com.xjt.ordershop.util.MessageUtils;
import com.xjt.ordershop.util.NetApiUtil;
import com.xjt.ordershop.util.ThreadUtil;

import java.util.List;

/**
 * Create by xuqunxing on  2019/8/26
 */
public class OrderDataListAdapter extends BaseAdapter<Order, BaseViewHolder> {

    public OrderDataListAdapter(int layoutResId, @Nullable List<Order> data) {
        super(R.layout.order_data_item, data);
    }

    public OrderDataListAdapter(@Nullable List<Order> data) {
        super(R.layout.order_data_item, data);
    }

    public OrderDataListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Order good, int i) {
        ImageView picIv = baseViewHolder.getView(R.id.home_item_pic_iv);
        TextView titleTv = baseViewHolder.getView(R.id.home_item_title_tv);
        TextView discountTv = baseViewHolder.getView(R.id.home_item_discount_tv);
        TextView priceTv = baseViewHolder.getView(R.id.home_item_price_tv);
        TextView stateTv = baseViewHolder.getView(R.id.home_item_state_tv);
        Button stateDoBt = baseViewHolder.getView(R.id.good_state_do_tv);
        LogUtils.e("======", "======pic:" + good.getGoodPic());
//        Glide.with(Global.getContext()).load(user.getGoodPic()).into(picIv);
        stateDoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadUtil.executeMore(new Runnable() {
                    @Override
                    public void run() {
                        good.setGoodState(good.getGoodState() + 1);
                        ServerResult<CommonResultMessage> commonResultMessageServerResult = NetApiUtil.udpateOrder(good.getId(), good.getGoodId(), good.getCategoryId(), good.getGoodState(), good.getGoodName(), good.getGoodDetail(), good.getGoodDiscount() + "",
                                good.getGoodPrice() + "", good.getGoodPic());
                        Global.runInMainThread(new Runnable() {
                            @Override
                            public void run() {
                                if (commonResultMessageServerResult != null && commonResultMessageServerResult.getResultBean() != null) {
                                    CommonResultMessage resultBean = commonResultMessageServerResult.getResultBean();
                                    MessageUtils.show(mContext, resultBean.getMessage());
                                    orderStateDo(good.getGoodState(), stateTv, stateDoBt);
                                } else {
                                    MessageUtils.show(mContext, "程序出错，请稍后再试！");
                                }
                            }
                        });
                    }
                });
            }
        });
        ImageLoader.getInstance().displayImage(good.getGoodPic(), picIv);
        titleTv.setText(good.getGoodName());
        discountTv.setText("折扣：" + CommonUtil.stripZeros(good.getGoodDiscount() + ""));
        priceTv.setText("价格：" + CommonUtil.stripZeros(CommonUtil.mul(Double.parseDouble(good.getGoodPrice()), CommonUtil.div(Double.parseDouble(good.getGoodDiscount()),10)) + ""));
        // categoryTc.setText("所属分类：" + good.getCategoryName());

        int goodState = good.getGoodState();
        stateDoBt.setVisibility(View.GONE);
        orderStateDo(goodState, stateTv, stateDoBt);
    }

    private void orderStateDo(int goodState, TextView stateTv, Button stateDoBt) {
        int loginUserRole = BaseConfigPreferences.getInstance(mContext).getLoginUserRole();
        if (goodState == 0) {
            stateTv.setText("状态：已下单");
            if (loginUserRole == 0) {
                stateDoBt.setVisibility(View.VISIBLE);
                stateDoBt.setText("配送");
            } else if (loginUserRole == 1) {
                stateDoBt.setVisibility(View.GONE);
            }
        } else if (goodState == 1) {
            stateTv.setText("状态：配送中");
            stateDoBt.setVisibility(View.GONE);
            if (loginUserRole == 0) {
                stateDoBt.setVisibility(View.VISIBLE);
                stateDoBt.setText("送达客户");
            } else if (loginUserRole == 1) {
                stateDoBt.setVisibility(View.GONE);
            }
        } else if (goodState == 2) {
            stateTv.setText("状态：送达客户");
            if (loginUserRole == 0) {
                stateDoBt.setVisibility(View.GONE);
            } else if (loginUserRole == 1) {
                stateDoBt.setVisibility(View.VISIBLE);
                stateDoBt.setText("确认收货");
            }
        } else if (goodState == 3) {
            stateTv.setText("状态：客户确认收货");
            stateDoBt.setVisibility(View.GONE);
        }
    }

}
