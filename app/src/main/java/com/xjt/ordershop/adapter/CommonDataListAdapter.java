package com.xjt.ordershop.adapter;


import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xjt.baselib.R;
import com.xjt.baselib.bean.Good;
import com.xjt.ordershop.util.CommonUtil;
import com.xjt.ordershop.util.LogUtils;

import java.util.List;

/**
 * Create by xuqunxing on  2019/8/26
 */
public class CommonDataListAdapter extends BaseAdapter<Good, BaseViewHolder> {

    public CommonDataListAdapter(int layoutResId, @Nullable List<Good> data) {
        super(R.layout.common_home_data_item, data);
    }

    public CommonDataListAdapter(@Nullable List<Good> data) {
        super(R.layout.common_home_data_item, data);
    }

    public CommonDataListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Good good, int i) {
        ImageView picIv = baseViewHolder.getView(R.id.home_item_pic_iv);
        TextView titleTv = baseViewHolder.getView(R.id.home_item_title_tv);
        TextView discountTv = baseViewHolder.getView(R.id.home_item_discount_tv);
        TextView priceTv = baseViewHolder.getView(R.id.home_item_price_tv);
        TextView categoryTc = baseViewHolder.getView(R.id.home_item_category_tv);
        LogUtils.e("======", "======pic:" + good.getGoodPic());
//        Glide.with(Global.getContext()).load(user.getGoodPic()).into(picIv);
        ImageLoader.getInstance().displayImage(good.getGoodPic(), picIv);
        titleTv.setText(good.getGoodName());
        discountTv.setText("折扣：" + CommonUtil.stripZeros(good.getGoodDiscount() + ""));
        priceTv.setText("价格：" + CommonUtil.stripZeros(good.getGoodPrice() + ""));
        categoryTc.setText("所属分类：" + good.getCategoryName());

    }
}
