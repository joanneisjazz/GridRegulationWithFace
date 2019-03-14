package com.jstech.gridregulation.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.UploadSiteCheckDataAdapter;
import com.jstech.gridregulation.api.AddTaskApi;
import com.jstech.gridregulation.api.DeteleTaskApi;
import com.jstech.gridregulation.api.SaveItemResultApi;
import com.jstech.gridregulation.api.SaveResultApi;
import com.jstech.gridregulation.api.UploadFileApi;
import com.jstech.gridregulation.api.UploadImageApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.bean.SaveResultBean;
import com.jstech.gridregulation.utils.FileUtil;
import com.jstech.gridregulation.utils.PictureSelectorUtil;
import com.jstech.gridregulation.utils.TextUtil;
import com.jstech.gridregulation.utils.ToastUtil;
import com.rxretrofitlibrary.greendao.DaoSession;
import com.rxretrofitlibrary.greendao.FileEntityDao;
import com.rxretrofitlibrary.greendao.RegulateResultBeanDao;
import com.rxretrofitlibrary.greendao.TaskBeanDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.FileEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

public class UploadSiteCheckDataActivity extends BaseActivity implements UploadSiteCheckDataAdapter.IUplaodable, HttpOnNextListener {

    private RecyclerView recyclerView;
    private TextView tvHint;

    private List<TaskBean> mData;
    private UploadSiteCheckDataAdapter mAdapter;


    private AddTaskApi addTaskApi;
    private SaveItemResultApi saveItemResultApi;
    private UploadFileApi uploadFileApi;
    private UploadImageApi uploadImageApi;
    private SaveResultApi saveResultApi;
    private DeteleTaskApi deteleTaskApi;
    private HttpManager manager;


    private TaskBeanDao taskBeanDao;
    private RegulateResultBeanDao regulateResultBeanDao;
    private FileEntityDao fileEntityDao;
    private TaskBean nowTask;//正在上传的任务


