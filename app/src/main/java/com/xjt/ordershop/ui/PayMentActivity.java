package com.xjt.ordershop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xjt.baselib.bean.Good;
import com.xjt.ordershop.R;
import com.xjt.ordershop.util.CommonUtil;
import com.xjt.ordershop.util.MessageUtils;
import com.xjt.ordershop.widget.DialogWidget;
import com.xjt.ordershop.widget.PayPasswordView;

/**
 * 支付页面
 * Created by xuqunxing on 2016/12/13.
 */
public class PayMentActivity extends AppCompatActivity implements View.OnClickListener {

    private Button payBt;
    private DialogWidget mDialogWidget;
    private LinearLayout enterPwdLL;
    private RelativeLayout payDetailRl;
    private Good good;
    private TextView needPayTv;
    private TextView orderMontyTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_ment);
        good = (Good) getIntent().getSerializableExtra("good");
        payBt = (Button) findViewById(R.id.btn_confirm_pay);
        enterPwdLL = (LinearLayout) findViewById(R.id.lin_pass);
        payDetailRl = (RelativeLayout) findViewById(R.id.re_pay_detail);
        orderMontyTv = (TextView) findViewById(R.id.activity_order_money_tv);
        needPayTv = (TextView) findViewById(R.id.activity_need_pay_tv);

        payBt.setOnClickListener(this);
        try {
            if (good != null) {
                orderMontyTv.setText("￥ " + CommonUtil.stripZeros(CommonUtil.mul(good.getGoodPrice(), CommonUtil.div(good.getGoodDiscount(),10)) + ""));
                needPayTv.setText("￥ " + CommonUtil.stripZeros(CommonUtil.mul(good.getGoodPrice(), CommonUtil.div(good.getGoodDiscount(),10)) + ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        mDialogWidget = new DialogWidget(PayMentActivity.this, getDecorViewDialog());
        mDialogWidget.show();
    }

    protected View getDecorViewDialog() {
        // TODO Auto-generated method stub
        String money = null;
        try {
            money = CommonUtil.stripZeros(CommonUtil.mul(good.getGoodPrice(), CommonUtil.div(good.getGoodDiscount(),10)) + "");
        } catch (Exception e) {
            e.printStackTrace();
            money = 0 + "";
        }
        return PayPasswordView.getInstance((money), this, new PayPasswordView.OnPayListener() {

            @Override
            public void onSurePay(String password) {
                // TODO Auto-generated method stub
                //if(password .equals("123456")){
                mDialogWidget.dismiss();
                mDialogWidget = null;
                //  payTextView.setText(password);

                MessageUtils.show(getApplicationContext(), "交易成功");
                Intent intent = new Intent(getApplicationContext(), BuyGoodActivity.class);
                setResult(RESULT_OK, intent);
                finish();
//                }else {
//                    ToastUtils.showText("密码错误");
//                }
            }

            @Override
            public void onCancelPay() {
                // TODO Auto-generated method stub
                mDialogWidget.dismiss();
                mDialogWidget = null;
                // ToastUtils.showText("交易已取消");
//                finish();
            }
        }).getView();
    }
}
