package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.api.GetRoNameApi;
import com.jstech.gridregulation.api.SaveUserInfoApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.bean.SupervisorBean;
import com.jstech.gridregulation.utils.TextUtil;
import com.rxretrofitlibrary.greendao.UserBeanDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.UserBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.SharedPreferencesHelper;

/**
 * Created by hesm on 2018/10/22.
 */

public class PersonalInfoActivity extends BaseActivity implements HttpOnNextListener {

    public static final int resust_code = 120;

    private EditText edtLoginName, edtRegulatorName, edtPhone, edtEmail, edtOrg;
    private HttpManager manager;
    private GetRoNameApi getRoNameApi;
    private SaveUserInfoApi saveUserInfoApi;
    private TextView tvSub;
    private TextView tvStatus;
    private ImageView ivPhoto;

    private String id = "";
    private String userExtId = "";

    private UserBean infoBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_info;
    }

    @Override
    public void initView() {

        setToolBarTitle("个人信息");
        setToolSubBarTitle("保存");
        userExtId = SharedPreferencesHelper.getInstance(this).getUserExtId(this);
        id = SharedPreferencesHelper.getInstance(this).getSharedPreference("id", "").toString();
        tvSub = getSubTitle();
        edtLoginName = findViewById(R.id.edt_login_name);
        edtRegulatorName = findViewById(R.id.edt_regulator_name);
        edtPhone = findViewById(R.id.edt_phone);
        edtEmail = findViewById(R.id.edt_email);
        edtOrg = findViewById(R.id.edt_org);
        ivPhoto = findViewById(R.id.iv_photo);
        tvStatus = findViewById(R.id.tv_status);

        setdata();
        manager = new HttpManager(this, this);
        saveUserInfoApi = new SaveUserInfoApi();
        tvSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == infoBean) {
                    return;
                }
                infoBean.setUsername(edtRegulatorName.getText().toString());
                infoBean.setLoginname(edtLoginName.getText().toString());
                infoBean.setMobile(edtPhone.getText().toString());
                infoBean.setOrgName(edtOrg.getText().toString());
                infoBean.setEmail(edtEmail.getText().toString());
                MyApplication.getInstance().getSession().getUserBeanDao().update(infoBean);
                saveUserInfoApi.setUserInfoBean(infoBean);
                manager.doHttpDeal(saveUserInfoApi);
            }
        });

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PersonalInfoActivity.this, TakeFacePicActivity.class), resust_code);
            }
        });
        getRoNameApi = new GetRoNameApi();
        getRoNameApi.setExtId(userExtId);
        manager.doHttpDeal(getRoNameApi);


    }

    @Override
    public void onNext(String resulte, String method) {
        JSONObject o = JSON.parseObject(resulte);
        if (!ConstantValue.isSuccess(o)) {
            return;
        }
//        if (method.equals(getPersonalInfoApi.getMethod())) {
//            infoBean = o.getObject(ConstantValue.RESULT, UserBean.class);
//            edtLoginName.setText(infoBean.getLoginname());
//            edtRegulatorName.setText(infoBean.getUsername());
//            edtPhone.setText(infoBean.getMobile());
//            edtOrg.setText(infoBean.getOrgName());
//            edtEmail.setText(infoBean.getEmail());
//        } else
        if (method.equals(saveUserInfoApi.getMethod())) {
            Toast.makeText(PersonalInfoActivity.this, "保存成功", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.putExtra(ConstantValue.KEY_CONTENT, infoBean);
//            MyApplication.getInstance().setUserBean(infoBean);
            PersonalInfoActivity.this.setResult(RESULT_OK, intent);
            finish();
        } else if (method.equals(getRoNameApi.getMethod())) {
            //获取到头像信息之后更新页面

            SupervisorBean supervisorBean = o.getObject("data", SupervisorBean.class);
            if ("0".equals(supervisorBean.getImageStatus())) {
                tvStatus.setText("未上传");
            } else if ("1".equals(supervisorBean.getImageStatus())) {
                tvStatus.setText("审核中");
            } else if ("2".equals(supervisorBean.getImageStatus())) {
                tvStatus.setText("未通过");
            } else if ("3".equals(supervisorBean.getImageStatus())) {
                tvStatus.setText("已通过");
            }
            if (!TextUtil.isEmpty(supervisorBean.getImage())) {
                Glide.with(PersonalInfoActivity.this).load(supervisorBean.getImage()).into(ivPhoto);
            }
        }

    }

    @Override
    public void onError(ApiException e, String method) {
        Toast.makeText(this, "保存失败，请检查网络", Toast.LENGTH_LONG).show();
    }

    private void setdata() {

        Glide.with(this).load(R.mipmap.addimg_1x).into(ivPhoto);
        infoBean = MyApplication.getInstance().getSession().getUserBeanDao().queryBuilder().where(
                UserBeanDao.Properties.Id.eq(id)
        ).unique();
        if (null != infoBean) {
            edtLoginName.setText(infoBean.getLoginname());
            edtRegulatorName.setText(infoBean.getUsername());
            edtPhone.setText(infoBean.getMobile());
            edtOrg.setText(infoBean.getOrgName());
            edtEmail.setText(infoBean.getEmail());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == resust_code) {
            manager.doHttpDeal(getRoNameApi);
        }
    }
}
