package com.xjt.ordershop.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.xjt.ordershop.R;
import com.xjt.ordershop.callback.OnClickItemCallBack;
import com.xjt.ordershop.callback.OnObjectCallback;
import com.xjt.ordershop.util.LogUtils;

/**
 * 权限提示
 * Created by xuqunxing on 2017/8/18.
 */
public class OrderGetTypeDialog extends Dialog {

    private Context mContext;
    private String tipstr;
    private String submitStr;

    public OrderGetTypeDialog(Context context) {
        super(context, R.style.corelib_Dialog_No_Anim);
        this.mContext = context;
        init();
    }

    public OrderGetTypeDialog(Context context, int themeResId) {
        super(context, R.style.corelib_Dialog_No_Anim);
        this.mContext = context;
        init();
    }

    public OrderGetTypeDialog(Context context, String title, String tip, String cancleStr, String okStr) {
        super(context, R.style.corelib_Dialog_No_Anim);
        this.mContext = context;
        init();
    }

    private void init() {
        getWindow().getDecorView().setBackgroundColor(mContext.getResources().getColor(R.color.black_alph_30));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);//宽高最大
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_get_type_tip);
        initViews();
    }

    private void initViews() {
        TextView ziquTv = findViewById(R.id.tv_dialog_ziqu_tv);
        TextView waimaiTv = findViewById(R.id.tv_dialog_waimai_tv);
        TextView cancleTv = findViewById(R.id.tv_cancle);

        cancleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ziquTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItemCallBack != null) {
                    dismiss();
                    onClickItemCallBack.onClickCallBack(2, "自取");
                }
            }
        });
        waimaiTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItemCallBack != null) {
                    dismiss();
                    onClickItemCallBack.onClickCallBack(1, "外卖");
                }
            }
        });
    }

    private OnObjectCallback onClickItemCallBack;

    public void setOnClickItemCallBack(OnObjectCallback onClickItemCallBack) {
        this.onClickItemCallBack = onClickItemCallBack;
    }

    public void setParmas(String tipStr, String submitStr) {
        this.tipstr = tipStr;
        this.submitStr = submitStr;
    }
}
