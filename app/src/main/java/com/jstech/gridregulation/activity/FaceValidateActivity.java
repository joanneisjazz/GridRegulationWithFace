package com.jstech.gridregulation.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.api.FaceValidateApi;
import com.jstech.gridregulation.api.UpdateProFileApi;
import com.jstech.gridregulation.api.UploadImageApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.bean.ProfileBean;
import com.jstech.gridregulation.utils.LogUtils;
import com.jstech.gridregulation.utils.PictureSelectorUtil;
import com.jstech.gridregulation.utils.ToastUtil;
import com.jstech.gridregulation.widget.MyPopupWindow;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.SharedPreferencesHelper;

public class FaceValidateActivity extends BaseActivity implements HttpOnNextListener {

    private ImageView imageView;
    private Button btnCancel, btnUpload;
    private LinearLayout layoutBtn;

    private FaceValidateApi faceValidateApi;
    private UpdateProFileApi updateProFileApi;
    private UploadImageApi uploadImageApi;
    private HttpManager manager;


    private String status = "";
    private String userExtId = "";
    private RegulateObjectBean objectBean;
    private MyPopupWindow window;
    private TextView tvContent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_face_validate;
    }

    @Override
    public void initView() {
        setToolBarTitle("验证中");
        setToolSubBarTitle("");

        userExtId = SharedPreferencesHelper.getInstance(this).getUserExtId(this);
        final String url = getIntent().getStringExtra("url");
        status = getIntent().getStringExtra(ConstantValue.KEY_CHECK_STATUS);
        objectBean = (RegulateObjectBean) getIntent().getSerializableExtra(ConstantValue.KEY_OBJECT_BEAN);

        window = new MyPopupWindow.Builder().setContext(this)
                .setContentView(R.layout.layout_new_task_window)
                .setTitle(getResources().getString(R.string.tip))
                .setPass("重新验证")
                .setUnpass("返回").builder();

        window.setPassButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToValidate();
            }
        });
        window.setUnPassButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentBack();
            }
        });
        tvContent = window.getContentFrameLayout().findViewById(R.id.text_view);
        tvContent.setText("验证失败，是否重新验证？");

        RequestOptions options = new RequestOptions();
        options.placeholder(null); //添加占位图
        options.error(null)
                .centerCrop()//居中显示
                .diskCacheStrategy(DiskCacheStrategy.NONE)//硬盘缓存
                .skipMemoryCache(true)//是否采用内存缓存功能
        ;//显示图片的指定大小;//图片的模糊化和黑白化处理

        btnCancel = findViewById(R.id.btn_cancel);
        btnUpload = findViewById(R.id.btn_upload);
        layoutBtn = findViewById(R.id.layout_btn);
        if (null == objectBean) {
            layoutBtn.setVisibility(View.VISIBLE);
            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    uploadImageApi.setPart(PictureSelectorUtil.imagesToMultipartBody3(url));
                    manager.doHttpDeal(uploadImageApi);
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(FaceValidateActivity.this, TakeFacePicActivity.class));
                    finish();
                }
            });
        } else {
            layoutBtn.setVisibility(View.GONE);
        }

        imageView = findViewById(R.id.image);
        Glide.with(this).applyDefaultRequestOptions(options).load(url).into(imageView);

        manager = new HttpManager(this, this);
        faceValidateApi = new FaceValidateApi();
        if (null != objectBean) {
            faceValidateApi.setPart(PictureSelectorUtil.imagesToMultipartBody3(url));
            manager.doHttpDeal(faceValidateApi);
        }

        updateProFileApi = new UpdateProFileApi();
        uploadImageApi = new UploadImageApi();

    }

    @Override
    protected void onDestroy() {
        Glide.with(this).clear(imageView);
        imageView = null;
        super.onDestroy();
    }

    @Override
    public void onNext(String resulte, String method) {

        /**
         * 验证结果
         * 成功--跳转到检查的页面
         * 失败--打开对话框，提示重新验证或者取消
         */

        JSONObject object = JSON.parseObject(resulte);
        String code = object.getString("code");
        if (!"0".equals(code)) {
            ToastUtil.toast(this, "验证失败");
            window.showAtLocationCenter(getLayoutId());
            return;
        }
        JSONObject jsonObject = object.getJSONObject("data");
        if (method.equals(faceValidateApi.getMethod())) {
            //如果相似度大于80%，验证成功，否则验证失败

            int similarity = jsonObject.getJSONObject("data").getIntValue("similarity");
            if (similarity > 80) {
                // TODO: 2019/2/20 还需要删掉图片
                intentToCheck();
            } else {
                window.showAtLocationCenter(getLayoutId());
            }
        } else if (method.equals(uploadImageApi.getMethod())) {
            updateProFileApi.setProfileBean(new ProfileBean(userExtId, object.toString()));
            manager.doHttpDeal(updateProFileApi);
        } else if (method.equals(updateProFileApi.getMethod())) {
            //返回
            setResult(Activity.RESULT_OK);
            finish();
        }


    }

    @Override
    public void onError(ApiException e, String method) {
        window.showAtLocationCenter(getLayoutId());
        LogUtils.d(e.getMessage());
    }


    private void intentToCheck() {
        Intent intent = new Intent();
        /**
         * 开启新的检查
         * 继续检查
         */

        if (status.equals(SiteActivity.NEW_TASK)) {
            intent.setClass(this, CheckTableSelect2Activity.class);
        } else if (status.equals(SiteActivity.CONTINUE_TASK)) {
            intent.setClass(this, RegulateItemListActivity.class);
        }
        intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, objectBean);
        intent.putExtra(ConstantValue.KEY_OBJECT_ID, objectBean.getId());
        startActivityForResult(intent, ConstantValue.REQUEST_CODE_SITE_CHECK);
        finish();
    }

    /**
     * 重新验证
     */
    private void intentToValidate() {
        Intent intent = new Intent();
        intent.setClass(this, TakeFacePicActivity.class);
        intent.putExtra(ConstantValue.KEY_CHECK_STATUS, status);
        intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, objectBean);
        startActivityForResult(intent, ConstantValue.REQUEST_CODE_SITE_CHECK);
        finish();
    }

    /**
     * 返回
     */
    private void intentBack() {
        Intent intent = new Intent();
        intent.setClass(this, SiteActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        intentBack();
    }
}
