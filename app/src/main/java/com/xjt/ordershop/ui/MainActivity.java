package com.xjt.ordershop.ui;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;
import com.xjt.ordershop.R;
import com.xjt.ordershop.base.BaseActivity;
import com.xjt.ordershop.base.BaseFragment;
import com.xjt.ordershop.callback.OnObjectCallback;
import com.xjt.ordershop.ui.fragment.GoodSearchFragment;
import com.xjt.ordershop.ui.fragment.HomeFragment;
import com.xjt.ordershop.ui.fragment.MyFragment;
import com.xjt.ordershop.ui.fragment.OrderFragment;
import com.xjt.ordershop.util.RequestPermissionUtil;
import com.xjt.ordershop.widget.bottomtab.CustomBottomTabWidget;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tabWidget)
    CustomBottomTabWidget tabWidget;
    private List<BaseFragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RequestPermissionUtil.requestPermissions(this, true, new String[]{Manifest.permission.READ_PHONE_STATE}, null);
        //初始化
        init();
    }


    private void init() {
        //构造Fragment的集合
        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new GoodSearchFragment());
        fragmentList.add(new OrderFragment());
        fragmentList.add(new MyFragment());
        //初始化CustomBottomTabWidget
        tabWidget.init(getSupportFragmentManager(), fragmentList);
        tabWidget.setCallBack(new OnObjectCallback() {
            @Override
            public void onClickCallBack(Object... value) {
                int position = (int) value[0];
                if(position == 0){
                    ImmersionBar.with(MainActivity.this).transparentStatusBar().statusBarDarkFont(true, 0.2f).init();
                }else if(position == 1){
                    ImmersionBar.with(MainActivity.this).statusBarDarkFont(true, 0.2f).init();
                }else if(position == 2){
                    ImmersionBar.with(MainActivity.this).statusBarDarkFont(true, 0.2f).init();
                }else if(position == 3){
                    ImmersionBar.with(MainActivity.this).statusBarDarkFont(true, 0.2f).init();
                }else if(position == 4){
                    ImmersionBar.with(MainActivity.this).transparentStatusBar().statusBarDarkFont(true, 0.2f).init();
                }
            }
        });
    }

    private long firstTime = 0;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            finish();
        }
    }

}
