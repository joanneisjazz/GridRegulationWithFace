package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.GridImageAdapter;
import com.jstech.gridregulation.api.AddTaskApi;
import com.jstech.gridregulation.api.SaveItemResultApi;
import com.jstech.gridregulation.api.UploadImageApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.utils.LogUtils;
import com.jstech.gridregulation.utils.PictureSelectorUtil;
import com.jstech.gridregulation.utils.SystemUtil;
import com.jstech.gridregulation.utils.TextUtil;
import com.jstech.gridregulation.utils.ToastUtil;
import com.jstech.gridregulation.widget.FullyGridLayoutManager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.rxretrofitlibrary.greendao.FileEntityDao;
import com.rxretrofitlibrary.greendao.RegulateResultBeanDao;
import com.rxretrofitlibrary.greendao.TaskBeanDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.FileEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RegulateItemCheckActivity extends BaseActivity implements View.OnClickListener, HttpOnNextListener {

    private LinearLayout layoutRoot, layoutContent;
    private TextView tvRegulateContent;
    private TextView tvQualified, tvBasicQualified, tvUnqualified;
    private TextView tvMethod;
    private TextView tvSave;
    private EditText edtReason;
    private RecyclerView rvPictures;
    private LinearLayout layoutReason;

    private GridImageAdapter adapter;
    private List<LocalMedia> selectList;

    private RegulateResultBeanDao regulateResultBeanDao;
    private FileEntityDao fileEntityDao;
    private TaskBeanDao taskBeanDao;
    private List<RegulateResultBean> mData;
    private RegulateResultBean item;
    private RegulateResultBean last;
    private TaskBean task;
    private RegulateObjectBean object;

    private String result = "";
    private String taskId = "";
    private Long beanId = 0l;
    private boolean isUploadItemPic = false;

    private HttpManager manager;
    private UploadImageApi uploadImageApi;
    private SaveItemResultApi saveItemResultApi;
    private AddTaskApi addTaskApi;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_regulate_item_check;
    }

    @Override
    public void initView() {
        setToolBarTitle("选择检查结果");
        setToolSubBarTitle("保存");
        findViews();
        initPics();

        manager = new HttpManager(this, this);
        uploadImageApi = new UploadImageApi();
        saveItemResultApi = new SaveItemResultApi();
        addTaskApi = new AddTaskApi();

        regulateResultBeanDao = MyApplication.getInstance().getSession().getRegulateResultBeanDao();
        fileEntityDao = MyApplication.getInstance().getSession().getFileEntityDao();
        taskBeanDao = MyApplication.getInstance().getSession().getTaskBeanDao();

        taskId = getIntent().getStringExtra(ConstantValue.KEY_TASK_ID);
        beanId = getIntent().getLongExtra(ConstantValue.KEY_ITEM_ID, 0l);
        object = (RegulateObjectBean) getIntent().getSerializableExtra(ConstantValue.KEY_OBJECT_BEAN);
        last = (RegulateResultBean) getIntent().getSerializableExtra(ConstantValue.KEY_CONTENT);

        task = taskBeanDao.queryBuilder().where(
                TaskBeanDao.Properties.Id.eq(taskId)
        ).unique();

        /**
         * 这个是从检查项列表页面点进来的时候
         */
        if (null != beanId && 0l != beanId) {
            item = regulateResultBeanDao.loadByRowId(beanId);
            updateView(item);
            return;
        }

        mData = regulateResultBeanDao.queryBuilder().where(
                RegulateResultBeanDao.Properties.Inspid.eq(taskId),
                RegulateResultBeanDao.Properties.Status.notEq(ConstantValue.OBJ_CHECK_STATUS_FINISH)
        ).list();
        /**
         * 如果已经不存在未做完的检查项，就判断一下是否都上传了
         * 没有上传的话，先把图片和检查项都上传
         */
        if (null != mData && mData.size() > 0) {
            item = mData.get(0);
            updateView(item);
        } else {
            if (null != last) {
                updateView(last);
            }
            uploadData();
        }

    }

    List<FileEntity> fileEntities;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_qualified:
                layoutReason.setVisibility(View.GONE);
                setSelectStyle(tvQualified, tvBasicQualified, tvUnqualified);
                result = ConstantValue.RESULT_QUALIFIED;
                break;
            case R.id.tv_basic_qualified:
                layoutReason.setVisibility(View.GONE);
                setSelectStyle(tvBasicQualified, tvQualified, tvUnqualified);
                result = ConstantValue.RESULT_BASIC_QUALIFIED;
                break;
            case R.id.tv_unqualified:
                layoutReason.setVisibility(View.VISIBLE);
                setSelectStyle(tvUnqualified, tvQualified, tvBasicQualified);
                result = ConstantValue.RESULT_UNQUALIFIED;
                break;
            case R.id.tv_content:
                Intent intent = new Intent(this, CheckMethodActivity.class);
                intent.putExtra("method", item.getId());
                startActivity(intent);
                break;
            case R.id.tv_method:
                Intent intent2 = new Intent(this, CheckMethodActivity.class);
                intent2.putExtra("method", item.getItemid());
                startActivity(intent2);
                break;
            case R.id.toolbar_subtitle:
                /**
                 * 先判断有没有结果
                 */
                if (TextUtil.isEmpty(result)) {
                    ToastUtil.toast(RegulateItemCheckActivity.this, "请先选择结果");
                    return;
                }
                uploadItemData();
                break;
        }
    }


    private void uploadItemData() {
        /**
         * 保存图片
         */

        if (null != selectList && selectList.size() > 0) {
            String local = "";
            for (LocalMedia media : selectList) {
                local = local + "," + media.getCompressPath();
                FileEntity fileEntity = new FileEntity();
                fileEntity.setLocalPath(media.getCompressPath());
                fileEntity.setTaskId(taskId);
                fileEntity.setBeanId(item.getBeanId());
                fileEntity.setType("item_img");
                fileEntityDao.insert(fileEntity);
            }
            if (local.length() > 1)
                local = local.substring(1);
            item.setLoacalPath(local);
        }
        item.setInsploc(MyApplication.getInstance().getLongtitude() + "," + MyApplication.getInstance().getLatitude());
        item.setInspresult(result);
        item.setInspdesc(edtReason.getText().toString());
        item.setStatus(ConstantValue.OBJ_CHECK_STATUS_FINISH);

        regulateResultBeanDao.update(item);

        if (null != selectList && selectList.size() > 0) {
            isUploadItemPic = true;
            uploadImageApi.setParts(PictureSelectorUtil.imagesToMultipartBody(selectList));
            manager.doHttpDeal(uploadImageApi);
            return;
        }

        intentToNextItem();
    }

    private void saveResult(String result, String reason) {
        /**
         * 保存图片
         */
        for (LocalMedia media : selectList) {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setLocalPath(media.getCompressPath());
            fileEntity.setTaskId(taskId);
            fileEntity.setId(item.getBeanId());
            fileEntity.setType("item_img");
            fileEntityDao.save(fileEntity);
        }
        item.setInspresult(result);
        item.setInspdesc(reason);
        item.setStatus(ConstantValue.OBJ_CHECK_STATUS_FINISH);
        regulateResultBeanDao.update(item);
    }

    @Override
    public void onNext(String resulte, String method) {
        JSONObject o = JSON.parseObject(resulte);
        if (!ConstantValue.isSuccess(o)) {
            return;
        }

        if (method.equals(addTaskApi.getMethod())) {
            task.setState1("1");
            taskBeanDao.update(task);
            uploadData();
        } else if (method.equals(uploadImageApi.getMethod())) {
            List<String> arrays = o.getJSONArray(ConstantValue.DATA).toJavaList(String.class);
            if (isUploadItemPic) {
                /**
                 * 删掉本地图片表的记录
                 */
                String onLinePath = "";
                for (String s : arrays) {
                    onLinePath = onLinePath + "," + s;
                }
                if (onLinePath.length() > 1)
                    onLinePath = onLinePath.substring(1);
                item.setImage(onLinePath);
                regulateResultBeanDao.update(item);
                fileEntityDao.deleteInTx(fileEntityDao.queryBuilder().where(
                        FileEntityDao.Properties.TaskId.eq(taskId),
                        FileEntityDao.Properties.BeanId.eq(item.getBeanId()),
                        FileEntityDao.Properties.Type.eq("item_img")
                ).list());
                intentToNextItem();
                return;
            }

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
            task.setState2_1("1");
            taskBeanDao.update(task);
            uploadData();
        } else if (method.equals(saveItemResultApi.getMethod())) {
            task.setState2("1");
            task.setState3("1");
            taskBeanDao.update(task);
            intentToNext();
        }

    }

    @Override
    public void onError(ApiException e, String method) {
        /**
         * 如果上传图片失败了，直接保存在本地，等有网络的时候再上传
         * 上传当前检查项图片失败，图片保存到本地，跳转至下一个检查项
         * 上传检查任务，上传所有检查项的图片失败的时候，跳转到签名的页面
         */
        if (method.equals(uploadImageApi.getMethod())) {
            if (isUploadItemPic) {
                intentToNextItem();
                return;
            }
        }
        intentToNext();

    }

    private void uploadData() {
        isUploadItemPic = false;
        if ("0".equals(task.getState1())) {
            //如果需要上传检查任务，调用上传检查任务的接口
            addTaskApi.setParams(task);
            manager.doHttpDeal(addTaskApi);
            return;
        }

        fileEntities = fileEntityDao.queryBuilder().where(
                FileEntityDao.Properties.TaskId.eq(task.getId()),
                FileEntityDao.Properties.Type.eq("item_img")
        ).list();

        if (null != fileEntities && fileEntities.size() > 0) {
            uploadImageApi = new UploadImageApi();
            uploadImageApi.setType("3");
            uploadImageApi.setParts(PictureSelectorUtil.imagesToMultipartBody2(fileEntities));
            manager.doHttpDeal(uploadImageApi);
            return;
        }

        List<RegulateResultBean> list = regulateResultBeanDao.queryBuilder().where(
                RegulateResultBeanDao.Properties.Inspid.eq(task.getId())
        ).list();
        saveItemResultApi.setParams(list);
        manager.doHttpDeal(saveItemResultApi);

    }


    private void updateView(RegulateResultBean bean) {
//        item = regulateResultBeanDao.loadByRowId(beanId);
        if (null == bean) {
            /**
             * 本地找不到对应的检查项，应该跳转到哪个页面呢？
             */
            return;
        }
        tvRegulateContent.setText(bean.getItemcontent());
        result = bean.getInspresult();
        if (ConstantValue.RESULT_QUALIFIED.equals(result)) {
            setSelectStyle(tvQualified, tvBasicQualified, tvUnqualified);
        } else if (ConstantValue.RESULT_BASIC_QUALIFIED.equals(result)) {
            setSelectStyle(tvBasicQualified, tvQualified, tvUnqualified);
        } else if (ConstantValue.RESULT_UNQUALIFIED.equals(result)) {
            setSelectStyle(tvUnqualified, tvQualified, tvBasicQualified);
            layoutReason.setVisibility(View.VISIBLE);
            edtReason.setText(bean.getInspdesc());
        }
        String image = bean.getLoacalPath();
        if (!TextUtil.isEmpty(image)) {
            //如果在线的图片是空的，表示图片还没有上传至服务器，需要从本地找到图片路径，然后显示出来
            String[] imgs = image.split(",");
            for (String str : imgs) {
                LocalMedia localMedia = new LocalMedia();
                localMedia.setCompressPath(str);
                localMedia.setPath(str);
                selectList.add(localMedia);
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 初始化图片相关的
     */
    private void initPics() {
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
                            PictureSelector.create(RegulateItemCheckActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList);
                            break;
                    }
                }
            }
        });
    }

    /**
     * 跳转到生成检查文件的页面
     */
    private void intentToNext() {
        Intent intent = new Intent(this, SiteCheckUploadActivity.class);
        intent.putExtra(ConstantValue.KEY_TASK_ID, task.getId());
        intent.putExtra(ConstantValue.KEY_TABLE_ID, task.getInsptable());
        intent.putExtra(ConstantValue.SITE_REGULATE_RESULT, getResult());
        intent.putExtra(ConstantValue.KEY_ITEMS, (Serializable) mData);
        intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, object);
        startActivityForResult(intent, ConstantValue.REQUEST_CODE_SITE_CHECK);
        dissmisPD();
        finish();
    }

    /**
     * 跳转到生成检查文件的页面
     */
    private void intentToNextItem() {
        Intent intent = new Intent(this, RegulateItemCheckActivity.class);
        intent.putExtra(ConstantValue.KEY_TASK_ID, task.getId());
        intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, object);
        intent.putExtra(ConstantValue.KEY_CONTENT, item);//把上一个检查项传进来
        startActivity(intent);
        dissmisPD();
        finish();
    }

    /**
     * 设置选中和未选中的样式
     */
    private void setSelectStyle(TextView tvSeleted, TextView tvUnSeleted1, TextView tvSeleted2) {
        tvSeleted.setBackground(getResources().getDrawable(R.drawable.bg_check_item_result_selected));
        tvUnSeleted1.setBackground(getResources().getDrawable(R.drawable.bg_check_item_result_unselect));
        tvSeleted2.setBackground(getResources().getDrawable(R.drawable.bg_check_item_result_unselect));
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelectorUtil.initPictureSelector(RegulateItemCheckActivity.this, 4, 1, 4, selectList);
        }
    };

    private String getResult() {
        String re = ConstantValue.RESULT_QUALIFIED;
        for (RegulateResultBean bean : mData) {
            if (ConstantValue.RESULT_UNQUALIFIED.equals(bean.getInspresult())) {
                return ConstantValue.RESULT_UNQUALIFIED;
            } else if (ConstantValue.RESULT_BASIC_QUALIFIED.equals(bean.getInspresult())) {
                re = ConstantValue.RESULT_BASIC_QUALIFIED;
            }
        }
        return re;
    }

    private void findViews() {
        tvSave = getSubTitle();
        tvRegulateContent = findViewById(R.id.tv_content);
        tvQualified = findViewById(R.id.tv_qualified);
        tvBasicQualified = findViewById(R.id.tv_basic_qualified);
        tvUnqualified = findViewById(R.id.tv_unqualified);
        tvMethod = findViewById(R.id.tv_method);
        edtReason = findViewById(R.id.edt_reason);
        rvPictures = findViewById(R.id.recyclerview_pictures);
        layoutReason = findViewById(R.id.layout_reason);
        layoutRoot = findViewById(R.id.layout_root);
        layoutContent = findViewById(R.id.layout_content);

        tvSave.setOnClickListener(this);
        tvQualified.setOnClickListener(this);
        tvBasicQualified.setOnClickListener(this);
        tvUnqualified.setOnClickListener(this);
        tvMethod.setOnClickListener(this);
        SystemUtil.keepLoginBtnNotOver(layoutRoot, layoutContent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK) {
            selectList = PictureSelector.obtainMultipleResult(data);

            Observable.just(selectList).observeOn(Schedulers.newThread())
                    .flatMap(new Func1<List<LocalMedia>, Observable<?>>() {
                        @Override
                        public Observable<?> call(List<LocalMedia> localMedia) {
                            PictureSelectorUtil.PictureListCreateWatermark(RegulateItemCheckActivity.this, selectList, ConstantValue.WATER_MARK + TextUtil.date());
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
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, RegulateItemListActivity.class);
        intent.putExtra(ConstantValue.KEY_TASK_ID, taskId);
        startActivity(intent);
        finish();
    }
}
