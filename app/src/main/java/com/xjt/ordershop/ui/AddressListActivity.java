package com.xjt.ordershop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.xjt.baselib.bean.Address;
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.ordershop.R;
import com.xjt.ordershop.adapter.AddressDataListAdapter;
import com.xjt.ordershop.adapter.WrapWrongLinearLayoutManger;
import com.xjt.ordershop.aop.checkLogin.CheckLoginImpl;
import com.xjt.ordershop.base.BaseActivity;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.callback.OnClickItemCallBack;
import com.xjt.ordershop.callback.OnObjectCallback;
import com.xjt.ordershop.util.CommonUtil;
import com.xjt.ordershop.util.Global;
import com.xjt.ordershop.util.MessageUtils;
import com.xjt.ordershop.util.NetApiUtil;
import com.xjt.ordershop.util.ThreadUtil;
import com.xjt.ordershop.widget.DeleteTipDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by xuqunxing on  2020/3/10
 */
public class AddressListActivity extends BaseActivity {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.common_back_rl)
    RelativeLayout backRl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.recycler_address_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.common_swiprefresh)
    SwipeRefreshLayout mSwipRefresh;
    @BindView(R.id.banner_add_tv)
    TextView addTv;
    private AddressDataListAdapter commonDataListAdapter;
    public static String KEY_ADDRESS_SELECT = "key_address_select";
    private boolean isSelectView = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ButterKnife.bind(this);
        commonTitleTv.setText("我的地址列表");
        ImmersionBar.with(this).statusBarView(statusBarView)
                .statusBarDarkFont(true, 0.2f)
                .init();
        initView();
    }

    private void initView() {
        isSelectView = getIntent().getBooleanExtra(KEY_ADDRESS_SELECT, false);
        mRecyclerView.setLayoutManager(new WrapWrongLinearLayoutManger(this));
        commonDataListAdapter = new AddressDataListAdapter(null);
        mRecyclerView.setAdapter(commonDataListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        commonDataListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @CheckLoginImpl
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (isSelectView) {
                    Address address = (Address) baseQuickAdapter.getData().get(i);
                    Intent intent = new Intent(getApplicationContext(), BuyGoodActivity.class);
                    intent.putExtra("address", address);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Address address = (Address) baseQuickAdapter.getData().get(i);
                    Intent intent = new Intent(AddressListActivity.this, AddressSetActivity.class);
                    intent.putExtra(AddressSetActivity.KEY_EDIT, true);
                    intent.putExtra(AddressSetActivity.KEY_ADDRESS, address);
                    startActivity(intent);
                }
            }
        });
        commonDataListAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @CheckLoginImpl
            @Override
            public boolean onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (isSelectView) {
                    return false;
                }
                Address address = (Address) baseQuickAdapter.getData().get(i);
                DeleteTipDialog deleteTipDialog = new DeleteTipDialog(AddressListActivity.this);
                deleteTipDialog.show();
                deleteTipDialog.setOnClickItemCallBack(new OnClickItemCallBack() {
                    @Override
                    public void onClickCallBack(String... value) {
                        deleteTipDialog.dismiss();

                        deleteAddressById(address.getId());
                    }
                });
                return false;
            }
        });
        commonDataListAdapter.setCallBack(new OnObjectCallback() {
            @Override
            public void onClickCallBack(Object... value) {
                initData();
            }
        });
        mSwipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    @OnClick({R.id.banner_add_tv, R.id.common_back_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_rl:
                finish();
                break;

            case R.id.banner_add_tv:
                startActivity(new Intent(AddressListActivity.this, AddressSetActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void deleteAddressById(int addressId) {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                ServerResult<CommonResultMessage> goodsCookBookDetail = NetApiUtil.deleteAddressById(addressId);
                Global.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (goodsCookBookDetail != null && goodsCookBookDetail.getResultBean() != null) {
                            CommonResultMessage resultBean = goodsCookBookDetail.getResultBean();
                            if (resultBean.isSuccess()) {
                                String message = resultBean.getMessage();
                                MessageUtils.show(AddressListActivity.this, message);
                            }
                            initData();
                        } else {
                            MessageUtils.show(AddressListActivity.this, "程序出错，请稍后再试！");
                        }
                        mSwipRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void initData() {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                ServerResult<CommonResultMessage> goodsCookBookDetail = NetApiUtil.getAddressList();
                Global.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (goodsCookBookDetail != null && goodsCookBookDetail.getResultBean() != null) {
                            CommonResultMessage resultBean = goodsCookBookDetail.getResultBean();
                            if (resultBean.isSuccess()) {
                                String message = resultBean.getMessage();
                                if (!TextUtils.isEmpty(message)) {
                                    try {
                                        List<Address> list = new Gson().fromJson(message, new TypeToken<List<Address>>() {
                                        }.getType());
                                        if (list != null && list.size() > 0) {
                                            commonDataListAdapter.setNewData(list);
                                        } else {
                                            commonDataListAdapter.setNewData(null);
                                            commonDataListAdapter.setEmptyView(CommonUtil.getEmptyView(AddressListActivity.this, R.drawable.common_icon_notgoods, "暂无数据"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        MessageUtils.show(AddressListActivity.this, message);
                                        commonDataListAdapter.setNewData(null);
                                        commonDataListAdapter.setEmptyView(CommonUtil.getEmptyView(AddressListActivity.this, R.drawable.common_icon_notgoods, "暂无数据"));
                                    }
                                }
                            }
                        } else {
                            MessageUtils.show(AddressListActivity.this, "程序出错，请稍后再试！");
                        }
                        mSwipRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

}
