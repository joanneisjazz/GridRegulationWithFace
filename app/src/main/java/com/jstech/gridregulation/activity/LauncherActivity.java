package com.jstech.gridregulation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.service.GPSService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.UserBean;
//import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.UserBeanDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.SharedPreferencesHelper;

/**
 * 启动页面
 */
public class LauncherActivity extends Activity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                Long tmpTime = Long.valueOf((long) SharedPreferencesHelper.getInstance(LauncherActivity.this).getSharedPreference("expTime", 0l));
                long time = tmpTime.longValue();
                long now = System.currentTimeMillis() / 1000;
                if (time == 0) {
                    startActivity(new Intent(LauncherActivity.this, LoginActivity.class));
                    finish();
                    return;
                }
                if (now >= time) {
                    startActivity(new Intent(LauncherActivity.this, LoginActivity.class));
                    finish();
                    return;
                }
//                UserBean userBean = MyApplication.instance.getSession().getUserBeanDao()
//                        .queryBuilder()
//                        .orderDesc(UserBeanDao.Properties.LoginTime)
//                        .list().get(0);
//                if (null == userBean) {
//                    startActivity(new Intent(LauncherActivity.this, LoginActivity.class));
//                    finish();
//                    return;
//                }
//                MyApplication.instance.setUserBean(userBean);
                Intent intent = new Intent(LauncherActivity.this, WorkDeskActivity.class);
                intent.putExtra(ConstantValue.KEY_OBJECT_ID, "1");
                startService(new Intent(LauncherActivity.this, GPSService.class));
                startActivity(intent);
                finish();

            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        handler = null;
        super.onDestroy();
    }
}
