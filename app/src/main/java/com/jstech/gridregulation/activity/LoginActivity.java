package com.jstech.gridregulation.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.api.LoginApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.service.GPSService;
import com.jstech.gridregulation.utils.AppManager;
import com.jstech.gridregulation.utils.LogUtils;
import com.jstech.gridregulation.utils.SystemUtil;
import com.jstech.gridregulation.utils.TextUtil;
import com.rxretrofitlibrary.greendao.UserBeanDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.UserBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

//import com.tinkerpatch.sdk.TinkerPatch;

/**
 * Created by hesm on 2018/10/22.
 */

public class LoginActivity extends BaseActivity implements HttpOnNextListener {

    boolean permissions1Granted = false;

    LoginApi loginApi;
    HttpManager manager;

    private AutoCompleteTextView edtLoginName;
    private EditText edtPassword;
    private LinearLayout layoutRootView, layoutContentView;
    private Button btnLogin;

    private ArrayAdapter<String> adapter;
    private List<String> mData;

    String permissons[] = new String[]{
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        btnLogin = findViewById(R.id.email_sign_in_button);
        edtLoginName = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);
        layoutRootView = findViewById(R.id.root_view);
        layoutContentView = findViewById(R.id.layout_content_view);
        keepLoginBtnNotOver(layoutRootView, layoutContentView);

        mData = new ArrayList<>();
//        List<UserBean> userBeans = MyApplication.instance.getSession().getUserBeanDao().loadAll();
//        for (int i = 0; i < userBeans.size(); i++) {
//            mData.add(userBeans.get(i).getLoginname());
//            mData.add(userBeans.get(i).getMobile());
//        }
        mData.add(SharedPreferencesHelper.getInstance(this).getSharedPreference("loginName", "").toString());
        mData.add(SharedPreferencesHelper.getInstance(this).getSharedPreference("phone", "").toString());

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mData);
        edtLoginName.setAdapter(adapter);

        loginApi = new LoginApi();
        manager = new HttpManager(this, this);
//        TinkerPatch patch = TinkerPatch.with();
//        if (null != patch) {
//            patch.fetchPatchUpdate(true);
//        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        requestForPermissions();
    }


    private void requestForPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, permissons, 99);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (null == grantResults || grantResults.length <= 0) {
            return;
        }
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                AppManager.getAppManager().finishAllActivity();
                return;
            }
            switch (i) {
                case 0:
                    permissions1Granted = true;
                    SDKInitializer.initialize(getApplicationContext());
                    SDKInitializer.setCoordType(CoordType.BD09LL);
                    break;
                default:
                    break;
            }

        }
    }


    @Override
    public void onNext(String resulte, String method) {
        LogUtils.d(resulte);
        JSONObject o = JSON.parseObject(resulte);
        if (!ConstantValue.isSuccess(o)) {
            Toast.makeText(LoginActivity.this, o.getString("msg"), Toast.LENGTH_LONG).show();
            return;
        }

        UserBean bean = o.getObject(ConstantValue.DATA, UserBean.class);

        if (TextUtil.isEmpty(bean.getUserExtId())) {
            Toast.makeText(LoginActivity.this, "该账户无法登陆", Toast.LENGTH_LONG).show();
            return;
        }
        UserBeanDao userBeanDao = MyApplication.getInstance().getSession().getUserBeanDao();
        UserBean exist = userBeanDao.queryBuilder().where(
                UserBeanDao.Properties.Id.eq(bean.getId())
        ).unique();
        if (null != exist) {
            userBeanDao.delete(exist);
        }
        userBeanDao.insert(bean);
        bean.setLoginTime(String.valueOf(System.currentTimeMillis()));
        Long extTime = Long.valueOf(bean.getExpTime());
        Long now = Long.valueOf(System.currentTimeMillis() / 1000);
        SharedPreferencesHelper.getInstance(LoginActivity.this).put("token", bean.getLoginname());
        SharedPreferencesHelper.getInstance(LoginActivity.this).put("expTime", extTime + now);
        SharedPreferencesHelper.getInstance(LoginActivity.this).put("extId", bean.getUserExtId());
        SharedPreferencesHelper.getInstance(LoginActivity.this).put("orgId", bean.getOrgId());
        SharedPreferencesHelper.getInstance(LoginActivity.this).put("id", bean.getId());
        SharedPreferencesHelper.getInstance(LoginActivity.this).put("userName", bean.getUsername());
        SharedPreferencesHelper.getInstance(LoginActivity.this).put("loginName", bean.getLoginname());
        SharedPreferencesHelper.getInstance(LoginActivity.this).put("phone", bean.getMobile());

//        UserBeanDao userBeanDao = MyApplication.getInstance().getSession().getUserBeanDao();
//        List<UserBean> beans = userBeanDao.loadAll();
//        for (UserBean bean1 : beans) {
//            if (bean1.getId().equals(bean.getId())) {
//                bean.setBeanId(bean1.getBeanId());
//                break;
//            }
//        }
//        MyApplication.getInstance().getSession().getUserBeanDao().save(bean);
//        MyApplication.getInstance().setUserBean(bean);

        /**
         * 开启定位
         */
        startService(new Intent(LoginActivity.this, GPSService.class));
        Intent intent = new Intent(LoginActivity.this, WorkDeskActivity.class);
        intent.putExtra(ConstantValue.KEY_CONTENT, bean);

        startActivity(intent);
    }

    @Override
    public void onError(ApiException e, String method) {
//        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * 保持登录按钮始终不会被覆盖
     *
     * @param root
     * @param subView
     */
    private void keepLoginBtnNotOver(final View root, final View subView) {

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                // 获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                // 若不可视区域高度大于200，则键盘显示,其实相当于键盘的高度
                if (rootInvisibleHeight > 200) {
                    // 显示键盘时
                    int srollHeight = rootInvisibleHeight - (root.getHeight() - subView.getHeight())
                            - SystemUtil.getNavigationBarHeight(root.getContext());
                    if (srollHeight > 0) {
                        //当键盘高度覆盖按钮时
                        root.scrollTo(0, srollHeight);
                    }
                } else {
                    // 隐藏键盘时
                    root.scrollTo(0, 0);
                }
            }
        });

    }

    private void attemptLogin() {

        SystemUtil.closeKeyboard(LoginActivity.this);
        // Reset errors.
        edtLoginName.setError(null);
        edtLoginName.setError(null);

        // Store values at the time of the login attempt.
        String loginname = edtLoginName.getText().toString();
        String password = edtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
//        if (!CheckUtils.isPassword(password)) {
//            edtPassword.setError(getString(R.string.error_invalid_password));
//            focusView = edtPassword;
//            cancel = true;
//        }

        // Check for a valid email address.
//        if (!CheckUtils.isMobileNO(loginname)) {
//            edtLoginName.setError(getString(R.string.error_invalid_email));
//            focusView = edtLoginName;
//            cancel = true;
//        }

//        if (cancel) {
//            focusView.requestFocus();
//            return;
//        }

        if (TextUtil.isEmpty(loginname)) {
            edtLoginName.setError(getString(R.string.error_invalid_email));
            focusView = edtLoginName;
            cancel = true;
        }
        if (TextUtil.isEmpty(password)) {
            edtPassword.setError(getString(R.string.error_invalid_password));
            focusView = edtPassword;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        UserBean userBean = new UserBean();
        userBean.setLoginname(loginname);
        userBean.setPassword(TextUtil.encrypt(password));
        loginApi.setUserBean(userBean);
        manager.doHttpDeal(loginApi);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
        return false;
    }


}
