package com.xjt.ordershop.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.baselib.bean.Good;
import com.xjt.ordershop.R;
import com.xjt.ordershop.aop.permission.NeedPermission;
import com.xjt.ordershop.aop.singleclick.SingleClick;
import com.xjt.ordershop.base.BaseActivity;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.util.BitmapUtil;
import com.xjt.ordershop.util.CommonUtil;
import com.xjt.ordershop.util.Global;
import com.xjt.ordershop.util.LogUtils;
import com.xjt.ordershop.util.MessageUtils;
import com.xjt.ordershop.util.NetApiUtil;
import com.xjt.ordershop.util.PhotoUtils;
import com.xjt.ordershop.util.ScreenUtil;
import com.xjt.ordershop.util.ThreadUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by xuqunxing on  2020/3/2
 */
public class GoodInfoActivity extends BaseActivity {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.common_back_rl)
    RelativeLayout backRl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.good_name_et)
    EditText goodNameEt;
    @BindView(R.id.good_detail_et)
    EditText goodDetailEt;
    @BindView(R.id.good_price_et)
    EditText goodPriceEt;
    @BindView(R.id.good_discount_et)
    EditText goodDiscountEt;
    @BindView(R.id.good_category_et)
    TextView goodCategoryTv;
    @BindView(R.id.good_pic_bt)
    Button goodPicBt;
    @BindView(R.id.good_add_tv)
    Button goodAddBt;
    @BindView(R.id.good_image_Iv)
    ImageView goodImageIv;
    private Uri imageUri = null;

    public static String KEY_EDIT = "key_edit";
    public static String KEY_GOOD = "key_good";
    public static String KEY_GOOD_ID = "key_good_id";
    private Good good = null;
    private int goodId = 0;
    private String goodPic = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_info);
        ButterKnife.bind(this);
        good = (Good) getIntent().getSerializableExtra(KEY_GOOD);
        goodId = getIntent().getIntExtra(KEY_GOOD_ID, 0);
        if (good == null && goodId == 0) {
            commonTitleTv.setText("添加商品");
            goodAddBt.setText("添加商品");
        } else {
            commonTitleTv.setText("修改商品");
            goodAddBt.setText("修改商品");
            if(good != null){
                goodNameEt.setText(good.getGoodName());
                goodDetailEt.setText(good.getGoodDetail());
                goodPriceEt.setText(good.getGoodPrice() + "");
                goodDiscountEt.setText(good.getGoodDiscount() + "");
                goodCategoryTv.setText(good.getCategoryName());
                goodCategoryTv.setTag(good.getCategoryId());
                goodPic = good.getGoodPic();
                ImageLoader.getInstance().displayImage(goodPic, goodImageIv);
            }else {
                getGoodById(goodId);
            }
        }
        ImmersionBar.with(this).statusBarView(statusBarView)
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    private void getGoodById(int goodId) {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                ServerResult<CommonResultMessage> goodsCookBookDetail = NetApiUtil.getGoodListByID(goodId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (goodsCookBookDetail != null && goodsCookBookDetail.getResultBean() != null) {
                            CommonResultMessage resultBean = goodsCookBookDetail.getResultBean();
                            if (resultBean.isSuccess()) {
                                String message = resultBean.getMessage();
                                if (!TextUtils.isEmpty(message)) {
                                    try {
                                        good = new Gson().fromJson(message, Good.class);
                                        if (good != null) {
                                            goodNameEt.setText(good.getGoodName());
                                            goodDetailEt.setText(good.getGoodDetail());
                                            goodPriceEt.setText(good.getGoodPrice() + "");
                                            goodDiscountEt.setText(good.getGoodDiscount() + "");
                                            goodCategoryTv.setText(good.getCategoryName());
                                            goodCategoryTv.setTag(good.getCategoryId());
                                            goodPic = good.getGoodPic();
                                            ImageLoader.getInstance().displayImage(goodPic, goodImageIv);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        MessageUtils.show(GoodInfoActivity.this, message);
                                    }
                                }
                            }
                        } else {
                            MessageUtils.show(GoodInfoActivity.this, "程序出错，请稍后再试！");
                        }
                    }
                });
            }
        });
    }

    @OnClick({R.id.good_pic_bt, R.id.good_add_tv, R.id.common_back_rl, R.id.good_category_et})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_rl:
                finish();
                break;
            case R.id.good_pic_bt:
                selectPic();
                break;
            case R.id.good_add_tv:
                addGood(view);
                break;
            case R.id.good_category_et:
                jumpToCategorySelectView(view);
                break;
        }
    }

    @SingleClick
    private void jumpToCategorySelectView(View view) {
        startActivityForResult(new Intent(GoodInfoActivity.this, FoodCategorySelectActivity.class), 1000);
    }

    @SingleClick(clickIntervals = 800)
    private void addGood(View view) {
        LogUtils.e("======", "======addGood");
        String goodName = goodNameEt.getText().toString();
        String goodDetail = goodDetailEt.getText().toString();
        String goodPrice = goodPriceEt.getText().toString();
        String goodDiscount = goodDiscountEt.getText().toString();
        Object tag = goodCategoryTv.getTag();
        String categoryName = goodCategoryTv.getText().toString();
        if (!TextUtils.isEmpty(goodName) && !TextUtils.isEmpty(goodDetail)
                && !TextUtils.isEmpty(goodPrice) && !TextUtils.isEmpty(goodDiscount) && (imageUri != null || goodPic != null) && tag != null) {
            if (good == null) {
                ThreadUtil.executeMore(new Runnable() {
                    @Override
                    public void run() {
                        String bitmapEncodeStr = BitmapUtil.encodeBitmap(PhotoUtils.compress(PhotoUtils.getImageAbsolutePath(GoodInfoActivity.this, imageUri), ScreenUtil.getCurrentScreenWidth(getApplicationContext()), ScreenUtil.getCurrentScreenHeight(getApplicationContext())));
                        ServerResult<CommonResultMessage> commonResultMessageServerResult = NetApiUtil.addGood(goodName, goodDetail, goodPrice, goodDiscount, bitmapEncodeStr, (int) tag, categoryName);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (commonResultMessageServerResult != null && commonResultMessageServerResult.getResultBean() != null) {
                                    CommonResultMessage resultBean = commonResultMessageServerResult.getResultBean();
                                    MessageUtils.show(GoodInfoActivity.this, resultBean.getMessage());
                                    finish();
                                }
                            }
                        });
                    }
                });
            } else {
                ThreadUtil.executeMore(new Runnable() {
                    @Override
                    public void run() {
                        String bitmapEncodeStr = null;
                        if (imageUri != null) {
                            bitmapEncodeStr = BitmapUtil.encodeBitmap(PhotoUtils.compress(PhotoUtils.getImageAbsolutePath(GoodInfoActivity.this, imageUri), ScreenUtil.getCurrentScreenWidth(getApplicationContext()), ScreenUtil.getCurrentScreenHeight(getApplicationContext())));
                        } else {
                            bitmapEncodeStr = goodPic;
                        }
                        ServerResult<CommonResultMessage> commonResultMessageServerResult = NetApiUtil.updateGood(good.getId(), goodName, goodDetail, goodPrice, goodDiscount, bitmapEncodeStr, (int) tag, categoryName);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (commonResultMessageServerResult != null && commonResultMessageServerResult.getResultBean() != null) {
                                    CommonResultMessage resultBean = commonResultMessageServerResult.getResultBean();
                                    MessageUtils.show(GoodInfoActivity.this, resultBean.getMessage());
                                    finish();
                                }
                            }
                        });
                    }
                });
            }
        } else {
            MessageUtils.show(GoodInfoActivity.this, "选项不能为空");
        }
    }

    @NeedPermission({android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void selectPic() {
        requestPermissionTest();
    }


    @Override
    protected void requestPermissionTest() {
        super.requestPermissionTest();
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/jpeg");
        startActivityForResult(intentToPickPic, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                String name = data.getStringExtra("name");
                int id = data.getIntExtra("id", 0);
                goodCategoryTv.setText(name);
                goodCategoryTv.setTag(id);
            } else if (requestCode == 2000) {
                if (data != null && data.getData() != null) {
                    imageUri = data.getData();
                }
                LogUtils.e("======", "======album:" + imageUri);
                Bitmap bitmapFromUri = PhotoUtils.getBitmapFromUri(imageUri, this);
                if (bitmapFromUri != null) {
                    goodImageIv.setImageBitmap(bitmapFromUri);
                    //  goodImageIv.setTag(imageUri);
                }
            }
        }
    }
}
