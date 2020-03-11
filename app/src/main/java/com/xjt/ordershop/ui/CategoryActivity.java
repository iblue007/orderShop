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
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.baselib.bean.Good;
import com.xjt.ordershop.R;
import com.xjt.ordershop.adapter.CommonDataListAdapter;
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
public class CategoryActivity extends BaseActivity {

    @BindView(R.id.recycler_goods_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.common_back_rl)
    RelativeLayout backRl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.common_swiprefresh)
    SwipeRefreshLayout mSwipRefresh;
    public static String KEY_CATEGORY_ID = "key_category_id";
    public static String KEY_CATEGORY_NAME = "key_category_name";
    private CommonDataListAdapter commonDataListAdapter;
    private int categoryId;
    private int categoryName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        commonTitleTv.setText("登录");
        ImmersionBar.with(this).statusBarView(statusBarView)
                .statusBarDarkFont(true, 0.2f)
                .init();
        categoryId = getIntent().getIntExtra(KEY_CATEGORY_ID, 0);
        if (categoryId <= 0) {
            finish();
            return;
        }
        categoryName = getIntent().getIntExtra(KEY_CATEGORY_NAME, 0);
        String name = "";
        if (categoryName == 1) {
            name = "甜点";
        } else if (categoryName == 2) {
            name = "面";
        } else if (categoryName == 3) {
            name = "肉";
        } else if (categoryName == 4) {
            name = "卤味";
        } else {
            name = "未命名分类";
        }
        commonTitleTv.setText(name);
        initView();
        initData();
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new WrapWrongLinearLayoutManger(this));
        commonDataListAdapter = new CommonDataListAdapter(null);
        mRecyclerView.setAdapter(commonDataListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        commonDataListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @CheckLoginImpl
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent();
                intent.setClass(CategoryActivity.this, GoodInfoActivity.class);
                startActivity(intent);
            }
        });
//        commonDataListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                initData();
//            }
//        });
    }

    @OnClick({R.id.common_back_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_rl:
                finish();
                break;
        }
    }

    private void initData() {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                ServerResult<CommonResultMessage> goodsCookBookDetail = NetApiUtil.getCategoryById(categoryId);
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
//
                                            commonDataListAdapter.setNewData(list);
                                            commonDataListAdapter.loadMoreComplete();
                                        }else {
                                            commonDataListAdapter.setNewData(null);
                                            commonDataListAdapter.setEmptyView(CommonUtil.getEmptyView(CategoryActivity.this, R.drawable.common_icon_notgoods, "暂无数据"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        MessageUtils.show(CategoryActivity.this, message);
                                        commonDataListAdapter.setNewData(null);
                                        commonDataListAdapter.setEmptyView(CommonUtil.getEmptyView(CategoryActivity.this, R.drawable.common_icon_notgoods, "暂无数据"));
                                    }
                                }
                            }
                        } else {
                            MessageUtils.show(CategoryActivity.this, "程序出错，请稍后再试！");
                        }
                        mSwipRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

}
