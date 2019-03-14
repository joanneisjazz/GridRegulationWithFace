package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.GridImageAdapter;
import com.jstech.gridregulation.api.GetCityApi;
import com.jstech.gridregulation.api.SaveIllegalCase;
import com.jstech.gridregulation.api.UploadImageApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.utils.KeyboardUtils;
import com.jstech.gridregulation.utils.LogUtils;
import com.jstech.gridregulation.utils.PictureSelectorUtil;
import com.jstech.gridregulation.utils.TextUtil;
import com.jstech.gridregulation.widget.FullyGridLayoutManager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.rxretrofitlibrary.greendao.CityEntityDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CaseEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CityEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hesm on 2018/11/8.
 * 违法违规案件上报
 */

public class CaseUploadActivity extends BaseActivity implements HttpOnNextListener {


    private EditText edtReporterName;
    private EditText edtPhone;
    private TextView tvDetails;
    private TextView tvDate;
    private TextView tvSite;
    private TextView tvErrorPhone;
    private TextView tvSave;
    private EditText edtObject;
    private RecyclerView rvPictures;
    private LinearLayout layoutDetails;
    private LinearLayout layoutDate;
    private LinearLayout layoutSite;

    private CityEntityDao cityEntityDao;
    private CaseEntity mCaseEntity;
    private HttpManager manager;
    private SaveIllegalCase saveIllegalCase;
    private UploadImageApi uploadImageApi;
    private GridImageAdapter adapter;
    private List<LocalMedia> selectList;

    private String siteCode = "";
    private String site = "";
    private String userName = "";
    private String userExtId = "";
    private String mobile = "";

    java.sql.Date hapDate = null;

    private TimePickerView pvDate;
    private OptionsPickerView pvSite;

