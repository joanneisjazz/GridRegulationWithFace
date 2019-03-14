package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.RegulateResultAdapter;
import com.jstech.gridregulation.api.AddTaskApi;
import com.jstech.gridregulation.api.DeteleTaskApi;
import com.jstech.gridregulation.api.GetEntRegulateRecordApi;
import com.jstech.gridregulation.api.GetItemResultApi;
import com.jstech.gridregulation.api.SaveItemResultApi;
import com.jstech.gridregulation.api.UploadImageApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.bean.SaveResultBean;
import com.jstech.gridregulation.utils.PictureSelectorUtil;
import com.jstech.gridregulation.utils.TextUtil;
import com.jstech.gridregulation.utils.ToastUtil;
import com.jstech.gridregulation.widget.MyPopupWindow;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 选中的检查项列表页面，显示每个检查的检查状况
 */
public class RegulateItemListActivity extends BaseActivity implements HttpOnNextListener {

    private RecyclerView recyclerView;

    private RegulateResultAdapter adapter;

    private String objectId;
    private String taskId = "";
    private String tableId = "";
    private TaskBean taskBean;
    private RegulateObjectBean object;
    private List<RegulateResultBean> mData;
    private List<FileEntity> fileEntities;


    private RegulateResultBeanDao regulateResultBeanDao;
    private TaskBeanDao taskBeanDao;
    private RegulateObjectBeanDao regulateObjectBeanDao;
    private FileEntityDao fileEntityDao;


    private HttpManager manager;
    private SaveItemResultApi saveItemResultApi;
    private AddTaskApi addTaskApi;
    private UploadImageApi uploadImageApi;
    private DeteleTaskApi deteleTaskApi;
    private GetEntRegulateRecordApi getEntRegulateRecordApi;
    private GetItemResultApi getItemResultApi;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_recycler_view;
    }

    @Override
    public void initView() {
        setToolBarTitle("检查结果");
        setToolSubBarTitle("保存");

        DaoSession session = MyApplication.getInstance().getSession();
        regulateResultBeanDao = session.getRegulateResultBeanDao();
        taskBeanDao = session.getTaskBeanDao();
        fileEntityDao = session.getFileEntityDao();
        regulateObjectBeanDao = session.getRegulateObjectBeanDao();

        recyclerView = findViewById(R.id.recyclerview);

        mData = new ArrayList<>();
        adapter = new RegulateResultAdapter(mData, this, R.layout.item_regulate_item_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        manager = new HttpManager(this, this);
        deteleTaskApi = new DeteleTaskApi();
        addTaskApi = new AddTaskApi();
        getItemResultApi = new GetItemResultApi();
        saveItemResultApi = new SaveItemResultApi();
        uploadImageApi = new UploadImageApi();
        getEntRegulateRecordApi = new GetEntRegulateRecordApi();
        getItemResultApi = new GetItemResultApi();

        setListener();
        initPopupWindow();

        object = (RegulateObjectBean) getIntent().getSerializableExtra(ConstantValue.KEY_OBJECT_BEAN);
        objectId = getIntent().getStringExtra(ConstantValue.KEY_OBJECT_ID);
        taskId = getIntent().getStringExtra(ConstantValue.KEY_TASK_ID);

        /**
         * 从地图页面直接进来，taskId为空，objectId不为空
         * 根据objectId去请求企业的检查记录接口，获取最新的数据更新
         * 如果失败，根据objectId去本地请求数据
         */
        if (!TextUtil.isEmpty(objectId) && TextUtil.isEmpty(taskId)) {
            getEntRegulateRecordApi.setParam(objectId);
            getEntRegulateRecordApi.setLength(1);
            getEntRegulateRecordApi.setStart(0);
            manager.doHttpDeal(getEntRegulateRecordApi);
            return;
        }

        /**
         * 从选择检查项的页面跳转进来的
         */
        taskBean = taskBeanDao.queryBuilder().where(
                TaskBeanDao.Properties.Id.eq(taskId)
        ).unique();

        mData.addAll(regulateResultBeanDao.queryBuilder().where(
                RegulateResultBeanDao.Properties.Inspid.eq(taskId)
        ).list());
        adapter.notifyDataSetChanged();

        object = regulateObjectBeanDao.queryBuilder().where(
                RegulateObjectBeanDao.Properties.Id.eq(taskBean.getEntid())
        ).unique();


    }

    private void setListener() {
        getSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPD();
                if (!isAllChecked()) {
                    ToastUtil.toast(RegulateItemListActivity.this, "请先对所有的检查项进行检查");
                    dissmisPD();
                }

                uploadData();
            }
        });

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RegulateItemListActivity.this, RegulateItemCheckActivity.class);
                intent.putExtra(ConstantValue.KEY_TASK_ID, taskBean.getId());
                intent.putExtra(ConstantValue.KEY_TABLE_ID, taskBean.getTableId());
                intent.putExtra(ConstantValue.KEY_OBJECT_ID, taskBean.getEntid());
                intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, object);
