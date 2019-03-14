package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.utils.AppManager;
import com.jstech.gridregulation.utils.SystemUtil;
import com.jstech.gridregulation.widget.MyPopupWindow;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    Button btnExit;
    TextView tvContent;
    LinearLayout layoutClear;
    TextView tvCache;
    MyPopupWindow windowExit;//是否开启新的检查的窗口
    MyPopupWindow windowClear;//清除缓存

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        setToolBarTitle("设置");
        setToolSubBarTitle("");
        btnExit = findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(this);
        layoutClear = findViewById(R.id.layout_clear);
        layoutClear.setOnClickListener(this);
        tvCache = findViewById(R.id.tv_cache);
        windowExit = new MyPopupWindow.Builder().setContext(this)
                .setContentView(R.layout.layout_new_task_window)
                .setTitle(getResources().getString(R.string.tip))
                .setPass(getResources().getString(R.string.confrim))
                .setUnpass(getResources().getString(R.string.cancel))
                .setOutSideCancel(true)
                .setUnpassListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        windowExit.dismiss();
                    }
                })
                .setFouse(true)
                .setwidth(SystemUtil.getWith(this) * 2 / 3)
                .setheight(ViewGroup.LayoutParams.WRAP_CONTENT).builder();
        tvContent = windowExit.getContentFrameLayout().findViewById(R.id.text_view);
        tvContent.setText("请确认是否退出？");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:

                windowExit.showAtLocation(getLayoutId(), Gravity.CENTER, 0, 0);
                windowExit.setPassButtonOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        startActivity(new Intent(PISettingActivity.this, LoginActivity.class));
                        AppManager.getAppManager().finishAllActivity();
                        finish();
                    }
                });
                break;
            case R.id.layout_clear:
                Toast.makeText(this, "清除成功", Toast.LENGTH_LONG).show();
                tvCache.setText("0Mb");
                break;
        }
    }

    @Override
    protected void onStop() {
        windowExit.dismiss();
        super.onStop();
    }
}
