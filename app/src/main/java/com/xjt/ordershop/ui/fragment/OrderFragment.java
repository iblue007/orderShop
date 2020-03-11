package com.xjt.ordershop.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.baselib.bean.Good;
import com.xjt.baselib.bean.Order;
import com.xjt.ordershop.R;
import com.xjt.ordershop.adapter.CommonDataListAdapter;
import com.xjt.ordershop.adapter.OrderDataListAdapter;
import com.xjt.ordershop.adapter.WrapWrongLinearLayoutManger;
import com.xjt.ordershop.aop.checkLogin.CheckLoginImpl;
import com.xjt.ordershop.base.BaseFragment;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.callback.OnClickItemCallBack;
import com.xjt.ordershop.ui.GoodDetailActivity;
import com.xjt.ordershop.ui.MainActivity;
import com.xjt.ordershop.ui.OrderDetailActivity;
import com.xjt.ordershop.ui.SettingActivity;
import com.xjt.ordershop.util.BaseConfigPreferences;
import com.xjt.ordershop.util.CommonUtil;
import com.xjt.ordershop.util.Global;
import com.xjt.ordershop.util.MessageUtils;
import com.xjt.ordershop.util.NetApiUtil;
import com.xjt.ordershop.util.ScreenUtil;
import com.xjt.ordershop.util.ThreadUtil;
import com.xjt.ordershop.widget.DeleteTipDialog;
import com.xjt.ordershop.widget.PermissionTipDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by xuqunxing on  2020/3/5
 */
public class OrderFragment extends BaseFragment {

    @BindView(R.id.recycler_goods_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.common_swiprefresh)
    SwipeRefreshLayout mSwipRefresh;
    private OrderDataListAdapter commonDataListAdapter;
    @BindView(R.id.common_back_rl)
    RelativeLayout backRl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.common_toolbar_rl)
    RelativeLayout commonToolbarRl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, null);
        //绑定 ButterKnife
        ButterKnife.bind(this, view);
//        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true, 0.2f).init();
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init();
        mRecyclerView.setLayoutManager(new WrapWrongLinearLayoutManger(getContext()));
        commonDataListAdapter = new OrderDataListAdapter(null);
        mRecyclerView.setAdapter(commonDataListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        commonDataListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @CheckLoginImpl
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Order order = commonDataListAdapter.getData().get(i);
                int orderId = order.getId();
                Intent intent = new Intent();
                intent.setClass(getContext(), OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.KEY_ORDER_ID, orderId);
                startActivity(intent);
            }
        });
        commonDataListAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Order order = (Order) baseQuickAdapter.getData().get(i);
                DeleteTipDialog deleteTipDialog = new DeleteTipDialog(getContext());
                deleteTipDialog.show();
                deleteTipDialog.setOnClickItemCallBack(new OnClickItemCallBack() {
                    @Override
                    public void onClickCallBack(String... value) {
                        deleteTipDialog.dismiss();
                        deleteOrderById(order.getId());
                    }
                });
                return false;
            }
        });
        mSwipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        backRl.setVisibility(View.GONE);
        commonTitleTv.setText("订单");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = ScreenUtil.getStatusBarHeight(getContext());
            LinearLayout.LayoutParams layoutParamsToolbar = (LinearLayout.LayoutParams) commonToolbarRl.getLayoutParams();
            layoutParamsToolbar.topMargin = statusBarHeight;
        }
        return view;
    }

    private void initData() {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                ServerResult<CommonResultMessage> goodsCookBookDetail = NetApiUtil.getOrderList();
                Global.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (goodsCookBookDetail != null && goodsCookBookDetail.getResultBean() != null) {
                            CommonResultMessage resultBean = goodsCookBookDetail.getResultBean();
                            if (resultBean.isSuccess()) {
                                String message = resultBean.getMessage();
                                if (!TextUtils.isEmpty(message)) {
                                    try {
                                        List<Order> list = new Gson().fromJson(message, new TypeToken<List<Order>>() {
                                        }.getType());
                                        if (list != null && list.size() > 0) {
                                            commonDataListAdapter.setNewData(list);
                                        } else {
                                            commonDataListAdapter.setNewData(null);
                                            commonDataListAdapter.setEmptyView(CommonUtil.getEmptyView(getActivity(), R.drawable.common_icon_notgoods, "暂无订单"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        MessageUtils.show(getContext(), message);
                                        commonDataListAdapter.setNewData(null);
                                        commonDataListAdapter.setEmptyView(CommonUtil.getEmptyView(getActivity(), R.drawable.common_icon_notgoods, "暂无订单"));
                                    }
                                }
                            }
                        } else {
                            MessageUtils.show(getContext(), "程序出错，请稍后再试！");
                        }
                        mSwipRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void deleteOrderById(int orderId) {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                ServerResult<CommonResultMessage> commonResultMessageServerResult = NetApiUtil.deleteOrderById(orderId);
                Global.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (commonResultMessageServerResult != null && commonResultMessageServerResult.getResultBean() != null) {
                            CommonResultMessage resultBean = commonResultMessageServerResult.getResultBean();
                            MessageUtils.show(getContext(), resultBean.getMessage());
                            initData();
                        } else {
                            MessageUtils.show(getContext(), "程序出错，请稍后再试！");
                        }
                    }
                });
            }
        });
    }

}
