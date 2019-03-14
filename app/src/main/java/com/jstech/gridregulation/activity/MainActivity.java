package com.jstech.gridregulation.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.fragment.PersonalCenterFragment;
import com.jstech.gridregulation.fragment.ProblemReportFragment;
import com.jstech.gridregulation.fragment.SiteRegulateMapFragment;
import com.jstech.gridregulation.utils.AppManager;
import com.jstech.gridregulation.utils.LogUtils;
import com.luck.picture.lib.config.PictureConfig;

public class MainActivity extends BaseActivity {

    BottomNavigationView bottomNavigationView;
    PersonalCenterFragment personalCenterFragment;
    ProblemReportFragment problemReportFragment;
    SiteRegulateMapFragment siteRegulateMapFragment;
    FragmentManager fm = getSupportFragmentManager();
    Fragment active;

    TextView tvSubTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        isReadPhonePermissionGranted();

        setToolBarTitle("网格化监管");
        setToolSubBarTitle("");
        tvSubTitle = getSubTitle();
        personalCenterFragment = PersonalCenterFragment.newInstance();
        problemReportFragment = ProblemReportFragment.newInstance();
        siteRegulateMapFragment = SiteRegulateMapFragment.newInstance();
        active = siteRegulateMapFragment;
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        fm.beginTransaction().add(R.id.frame_content, personalCenterFragment, "个人中心").hide(personalCenterFragment).commit();
        fm.beginTransaction().add(R.id.frame_content, problemReportFragment, "线索上报").hide(problemReportFragment).commit();
        fm.beginTransaction().add(R.id.frame_content, siteRegulateMapFragment, "现场检查").commit();


    }

    public boolean isReadPhonePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                LogUtils.v("Permission is granted");
                return true;
            } else {

                LogUtils.v("Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            LogUtils.v("Permission is granted");
            return true;
        }
    }

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.site_regulate:
                    tvSubTitle.setText("刷新");
                    tvSubTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            siteRegulateMapFragment.refresh();
                        }
                    });
                    fm.beginTransaction().hide(active).show(siteRegulateMapFragment).commit();
                    active = siteRegulateMapFragment;
                    return true;
                case R.id.problem_report:
                    tvSubTitle.setText("保存");
                    tvSubTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            problemReportFragment.save();
                        }
                    });
                    fm.beginTransaction().hide(active).show(problemReportFragment).commit();
                    active = problemReportFragment;
                    return true;
                case R.id.personal_center:
                    tvSubTitle.setText("设置");
                    tvSubTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            problemReportFragment.save();
                            personalCenterFragment.setting();
                        }
                    });
                    fm.beginTransaction().hide(active).show(personalCenterFragment).commit();
                    active = personalCenterFragment;
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (null == grantResults || grantResults.length <= 0) {
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            LogUtils.v("Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            if (requestCode == 99) {
                SDKInitializer.initialize(getApplicationContext());
                //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
                //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
                SDKInitializer.setCoordType(CoordType.BD09LL);

            }
        } else {
            AppManager.getAppManager().finishAllActivity();
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (active == siteRegulateMapFragment) {
            siteRegulateMapFragment.refresh();
        } else if (active == problemReportFragment) {

        } else {

        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            problemReportFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