//                intent.putExtra(ConstantValue.KEY_TASK_ID,taskId);
                intent.putExtra(ConstantValue.KEY_ITEM_ID, mData.get(position).getBeanId());
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public void onNext(String resulte, String method) {
        JSONObject o = JSON.parseObject(resulte);
        if (!ConstantValue.isSuccess(o)) {
            return;
        }
        if (method.equals(addTaskApi.getMethod())) {
            taskBean.setState1("1");
            taskBeanDao.update(taskBean);
            uploadData();
        } else if (method.equals(uploadImageApi.getMethod())) {
            //获取到图片的地址以后，要更新检查项的表
            List<String> arrays = o.getJSONArray(ConstantValue.DATA).toJavaList(String.class);
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
            uploadData();
        } else if (method.equals(saveItemResultApi.getMethod())) {
            taskBean.setState2("1");
            taskBean.setState3("1");
            taskBeanDao.update(taskBean);
            intentToNext();
        } else if (method.equals(deteleTaskApi.getMethod())) {
            taskBeanDao.delete(taskBean);
            intentToBack();
        } else if (method.equals(getEntRegulateRecordApi.getMethod())) {
            /**
             * 从服务器获取检验项目，然后保存到本地
             */
            List<TaskBean> list = o.getJSONArray(ConstantValue.RESULT).toJavaList(TaskBean.class);
            if (null == list && list.size() <= 0) {
                Toast.makeText(RegulateItemListActivity.this, "未找到该任务的检查项，请开始生成检查任务", Toast.LENGTH_LONG).show();
                startActivity(new Intent(RegulateItemListActivity.this, SiteActivity.class));
                return;
            }
            taskBean = list.get(0);
            taskId = taskBean.getId();
            if (null == taskBean || ConstantValue.OBJ_CHECK_STATUS_FINISH.equals(taskBean.getInspstatus())) {
                Toast.makeText(RegulateItemListActivity.this, "该任务已经完成检查，请刷新企业信息", Toast.LENGTH_LONG).show();
                startActivity(new Intent(RegulateItemListActivity.this, SiteActivity.class));
                return;
            }
            if (taskBean.getInspstatus() != ConstantValue.OBJ_CHECK_STATUS_FINISH) {
                TaskBean taskBean1 = taskBeanDao.queryBuilder().where(
                        TaskBeanDao.Properties.Id.eq(taskId)
                ).unique();
                if (null == taskBean1) {
                    taskBean.setState1("1");
                    taskBean.setState2_1("0");
                    taskBean.setState2("0");
                    taskBean.setState3("0");
                    taskBean.setState4("0");
                    taskBean.setState5("0");
                    taskBean.setState6("0");
                    taskBean.setEntname(object.getName());
                    taskBean.setRegulatorName(SharedPreferencesHelper.getInstance(this).getSharedPreference("userName", "").toString());
                    taskBeanDao.insertInTx(taskBean);
                } else {
                    taskBean = taskBean1;
                }
                getItemResultApi.setParam(taskId);
                manager.doHttpDeal(getItemResultApi);
            }

        } else if (method.equals(getItemResultApi.getMethod())) {
            /**
             * 获取到检验项目之后，保存到本地
             */
            mData.clear();
            mData.addAll(o.getJSONArray(ConstantValue.RESULT).toJavaList(RegulateResultBean.class));
            if (null != mData && mData.size() > 0) {
                tableId = mData.get(0).getInsptable();
            }
            List<RegulateResultBean> regulateResultBeans = regulateResultBeanDao.queryBuilder().where(
                    RegulateResultBeanDao.Properties.Inspid.eq(taskId)
            ).list();
            if (null != regulateResultBeans && regulateResultBeans.size() > 0) {
                regulateResultBeanDao.deleteInTx(regulateResultBeans);
            }
            regulateResultBeanDao.saveInTx(mData);
            adapter.notifyDataSetChanged();
            taskBean.setState2("1");
            taskBeanDao.update(taskBean);
        }
    }

    @Override
    public void onError(ApiException e, String method) {
        if (method.equals(deteleTaskApi.getMethod())) {
            intentToBack();
        } else if (method.equals(getEntRegulateRecordApi.getMethod())) {
            /**
             * 获取企业的检查记录失败了，直接使用本地的记录
             * 根据企业id去任务表里查找检查状态为1的任务
             */
            taskBean = taskBeanDao.queryBuilder().where(
                    TaskBeanDao.Properties.Entid.eq(objectId), TaskBeanDao.Properties.State6.notEq("1"),
                    TaskBeanDao.Properties.DeleteFlag.notEq("1")
            ).unique();
            if (null != taskBean) {
                mData.addAll(regulateResultBeanDao.queryBuilder().where(
                        RegulateResultBeanDao.Properties.Inspid.eq(taskBean.getId())
                ).list());
                if (null != mData && !mData.isEmpty() && mData.size() > 0) {
                    adapter.notifyDataSetChanged();
                }
            } else {
                /**
                 * todo
                 * 如果没有任何检查项，直接删掉该任务
                 */
                ToastUtil.toast(RegulateItemListActivity.this, "请在有网路的环境下更新数据");
                Intent intent = new Intent(RegulateItemListActivity.this, SiteActivity.class);
                startActivity(intent);
                finish();
            }
        } else if (method.equals(getItemResultApi.getMethod())) {
            /**
             * 更新检查项失败，直接使用本地的检查项
             */

            mData.addAll(regulateResultBeanDao.queryBuilder().where(
                    RegulateResultBeanDao.Properties.Inspid.eq(taskId)
            ).list());
            if (null != mData && !mData.isEmpty() && mData.size() > 0) {
                adapter.notifyDataSetChanged();
            } else {
                /**
                 * todo
                 * 如果没有任何检查项，直接删掉该任务
                 */
            }
        } else {
            intentToNext();
        }
    }

    /**
     * 跳转到生成检查文件的页面
     */
    private void intentToNext() {
        Intent intent = new Intent(this, SiteCheckUploadActivity.class);
        intent.putExtra(ConstantValue.KEY_TASK_ID, taskBean.getId());
        intent.putExtra(ConstantValue.KEY_TABLE_ID, tableId);
        intent.putExtra(ConstantValue.SITE_REGULATE_RESULT, getResult());
        intent.putExtra(ConstantValue.KEY_ITEMS, (Serializable) mData);
        intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, object);
        startActivityForResult(intent, ConstantValue.REQUEST_CODE_SITE_CHECK);
        dissmisPD();
        finish();
    }

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

    private void uploadData() {
        if ("0".equals(taskBean.getState1())) {
            //如果需要上传检查任务，调用上传检查任务的接口
            addTaskApi.setParams(taskBean);
            manager.doHttpDeal(addTaskApi);
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

        List<RegulateResultBean> list = regulateResultBeanDao.queryBuilder().where(
                RegulateResultBeanDao.Properties.Inspid.eq(taskBean.getId())
        ).list();
        saveItemResultApi.setParams(list);
        manager.doHttpDeal(saveItemResultApi);

    }

    /**
     * 判断所有的检查项是不是都检查完了
     */

    private boolean isAllChecked() {
        for (RegulateResultBean resultBean : mData) {
            if (TextUtil.isEmpty(resultBean.getInspresult())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        tipWindow.showAtLocationCenter(getLayoutId());
    }

    private MyPopupWindow tipWindow;
    private TextView tvTip;

    private void initPopupWindow() {
        tipWindow = new MyPopupWindow.Builder().setContext(this).
                setContentView(R.layout.layout_back_tip).setTitle(getResources().getString(R.string.tip))
                .setPass(getString(R.string.confrim))
                .setUnpass(getString(R.string.cancel))
                .setPassListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipWindow.dismiss();
                        deleteTask();
                    }
                })
                .builder();
        tvTip = tipWindow.getContentFrameLayout().findViewById(R.id.tv_content);
        tvTip.setText("请确认是否删除本次检查任务的信息？");
    }

    private void deleteTask() {
        SaveResultBean saveResultBean = new SaveResultBean();
        saveResultBean.setInsp(taskBean);
        deteleTaskApi.setBean(saveResultBean);
        taskBean.setState5("1");
        taskBean.setDeleteFlag("1");
        taskBeanDao.update(taskBean);
        regulateResultBeanDao.deleteInTx(regulateResultBeanDao.queryBuilder().where(
                RegulateResultBeanDao.Properties.Inspid.eq(taskBean.getId())
        ).list());
        int count = 0;
        if (!TextUtil.isEmpty(object.getInspcount()) && !"0".equals(object.getInspcount())) {
            count = Integer.valueOf(object.getInspcount());
            count--;
        }
        object.setInspcount(String.valueOf(count));
        object.setInspstatus(ConstantValue.OBJ_CHECK_STATUS_FINISH);
        regulateObjectBeanDao.update(object);
        if (TextUtil.isEmpty(taskBean.getState1()) || taskBean.getState1().equals("0")) {
            //如果检查任务没有上传至服务器，只需要在本地删掉就可以了
            taskBeanDao.delete(taskBean);
            intentToBack();
        } else {
            manager.doHttpDeal(deteleTaskApi);
        }
    }

    private void intentToBack() {
        Intent intent = new Intent(RegulateItemListActivity.this, SiteActivity.class);
//        intent.putExtra(ConstantValue.KEY_OBJECT_ID, object.getId());
        dissmisPD();
        startActivity(intent);
        finish();
    }
}
