package com.xjt.ordershop.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xjt.ordershop.R;
import com.xjt.ordershop.config.AppConfig;

import java.math.BigDecimal;

/**
 * Create by xuqunxing on  2020/3/6
 */
public class CommonUtil {

    /*取出没用的小数点后面的0 例如2.00 -> 2*/
    public static String stripZeros(String number) {
        try {
            if (TextUtils.isEmpty(number)) {
                return AppConfig.NO_PRICE;
            }
            BigDecimal result = new BigDecimal(number).stripTrailingZeros();
            if (result.compareTo(BigDecimal.ZERO) == 0) {
                return 0 + "";
            } else {
                return result.toPlainString();
            }
        } catch (Exception e) {
            return AppConfig.NO_PRICE;
        }

    }

    //double 数字相加的精确计算
    public static Double add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.add(b2).doubleValue();
    }

    //double 数字相减的精确计算
    public static double sub(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.subtract(b2).doubleValue();
    }

    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    //double 数字的四舍五入(#：没有则为空 0：没有则补0)
    public static String sswrDouble(double value) {
        return new java.text.DecimalFormat("0.##").format(value);
    }

    public static double div(double value1, double value2) {
        return div(value1, value2, 10);
    }

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    public static View getEmptyView(Context context, int imgId, String emptyText) {
        View view = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.common_data_empty, null);
            if (imgId > 0) {
                view.findViewById(R.id.iv_empty_img).setVisibility(View.VISIBLE);
                view.findViewById(R.id.iv_empty_img).setBackground(context.getResources().getDrawable(imgId));
            } else if (imgId == -1) {
                view.findViewById(R.id.iv_empty_img).setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(emptyText)) {
                ((TextView) view.findViewById(R.id.tv_empty)).setText(emptyText);
            }
        }
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeAllViews();
        }
        return view;
    }


}
