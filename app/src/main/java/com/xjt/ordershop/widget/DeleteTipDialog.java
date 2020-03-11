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
import com.xjt.ordershop.util.LogUtils;

/**
 * 权限提示
 * Created by xuqunxing on 2017/8/18.
 */
public class DeleteTipDialog extends Dialog {

    private Context mContext;
    private String tipstr;
    private String submitStr;

    public DeleteTipDialog(Context context) {
        super(context, R.style.corelib_Dialog_No_Anim);
        this.mContext = context;
        init();
    }

    public DeleteTipDialog(Context context, int themeResId) {
        super(context, R.style.corelib_Dialog_No_Anim);
        this.mContext = context;
        init();
    }

    public DeleteTipDialog(Context context, String title, String tip, String cancleStr, String okStr) {
        super(context, R.style.corelib_Dialog_No_Anim);
        this.mContext = context;
        init();
    }

//    public PermissionTipDialog(Context context, List<RedPacketsBean> redPacketsBeanList) {
//        super(context, R.style.corelib_Dialog_No_Anim);
//        this.mContext = context;
//        init();
//    }


    private void init() {
        getWindow().getDecorView().setBackgroundColor(mContext.getResources().getColor(R.color.black_alph_30));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);//宽高最大
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_delete_tip);
        initViews();
    }

    private void initViews() {
        TextView tipTv = findViewById(R.id.common_permission_tip_tv);
        TextView submitTv = findViewById(R.id.common_permission_tip_submit);

        if (!TextUtils.isEmpty(tipstr)) {
            tipTv.setText(tipstr);
        }
        if (!TextUtils.isEmpty(submitStr)) {
            submitTv.setText(submitStr);
        }
        tipTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("======", "======qq88888888" + onClickItemCallBack);
            }
        });
        submitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("======", "======qq7777777" + onClickItemCallBack);
                if (onClickItemCallBack != null) {
                    dismiss();
                    onClickItemCallBack.onClickCallBack("");
                }
            }
        });
    }

    private OnClickItemCallBack onClickItemCallBack;

    public void setOnClickItemCallBack(OnClickItemCallBack onClickItemCallBack) {
        this.onClickItemCallBack = onClickItemCallBack;
    }

    public void setParmas(String tipStr, String submitStr) {
        this.tipstr = tipStr;
        this.submitStr = submitStr;
    }
}