    private List<CityEntity> citys;//市
    private List<List<CityEntity>> countys;//县
    private List<List<List<CityEntity>>> villages;//乡
    private GetCityApi getCityApi;
    private long start;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_problem_report;
    }

    @Override
    public void initView() {
        setToolBarTitle("案件上报");
        setToolSubBarTitle("保存");
        findViews();
        initData();
        setListener();
    }

    private void findViews() {
        tvSave = getSubTitle();
        edtReporterName = findViewById(R.id.edt_reporter_name);
        edtPhone = findViewById(R.id.edt_phone);
        tvDetails = findViewById(R.id.tv_details);
        tvDate = findViewById(R.id.tv_happen_date);
        tvSite = findViewById(R.id.tv_happen_site);
        tvErrorPhone = findViewById(R.id.tv_error_phone);
        edtObject = findViewById(R.id.edt_object);
        rvPictures = findViewById(R.id.recyclerview_pictures);
        layoutDetails = findViewById(R.id.layout_details);
        layoutDate = findViewById(R.id.layout_date);
        layoutSite = findViewById(R.id.layout_site);
    }

    private void initData() {
        cityEntityDao = MyApplication.getInstance().getSession().getCityEntityDao();
        mCaseEntity = new CaseEntity();
//        UserBean userBean = MyApplication.getInstance().getUserBean();
        userName = SharedPreferencesHelper.getInstance(this).getSharedPreference("userName", "").toString();
        userExtId = SharedPreferencesHelper.getInstance(this).getSharedPreference("extId", "").toString();
        mobile = SharedPreferencesHelper.getInstance(this).getSharedPreference("phone", "").toString();

        edtReporterName.setText(userName);
        edtPhone.setText(mobile);
        mCaseEntity.setReporter(userExtId);
        mCaseEntity.setReportername(userName);
        mCaseEntity.setCreateBy(userExtId);

        manager = new HttpManager(this, this);
        saveIllegalCase = new SaveIllegalCase();
        uploadImageApi = new UploadImageApi();
        getCityApi = new GetCityApi();
        selectList = new ArrayList<>();
        FullyGridLayoutManager fullyGridLayoutManager = new FullyGridLayoutManager(CaseUploadActivity.this, 4, GridLayoutManager.VERTICAL, false);
        fullyGridLayoutManager.setScrollEnabled(false);
        rvPictures.setLayoutManager(fullyGridLayoutManager);
        adapter = new GridImageAdapter(CaseUploadActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(9);
        rvPictures.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(CaseUploadActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList);
                            break;
                    }
                }
            }
        });


        //时间选择器
        pvDate = new TimePickerBuilder(CaseUploadActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (System.currentTimeMillis() < date.getTime()) {
                    Toast.makeText(CaseUploadActivity.this, "案件发生日期不能再当前日期之后", Toast.LENGTH_SHORT).show();
                    return;
                }
                hapDate = new java.sql.Date(date.getTime());
                tvDate.setText(hapDate.toString());
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                long now = System.currentTimeMillis();
                if (now < date.getTime()) {
                    return;
                }
            }
        }).build();


        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        List<CityEntity> list = cityEntityDao.loadAll();
        if (null == list || list.isEmpty()) {
            manager.doHttpDeal(getCityApi);
        } else {
            initCitys();
        }
    }

    private void initCitys() {
        //初始化地址数据和地址选择器
        Observable.just(true).observeOn(Schedulers.newThread())
                .map(new Func1<Boolean, Object>() {
                    @Override
                    public Object call(Boolean aBoolean) {
                        //初始化地址数据
                        countys = new ArrayList<>();
                        villages = new ArrayList<>();

                        //获取山西省下面所有的市级数据
                        citys = cityEntityDao.queryBuilder().where(CityEntityDao.Properties.Pcode.eq("140000000000")).list();
                        for (CityEntity cityEntity : citys) {
                            List<List<CityEntity>> v = new ArrayList<>();//该省的所有地区列表（第三极）
                            //获取该市下所有县的数据
                            List<CityEntity> cityEntityList = cityEntityDao.queryBuilder()
                                    .where(CityEntityDao.Properties.Pcode.eq(cityEntity.getCode()))
                                    .list();
                            for (CityEntity entity : cityEntityList) {
                                //获取该县下所有乡镇的数据
                                List<CityEntity> cityEntityList1 = cityEntityDao.queryBuilder()
                                        .where(CityEntityDao.Properties.Pcode.eq(entity.getCode()))
                                        .list();
                                v.add(cityEntityList1);
                            }
                            countys.add(cityEntityList);
                            villages.add(v);
                        }

                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        pvSite = new OptionsPickerBuilder(CaseUploadActivity.this, new OnOptionsSelectListener() {
                            @Override
                            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                                site = citys.get(options1).getName() +
                                        countys.get(options1).get(options2).getName() +
                                        villages.get(options1).get(options2).get(options3).getName();
                                siteCode = villages.get(options1).get(options2).get(options3).getCode();
                                tvSite.setText(site);
                            }
                        }).build();
                        pvSite.setPicker(citys, countys, villages);
                    }
                });
    }

    public void setListener() {
        layoutDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideKeyboard(CaseUploadActivity.this);
                Intent intent = new Intent(CaseUploadActivity.this, InputCaseDetailActivity.class);
                intent.putExtra(ConstantValue.KEY_CONTENT, tvDetails.getText().toString());
                startActivityForResult(intent, 101);
            }
        });

        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtils.hideKeyboard(CaseUploadActivity.this);
                pvDate.show();
            }
        });
        layoutSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtils.hideKeyboard(CaseUploadActivity.this);
                pvSite.show();
            }
        });

        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                /**
                 * 判断是否是电话号码
                 */
