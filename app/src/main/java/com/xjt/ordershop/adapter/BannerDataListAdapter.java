package com.xjt.ordershop.adapter;


import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xjt.baselib.bean.BannerBean;
import com.xjt.ordershop.R;

import java.util.List;

/**
 * Create by xuqunxing on  2019/8/26
 */
public class BannerDataListAdapter extends BaseAdapter<BannerBean, BaseViewHolder> {

    public BannerDataListAdapter(int layoutResId, @Nullable List<BannerBean> data) {
        super(R.layout.banner_data_item, data);
    }

    public BannerDataListAdapter(@Nullable List<BannerBean> data) {
        super(R.layout.banner_data_item, data);
    }

    public BannerDataListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BannerBean banner, int i) {
        ImageView picIv = baseViewHolder.getView(R.id.home_item_pic_iv);
        TextView titleTv = baseViewHolder.getView(R.id.home_item_title_tv);
        TextView typeTv = baseViewHolder.getView(R.id.home_item_type_tv);
        TextView timeTv = baseViewHolder.getView(R.id.home_item_time_tv);
        titleTv.setText(banner.getName());
        String type = "";
        if (banner.getbType() == 1) {
            type = "商品详情活动";
        } else if (banner.getbType() == 2) {
            type = "分类详情活动";
        }
        typeTv.setText("类型：" + type);
        timeTv.setText("添加时间：" + banner.getBdate());

        ImageLoader.getInstance().displayImage(banner.getPicStr(), picIv);
//        if (banner.getPicStr().contains("http:")) {
//            ImageLoader.getInstance().displayImage(banner.getPicStr(), picIv);
//        } else {
//            String picStr = banner.getPicStr();
//            int picInt = Integer.parseInt(picStr);
//            String picLocal = "drawable://" + R.drawable.category_4_1;
//            if (picInt == 1) {
//                picLocal = "drawable://" + R.drawable.category_4_1;
//            } else if (picInt == 2) {
//                picLocal = "drawable://" + R.drawable.category_4_2;
//            } else if (picInt == 3) {
//                picLocal = "drawable://" + R.drawable.category_4_3;
//            } else if (picInt == 4) {
//                picLocal = "drawable://" + R.drawable.category_4_4;
//            }
//            ImageLoader.getInstance().displayImage(picLocal, picIv);
//        }
    }
}
