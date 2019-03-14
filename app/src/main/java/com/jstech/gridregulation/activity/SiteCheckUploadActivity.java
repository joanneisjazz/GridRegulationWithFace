package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.GridImageAdapter;
import com.jstech.gridregulation.api.AddTaskApi;
import com.jstech.gridregulation.api.SaveItemResultApi;
import com.jstech.gridregulation.api.SaveResultApi;
import com.jstech.gridregulation.api.UploadFileApi;
import com.jstech.gridregulation.api.UploadImageApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.bean.SaveResultBean;
import com.jstech.gridregulation.pdf.model.PdfData;
import com.jstech.gridregulation.utils.FileUtil;
import com.jstech.gridregulation.utils.LogUtils;
import com.jstech.gridregulation.utils.PDFUtil;
import com.jstech.gridregulation.utils.PictureSelectorUtil;
import com.jstech.gridregulation.utils.SystemUtil;
import com.jstech.gridregulation.utils.TaskUploadUtil;
import com.jstech.gridregulation.utils.TextUtil;
import com.jstech.gridregulation.widget.FullyGridLayoutManager;
import com.jstech.gridregulation.widget.MyPopupWindow;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.rxretrofitlibrary.greendao.DaoSession;
import com.rxretrofitlibrary.greendao.FileEntityDao;
import com.rxretrofitlibrary.greendao.RegulateObjectBeanDao;
import com.rxretrofitlibrary.greendao.RegulateResultBeanDao;
import com.rxretrofitlibrary.greendao.TaskBeanDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.FileEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBeanDao;

/**
 * 上传检查结果
 */
public class SiteCheckUploadActivity extends BaseActivity implements View.OnClickListener, HttpOnNextListener {

    private LinearLayout layoutRegulatorSign;
    private LinearLayout layoutObjectSign;
    private LinearLayout layoutRegulatorSignImages;
    private LinearLayout layoutObjectSignImages;
    private ImageView ivRegulatorSign1;
    private ImageView ivRegulatorSign2;
    private ImageView ivRegulatorSignMore;
    private ImageView ivObjectSignImage;
    private TextView tvSubTitle;
    private TextView tvOpinion;
    private TextView tvObjectName;
    private TextView tvRegulatorName;
    private TextView tvRegulateDate;
    private RecyclerView rvPictures;
    private LinearLayout layoutOpinion;

    private boolean isFisrtRegulator = true;
    private String taskId = "";
    private String tableId = "";
    private String result = "";
    private TaskBean taskBean;
    private RegulateObjectBean object;
    private List<RegulateResultBean> itemResultBeans;

    private TaskBeanDao taskBeanDao;
    private RegulateObjectBeanDao regulateObjectBeanDao;
    private FileEntityDao fileEntityDao;
    private RegulateResultBeanDao regulateResultBeanDao;

    private HttpManager manager;
    private SaveResultApi saveResultApi;
    private UploadImageApi uploadImageApi;
    private UploadFileApi uploadFileApi;
    private AddTaskApi addTaskApi;
    private SaveItemResultApi saveItemResultApi;
    TaskUploadUtil taskUploadUtil;

    private MyPopupWindow window;
    private MyPopupWindow signWindow;
    private GridImageAdapter adapter;
    private List<LocalMedia> selectList;

    private String pdfOnline;
    private String pdfLocalPath;