//                if (TextUtil.isMobileNO(s.toString())) {
//                    tvErrorPhone.setVisibility(View.GONE);
//                } else {
//                    tvErrorPhone.setVisibility(View.VISIBLE);
//                }
            }
        });

    }

    @Override
    public void onNext(String resulte, String method) {
        JSONObject o = JSON.parseObject(resulte);
        String code = o.getString(ConstantValue.CODE);
        if (!ConstantValue.CODE_SUCCESS.equals(code)) {
            return;
        }

        if (method.equals(uploadImageApi.getMethod())) {
            PictureFileUtils.deleteCacheDirFile(CaseUploadActivity.this);
            String pics = "";
            for (String s : o.getJSONArray("data").toJavaList(String.class)) {
                pics = pics + "," + s;
            }
            pics = pics.subSequence(1, pics.length()).toString();
            mCaseEntity.setFileaddr(pics);
            mCaseEntity.setContent(tvDetails.getText().toString());
            uploadData();

        } else if (method.equals(saveIllegalCase.getMethod())) {

            clearData();
            Toast.makeText(CaseUploadActivity.this, "上报成功", Toast.LENGTH_LONG).show();
            startActivity(new Intent(CaseUploadActivity.this, IlleagelCaseListActivity.class));
            finish();
        } else if (method.equals(getCityApi.getMethod())) {

            cityEntityDao.deleteAll();
            cityEntityDao.saveInTx(JSON.parseObject(resulte).getJSONArray(ConstantValue.RESULT).toJavaList(CityEntity.class));
            initCitys();

        }

    }

    @Override
    public void onError(ApiException e, String method) {
        PictureFileUtils.deleteCacheDirFile(CaseUploadActivity.this);
        Toast.makeText(CaseUploadActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelectorUtil.initPictureSelector(CaseUploadActivity.this, 9, 1, 4, selectList);
        }

    };

    /**
     * 保存图片信息
     */
    private void savePics() {
        if (null == tvDate.getText() || TextUtils.isEmpty(tvDate.getText())) {
            Toast.makeText(CaseUploadActivity.this, "请选择时间", Toast.LENGTH_LONG).show();
            return;
        }
        uploadImageApi.setParts(PictureSelectorUtil.imagesToMultipartBody(selectList));
        manager.doHttpDeal(uploadImageApi);
    }

    /**
     * 上传违法违规案件的信息
     */
    public void uploadData() {
        mCaseEntity.setContacts(edtReporterName.getText().toString());
        mCaseEntity.setContent(tvDetails.getText().toString());
        mCaseEntity.setReportertel(edtPhone.getText().toString());
        mCaseEntity.setHapDate(tvDate.getText().toString());
        mCaseEntity.setHaplocaddr(tvSite.getText().toString());
        mCaseEntity.setHaploccode(siteCode);
        mCaseEntity.setHaplocaddr(site);
        mCaseEntity.setHaploccode(siteCode);
        mCaseEntity.setSubject(edtObject.getText().toString());
        mCaseEntity.setStatus("0");
        saveIllegalCase.setmCaseEntity(mCaseEntity);
        manager.doHttpDeal(saveIllegalCase);
    }

    /**
     * 上传成功之后清除数据
     */
    private void clearData() {
        edtPhone.setText("");
        edtReporterName.setText("");
        edtObject.setText("");
        tvDetails.setText("");
        tvSite.setText("");
        tvDate.setText("");
        selectList.clear();
        adapter.notifyDataSetChanged();
        mCaseEntity = null;
        mCaseEntity = new CaseEntity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    Observable.just(selectList).observeOn(Schedulers.newThread())
                            .flatMap(new Func1<List<LocalMedia>, Observable<?>>() {
                                @Override
                                public Observable<?> call(List<LocalMedia> localMedia) {
                                    PictureSelectorUtil.PictureListCreateWatermark(CaseUploadActivity.this, selectList, ConstantValue.WATER_MARK + TextUtil.date());
                                    return null;
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Object>() {
                                @Override
                                public void onError(Throwable e) {
                                    LogUtils.d(e.getMessage());
                                }

                                @Override
                                public void onCompleted() {
                                    LogUtils.d("水印添加完成");
                                    adapter.setList(selectList);
                                    adapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onNext(Object o) {

                                }
                            });
                    break;
                case 101:
                    if (null == data.getStringExtra(ConstantValue.RESULT) || data.getStringExtra(ConstantValue.RESULT).isEmpty()) {
                        tvDetails.setText("无");
                    } else {
                        tvDetails.setText(data.getStringExtra(ConstantValue.RESULT));
                    }
                    break;
            }
        }
    }

    public void save() {
        if (null == tvDate.getText() || "".equals(tvDate.getText())
                || "请选择时间".equals(tvDate.getText())) {
            Toast.makeText(CaseUploadActivity.this, "请选择案件发生时间", Toast.LENGTH_LONG).show();
            return;
        }
        if (null == tvSite.getText() || "".equals(tvSite.getText())
                || "请选择地点".equals(tvDate.getText())) {
            Toast.makeText(CaseUploadActivity.this, "请选择案件发生地点", Toast.LENGTH_LONG).show();
            return;
        }

        if (null == selectList || selectList.size() <= 0) {
            uploadData();
        } else {
            savePics();
        }
    }
}
