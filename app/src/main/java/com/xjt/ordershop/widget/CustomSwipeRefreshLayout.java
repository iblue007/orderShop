package com.xjt.ordershop.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.xjt.ordershop.R;
import com.xjt.ordershop.util.ScreenUtil;

/**
 * 统一下拉刷新样式
 * Created by linliangbin on 2018/7/13 11:29.
 */
public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {

    public CustomSwipeRefreshLayout(Context context) {
        super(context);
        init();
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        //设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        setProgressViewOffset(true, 0, ScreenUtil.dip2px(getContext(), 100));
        //设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        setSize(SwipeRefreshLayout.DEFAULT);
        setColorSchemeResources(R.color.tab_indicator_color);
        // 设定下拉圆圈的背景
        setProgressBackgroundColorSchemeResource(R.color.white);

    }
}
