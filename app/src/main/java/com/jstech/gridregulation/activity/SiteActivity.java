package com.jstech.gridregulation.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.fragment.RegulateObjectListFragment;
import com.jstech.gridregulation.fragment.RegulateObjectMapFragment;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;

/**
 * Created by hesm on 2018/11/8.
 * 现场检查,选择检查企业，开启检查任务
 */

public class SiteActivity extends BaseActivity {
    public static final String NEW_TASK = "0";
    public static final String CONTINUE_TASK = "1";

    private RegulateObjectListFragment fragmentList;
    private RegulateObjectMapFragment fragmentMap;

    private FragmentManager fm = getSupportFragmentManager();
    private Fragment currentFragment;
    private String status;
    private boolean isFirst = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_site_check_trans;
    }

    @Override
    public void initView() {
        setToolBarTitle(getResources().getString(R.string.site_regulate));
        setToolSubBarTitle("地图模式");
        fragmentList = RegulateObjectListFragment.newInstance();
        fragmentMap = RegulateObjectMapFragment.newInstance();
        currentFragment = fragmentList;
        fm.beginTransaction().add(R.id.frame_content, fragmentMap, "地图模式").commit();
        fm.beginTransaction().add(R.id.frame_content, fragmentList, "列表模式").hide(fragmentMap).commit();
        status = "list";
        getSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("list".equals(status)) {
                    fm.beginTransaction().hide(currentFragment).show(fragmentMap).commit();
                    currentFragment = fragmentMap;
                    status = "map";
                    setToolSubBarTitle("列表模式");
                } else {
                    fm.beginTransaction().hide(currentFragment).show(fragmentList).commit();
                    currentFragment = fragmentList;
                    setToolSubBarTitle("地图模式");
                    status = "list";
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        currentFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        fragmentList = null;
        fragmentMap = null;
        super.onDestroy();
    }

    private FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.frame_content, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }

    /**
     * 人脸验证
     *
     * @param code
     */
    public static void validate(Activity activity,String code, RegulateObjectBean objectBean) {
        Intent intent = new Intent(activity, TakeFacePicActivity.class);
        intent.putExtra(ConstantValue.KEY_CHECK_STATUS, String.valueOf(code));
        intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, objectBean);
        activity.startActivityForResult(intent, ConstantValue.REQUEST_CODE_SITE_CHECK);
        activity.finish();
    }
}
