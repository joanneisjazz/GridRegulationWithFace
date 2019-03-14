package com.jstech.gridregulation.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.activity.IlleagelCaseListActivity;
import com.jstech.gridregulation.activity.InputCaseDetailActivity;
import com.jstech.gridregulation.adapter.GridImageAdapter;
import com.jstech.gridregulation.api.SaveIllegalCase;
import com.jstech.gridregulation.api.UploadImageApi;
import com.jstech.gridregulation.base.BaseFragment;
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
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CaseEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CityEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.UserBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class ProblemReportFragment extends BaseFragment implements HttpOnNextListener {
    public static ProblemReportFragment newInstance() {

        Bundle args = new Bundle();

        ProblemReportFragment fragment = new ProblemReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    EditText edtReporterName;
    EditText edtPhone;
    TextView tvDetails;
    TextView tvDate;
    TextView tvSite;
    TextView tvErrorPhone;
    EditText edtObject;
    RecyclerView rvPictures;
    LinearLayout layoutDetails;
    LinearLayout layoutDate;
    LinearLayout layoutSite;

    CityEntityDao cityEntityDao;
    CaseEntity mCaseEntity;
    HttpManager manager;
    SaveIllegalCase saveIllegalCase;
    UploadImageApi uploadImageApi;
    private GridImageAdapter adapter;
    private List<LocalMedia> selectList;

    String siteCode = "";
    String site = "";

    java.sql.Date hapDate = null;

    TimePickerView pvDate;
    OptionsPickerView pvSite;

    List<CityEntity> citys;//市
    List<List<CityEntity>> countys;//县
    List<List<List<CityEntity>>> villages;//乡


    @Override
    protected void LazyLoad() {

    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_problem_report;
    }


    @Override
    public void initView(View rootView) {
        findViews(rootView);
        initData();
        setListener();
    }

    private void findViews(View view) {
        edtReporterName = view.findViewById(R.id.edt_reporter_name);
        edtPhone = view.findViewById(R.id.edt_phone);
        tvDetails = view.findViewById(R.id.tv_details);
        tvDate = view.findViewById(R.id.tv_happen_date);
        tvSite = view.findViewById(R.id.tv_happen_site);
        tvErrorPhone = view.findViewById(R.id.tv_error_phone);
        edtObject = view.findViewById(R.id.edt_object);
        rvPictures = view.findViewById(R.id.recyclerview_pictures);
        layoutDetails = view.findViewById(R.id.layout_details);
        layoutDate = view.findViewById(R.id.layout_date);
        layoutSite = view.findViewById(R.id.layout_site);
        edtReporterName.setText(MyApplication.getInstance().getUserBean().getUsername());
        edtPhone.setText("18650012302");

    }

    private void initData() {
        cityEntityDao = MyApplication.getInstance().getSession().getCityEntityDao();
        mCaseEntity = new CaseEntity();
        UserBean userBean = MyApplication.getInstance().getUserBean();
        mCaseEntity.setReporter(userBean.getUserExtId());
        mCaseEntity.setReportername(userBean.getUsername());
        mCaseEntity.setCreateBy(MyApplication.getInstance().getUserBean().getUserExtId());

        manager = new HttpManager(this, (RxAppCompatActivity) getActivity());
        saveIllegalCase = new SaveIllegalCase();
        uploadImageApi = new UploadImageApi();

        selectList = new ArrayList<>();
        FullyGridLayoutManager fullyGridLayoutManager = new FullyGridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
        fullyGridLayoutManager.setScrollEnabled(false);
        rvPictures.setLayoutManager(fullyGridLayoutManager);
        adapter = new GridImageAdapter(getActivity(), onAddPicClickListener);
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
                            PictureSelector.create(getActivity()).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList);
                            break;
                    }
                }
            }
        });


        //时间选择器
        pvDate = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                hapDate = new java.sql.Date(date.getTime());
                tvDate.setText(hapDate.toString());
                Toast.makeText(getActivity(), date.toString(), Toast.LENGTH_SHORT).show();
            }
        }).build();

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
                        pvSite = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
                            @Override
                            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                                site = citys.get(options1).getSname() +
                                        countys.get(options1).get(options2).getSname() +
                                        villages.get(options1).get(options2).get(options3).getSname();
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
                Intent intent = new Intent(getActivity(), InputCaseDetailActivity.class);
                intent.putExtra(ConstantValue.KEY_CONTENT, tvDetails.getText().toString());
                startActivityForResult(intent, 101);
            }
        });

        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtils.hideKeyboard(getActivity());
                pvDate.show();
            }
        });
        layoutSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtils.hideKeyboard(getActivity());
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
            PictureFileUtils.deleteCacheDirFile(getActivity());
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
            Toast.makeText(getActivity(), "上报成功", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getActivity(), IlleagelCaseListActivity.class));
        }

    }

    @Override
    public void onError(ApiException e, String method) {
        PictureFileUtils.deleteCacheDirFile(getActivity());
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelectorUtil.initPictureSelector(getActivity(), 9, 1, 4, selectList);
        }

    };

    /**
     * 保存图片信息
     */
    private void savePics() {
        if (null == tvDate.getText() || TextUtils.isEmpty(tvDate.getText())) {
            Toast.makeText(getActivity(), "请选择时间", Toast.LENGTH_LONG).show();
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
                                    PictureSelectorUtil.PictureListCreateWatermark(getActivity(), selectList, ConstantValue.WATER_MARK + TextUtil.date());
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
            Toast.makeText(getActivity(), "请选择案件发生时间", Toast.LENGTH_LONG).show();
            return;
        }
        if (null == tvSite.getText() || "".equals(tvSite.getText())
                || "请选择地点".equals(tvDate.getText())) {
            Toast.makeText(getActivity(), "请选择案件发生地点", Toast.LENGTH_LONG).show();
            return;
        }

        if (null == selectList || selectList.size() <= 0) {
            uploadData();
        } else {
            savePics();
        }
    }
}
