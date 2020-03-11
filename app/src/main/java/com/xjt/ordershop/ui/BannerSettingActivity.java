package com.xjt.ordershop.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.xjt.baselib.bean.CommonResultMessage;
import com.xjt.ordershop.R;
import com.xjt.ordershop.aop.permission.NeedPermission;
import com.xjt.ordershop.aop.singleclick.SingleClick;
import com.xjt.ordershop.base.BaseActivity;
import com.xjt.ordershop.base.basehttp.ServerResult;
import com.xjt.ordershop.util.BitmapUtil;
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
 * Create by xuqunxing on  2020/3/7
 */
public class BannerSettingActivity extends BaseActivity {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.common_back_rl)
    RelativeLayout backRl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.bannername_et)
    EditText nameEt;
    @BindView(R.id.banner_select_category_bt)
    Button selectCategoryBt;
    @BindView(R.id.banner_select_good_bt)
    Button selectGoodBt;
    @BindView(R.id.banner_select_category_ll)
    LinearLayout categoryLL;
    @BindView(R.id.banner_select_category_tv)
    TextView categoryTv;
    @BindView(R.id.banner_select_good_ll)
    LinearLayout goodLL;
    @BindView(R.id.banner_select_good_tv)
    TextView goodTv;
    @BindView(R.id.banenr_pic_iv)
    ImageView bannerPicIv;
    @BindView(R.id.banner_add_tv)
    TextView addTv;
    private int goodId = 0;
    private int bType;///0 不处理，1 跳商品详情页 2 跳商品分类页面
    private Uri imageUri = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_set);
        ButterKnife.bind(this);
        commonTitleTv.setText("轮播图活动添加");
        ImmersionBar.with(this).statusBarView(statusBarView)
                .statusBarDarkFont(true, 0.2f)
                .init();
        goodLL.setVisibility(View.GONE);
        categoryLL.setVisibility(View.GONE);
    }


    @OnClick({R.id.banner_add_tv, R.id.banner_select_good_tv, R.id.banner_select_category_tv, R.id.common_back_rl, R.id.banner_select_category_bt, R.id.banner_select_good_bt, R.id.banner_pic_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_rl:
                finish();
                break;
            case R.id.banner_select_good_bt:
                goodLL.setVisibility(View.VISIBLE);
                categoryLL.setVisibility(View.GONE);
                selectCategoryBt.setBackgroundColor(Color.parseColor("#ffffff"));
                selectGoodBt.setBackgroundColor(Color.parseColor("#cccccc"));
                bType = 1;
                break;
            case R.id.banner_select_category_bt:
                goodLL.setVisibility(View.GONE);
                categoryLL.setVisibility(View.VISIBLE);
                selectCategoryBt.setBackgroundColor(Color.parseColor("#cccccc"));
                selectGoodBt.setBackgroundColor(Color.parseColor("#ffffff"));
                bType = 2;
                break;
            case R.id.banner_add_tv:
                addBanner(view);
                break;
            case R.id.banner_select_good_tv:
                jumptoGoodSelectView(view);
                break;
            case R.id.banner_select_category_tv:
                jumpToCategorySelectView(view);
                break;

            case R.id.banner_pic_bt:
                selectPic();
                break;
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

    @SingleClick
    private void addBanner(View view) {
        if (bType == 1) {
            String nameStr = nameEt.getText().toString();
            if (TextUtils.isEmpty(nameStr) || goodId == 0 || imageUri == null) {
                MessageUtils.show(this, "参数不能为空");
                return;
            }
            addBannerAction(nameStr, bType, -1, goodId);
        } else if (bType == 2) {
            String nameStr = nameEt.getText().toString();
            int categoryId = (int) categoryTv.getTag();
            if (TextUtils.isEmpty(nameStr) || categoryId == 0 || imageUri == null) {
                MessageUtils.show(this, "参数不能为空");
                return;
            }
            addBannerAction(nameStr, bType, categoryId, -1);
        } else {
            MessageUtils.show(this, "参数错误");
        }
    }

    @SingleClick
    private void jumptoGoodSelectView(View view) {
        Intent intent = new Intent(BannerSettingActivity.this, GoodSearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(GoodSearchActivity.KEY_GOOD_CATEGORY_SHOW, false);
        intent.putExtra(GoodSearchActivity.KEY_BUNDLE, bundle);
        startActivityForResult(intent, 3000);
    }


    @SingleClick
    private void jumpToCategorySelectView(View view) {
        startActivityForResult(new Intent(BannerSettingActivity.this, FoodCategorySelectActivity.class), 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                String name = data.getStringExtra("name");
                int id = data.getIntExtra("id", 0);
                categoryTv.setText(name);
                categoryTv.setTag(id);
            } else if (requestCode == 2000) {
                if (data != null && data.getData() != null) {
                    imageUri = data.getData();
                }
                LogUtils.e("======", "======album:" + imageUri);
                Bitmap bitmapFromUri = PhotoUtils.getBitmapFromUri(imageUri, this);
                if (bitmapFromUri != null) {
                    bannerPicIv.setImageBitmap(bitmapFromUri);
                }
            } else if (requestCode == 3000) {
                String goodName = data.getStringExtra("goodName");
                // goodPic = data.getStringExtra("goodPic");
                goodId = data.getIntExtra("goodId", 0);
                goodTv.setText(goodName);
                LogUtils.e("======", "======goodName:" + goodName + "--goodPic:" + "--goodId:" + goodId);
            }
        }
    }

    private void addBannerAction(String name, int bType, int categoryId, int goodId) {
        String bitmapEncodeStr = BitmapUtil.encodeBitmap(PhotoUtils.compress(PhotoUtils.getImageAbsolutePath(BannerSettingActivity.this, imageUri), ScreenUtil.getCurrentScreenWidth(getApplicationContext()), ScreenUtil.getCurrentScreenHeight(getApplicationContext())));
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                ServerResult<CommonResultMessage> goodsCookBookDetail = NetApiUtil.addBanner(bType, categoryId, goodId, bitmapEncodeStr, name);
                Global.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (goodsCookBookDetail != null && goodsCookBookDetail.getResultBean() != null) {
                            CommonResultMessage resultBean = goodsCookBookDetail.getResultBean();
                            String message = resultBean.getMessage();
                            resultBean.isSuccess();
                            MessageUtils.show(BannerSettingActivity.this, message);
                            finish();
                        } else {
                            MessageUtils.show(BannerSettingActivity.this, "程序出错，请稍后再试！");
                        }

                    }
                });
            }
        });
    }
}
