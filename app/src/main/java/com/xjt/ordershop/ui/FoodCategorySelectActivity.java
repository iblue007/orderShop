package com.xjt.ordershop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.xjt.baselib.bean.Category;
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.baselib.bean.Good;
import com.xjt.baselib.bean.User;
import com.xjt.ordershop.R;
import com.xjt.ordershop.adapter.FoodCategoryAdapter;
import com.xjt.ordershop.adapter.WrapWrongLinearLayoutManger;
import com.xjt.ordershop.base.BaseActivity;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.callback.OnClickItemCallBack;
import com.xjt.ordershop.callback.OnObjectCallback;
import com.xjt.ordershop.util.BaseConfigPreferences;
import com.xjt.ordershop.util.MessageUtils;
import com.xjt.ordershop.util.NetApiUtil;
import com.xjt.ordershop.util.ThreadUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by xuqunxing on  2020/3/6
 */
public class FoodCategorySelectActivity extends BaseActivity {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.common_back_rl)
    RelativeLayout backRl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.food_cateogyr_select_rv)
    RecyclerView mRecyclerView;
    private FoodCategoryAdapter foodCategoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_category_select);
        ButterKnife.bind(this);
        commonTitleTv.setText("选择分类");
        ImmersionBar.with(this).statusBarView(statusBarView)
                .statusBarDarkFont(true, 0.2f)
                .init();
        mRecyclerView.setLayoutManager(new WrapWrongLinearLayoutManger(getApplicationContext()));
        foodCategoryAdapter = new FoodCategoryAdapter(null);
        mRecyclerView.setAdapter(foodCategoryAdapter);
        foodCategoryAdapter.setOnClickItemCallBack(new OnObjectCallback() {
            @Override
            public void onClickCallBack(Object... value) {
                int id = (int) value[0];
                String name = (String) value[1];
                Intent intent = new Intent();
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        initData();
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
                ServerResult<CommonResultMessage> categoryList = NetApiUtil.getCategoryList();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (categoryList != null && categoryList.getResultBean() != null) {
                            CommonResultMessage resultBean = categoryList.getResultBean();
                            if (resultBean.isSuccess()) {
                                List<Category> list = new Gson().fromJson(resultBean.getMessage(), new TypeToken<List<Category>>() {
                                }.getType());
                                if (list != null && list.size() > 0) {
                                    foodCategoryAdapter.setNewData(list);
                                }
                            } else {
                                MessageUtils.show(FoodCategorySelectActivity.this, resultBean.getMessage());
                            }
                        } else {
                            MessageUtils.show(FoodCategorySelectActivity.this, "程序出错，请稍后再试！");
                        }
                    }
                });
            }
        });
    }

}
