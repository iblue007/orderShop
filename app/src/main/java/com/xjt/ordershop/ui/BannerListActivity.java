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
import com.xjt.baselib.bean.BannerBean;
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.ordershop.R;
import com.xjt.ordershop.adapter.BannerDataListAdapter;
import com.xjt.ordershop.adapter.WrapWrongLinearLayoutManger;
import com.xjt.ordershop.aop.checkLogin.CheckLoginImpl;
import com.xjt.ordershop.base.BaseActivity;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.util.CommonUtil;
import com.xjt.ordershop.util.Global;
import com.xjt.ordershop.util.MessageUtils;
import com.xjt.ordershop.util.NetApiUtil;
import com.xjt.ordershop.util.ThreadUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by xuqunxing on  2020/3/7
 */
public class BannerListActivity extends BaseActivity {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.common_back_rl)
    RelativeLayout backRl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.recycler_goods_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.common_swiprefresh)
    SwipeRefreshLayout mSwipRefresh;
    @BindView(R.id.banner_add_tv)
    TextView addTv;
    private BannerDataListAdapter commonDataListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_list);
        ButterKnife.bind(this);
        commonTitleTv.setText("轮播图列表");
        ImmersionBar.with(this).statusBarView(statusBarView)
                .statusBarDarkFont(true, 0.2f)
                .init();
        initView();
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new WrapWrongLinearLayoutManger(this));
        commonDataListAdapter = new BannerDataListAdapter(null);
        mRecyclerView.setAdapter(commonDataListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        commonDataListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @CheckLoginImpl
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                BannerBean banner = commonDataListAdapter.getData().get(i);
                if (banner.getbType() == 1) {
                    int id = commonDataListAdapter.getData().get(i).getId();
                    Intent intent = new Intent();
                    intent.setClass(BannerListActivity.this, GoodDetailActivity.class);
                    intent.putExtra(GoodDetailActivity.KEY_GOOD_ID, id);
                    startActivity(intent);
                } else if (banner.getbType() == 2) {
                    Intent intent = new Intent();
                    intent.setClass(BannerListActivity.this, CategoryActivity.class);
                    intent.putExtra(CategoryActivity.KEY_CATEGORY_ID, banner.getCategoryId());
                    intent.putExtra(CategoryActivity.KEY_CATEGORY_NAME, banner.getCategoryId());
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

    @OnClick({R.id.banner_add_tv, R.id.common_back_rl, R.id.banner_remove_all_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_rl:
                finish();
                break;
            case R.id.banner_remove_all_tv:
                removeAllData();
                break;

            case R.id.banner_add_tv:
                startActivity(new Intent(BannerListActivity.this, BannerSettingActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void removeAllData() {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                ServerResult<CommonResultMessage> goodsCookBookDetail = NetApiUtil.removeAllBanner();
                Global.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (goodsCookBookDetail != null && goodsCookBookDetail.getResultBean() != null) {
                            CommonResultMessage resultBean = goodsCookBookDetail.getResultBean();
                            if (resultBean.isSuccess()) {
                                String message = resultBean.getMessage();
                                MessageUtils.show(BannerListActivity.this, message);
                            }
                            initData();
                        } else {
                            MessageUtils.show(BannerListActivity.this, "程序出错，请稍后再试！");
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
                ServerResult<CommonResultMessage> goodsCookBookDetail = NetApiUtil.getBannerList();
                Global.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (goodsCookBookDetail != null && goodsCookBookDetail.getResultBean() != null) {
                            CommonResultMessage resultBean = goodsCookBookDetail.getResultBean();
                            if (resultBean.isSuccess()) {
                                String message = resultBean.getMessage();
                                if (!TextUtils.isEmpty(message)) {
                                    try {
                                        List<BannerBean> list = new Gson().fromJson(message, new TypeToken<List<BannerBean>>() {
                                        }.getType());
                                        if (list != null && list.size() > 0) {
                                            commonDataListAdapter.setNewData(list);
                                        } else {
                                            commonDataListAdapter.setNewData(null);
                                            commonDataListAdapter.setEmptyView(CommonUtil.getEmptyView(BannerListActivity.this, R.drawable.common_icon_notgoods, "暂无数据"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        MessageUtils.show(BannerListActivity.this, message);
                                        commonDataListAdapter.setNewData(null);
                                        commonDataListAdapter.setEmptyView(CommonUtil.getEmptyView(BannerListActivity.this, R.drawable.common_icon_notgoods, "暂无数据"));
                                    }
                                }
                            }
                        } else {
                            MessageUtils.show(BannerListActivity.this, "程序出错，请稍后再试！");
                        }
                        mSwipRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

}
