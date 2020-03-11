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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.baselib.bean.Good;
import com.xjt.ordershop.R;
import com.xjt.ordershop.adapter.CommonDataListAdapter;
import com.xjt.ordershop.adapter.WrapWrongLinearLayoutManger;
import com.xjt.ordershop.aop.checkLogin.CheckLoginImpl;
import com.xjt.ordershop.aop.singleclick.SingleClick;
import com.xjt.ordershop.base.BaseFragment;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.callback.OnObjectCallback;
import com.xjt.ordershop.ui.CategoryActivity;
import com.xjt.ordershop.ui.GoodDetailActivity;
import com.xjt.ordershop.ui.GoodInfoActivity;
import com.xjt.ordershop.ui.GoodSearchActivity;
import com.xjt.ordershop.ui.MainActivity;
import com.xjt.ordershop.util.CommonUtil;
import com.xjt.ordershop.util.Global;
import com.xjt.ordershop.util.LogUtils;
import com.xjt.ordershop.util.MessageUtils;
import com.xjt.ordershop.util.NetApiUtil;
import com.xjt.ordershop.util.ScreenUtil;
import com.xjt.ordershop.util.ThreadUtil;
import com.xjt.ordershop.widget.HomeAdsView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by xuqunxing on  2020/3/7
 */
public class GoodSearchFragment extends BaseFragment {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.search_toolbar_left_rl)
    RelativeLayout backRl;
    @BindView(R.id.search_toolbar_edittext)
    EditText searchEt;
    @BindView(R.id.recycler_goods_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.common_swiprefresh)
    SwipeRefreshLayout mSwipRefresh;
    @BindView(R.id.home_ads_view)
    HomeAdsView homeAdsView;
    @BindView(R.id.search_toolbar_ll)
    LinearLayout toolbarLl;

    private CommonDataListAdapter commonDataListAdapter;
    private OnObjectCallback onObjectCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = View.inflate(getContext(), R.layout.fragment_good_search, null);
        ButterKnife.bind(this, inflate);
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = ScreenUtil.getStatusBarHeight(getContext());
            LinearLayout.LayoutParams layoutParamsToolbar = (LinearLayout.LayoutParams) toolbarLl.getLayoutParams();
            layoutParamsToolbar.topMargin = statusBarHeight;
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            boolean isShowCategory = arguments.getBoolean(GoodSearchActivity.KEY_GOOD_CATEGORY_SHOW, true);
            if (isShowCategory) {
                homeAdsView.setVisibility(View.VISIBLE);
            } else {
                homeAdsView.setVisibility(View.GONE);
            }
        }
        if (getContext() instanceof MainActivity) {
            backRl.setVisibility(View.GONE);
        } else {
            backRl.setVisibility(View.VISIBLE);
        }
        mSwipRefresh.setVisibility(View.GONE);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initAds();
    }

    private void initData() {
        mRecyclerView.setLayoutManager(new WrapWrongLinearLayoutManger(getContext()));
        commonDataListAdapter = new CommonDataListAdapter(null);
        mRecyclerView.setAdapter(commonDataListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        commonDataListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @CheckLoginImpl
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Good good = commonDataListAdapter.getData().get(i);
                if (onObjectCallback != null && good != null) {
                    onObjectCallback.onClickCallBack(good.getId(), good.getGoodName(), good.getGoodPic());
                } else {
                    int id = commonDataListAdapter.getData().get(i).getId();
                    Intent intent = new Intent();
                    intent.setClass(getContext(), GoodDetailActivity.class);
                    intent.putExtra(GoodDetailActivity.KEY_GOOD_ID, id);
                    startActivity(intent);
                }
            }
        });
        mSwipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    @OnClick({R.id.search_toolbar_left_rl, R.id.search_toolbar_search_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_toolbar_left_rl:
                getActivity().finish();
                break;
            case R.id.search_toolbar_search_tv:
                searchGood(view);
                break;
        }
    }

    @SingleClick
    private void searchGood(View view) {
        String searchStr = searchEt.getText().toString();
        if (TextUtils.isEmpty(searchStr)) {
            MessageUtils.show(getContext(), "搜索关键词不能为空");
            return;
        }
        mSwipRefresh.setVisibility(View.VISIBLE);
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                ServerResult<CommonResultMessage> goodsCookBookDetail = NetApiUtil.getGoodListByKey(searchStr);
                Global.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (goodsCookBookDetail != null && goodsCookBookDetail.getResultBean() != null) {
                            CommonResultMessage resultBean = goodsCookBookDetail.getResultBean();
                            if (resultBean.isSuccess()) {
                                String message = resultBean.getMessage();
                                if (!TextUtils.isEmpty(message)) {
                                    try {
                                        List<Good> list = new Gson().fromJson(message, new TypeToken<List<Good>>() {
                                        }.getType());
                                        if (list != null && list.size() > 0) {
                                            commonDataListAdapter.setNewData(list);
                                        } else {
                                            commonDataListAdapter.setNewData(null);
                                            commonDataListAdapter.setEmptyView(CommonUtil.getEmptyView(getActivity(), 0, ""));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        MessageUtils.show(getContext(), message);
                                        commonDataListAdapter.setNewData(null);
                                        commonDataListAdapter.setEmptyView(CommonUtil.getEmptyView(getActivity(), 0, ""));
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

    public void setCallBack(OnObjectCallback onObjectCallback1) {
        this.onObjectCallback = onObjectCallback1;
    }

    private void initAds() {
        homeAdsView.setOnAdsClickListener(new HomeAdsView.OnAdsClickListener() {
            @Override
            public void onAds1Click() {
                LogUtils.e("======", "======Ads1");
                startCategoryActivity("甜点", 1);
            }

            @Override
            public void onAds2Click() {
                LogUtils.e("======", "======Ads2");
                startCategoryActivity("面", 2);
            }

            @Override
            public void onAds3Click() {
                LogUtils.e("======", "======Ads3");
                startCategoryActivity("肉", 3);
            }

            @Override
            public void onAds4Click() {
                LogUtils.e("======", "======Ads4");
                startCategoryActivity("卤味", 4);
            }
        });
    }

    private void startCategoryActivity(String name, int id) {
        Intent intent = new Intent();
        intent.putExtra(CategoryActivity.KEY_CATEGORY_ID, id);
        intent.putExtra(CategoryActivity.KEY_CATEGORY_NAME, id);
        intent.setClass(getContext(), CategoryActivity.class);
        getContext().startActivity(intent);
    }

}