    /**
     * 上传检查项的图片
     */
    private List<FileEntity> regulateSign;
    private List<FileEntity> objSign;
    private List<FileEntity> fileEntities;

    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_upload_site_check_data;
    }

    @Override
    public void initView() {
        setToolBarTitle("现场检查任务");
        setToolSubBarTitle("一键提交");
        recyclerView = findViewById(R.id.recyclerView);
        tvHint = findViewById(R.id.tv_hint);
        mData = new ArrayList<>();
        mAdapter = new UploadSiteCheckDataAdapter(mData, this, R.layout.item_upload_site_check_data, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        manager = new HttpManager(this, this);
        initData();

        getSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAllTasks();
            }
        });

    }

    @Override
    public void click(TaskBean taskBean) {
        nowTask = taskBean;
        uploadTask(nowTask);
    }

    private void initData() {
        userId = SharedPreferencesHelper.getInstance(this).getUserExtId(this);
        DaoSession session = MyApplication.getInstance().getSession();
        fileEntityDao = session.getFileEntityDao();
        regulateResultBeanDao = session.getRegulateResultBeanDao();
        taskBeanDao = session.getTaskBeanDao();
        addTaskApi = new AddTaskApi();
        saveItemResultApi = new SaveItemResultApi();
        uploadFileApi = new UploadFileApi();
        deteleTaskApi = new DeteleTaskApi();
        saveResultApi = new SaveResultApi();

        mData.addAll(taskBeanDao.queryBuilder().where(
                TaskBeanDao.Properties.Oisuper.eq(userId),
                TaskBeanDao.Properties.State5.eq("1"),
                TaskBeanDao.Properties.State6.notEq("1")
        ).list());
        if (mData.size() == 0) {
            tvHint.setVisibility(View.VISIBLE);
        } else {
            tvHint.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }


    private void uploadAllTasks() {
        if (null == mData && mData.size() == 0) {
            tvHint.setVisibility(View.VISIBLE);
            ToastUtil.toast(this, "现场检查任务已全部上传完毕");
            return;
        }
        nowTask = mData.get(0);
        uploadTask(nowTask);

    }

    private void uploadTask(TaskBean bean) {
        if (null == bean) {
            return;
        }

        if ("1".equals(bean.getDeleteFlag())) {
            deleteTask(bean);
            return;
        }
        if ("0".equals(bean.getState1())) {
            //如果需要上传检查任务，调用上传检查任务的接口
            addTaskApi.setParams(nowTask);
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

    /**
     * 删除检查任务
     */
    private void deleteTask(TaskBean taskBean) {
        SaveResultBean saveResultBean = new SaveResultBean();
        saveResultBean.setInsp(taskBean);
        deteleTaskApi.setBean(saveResultBean);
        taskBeanDao.delete(taskBean);
        regulateResultBeanDao.deleteInTx(regulateResultBeanDao.queryBuilder().where(
                RegulateResultBeanDao.Properties.Inspid.eq(taskBean.getId())
        ).list());
        manager.doHttpDeal(deteleTaskApi);
    }

    @Override
    public void onNext(String resulte, String method) {

        JSONObject o = JSON.parseObject(resulte);
        if (!ConstantValue.isSuccess(o)) {
            return;
        }
        if (method.equals(addTaskApi.getMethod())) {
            //上传检查任务，更新检查任务的信息
            nowTask.setState1("1");
            taskBeanDao.update(nowTask);
            uploadTask(nowTask);
        } else if (method.equals(saveItemResultApi.getMethod())) {
            //上传检查项，更新检查任务里的检查项上传的状态
            nowTask.setState2("1");
            nowTask.setState3("1");
            taskBeanDao.update(nowTask);
            uploadTask(nowTask);
        } else if (method.equals(uploadFileApi.getMethod())) {
            //上传pdf文件，更新检查任务里的pdf文件上传的状态
            fileEntityDao.deleteInTx(fileEntityDao.queryBuilder().where(
                    FileEntityDao.Properties.TaskId.eq(nowTask.getId()),
                    FileEntityDao.Properties.Type.eq("pdf")
            ).list());
            nowTask.setInspdoc(o.getObject(ConstantValue.DATA, String.class));
            nowTask.setState4("1");
            taskBeanDao.update(nowTask);
            uploadTask(nowTask);
        } else if (method.equals(uploadImageApi.getMethod())) {
            List<String> arrays = o.getJSONArray(ConstantValue.DATA).toJavaList(String.class);
            if ("1".equals(uploadImageApi.getType())) {
                //上传检查人签字，更新检查任务里的检查人签字上传的状态
                String imgs = "";
                for (String str : arrays) {
                    imgs = imgs + "," + str;
                }
                imgs = imgs.substring(1);
                nowTask.setSupersign(imgs);
                taskBeanDao.update(nowTask);
                fileEntityDao.deleteInTx(regulateSign);
                uploadTask(nowTask);
            } else if ("2".equals(uploadImageApi.getType())) {
                //上传检查对象签字，更新检查任务里的检查对象签字上传的状态
                if (null != arrays && arrays.size() > 0) {
                    nowTask.setEntsign(arrays.get(0));
                    taskBeanDao.update(nowTask);
                }
                fileEntityDao.deleteInTx(objSign);
                uploadTask(nowTask);
//                uploadImages(nowTask);
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
                nowTask.setState2_1("1");
                taskBeanDao.update(nowTask);
                uploadTask(nowTask);
//                uploadAll(nowTask);
            }
        } else if (method.equals(saveResultApi.getMethod())) {
            //上传所有的信息
            nowTask.setState6("1");
            taskBeanDao.update(nowTask);
            mData.remove(nowTask);
            mAdapter.notifyDataSetChanged();
            if (null == mData || mData.isEmpty() || mData.size() <= 0) {
                tvHint.setVisibility(View.VISIBLE);
                Toast.makeText(UploadSiteCheckDataActivity.this, "检查任务信息上传成功", Toast.LENGTH_LONG).show();
                return;
            }
            uploadAllTasks();

        } else if (method.equals(deteleTaskApi.getMethod())) {
            taskBeanDao.delete(nowTask);
            mData.remove(nowTask);
            mAdapter.notifyDataSetChanged();
            uploadAllTasks();
        }
    }

    @Override
    public void onError(ApiException e, String method) {
        ToastUtil.toast(UploadSiteCheckDataActivity.this, "上传失败，请检查网络");
    }
}
