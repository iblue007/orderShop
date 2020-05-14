package com.xjt.ordershop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xjt.baselib.bean.Address;
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.baselib.bean.Good;
import com.xjt.ordershop.R;
import com.xjt.ordershop.adapter.AddressDataListAdapter;
import com.xjt.ordershop.adapter.CommonDataListAdapter;
import com.xjt.ordershop.adapter.WrapWrongLinearLayoutManger;
import com.xjt.ordershop.base.BaseActivity;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.callback.OnClickItemCallBack;
import com.xjt.ordershop.callback.OnObjectCallback;
import com.xjt.ordershop.util.BaseConfigPreferences;
import com.xjt.ordershop.util.CommonUtil;
import com.xjt.ordershop.util.Global;
import com.xjt.ordershop.util.MessageUtils;
import com.xjt.ordershop.util.NetApiUtil;
import com.xjt.ordershop.util.ThreadUtil;
import com.xjt.ordershop.widget.OrderGetTypeDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by xuqunxing on  2020/3/11
 */
public class BuyGoodActivity extends BaseActivity {

    @BindView(R.id.common_back_rl)
    RelativeLayout backRl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.common_toolbar_rl)
    RelativeLayout commonToolbarRl;
    @BindView(R.id.recycler_address_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.recycler_goods_rv)
    RecyclerView mRecyclerViewGood;
    @BindView(R.id.activity_buy_price_tv)
    TextView buyPriceTv;
    @BindView(R.id.activity_buy_address_change_tv)
    TextView addressChangeTv;
    @BindView(R.id.activity_buy_get_type_tv)
    TextView getTypeTv;
    @BindView(R.id.activity_buy_get_type_ll)
    LinearLayout getTypeLL;
    @BindView(R.id.activity_buy_address_change_LL)
    LinearLayout addressChangeLL;
    @BindView(R.id.activity_shop_address_LL)
    LinearLayout shopAddressLL;
    @BindView(R.id.activity_shop_address_tv)
    TextView shopAddressTv;

    private CommonDataListAdapter commonDataListAdapter;
    private AddressDataListAdapter addressDataListAdapter;
    private Good good;
    private int getTypeInt = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_good);
        ButterKnife.bind(this);
        commonTitleTv.setText("订单确认");
        mRecyclerView.setLayoutManager(new WrapWrongLinearLayoutManger(this));
        addressDataListAdapter = new AddressDataListAdapter(null);
        mRecyclerView.setAdapter(addressDataListAdapter);
        addressDataListAdapter.setNoShowState(true);
        mRecyclerViewGood.setLayoutManager(new WrapWrongLinearLayoutManger(this));
        commonDataListAdapter = new CommonDataListAdapter(null);
        mRecyclerViewGood.setAdapter(commonDataListAdapter);
        shopAddressLL.setVisibility(View.GONE);
        good = (Good) getIntent().getSerializableExtra("good");
        List<Good> goodList = new ArrayList<>();
        goodList.add(good);
        commonDataListAdapter.setNewData(goodList);
        buyPriceTv.setText(CommonUtil.stripZeros(CommonUtil.mul(good.getGoodPrice(), CommonUtil.div(good.getGoodDiscount(),10)) + ""));
        initData();
    }

    private void initData() {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                ServerResult<CommonResultMessage> goodsCookBookDetail = NetApiUtil.getAddressDefault();
                Global.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (goodsCookBookDetail != null && goodsCookBookDetail.getResultBean() != null) {
                            CommonResultMessage resultBean = goodsCookBookDetail.getResultBean();
                            if (resultBean.isSuccess()) {
                                String message = resultBean.getMessage();
                                if (!TextUtils.isEmpty(message)) {
                                    try {
                                        List<Address> addressList = new Gson().fromJson(message, new TypeToken<List<Address>>() {
                                        }.getType());
                                        if (addressList != null && addressList.size() > 0) {
                                            addressDataListAdapter.setNewData(addressList);
                                        } else {
                                            addressDataListAdapter.setNewData(null);
                                            addressDataListAdapter.setEmptyView(CommonUtil.getEmptyView(BuyGoodActivity.this, R.drawable.common_icon_notgoods, "暂无数据"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        MessageUtils.show(BuyGoodActivity.this, message);
                                        addressDataListAdapter.setNewData(null);
                                        addressDataListAdapter.setEmptyView(CommonUtil.getEmptyView(BuyGoodActivity.this, R.drawable.common_icon_notgoods, "暂无数据"));
                                    }
                                }
                            }
                        } else {
                            MessageUtils.show(BuyGoodActivity.this, "程序出错，请稍后再试！");
                        }
                    }
                });
            }
        });
    }

    @OnClick({R.id.activity_buy_sbumit_bt, R.id.common_back_rl, R.id.activity_buy_get_type_ll, R.id.activity_buy_address_change_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_rl:
                finish();
                break;
            case R.id.activity_buy_get_type_ll:
                OrderGetTypeDialog orderGetTypeDialog = new OrderGetTypeDialog(BuyGoodActivity.this);
                orderGetTypeDialog.show();
                orderGetTypeDialog.setOnClickItemCallBack(new OnObjectCallback() {
                    @Override
                    public void onClickCallBack(Object... value) {
                        getTypeInt = (int) value[0];
                        String valueString = (String) value[1];
                        getTypeTv.setText("订单类型：" + valueString);
                        if (getTypeInt == 1) {
                            addressChangeLL.setVisibility(View.VISIBLE);
                            shopAddressLL.setVisibility(View.GONE);
                        } else if (getTypeInt == 2) {//自取
                            addressChangeLL.setVisibility(View.GONE);
                            shopAddressLL.setVisibility(View.VISIBLE);
                            shopAddressTv.setText(BaseConfigPreferences.getInstance(BuyGoodActivity.this).getValue(BaseConfigPreferences.LOGIN_SHOP_ADDRESS));
                        }
                    }
                });
                break;
            case R.id.activity_buy_address_change_tv:
                Intent intent1 = new Intent(getApplicationContext(), AddressListActivity.class);
                intent1.putExtra(AddressListActivity.KEY_ADDRESS_SELECT, true);
                startActivityForResult(intent1, 2000);
                break;
            case R.id.activity_buy_sbumit_bt:
                if (getTypeInt == 1) {
                    if (addressDataListAdapter.getData().size() == 0) {
                        MessageUtils.show(BuyGoodActivity.this, "送货地址不能为空");
                        return;
                    }
                    Intent intent = new Intent(getApplicationContext(), PayMentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("good", good);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1000);
                } else if (getTypeInt == 2) {
                    Intent intent = new Intent(getApplicationContext(), PayMentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("good", good);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1000);
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                submitDo();
            } else if (requestCode == 2000) {
                Address address = (Address) data.getSerializableExtra("address");
                List<Address> addressList11 = new ArrayList<>();
                addressList11.add(address);
                addressDataListAdapter.setNewData(addressList11);
            }
        }
    }

    private void submitDo() {
        try {
            Address address = addressDataListAdapter.getData().get(0);
            int userId = BaseConfigPreferences.getInstance(this).getLoginUserId();
            ThreadUtil.executeMore(new Runnable() {
                @Override
                public void run() {
                    ServerResult<CommonResultMessage> commonResultMessageServerResult = NetApiUtil.addOrder(userId, good.getId(), good.getCategoryId(), 0,getTypeInt,good.getGoodName(), good.getGoodDetail(),
                            good.getGoodDiscount() + "", good.getGoodPrice() + "", good.getGoodPic(), address.getName(), address.getPhone(), address.getAddress(), shopAddressTv.getText().toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (commonResultMessageServerResult != null && commonResultMessageServerResult.getResultBean() != null) {
                                CommonResultMessage resultBean = commonResultMessageServerResult.getResultBean();
                                MessageUtils.show(BuyGoodActivity.this, resultBean.getMessage());
                                startActivity(new Intent(BuyGoodActivity.this, MainActivity.class));
                                finish();
                            } else {
                                MessageUtils.show(BuyGoodActivity.this, "程序出错，请稍后再试！");
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
