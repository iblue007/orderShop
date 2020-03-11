package com.xjt.ordershop.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xjt.baselib.bean.Category;
import com.xjt.ordershop.R;
import com.xjt.ordershop.callback.OnObjectCallback;

import java.util.List;

/**
 * Create by xuqunxing on  2019/3/22
 */
public class FoodCategoryAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

    public FoodCategoryAdapter(List<Category> mDatas) {
        super(R.layout.item_food_category, mDatas);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Category businessTypeBean, int pos) {
        TextView typeName = baseViewHolder.getView(R.id.login_register_business_type_name);
        typeName.setText(businessTypeBean.getCategoryName());
        typeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItemCallBack != null) {
                    onClickItemCallBack.onClickCallBack(businessTypeBean.getId(), businessTypeBean.getCategoryName());
                }
            }
        });
    }

    private OnObjectCallback onClickItemCallBack;

    public void setOnClickItemCallBack(OnObjectCallback onClickItemCallBack1) {
        onClickItemCallBack = onClickItemCallBack1;
    }
}
