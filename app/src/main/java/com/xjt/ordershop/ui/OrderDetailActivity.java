package com.xjt.ordershop.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.baselib.bean.Good;
import com.xjt.baselib.bean.Order;
import com.xjt.baselib.bean.User;
import com.xjt.ordershop.R;
import com.xjt.ordershop.aop.singleclick.SingleClick;
import com.xjt.ordershop.base.BaseActivity;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.callback.OnClickItemCallBack;
import com.xjt.ordershop.util.BaseConfigPreferences;
import com.xjt.ordershop.util.CommonUtil;
import com.xjt.ordershop.util.Global;
import com.xjt.ordershop.util.LogUtils;
import com.xjt.ordershop.util.MessageUtils;
import com.xjt.ordershop.util.NetApiUtil;
import com.xjt.ordershop.util.ScreenUtil;
import com.xjt.ordershop.util.ThreadUtil;
import com.xjt.ordershop.widget.TipDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by xuqunxing on  2020/3/2
 */
public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.common_back_rl)
    RelativeLayout backRl;
    @BindView(R.id.good_detail_tv)
    TextView detailTv;
    @BindView(R.id.good_category_tv)
    TextView categoryTv;
    @BindView(R.id.good_discount_tv)
    TextView discountTv;
    @BindView(R.id.good_price_tv)
    TextView priceTv;
    @BindView(R.id.good_name_tv)
    TextView nameTv;
    @BindView(R.id.good_image_Iv)
    ImageView imageView;
    @BindView(R.id.good_add_tv)
    Button addBt;
    @BindView(R.id.good_order_no_ll)
    LinearLayout orderNoLL;
    @BindView(R.id.good_order_no_tv)
    TextView orderNoTv;
    @BindView(R.id.good_order_address_tv)
    TextView addressTv;
    @BindView(R.id.good_order_phone_tv)
    TextView phoneTv;
    @BindView(R.id.good_order_buyer_tv)
    TextView buyerTv;
    @BindView(R.id.good_order_phone_ll)
    LinearLayout phoneLL;
    @BindView(R.id.good_order_phone_iv)
    ImageView phoneIv;
    public static String KEY_ORDER_ID = "key_order_id";
    private int orderId = 0;
    private Order orderBean;
    private float scale = 1920 * 1.0f / 1080;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true, 0.2f).init();
        orderId = getIntent().getIntExtra(KEY_ORDER_ID, 0);
        if (orderId == 0) {
            MessageUtils.show(this, "订单ID 不能为空");
            finish();
        }
        addBt.setVisibility(View.GONE);
        categoryTv.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = ScreenUtil.getStatusBarHeight(this);
            RelativeLayout.LayoutParams layoutParamsToolbar = (RelativeLayout.LayoutParams) backRl.getLayoutParams();
            layoutParamsToolbar.topMargin = statusBarHeight;
        }
        // int currentScreenWidth = ScreenUtil.getCurrentScreenWidth(this);
        int bannerRlHeigh = (ScreenUtil.getCurrentScreenHeight(this) * 4) / 5;
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams1.width = ScreenUtil.getCurrentScreenWidth(this);
        layoutParams1.height = bannerRlHeigh;
        LogUtils.e("====", "====hei:" + bannerRlHeigh);
        imageView.setLayoutParams(layoutParams1);
        if (BaseConfigPreferences.getInstance(this).getLoginUserRole() == 0) {
            phoneIv.setVisibility(View.GONE);
        }
        initData();
    }

    @OnClick({R.id.common_back_rl, R.id.good_order_phone_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_rl:
                finish();
                break;
            case R.id.good_order_phone_ll:
                if (BaseConfigPreferences.getInstance(this).getLoginUserRole() > 0) {
                    callPhone(view, orderBean.getBuyerphone());
                }
                break;
        }
    }

    private void initData() {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                ServerResult<CommonResultMessage> goodsCookBookDetail = NetApiUtil.getOrderById(orderId);
                Global.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (goodsCookBookDetail != null && goodsCookBookDetail.getResultBean() != null) {
                            CommonResultMessage resultBean = goodsCookBookDetail.getResultBean();
                            if (resultBean.isSuccess()) {
                                String message = resultBean.getMessage();
                                if (!TextUtils.isEmpty(message)) {
                                    try {
                                        orderBean = new Gson().fromJson(message, Order.class);
                                        if (orderBean != null) {
                                            nameTv.setText(orderBean.getGoodName());
                                            discountTv.setText(CommonUtil.stripZeros(orderBean.getGoodPrice() + ""));
                                            //categoryTv.setText(orderBean.getCategoryId());
                                            detailTv.setText(orderBean.getGoodDetail());
                                            ImageLoader.getInstance().displayImage(orderBean.getGoodPic(), imageView);
                                            buyerTv.setText(orderBean.getBuyerName());
                                            phoneTv.setText(orderBean.getBuyerphone());
                                            addressTv.setText(orderBean.getBuyerAddress());
                                            priceTv.setText(CommonUtil.stripZeros(CommonUtil.mul(Double.parseDouble(orderBean.getGoodPrice()), Double.parseDouble(orderBean.getGoodDiscount())) + ""));
                                            orderNoTv.setText(orderId + "");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        MessageUtils.show(OrderDetailActivity.this, message);
                                    }
                                }
                            }
                        } else {
                            MessageUtils.show(OrderDetailActivity.this, "程序出错，请稍后再试！");
                        }

                    }
                });
            }
        });
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
