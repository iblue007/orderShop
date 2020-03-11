package com.xjt.ordershop.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by xuqunxing on  2020/3/2
 */
public class GoodDetailActivity extends BaseActivity {

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
    public static String KEY_GOOD_ID = "key_good_id";
    public static String KEY_ORDER_ID = "key_order_id";
    public static String KEY_IS_ORDER = "key_is_order";
    private int orderId = 0;
    private int goodId = 0;
    private Good good;
    private float scale = 1920 * 1.0f / 1080;
    private boolean isOrderDetail = false;
    private String loginAccount;
    private String loginName;
    private String longAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        ButterKnife.bind(this);
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true, 0.2f).init();
        isOrderDetail = getIntent().getBooleanExtra(KEY_IS_ORDER, false);
        if (!isOrderDetail) {
            addBt.setVisibility(View.VISIBLE);
            orderNoLL.setVisibility(View.GONE);
            goodId = getIntent().getIntExtra(KEY_GOOD_ID, 0);
            if (goodId == 0) {
                MessageUtils.show(this, "商品ID 不能为空");
                finish();
            }
        } else {
            addBt.setVisibility(View.GONE);
            orderNoLL.setVisibility(View.VISIBLE);
            goodId = getIntent().getIntExtra(KEY_GOOD_ID, 0);
            orderId = getIntent().getIntExtra(KEY_ORDER_ID, 0);
        }
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
        initData();
    }

    @OnClick({R.id.good_add_tv, R.id.common_back_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_rl:
                finish();
                break;
            case R.id.good_add_tv:
                addOrder(view);
                break;
        }
    }

    @SingleClick
    private void addOrder(View view) {
        if (good == null) {
            MessageUtils.show(GoodDetailActivity.this, "参数错误 请退出重试");
            return;
        }
        if (TextUtils.isEmpty(loginAccount)) {
            loginAccount = BaseConfigPreferences.getInstance(this).getLoginAccount();
        }
        if (TextUtils.isEmpty(loginAccount)) {
            loginName = BaseConfigPreferences.getInstance(this).getValue(BaseConfigPreferences.LOGIN_NAME);
        }
        if (TextUtils.isEmpty(loginAccount)) {
            longAddress = BaseConfigPreferences.getInstance(this).getLoginAddress();
        }
        if (!TextUtils.isEmpty(longAddress)) {
            ThreadUtil.executeMore(new Runnable() {
                @Override
                public void run() {
                    ServerResult<CommonResultMessage> commonResultMessageServerResult = NetApiUtil.addOrder(good.getId(), good.getCategoryId(), 0, good.getGoodName(), good.getGoodDetail(),
                            good.getGoodDiscount() + "", good.getGoodPrice() + "", good.getGoodPic(), loginName, loginAccount, longAddress);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (commonResultMessageServerResult != null && commonResultMessageServerResult.getResultBean() != null) {
                                CommonResultMessage resultBean = commonResultMessageServerResult.getResultBean();
                                MessageUtils.show(GoodDetailActivity.this, resultBean.getMessage());
                            } else {
                                MessageUtils.show(GoodDetailActivity.this, "程序出错，请稍后再试！");
                            }
                        }
                    });
                }
            });
        } else {
            TipDialog tipDialog = new TipDialog(GoodDetailActivity.this);
            tipDialog.show();
            tipDialog.setOnClickItemCallBack(new OnClickItemCallBack() {
                @Override
                public void onClickCallBack(String... value) {
                    startActivity(new Intent(GoodDetailActivity.this, AddressListActivity.class));
                }
            });
        }

    }

    private void initData() {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                ServerResult<CommonResultMessage> goodsCookBookDetail = NetApiUtil.getGoodListByID(goodId);
                ServerResult<CommonResultMessage> userById = NetApiUtil.getUserById(BaseConfigPreferences.getInstance(GoodDetailActivity.this).getLoginUserId());
                Global.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (goodsCookBookDetail != null && goodsCookBookDetail.getResultBean() != null) {
                            CommonResultMessage resultBean = goodsCookBookDetail.getResultBean();
                            if (resultBean.isSuccess()) {
                                String message = resultBean.getMessage();
                                if (!TextUtils.isEmpty(message)) {
                                    try {
                                        good = new Gson().fromJson(message, Good.class);
                                        if (good != null) {
                                            nameTv.setText(good.getGoodName());

                                            discountTv.setText(CommonUtil.stripZeros(good.getGoodPrice() + ""));
                                            categoryTv.setText(good.getCategoryName());
                                            detailTv.setText(good.getGoodDetail());
                                            ImageLoader.getInstance().displayImage(good.getGoodPic(), imageView);
                                            if (!isOrderDetail) {
                                                priceTv.setText(CommonUtil.stripZeros(good.getGoodPrice() + ""));
                                            } else {
                                                priceTv.setText(CommonUtil.stripZeros(CommonUtil.mul(good.getGoodPrice(), good.getGoodDiscount()) + ""));
                                                orderNoTv.setText(orderId + "");
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        MessageUtils.show(GoodDetailActivity.this, message);
                                    }
                                }
                            }
                        } else {
                            MessageUtils.show(GoodDetailActivity.this, "程序出错，请稍后再试！");
                        }

                        if (userById != null && userById.getResultBean() != null) {
                            CommonResultMessage resultBean = userById.getResultBean();
                            if (resultBean.isSuccess()) {
                                String message = resultBean.getMessage();
                                User user = new Gson().fromJson(message, User.class);
                                loginAccount = user.getMobile();
                                loginName = user.getUserName();
                                longAddress = user.getAddress() + " " + user.getAddressDetail();
                            }
                        }
                    }
                });
            }
        });
    }


}
