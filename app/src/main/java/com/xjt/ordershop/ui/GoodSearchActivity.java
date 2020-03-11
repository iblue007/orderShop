package com.xjt.ordershop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xjt.ordershop.R;
import com.xjt.ordershop.base.BaseActivity;
import com.xjt.ordershop.callback.OnObjectCallback;
import com.xjt.ordershop.ui.fragment.GoodSearchFragment;

/**
 * Create by xuqunxing on  2020/3/7
 */
public class GoodSearchActivity extends BaseActivity {

    public static String KEY_GOOD_CATEGORY_SHOW = "key_good_category_show";
    public static String KEY_BUNDLE = "key_bundle";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_search);
        GoodSearchFragment goodSearchFragment = new GoodSearchFragment();
        getSupportFragmentManager()    //
                .beginTransaction()
                .add(R.id.fragment_container, goodSearchFragment)
                .commit();
        Bundle bundleExtra = getIntent().getBundleExtra(KEY_BUNDLE);
        goodSearchFragment.setArguments(bundleExtra);
        goodSearchFragment.setCallBack(new OnObjectCallback() {
            @Override
            public void onClickCallBack(Object... value) {
                int goodId = (int) value[0];
                String goodName = (String) value[1];
                String goodPic = (String) value[2];
                Intent intent = new Intent();
                intent.putExtra("goodId", goodId);
                intent.putExtra("goodName", goodName);
                intent.putExtra("goodPic", goodPic);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


}
