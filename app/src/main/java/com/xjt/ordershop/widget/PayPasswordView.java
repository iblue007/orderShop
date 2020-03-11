package com.xjt.ordershop.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xjt.ordershop.R;
import com.xjt.ordershop.util.BaseConfigPreferences;
import com.xjt.ordershop.util.MessageUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Dialog ��ʾ��ͼ
 *
 * @author LanYan
 */
public class PayPasswordView {

    @BindView(R.id.pay_keyboard_del)
    ImageView del;
    @BindView(R.id.pay_keyboard_zero)
    ImageView zero;
    @BindView(R.id.pay_keyboard_one)
    ImageView one;
    @BindView(R.id.pay_keyboard_two)
    ImageView two;
    @BindView(R.id.pay_keyboard_three)
    ImageView three;
    @BindView(R.id.pay_keyboard_four)
    ImageView four;
    @BindView(R.id.pay_keyboard_five)
    ImageView five;
    @BindView(R.id.pay_keyboard_sex)
    ImageView sex;
    @BindView(R.id.pay_keyboard_seven)
    ImageView seven;
    @BindView(R.id.pay_keyboard_eight)
    ImageView eight;
    @BindView(R.id.pay_keyboard_nine)
    ImageView nine;
    @BindView(R.id.pay_cancel)
    TextView cancel;
    @BindView(R.id.pay_sure)
    TextView sure;
    @BindView(R.id.pay_box1)
    TextView box1;
    @BindView(R.id.pay_box2)
    TextView box2;
    @BindView(R.id.pay_box3)
    TextView box3;
    @BindView(R.id.pay_box4)
    TextView box4;
    @BindView(R.id.pay_box5)
    TextView box5;
    @BindView(R.id.pay_box6)
    TextView box6;
    @BindView(R.id.pay_title)
    TextView title;
    @BindView(R.id.pay_content)
    TextView content;
    @BindView(R.id.lockgp_activity_pay_loading)
    RelativeLayout loadingRl;
    private Handler handler = new Handler();
    private ArrayList<String> mList = new ArrayList<String>();
    private View mView;
    private OnPayListener listener;
    private Context mContext;
    private final String payPwd = "123456";

    public PayPasswordView(String monney, Context mContext, OnPayListener listener) {
        getDecorView(monney, mContext, listener);
       // payPwd = BaseConfigPreferences.getInstance(mContext).getValue(BaseConfigPreferences.APP_PAY_PASSWORD);
    }

    public static PayPasswordView getInstance(String monney, Context mContext, OnPayListener listener) {
        return new PayPasswordView(monney, mContext, listener);
    }

    public void getDecorView(String monney, Context mContext, OnPayListener listener) {
        this.listener = listener;
        this.mContext = mContext;
        mView = LayoutInflater.from(mContext).inflate(R.layout.item_paypassword, null);
        ButterKnife.bind(this, mView);
        content.setText("消费金额：" + monney + "元");
    }

    @OnClick({R.id.pay_keyboard_del, R.id.pay_keyboard_zero,
            R.id.pay_keyboard_one, R.id.pay_keyboard_two,
            R.id.pay_keyboard_three, R.id.pay_keyboard_four,
            R.id.pay_keyboard_five, R.id.pay_keyboard_sex,
            R.id.pay_keyboard_seven, R.id.pay_keyboard_eight,
            R.id.pay_keyboard_nine, R.id.pay_cancel,
            R.id.pay_sure})
    public void onViewClicked(View v) {
        if (v == zero) {
            parseActionType(KeyboardEnum.zero);
        } else if (v == one) {
            parseActionType(KeyboardEnum.one);
        } else if (v == two) {
            parseActionType(KeyboardEnum.two);
        } else if (v == three) {
            parseActionType(KeyboardEnum.three);
        } else if (v == four) {
            parseActionType(KeyboardEnum.four);
        } else if (v == five) {
            parseActionType(KeyboardEnum.five);
        } else if (v == sex) {
            parseActionType(KeyboardEnum.sex);
        } else if (v == seven) {
            parseActionType(KeyboardEnum.seven);
        } else if (v == eight) {
            parseActionType(KeyboardEnum.eight);
        } else if (v == nine) {
            parseActionType(KeyboardEnum.nine);
        } else if (v == cancel) {
            parseActionType(KeyboardEnum.cancel);
        } else if (v == sure) {
            parseActionType(KeyboardEnum.sure);
        } else if (v == del) {
            parseActionType(KeyboardEnum.del);
        }
    }

    @OnLongClick(R.id.pay_keyboard_del)
    public boolean onLongClick(View v) {
        // TODO Auto-generated method stub
        parseActionType(KeyboardEnum.longdel);
        return false;
    }

    private void parseActionType(KeyboardEnum type) {
        // TODO Auto-generated method stub
        if (type.getType() == KeyboardEnum.ActionEnum.add) {
            if (mList.size() < 6) {
                mList.add(type.getValue());
                updateUi();
            }
        } else if (type.getType() == KeyboardEnum.ActionEnum.delete) {
            if (mList.size() > 0) {
                mList.remove(mList.get(mList.size() - 1));
                updateUi();
            }
        } else if (type.getType() == KeyboardEnum.ActionEnum.cancel) {
            listener.onCancelPay();
        } else if (type.getType() == KeyboardEnum.ActionEnum.sure) {
            if (mList.size() < 6) {
                Toast.makeText(mContext, "支付密码必须6位", Toast.LENGTH_SHORT).show();
            } else {
                String payValue = "";
                for (int i = 0; i < mList.size(); i++) {
                    payValue += mList.get(i);
                }
                loadingRl.setVisibility(View.VISIBLE);
                final String finalPayValue = payValue;
                if (payPwd.equals(finalPayValue)) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSurePay(finalPayValue);
                        }
                    }, 2000);
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listener.onCancelPay();
                            MessageUtils.show(mContext,"密码错误");
                        }
                    }, 2000);
                }
            }
        } else if (type.getType() == KeyboardEnum.ActionEnum.longClick) {
            mList.clear();
            updateUi();
        }

    }

    private void updateUi() {
        // TODO Auto-generated method stub
        if (mList.size() == 0) {
            box1.setText("");
            box2.setText("");
            box3.setText("");
            box4.setText("");
            box5.setText("");
            box6.setText("");
        } else if (mList.size() == 1) {
            box1.setText(mList.get(0));
            box2.setText("");
            box3.setText("");
            box4.setText("");
            box5.setText("");
            box6.setText("");
        } else if (mList.size() == 2) {
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText("");
            box4.setText("");
            box5.setText("");
            box6.setText("");
        } else if (mList.size() == 3) {
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText(mList.get(2));
            box4.setText("");
            box5.setText("");
            box6.setText("");
        } else if (mList.size() == 4) {
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText(mList.get(2));
            box4.setText(mList.get(3));
            box5.setText("");
            box6.setText("");
        } else if (mList.size() == 5) {
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText(mList.get(2));
            box4.setText(mList.get(3));
            box5.setText(mList.get(4));
            box6.setText("");
        } else if (mList.size() == 6) {
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText(mList.get(2));
            box4.setText(mList.get(3));
            box5.setText(mList.get(4));
            box6.setText(mList.get(5));
        }
    }

    public interface OnPayListener {
        void onCancelPay();

        void onSurePay(String password);
    }

    public View getView() {
        return mView;
    }
}
