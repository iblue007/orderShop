package com.xjt.ordershop.ui.fragment;

import android.content.Intent;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.xjt.baselib.bean.BannerBean;
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.baselib.bean.Good;
import com.xjt.ordershop.R;
import com.xjt.ordershop.adapter.CommonDataListAdapter;
import com.xjt.ordershop.adapter.WrapWrongLinearLayoutManger;
import com.xjt.ordershop.aop.checkLogin.CheckLoginImpl;
import com.xjt.ordershop.aop.singleclick.SingleClick;
import com.xjt.ordershop.base.BaseFragment;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.callback.OnClickItemCallBack;
import com.xjt.ordershop.ui.BannerListActivity;
import com.xjt.ordershop.ui.CategoryActivity;
import com.xjt.ordershop.ui.GoodDetailActivity;
import com.xjt.ordershop.ui.GoodInfoActivity;
import com.xjt.ordershop.util.BaseConfigPreferences;
import com.xjt.ordershop.util.CommonUtil;
import com.xjt.ordershop.util.GlideImageLoader;
import com.xjt.ordershop.util.Global;
import com.xjt.ordershop.util.LogUtils;
import com.xjt.ordershop.util.MessageUtils;
import com.xjt.ordershop.util.NetApiUtil;
import com.xjt.ordershop.util.ThreadUtil;
import com.xjt.ordershop.widget.DeleteTipDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by xuqunxing on  2020/3/5
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.home_banner)
    Banner banner;
    @BindView(R.id.recycler_goods_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.common_swiprefresh)
    SwipeRefreshLayout mSwipRefresh;
    private CommonDataListAdapter commonDataListAdapter;
    private List<String> imageUrls = new ArrayList<>();
    private List<BannerBean> bannerList;
    private int rows = 10;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.e("======", "======onCreateView");
        View view = inflater.inflate(R.layout.fragment_home, null);
        //绑定 ButterKnife
        ButterKnife.bind(this, view);
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true, 0.2f).init();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initPic();
        initBanner();
        initView();
        initBannerData();
        banner.setVisibility(View.GONE);
        LogUtils.e("======", "======onActivityCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new WrapWrongLinearLayoutManger(getContext()));
        commonDataListAdapter = new CommonDataListAdapter(null);
        mRecyclerView.setAdapter(commonDataListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        commonDataListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @CheckLoginImpl
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Good good = commonDataListAdapter.getData().get(i);
                int loginUserRole = BaseConfigPreferences.getInstance(getContext()).getLoginUserRole();
                if (loginUserRole == 0) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), GoodInfoActivity.class);
                    intent.putExtra(GoodInfoActivity.KEY_EDIT, true);
                    intent.putExtra(GoodInfoActivity.KEY_GOOD, good);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), GoodDetailActivity.class);
                    intent.putExtra(GoodDetailActivity.KEY_GOOD_ID, good.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
        commonDataListAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @SingleClick
            @Override
            public boolean onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                DeleteTipDialog deleteTipDialog = new DeleteTipDialog(getContext());
                deleteTipDialog.show();
                deleteTipDialog.setOnClickItemCallBack(new OnClickItemCallBack() {
                    @Override
                    public void onClickCallBack(String... value) {
                        deleteTipDialog.dismiss();
                        Good good = (Good) baseQuickAdapter.getData().get(i);
                        deleteGoodById(good.getId());
                    }
                });
                return false;
            }
        });
        commonDataListAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
        commonDataListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page = page + 1;
                initData();
            }
        });
        mSwipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                initData();
                initBannerData();
            }
        });
    }

    private void deleteGoodById(int id) {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                ServerResult<CommonResultMessage> commonResultMessageServerResult = NetApiUtil.deleteGoodById(id);
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

    private void initData() {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                ServerResult<CommonResultMessage> goodsCookBookDetail = NetApiUtil.getGoodList(rows, page);
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
                                            if (page == 1) {
                                                commonDataListAdapter.setNewData(list);
                                            } else {
                                                commonDataListAdapter.addData(list);
                                            }
                                            commonDataListAdapter.loadMoreComplete();
                                            if (list.size() < rows) {
                                                commonDataListAdapter.loadMoreEnd(true);
                                            }
                                        } else {
                                            commonDataListAdapter.setNewData(null);
                                            commonDataListAdapter.setEmptyView(CommonUtil.getEmptyView(getActivity(), R.drawable.common_icon_notgoods, "暂无数据"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        commonDataListAdapter.setNewData(null);
                                        commonDataListAdapter.setEmptyView(CommonUtil.getEmptyView(getActivity(), R.drawable.common_icon_notgoods, "暂无数据"));
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

    private void initBannerData() {
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
                                        bannerList = new Gson().fromJson(message, new TypeToken<List<BannerBean>>() {
                                        }.getType());
                                        if (bannerList != null && bannerList.size() > 0) {
                                            List<String> imageList = new ArrayList<>();
                                            for (int i = 0; i < bannerList.size(); i++) {
                                                BannerBean banner = bannerList.get(i);
                                                imageList.add(banner.getPicStr());
                                            }
                                            banner.update(imageList);
                                            banner.setVisibility(View.VISIBLE);
                                        } else {
                                            banner.setVisibility(View.GONE);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        banner.setVisibility(View.GONE);
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
//    private void initPic() {
//        imageUrls.clear();
//        imageUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1583413464372&di=74d230e265ebe41385c94ce0263bf395&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F36%2F48%2F19300001357258133412489354717.jpg");
//        imageUrls.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1208538952,1443328523&fm=26&gp=0.jpg");
//    }

    private void initBanner() {
        //设置banner的各种属性
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new GlideImageLoader())
                .setImages(imageUrls)
                .setBannerAnimation(Transformer.Default)
                .isAutoPlay(true)
                .setDelayTime(3000)
                .setIndicatorGravity(BannerConfig.CENTER)
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        LogUtils.e("======", "======position:" + position);
                        if (bannerList != null) {
                            BannerBean bannerBean = bannerList.get(position);
                            int getbType = bannerBean.getbType();
                            if (getbType == 1) {
                                int loginUserRole = BaseConfigPreferences.getInstance(getContext()).getLoginUserRole();
                                if (loginUserRole == 0) {
                                    Intent intent1 = new Intent();
                                    intent1.setClass(getContext(), GoodInfoActivity.class);
                                    intent1.putExtra(GoodInfoActivity.KEY_EDIT, true);
                                    intent1.putExtra(GoodInfoActivity.KEY_GOOD_ID, bannerBean.getGoodId());
                                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent1);
                                } else {
                                    Intent intent2 = new Intent();
                                    intent2.setClass(getContext(), GoodDetailActivity.class);
                                    intent2.putExtra(GoodDetailActivity.KEY_GOOD_ID, bannerBean.getGoodId());
                                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent2);
                                }
                            } else if (getbType == 2) {
                                Intent intent = new Intent();
                                intent.setClass(getContext(), CategoryActivity.class);
                                intent.putExtra(CategoryActivity.KEY_CATEGORY_ID, bannerBean.getCategoryId());
                                intent.putExtra(CategoryActivity.KEY_CATEGORY_NAME, bannerBean.getCategoryId());
                                startActivity(intent);
                            }
                        }
                    }
                })
                .start();

    }

    @Override
    public void onStart() {
        super.onStart();
        //增加banner的体验
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //增加banner的体验
        banner.stopAutoPlay();
    }


}
