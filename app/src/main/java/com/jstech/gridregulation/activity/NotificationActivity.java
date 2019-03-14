package com.jstech.gridregulation.activity;

import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseActivity;

/**
 * Created by hesm on 2018/11/11.
 */

public class NotificationActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_notification;
    }

    @Override
    public void initView() {

        setToolBarTitle("消息");
        setToolSubBarTitle("");
    }
}