    private List<FileEntity> regulateSign;
    private List<FileEntity> objSign;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_site_check_upload;
    }

    @Override
    public void initView() {
        setToolSubBarTitle("保存");

        getData();
        findViews();
        initApis();
        setListener();
        initWindow();
        selectList = new ArrayList<>();

        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        manager.setScrollEnabled(false);
        rvPictures.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(4);
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
                            PictureSelector.create(SiteCheckUploadActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList);
                            break;
                    }
                }
            }
        });

    }

    private void findViews() {
        layoutRegulatorSign = findViewById(R.id.layout_regulate_sign);
        layoutObjectSign = findViewById(R.id.layout_object_sign);
        layoutRegulatorSignImages = findViewById(R.id.layout_regulate_sign_image);
        layoutObjectSignImages = findViewById(R.id.layout_object_sign_images);
        ivRegulatorSign1 = findViewById(R.id.iv_regulator_sign_1);
        ivRegulatorSign2 = findViewById(R.id.iv_regulator_sign_2);
        ivRegulatorSignMore = findViewById(R.id.iv_regulator_sign_more);
        ivObjectSignImage = findViewById(R.id.iv_object_sign_image);
        tvSubTitle = findViewById(R.id.toolbar_subtitle);
        tvOpinion = findViewById(R.id.tv_regulate_opinion);
        tvObjectName = findViewById(R.id.tv_object_name);
        tvRegulatorName = findViewById(R.id.tv_regulator_name);
        tvRegulateDate = findViewById(R.id.tv_regulate_date);
        rvPictures = findViewById(R.id.recyclerview_pictures);
        layoutOpinion = findViewById(R.id.layout_opinion);
    }

    private TextView textView;
    private TextView tvSign;

    private void initWindow() {
        window = new MyPopupWindow.Builder().setContext(this)
                .setContentView(R.layout.layout_new_task_window)
                .setTitle(getResources().getString(R.string.tip))
                .setPass("取消")
                .setUnpass("离开")
                .setOutSideCancel(true)
                .setUnpassListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        window.dismiss();
                        Intent intent = new Intent(SiteCheckUploadActivity.this, RegulateItemListActivity.class);
                        intent.putExtra(ConstantValue.KEY_TASK_ID, taskBean.getId());
                        intent.putExtra(ConstantValue.KEY_TABLE_ID, tableId);
                        intent.putExtra(ConstantValue.KEY_OBJECT_ID, taskBean.getEntid());
                        intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, object);
                        startActivity(intent);
                        finish();
                    }
                })
                .setPassListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        window.dismiss();
                    }
                })
                .setFouse(true)
                .setwidth(SystemUtil.getWith(this) * 2 / 3)
                .setheight(ViewGroup.LayoutParams.WRAP_CONTENT).builder();
        textView = window.getContentFrameLayout().findViewById(R.id.text_view);
        textView.setText("请确认是否离开？");

        signWindow = new MyPopupWindow.Builder().setContext(this)
                .setContentView(R.layout.layout_new_task_window)
                .setTitle(getResources().getString(R.string.tip))
                .setPass("确定")
                .setUnpass("取消")
                .setPassListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        signWindow.dismiss();
                        intentToSign(clickType);
                    }
                })
                .setwidth(SystemUtil.getWith(this) * 2 / 3)
                .setheight(ViewGroup.LayoutParams.WRAP_CONTENT).builder();
        tvSign = signWindow.getContentFrameLayout().findViewById(R.id.text_view);
        tvSign.setText("请确认是否重新签名？");
    }

    private void getData() {
        DaoSession session = MyApplication.getInstance().getSession();
        taskBeanDao = session.getTaskBeanDao();
        fileEntityDao = session.getFileEntityDao();
        regulateObjectBeanDao = session.getRegulateObjectBeanDao();
        regulateResultBeanDao = session.getRegulateResultBeanDao();
        taskId = getIntent().getStringExtra(ConstantValue.KEY_TASK_ID);
        result = getIntent().getStringExtra(ConstantValue.SITE_REGULATE_RESULT);
        object = (RegulateObjectBean) getIntent().getSerializableExtra(ConstantValue.KEY_OBJECT_BEAN);
        tableId = getIntent().getStringExtra(ConstantValue.KEY_TABLE_ID);
        itemResultBeans = (List<RegulateResultBean>) getIntent().getSerializableExtra(ConstantValue.KEY_ITEMS);
        if (null == itemResultBeans || itemResultBeans.isEmpty()) {
            itemResultBeans = regulateResultBeanDao.queryBuilder().where(
                    RegulateResultBeanDao.Properties.Inspid.eq(taskId)
            ).list();
        }
        taskBean = taskBeanDao.queryBuilder().where(
                TaskBeanDao.Properties.Id.eq(taskId)
        ).unique();
    }

    private void setListener() {
        tvObjectName.setText(object.getName());
        tvRegulatorName.setText(SharedPreferencesHelper.getInstance(this).getSharedPreference("userName", "").toString());
        tvRegulateDate.setText(TextUtil.date());
        ivRegulatorSign1.setOnClickListener(this);
        ivRegulatorSign2.setOnClickListener(this);
        ivObjectSignImage.setOnClickListener(this);
        layoutRegulatorSign.setOnClickListener(this);
        layoutObjectSign.setOnClickListener(this);
        tvSubTitle.setOnClickListener(this);
        layoutOpinion.setOnClickListener(this);
        ivRegulatorSignMore.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        taskBean = null;
//        taskBean = taskBeanDao.queryBuilder().where(TaskBeanDao.Properties.Id.eq(taskId)).unique();
        layoutOpinion.setClickable(true);
        if (null == data) {
            return;
        }
        String localPath = data.getStringExtra(ConstantValue.KEY_LOCAL);
        String onlinePath = data.getStringExtra(ConstantValue.KEY_ONLINE);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ConstantValue.REQUEST_CODE_REGULATOR_1://第一个检查人签字
                    if (TextUtil.isEmpty(localPath)) {
                        //如果没有图片
                        ivRegulatorSign1.setVisibility(View.GONE);

                    }
                    isFisrtRegulator = false;
                    layoutRegulatorSignImages.setVisibility(View.VISIBLE);
                    if (TextUtil.isEmpty(localPath)) {//如果兩個路徑有一个为爲空，表示签名图片不存在
//                        layoutRegulatorSignImages.setVisibility(View.GONE);
                        ivRegulatorSign1.setVisibility(View.GONE);
                        isFisrtRegulator = true;
                        layoutRegulatorSign.setClickable(true);
                        if (TextUtil.isEmpty(taskBean.getRegulator2SignPath())) {
                            layoutRegulatorSignImages.setVisibility(View.GONE);
                        } else {
                            layoutRegulatorSignImages.setVisibility(View.VISIBLE);
                            ivRegulatorSignMore.setVisibility(View.VISIBLE);
                        }
//                        layoutRegulatorSignImages.setVisibility(View.GONE);
                    } else {
                        taskBean.setRegulator1SignPathLocal(localPath);
                        taskBean.setRegulator1SignPath(onlinePath);
                        ivRegulatorSign1.setVisibility(View.VISIBLE);
//                        ivRegulatorSignMore.setVisibility(View.VISIBLE);
                        Glide.with(SiteCheckUploadActivity.this).load(localPath).into(ivRegulatorSign1);
                        if (ivRegulatorSign2.getVisibility() == View.VISIBLE) {
                            ivRegulatorSignMore.setVisibility(View.GONE);
                        } else {
                            ivRegulatorSignMore.setVisibility(View.VISIBLE);
                        }
                    }
                    taskBeanDao.update(taskBean);
                    break;
                case ConstantValue.REQUEST_CODE_REGULATOR_2://第二个检查人签字
                    if (TextUtil.isEmpty(localPath)) {
                        layoutRegulatorSign.setClickable(true);
                        ivRegulatorSign2.setVisibility(View.GONE);
                        ivRegulatorSignMore.setVisibility(View.VISIBLE);
                    } else {
                        ivRegulatorSignMore.setVisibility(View.GONE);
                        taskBean.setRegulator2SignPath(onlinePath);
                        taskBean.setRegulator2SignPathLocal(localPath);
                        ivRegulatorSign2.setVisibility(View.VISIBLE);
                        layoutRegulatorSign.setClickable(false);
                        Glide.with(SiteCheckUploadActivity.this).load(localPath).into(ivRegulatorSign2);
                    }
                    taskBeanDao.update(taskBean);
                    break;
                case ConstantValue.REQUEST_CODE_OBJECT:
                    layoutObjectSignImages.setVisibility(View.VISIBLE);
                    if (TextUtil.isEmpty(localPath)) {
                        layoutObjectSignImages.setVisibility(View.GONE);
                        layoutObjectSign.setClickable(true);
                    } else {
                        taskBean.setEntsign(onlinePath);
                        taskBean.setObjectSignPath(localPath);
                        layoutObjectSign.setClickable(false);
                        Glide.with(SiteCheckUploadActivity.this).load(localPath).into(ivObjectSignImage);
                    }
                    taskBeanDao.update(taskBean);
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    Observable.just(selectList).observeOn(Schedulers.newThread())
                            .flatMap(new Func1<List<LocalMedia>, Observable<?>>() {
                                @Override
                                public Observable<?> call(List<LocalMedia> localMedia) {
                                    PictureSelectorUtil.PictureListCreateWatermark(SiteCheckUploadActivity.this, selectList, ConstantValue.WATER_MARK + TextUtil.date());
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
                case 1001:
                    if (TextUtil.isEmpty(data.getStringExtra(ConstantValue.RESULT))) {
                        tvOpinion.setText("无");
                    } else {
                        tvOpinion.setText(data.getStringExtra(ConstantValue.RESULT));
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int clickType = -1;

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(SiteCheckUploadActivity.this, SignatureActivity.class);
        intent.putExtra(ConstantValue.KEY_TASK_ID, taskId);
        switch (v.getId()) {
            case R.id.layout_regulate_sign:
                clickType = 1;
                if (isFisrtRegulator) {
                    intent.putExtra(ConstantValue.CODE, ConstantValue.REQUEST_CODE_REGULATOR_1);
                    startActivityForResult(intent, ConstantValue.REQUEST_CODE_REGULATOR_1);
                } else {
                    intent.putExtra(ConstantValue.CODE, ConstantValue.REQUEST_CODE_REGULATOR_2);
                    startActivityForResult(intent, ConstantValue.REQUEST_CODE_REGULATOR_2);
                }
                break;
            case R.id.layout_object_sign:
                clickType = 2;
                intent.putExtra(ConstantValue.KEY_LOCAL, taskBean.getObjectSignPath());
                intent.putExtra(ConstantValue.KEY_ONLINE, taskBean.getEntsign());
                intent.putExtra(ConstantValue.CODE, ConstantValue.REQUEST_CODE_OBJECT);
                startActivityForResult(intent, ConstantValue.REQUEST_CODE_OBJECT);
                break;
            case R.id.iv_regulator_sign_1:
                clickType = 3;
                intent.putExtra(ConstantValue.KEY_LOCAL, taskBean.getRegulator1SignPathLocal());
                intent.putExtra(ConstantValue.KEY_ONLINE, taskBean.getRegulator1SignPath());
                intent.putExtra(ConstantValue.CODE, ConstantValue.REQUEST_CODE_REGULATOR_1);
                startActivityForResult(intent, ConstantValue.REQUEST_CODE_REGULATOR_1);
                break;
            case R.id.iv_regulator_sign_2:
                clickType = 4;
                intent.putExtra(ConstantValue.KEY_LOCAL, taskBean.getRegulator2SignPathLocal());
                intent.putExtra(ConstantValue.KEY_ONLINE, taskBean.getRegulator2SignPath());
                intent.putExtra(ConstantValue.CODE, ConstantValue.REQUEST_CODE_REGULATOR_2);
                startActivityForResult(intent, ConstantValue.REQUEST_CODE_REGULATOR_2);
                break;
            case R.id.iv_regulator_sign_more:
                intent.putExtra(ConstantValue.CODE, ConstantValue.REQUEST_CODE_REGULATOR_2);
                startActivityForResult(intent, ConstantValue.REQUEST_CODE_REGULATOR_2);
                break;
            case R.id.iv_object_sign_image:
                clickType = 5;
                intent.putExtra(ConstantValue.KEY_LOCAL, taskBean.getObjectSignPath());
                intent.putExtra(ConstantValue.KEY_ONLINE, taskBean.getEntsign());
//                intent.putExtra(ConstantValue.SGIN_PATH, taskBean.getObjectSignPath());
                startActivityForResult(intent, ConstantValue.REQUEST_CODE_OBJECT);
                break;
            case R.id.layout_opinion:
                layoutOpinion.setClickable(false);
                Intent intent1 = new Intent(SiteCheckUploadActivity.this, InputCaseDetailActivity.class);
                intent1.putExtra(ConstantValue.KEY_CONTENT, tvOpinion.getText().toString());
                startActivityForResult(intent1, 1001);
                break;
            case R.id.toolbar_subtitle:
                if (TextUtil.isEmpty(taskBean.getRegulator1SignPathLocal())) {
                    if (TextUtil.isEmpty(taskBean.getRegulator2SignPath())) {
                        Toast.makeText(this, "检查人员还未签名，请签名", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                if (TextUtil.isEmpty(taskBean.getObjectSignPath())) {
                    Toast.makeText(this, "单位负责人还未签名，请签名", Toast.LENGTH_LONG).show();
                    return;
                }
                /**
                 * 先判断检查任务和检查项是否上传了
                 */
                showPD();

                //保存检查结果
                taskBean.setInspstatus("2");
                taskBean.setState5("1");//表示这个检查已经做完了
                taskBean.setInspresult(result);
                taskBean.setInspopinion(tvOpinion.getText().toString());
                taskBean.setCreateDateLocal(tvRegulateDate.getText().toString());
                taskBeanDao.update(taskBean);

//                String onlinePath = "";
//                if (!TextUtil.isEmpty(taskBean.getRegulator1SignPath())) {
//                    onlinePath = taskBean.getRegulator1SignPath();
//                }
//                if (!TextUtil.isEmpty(taskBean.getRegulator2SignPath())) {
//                    if (!TextUtil.isEmpty(onlinePath)) {
//                        onlinePath = onlinePath + "," + taskBean.getRegulator2SignPath();
//                    } else {
//                        onlinePath = taskBean.getRegulator2SignPath();
//                    }
//                }
//                taskBean.setSupersign(onlinePath);
//                taskBeanDao.update(taskBean);
                object.setInspstatus(ConstantValue.OBJ_CHECK_STATUS_FINISH);
                regulateObjectBeanDao.update(object);
                //先判断一下pdf有没有生成过
                //如果已经生成，就直接上传检查信息
                if (!TextUtil.isEmpty(pdfLocalPath)) {
                    taskUploadUtil.uploadTask(taskBean);
//                    uploadData();
                    return;
                }

                //生成pdf文件
                Toast.makeText(SiteCheckUploadActivity.this, "正在生成现场检查报告，请稍等", Toast.LENGTH_SHORT).show();
                Observable.just(true).observeOn(Schedulers.newThread())
                        .map(new Func1<Boolean, Boolean>() {
                            @Override
                            public Boolean call(Boolean aBoolean) {
                                pdfLocalPath = initPdf();
                                return aBoolean;
                            }
                        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dissmisPD();
                        pdfLocalPath = "";
                        Toast.makeText(SiteCheckUploadActivity.this, "生成现场检查报告失败，请点击重新生成", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Boolean s) {
                        FileEntity entity = new FileEntity();
                        entity.setType("pdf");
                        entity.setTaskId(taskBean.getId());
                        entity.setLocalPath(pdfLocalPath);
                        fileEntityDao.save(entity);
//                        taskUploadUtil.uploadTask(taskBean);
                        uploadTask(taskBean);
                    }
                });

                break;
        }
    }


    private void intentToRegulateList() {
        int count = 0;
        if (TextUtil.isEmpty(object.getInspcount()) && !"0".equals(object.getInspcount())) {
            count = Integer.valueOf(object.getInspcount());
        }
        count++;
        object.setInspcount(String.valueOf(count));

        Intent intent2 = new Intent(SiteCheckUploadActivity.this, RegulateListAvtivity.class);
        intent2.putExtra(ConstantValue.CODE, "ent");
        intent2.putExtra(ConstantValue.KEY_CONTENT, ConstantValue.OBJ_CHECK_STATUS_FINISH);
        intent2.putExtra(ConstantValue.KEY_OBJECT_ID, taskBean.getEntid());
        startActivity(intent2);
        dissmisPD();
        finish();
    }

    private SaveResultBean saveResultBean = new SaveResultBean();

    private void save() {
        taskBean.setInspdoc(pdfOnline);
//        taskBean.setInspstatus(ConstantValue.OBJ_CHECK_STATUS_FINISH);
        taskBean.setInspopinion(tvOpinion.getText().toString());
        taskBean.setInspresult(result);
        taskBean.setInsploc(String.format("%s,%s", MyApplication.instance.getLongtitude(), MyApplication.instance.getLatitude()));
        taskBean.setOisuper(SharedPreferencesHelper.getInstance(this).getSharedPreference("extId", "").toString());
        String regulatorSign = "";
        if (TextUtil.isEmpty(taskBean.getRegulator1SignPath())) {
            regulatorSign = taskBean.getRegulator2SignPath();
        } else {
            regulatorSign = taskBean.getRegulator1SignPath();
            if (!TextUtil.isEmpty(taskBean.getRegulator2SignPath())) {
                regulatorSign = regulatorSign + "," + taskBean.getRegulator2SignPath();
            }
        }
        taskBean.setSupersign(regulatorSign);
        saveResultBean.setInsp(taskBean);
        saveResultApi.setBean(saveResultBean);
        manager.doHttpDeal(saveResultApi);
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelectorUtil.initPictureSelector(SiteCheckUploadActivity.this, 4, 1, 4, selectList);
        }
    };

    private void uploadsPics() {
        if (null == selectList || selectList.size() == 0) {
            save();
        } else {
            uploadImageApi.setParts(PictureSelectorUtil.imagesToMultipartBody(selectList));
            manager.doHttpDeal(uploadImageApi);
        }
    }

    @Override
    public void onBackPressed() {
        window.showAtLocation(getLayoutId(), Gravity.CENTER, 0, 0);
    }

    private String initPdf() {
        String name = SharedPreferencesHelper.getInstance(this).getSharedPreference("userName", "").toString();
        PdfData pdfData = PDFUtil.initPdfData(name, object, taskBean, itemResultBeans, selectList, tvOpinion.getText().toString());
        return PDFUtil.createPdfByPdfData(pdfData);
    }

    private void intentToSign(int type) {
        Intent intent = new Intent(SiteCheckUploadActivity.this, SignatureActivity.class);
        intent.putExtra(ConstantValue.KEY_TASK_ID, taskId);
        switch (type) {
            //第一个检查人签字
            case 3:
                intent.putExtra(ConstantValue.KEY_LOCAL, taskBean.getRegulator1SignPathLocal());
                intent.putExtra(ConstantValue.KEY_ONLINE, taskBean.getRegulator1SignPath());
                intent.putExtra(ConstantValue.CODE, ConstantValue.REQUEST_CODE_REGULATOR_1);
                startActivityForResult(intent, ConstantValue.REQUEST_CODE_REGULATOR_1);
                break;
            //第二个检查人签字
            case 4:
                intent.putExtra(ConstantValue.KEY_LOCAL, taskBean.getRegulator2SignPathLocal());
                intent.putExtra(ConstantValue.KEY_ONLINE, taskBean.getRegulator2SignPath());
                intent.putExtra(ConstantValue.CODE, ConstantValue.REQUEST_CODE_REGULATOR_2);
                startActivityForResult(intent, ConstantValue.REQUEST_CODE_REGULATOR_2);
                break;
            //检查对象签字
            case 5:
                intent.putExtra(ConstantValue.KEY_LOCAL, taskBean.getObjectSignPath());
                intent.putExtra(ConstantValue.KEY_ONLINE, taskBean.getEntsign());
                startActivityForResult(intent, ConstantValue.REQUEST_CODE_OBJECT);
                break;

        }
    }

    private void uploadTask(TaskBean bean) {
        if (null == bean) {
            return;
        }
        if ("0".equals(bean.getState1())) {
            //如果需要上传检查任务，调用上传检查任务的接口
            addTaskApi.setParams(bean);
            manager.doHttpDeal(addTaskApi);
            return;
        }
        //如果不需要上传检查任务，判断是否需要上传检查项
        uploadItemPics(bean);
//        uploadItems(bean);
    }

    private void uploadItemPics(TaskBean taskBean) {
        if ("1".equals(taskBean.getState3())) {
            uploadPDF(taskBean);
            return;
        }

        fileEntities = fileEntityDao.queryBuilder().where(
                FileEntityDao.Properties.TaskId.eq(taskBean.getId()),
                FileEntityDao.Properties.Type.eq("item_img")
        ).list();

        if (null != fileEntities && fileEntities.size() > 0) {
            uploadImageApi = new UploadImageApi();
            uploadImageApi.setType("3");
            uploadImageApi.setParts(PictureSelectorUtil.imagesToMultipartBody2(fileEntities));
            manager.doHttpDeal(uploadImageApi);
            return;
        }
        uploadItems(taskBean);
    }

    /**
     * 上传检查项的结果
     *
     * @param
     */
    private void uploadItems(TaskBean bean) {
        //先判断检查项的结果是否已经上传过了，没有上传才需要调用上传检查项的接口
        if ("0".equals(bean.getState3()) || "0".equals(bean.getState2())) {
            List<RegulateResultBean> list = regulateResultBeanDao.queryBuilder().where(
                    RegulateResultBeanDao.Properties.Inspid.eq(bean.getId())
            ).list();
            saveItemResultApi.setParams(list);
            manager.doHttpDeal(saveItemResultApi);
            return;
        }
        uploadPDF(bean);
    }

    /**
     * 上传pdf文档
     *
     * @param bean
     */
    private void uploadPDF(TaskBean bean) {
        if ("0".equals(bean.getState4())) {
            FileEntity pdf = fileEntityDao.queryBuilder().where(
                    FileEntityDao.Properties.TaskId.eq(bean.getId()),
                    FileEntityDao.Properties.Type.eq("pdf")
            ).unique();
            if (null != pdf) {
                uploadFileApi.setPart(FileUtil.uploadFile(pdf.getLocalPath()));
                manager.doHttpDeal(uploadFileApi);
            }
            return;
        }
        uploadSuperSign(bean);
    }

    /**
     * 上传检查人签字
     */


    private void uploadSuperSign(TaskBean taskBean) {
        regulateSign = fileEntityDao.queryBuilder().where(
                FileEntityDao.Properties.TaskId.eq(taskBean.getId()),
                FileEntityDao.Properties.Type.eq("reg_sign")
        ).list();
        if (null != regulateSign && regulateSign.size() > 0) {
            uploadImageApi.setParts(PictureSelectorUtil.imagesToMultipartBody4(regulateSign));
            uploadImageApi.setType("1");
            manager.doHttpDeal(uploadImageApi);
            return;
        }
        String superSign = "";
        if (!TextUtil.isEmpty(taskBean.getRegulator1SignPath())) {
            superSign = taskBean.getRegulator1SignPath();
        }
        if (!TextUtil.isEmpty(taskBean.getRegulator2SignPath())) {
            if (TextUtil.isEmpty(superSign)) {
                superSign = taskBean.getRegulator2SignPath();
            } else {
                superSign = superSign + "," + taskBean.getRegulator2SignPath();
            }
        }
        taskBean.setSupersign(superSign);
        uploadEntSign(taskBean);
    }

    /**
     * 上传检查对象签字
     */
    private void uploadEntSign(TaskBean taskBean) {
        objSign = fileEntityDao.queryBuilder().where(
                FileEntityDao.Properties.TaskId.eq(taskBean.getId()),
                FileEntityDao.Properties.Type.eq("obj_sign")
        ).list();
        if (null != objSign && objSign.size() > 0) {
            uploadImageApi.setParts(PictureSelectorUtil.imagesToMultipartBody4(objSign));
            uploadImageApi.setType("2");
            manager.doHttpDeal(uploadImageApi);
            return;
        }
        uploadAll(taskBean);
    }

    /**
     * 上传所有的信息
     *
     * @param
     * @param taskBean
     */
    private void uploadAll(TaskBean taskBean) {
        SaveResultBean saveResultBean = new SaveResultBean();
        saveResultBean.setImgs(taskBean.getImage());
        saveResultBean.setInsp(taskBean);
        saveResultApi.setBean(saveResultBean);
        manager.doHttpDeal(saveResultApi);
    }

    private List<FileEntity> fileEntities;


    private void initApis() {
        manager = new HttpManager(this, this);
        saveResultApi = new SaveResultApi();
        uploadImageApi = new UploadImageApi();
        uploadFileApi = new UploadFileApi();
        addTaskApi = new AddTaskApi();
        saveItemResultApi = new SaveItemResultApi();
        taskUploadUtil = new TaskUploadUtil(manager);
        taskUploadUtil.setAddTaskApi(addTaskApi);
        taskUploadUtil.setSaveItemResultApi(saveItemResultApi);
        taskUploadUtil.setUploadImageApi(uploadImageApi);
        taskUploadUtil.setUploadFileApi(uploadFileApi);
        taskUploadUtil.setSaveResultApi(saveResultApi);
    }

    @Override
    protected void onStop() {
        window.dismiss();
        signWindow.dismiss();
        super.onStop();
    }


    @Override
    public void onNext(String resulte, String method) {
        JSONObject o = JSON.parseObject(resulte);
        if (!ConstantValue.isSuccess(o)) {
            return;
        }
        if (method.equals(addTaskApi.getMethod())) {
            //上传检查任务，更新检查任务的信息
            taskBean.setState1("1");
            taskBeanDao.update(taskBean);
            uploadTask(taskBean);
//                taskUploadUtil.uploadTask(taskBean);
//            uploadData();
        } else if (method.equals(saveItemResultApi.getMethod())) {
            //上传检查项，更新检查任务里的检查项上传的状态
            taskBean.setState2("1");
            taskBean.setState3("1");
            taskBeanDao.update(taskBean);
            uploadTask(taskBean);
//                taskUploadUtil.uploadTask(taskBean);
//            uploadData();
        } else if (method.equals(uploadFileApi.getMethod())) {
            pdfOnline = o.getObject(ConstantValue.DATA, String.class);
            taskBean.setInspdoc(pdfOnline);
            taskBean.setState4("1");
            taskBeanDao.update(taskBean);
            fileEntityDao.delete(fileEntityDao.queryBuilder().where(
                    FileEntityDao.Properties.TaskId.eq(taskBean.getId()),
                    FileEntityDao.Properties.Type.eq("pdf")
            ).unique());
            uploadTask(taskBean);
//                taskUploadUtil.uploadTask(taskBean);
//            uploadData();
        } else if (method.equals(uploadImageApi.getMethod())) {
            String type = uploadImageApi.getType();
            List<String> arrays = o.getJSONArray(ConstantValue.DATA).toJavaList(String.class);
            if ("1".equals(type)) {
                //上传检查人签字，更新检查任务里的检查人签字上传的状态
                String imgs = "";
                for (String str : arrays) {
                    imgs = imgs + "," + str;
                }
                if (!TextUtil.isEmpty(imgs))
                    imgs = imgs.substring(1);
                taskBean.setSupersign(imgs);
                taskBeanDao.update(taskBean);
                fileEntityDao.deleteInTx(regulateSign);

            } else if ("2".equals(type)) {
                //上传检查对象签字，更新检查任务里的检查对象签字上传的状态
                if (null != arrays && arrays.size() > 0) {
                    fileEntityDao.deleteInTx(objSign);
                    taskBean.setEntsign(arrays.get(0));
                    taskBeanDao.update(taskBean);
                    fileEntityDao.deleteInTx(objSign);
                }
            } else {
                //上传检查图片，更新检查任务里的检查图片上传的状态
                //获取到图片的地址以后，要更新检查项的表
                for (int i = 0; i < fileEntities.size(); i++) {
                    FileEntity fileEntity = fileEntities.get(i);
                    RegulateResultBean regulateResultBean = regulateResultBeanDao.loadByRowId(fileEntity.beanId);
                    String path = regulateResultBean.getImage();
                    if (TextUtil.isEmpty(path)) {
                        path = arrays.get(i);
                    } else {
                        path = path + "," + arrays.get(i);
                    }
                    regulateResultBean.setImage(path);
                    regulateResultBeanDao.update(regulateResultBean);
                    fileEntityDao.delete(fileEntity);
                }
                taskBean.setState2_1("1");
                taskBeanDao.update(taskBean);
            }
            uploadTask(taskBean);
//                taskUploadUtil.uploadTask(taskBean);
//            uploadData();
        } else if (method.equals(saveResultApi.getMethod())) {
            taskBean.setState6("1");
            taskBean.setStatus(ConstantValue.OBJ_CHECK_STATUS_FINISH);
            taskBeanDao.update(taskBean);
            Toast.makeText(SiteCheckUploadActivity.this, "保存成功", Toast.LENGTH_LONG).show();
            intentToRegulateList();
        }
    }

    @Override
    public void onError(ApiException e, String method) {
        taskBean.setState6("0");
        taskBeanDao.update(taskBean);
        intentToRegulateList();
    }
}
