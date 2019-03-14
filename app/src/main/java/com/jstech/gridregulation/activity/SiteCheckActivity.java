package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.CheckResult2Adapter;
import com.jstech.gridregulation.api.DeteleTaskApi;
import com.jstech.gridregulation.api.GetEntRegulateRecordApi;
import com.jstech.gridregulation.api.GetItemResultApi;
import com.jstech.gridregulation.api.SaveItemResultApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.bean.SaveResultBean;
import com.jstech.gridregulation.utils.LogUtils;
import com.jstech.gridregulation.utils.TextUtil;
import com.jstech.gridregulation.widget.MyPopupWindow;
import com.rxretrofitlibrary.greendao.DaoSession;
import com.rxretrofitlibrary.greendao.RegulateObjectBeanDao;
import com.rxretrofitlibrary.greendao.RegulateResultBeanDao;
import com.rxretrofitlibrary.greendao.TaskBeanDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 选完检查项目后开始进行检查
 */
public class SiteCheckActivity extends BaseActivity implements
        CheckResult2Adapter.MethodInterface, View.OnClickListener, HttpOnNextListener {

    private RecyclerView recyclerView;
    private TextView tvNext;

    private CheckResult2Adapter mResultAdapter;
    private ArrayList<RegulateResultBean> mItemList;
    private HttpManager manager;
    private SaveItemResultApi saveItemResultApi;
    private DeteleTaskApi deteleTaskApi;
    private GetEntRegulateRecordApi getEntRegulateRecordApi;
    private GetItemResultApi getItemResultApi;

    private String taskId;
    private String objectId;

    private RegulateResultBeanDao resultBeanDao;
    private TaskBeanDao taskBeanDao;
    private RegulateObjectBeanDao regulateObjectBeanDao;
    private String result = "";
    private String tableId = "";
    private RegulateObjectBean object;
    private TaskBean taskBean;

    private int length = 1;
    private int start = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_site_check;
    }

    @Override
    public void initView() {
//        initDao();
        recyclerView = findViewById(R.id.recyclerview);
        tvNext = getSubTitle();
        initDao();
        mItemList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mResultAdapter = new CheckResult2Adapter(this, mItemList, this);
        mResultAdapter.setHasStableIds(true);
        recyclerView.setAdapter(mResultAdapter);

        getData();
        tvNext.setText(R.string.next);
        tvNext.setOnClickListener(this);


        initPopupWindow();
    }

    private void initDao() {
        DaoSession session = MyApplication.getInstance().getSession();
        resultBeanDao = session.getRegulateResultBeanDao();
        taskBeanDao = session.getTaskBeanDao();
        regulateObjectBeanDao = session.getRegulateObjectBeanDao();
        regulateObjectBeanDao = session.getRegulateObjectBeanDao();
    }

    private void getData() {

        manager = new HttpManager(this, this);
        deteleTaskApi = new DeteleTaskApi();
        saveItemResultApi = new SaveItemResultApi();
        getItemResultApi = new GetItemResultApi();
        getEntRegulateRecordApi = new GetEntRegulateRecordApi();

        objectId = getIntent().getStringExtra(ConstantValue.KEY_OBJECT_ID);
        taskId = getIntent().getStringExtra(ConstantValue.KEY_TASK_ID);
        tableId = getIntent().getStringExtra(ConstantValue.KEY_TABLE_ID);

        object = (RegulateObjectBean) getIntent().getSerializableExtra(ConstantValue.KEY_OBJECT_BEAN);
        /**
         * 如果是从选择检查项的页面跳进来或者从检查结果上传页面跳进来，taskId不为空，直接去本地找数据
         */
        if (!TextUtil.isEmpty(taskId)) {
            taskBean = taskBeanDao.queryBuilder().where(
                    TaskBeanDao.Properties.Id.eq(taskId)
            ).unique();
            mItemList.addAll(resultBeanDao.queryBuilder().where(
                    RegulateResultBeanDao.Properties.Inspid.eq(taskId)
            ).list());
            mResultAdapter.notifyDataSetChanged();
            return;
        }
        /**
         * 从地图页面直接进来，taskId为空，objectId不为空
         * 根据objectId去请求企业的检查记录接口，获取最新的数据更新
         * 如果失败，根据objectId去本地请求数据
         */
        if (!TextUtil.isEmpty(objectId) && TextUtil.isEmpty(taskId)) {
            getEntRegulateRecordApi.setParam(objectId);
            getEntRegulateRecordApi.setLength(length);
            getEntRegulateRecordApi.setStart(start);
            manager.doHttpDeal(getEntRegulateRecordApi);
        }


    }

    /**
     * 检查是否所有的项目都已经有结果
     */
    private int isAllChecked() {
        for (int i = 0; i < mItemList.size(); i++) {
            RegulateResultBean b = mItemList.get(i);
            if (null == b.getInspresult() || "".equals(b.getInspresult()) || b.getInspresult().isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 查看检查方法
     *
     * @param id
     */
    @Override
    public void showMethod(String id) {
        Intent intent = new Intent(this, CheckMethodActivity.class);
        intent.putExtra("method", id);
        startActivity(intent);
    }

    @Override
    public void selectResult(String result, final RegulateResultBean data) {
        data.setInspresult(result);
        final String r = result;
//        resultBeanDao.update(data);
        if (result == ConstantValue.RESULT_UNQUALIFIED) {
            //选择不合格
            reasonWindow.setPassButtonOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = edtReason.getText().toString();
                    reasonWindow.dismiss();
                    if (!TextUtil.isEmpty(s)) {
                        data.setInspdesc(s);
                    }
                    reasonWindow.dismiss();
                    mResultAdapter.notifyDataSetChanged();

                }
            });
            reasonWindow.setUnPassButtonOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtil.isEmpty(edtReason.getText().toString())) {
                        data.setInspdesc("无");
                    } else {
                        data.setInspdesc(edtReason.getText().toString());
                    }
                    reasonWindow.dismiss();
//                    resultBeanDao.update(data);
                    mResultAdapter.notifyDataSetChanged();
                }
            });
            if (!TextUtil.isEmpty(data.getInspdesc()) && !ConstantValue.NULL.equals(data.getInspdesc())) {
                edtReason.setText(data.getInspdesc());
            } else {
                edtReason.setText("");
            }
            reasonWindow.showAtLocation(getLayoutId(), Gravity.CENTER, 0, 0);
        } else {
            data.setInspdesc("");
        }
        mResultAdapter.notifyDataSetChanged();

    }

    private MyPopupWindow reasonWindow;
    private MyPopupWindow tipWindow;
    private EditText edtReason;
    private TextView tvTip;

    private void initPopupWindow() {
        reasonWindow = new MyPopupWindow.Builder().setContext(this).
                setContentView(R.layout.layout_unqualified_reason_input).setTitle("请输入原因")
                .setPass(getString(R.string.confrim))
                .setUnpass(getString(R.string.cancel))
                .builder();
        edtReason = reasonWindow.getContentFrameLayout().findViewById(R.id.edit);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (null != reasonWindow && reasonWindow.isShowing()) {
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_subtitle:
                showPD();
                int i = isAllChecked();
                if (-1 != i) {
                    Toast.makeText(SiteCheckActivity.this, mItemList.get(i).getItemcontent() + "还未选择检查结果", Toast.LENGTH_LONG).show();
                    recyclerView.smoothScrollToPosition(i);
                    dissmisPD();
                    return;
                }
                /**
                 * 先将结果保存至本地
                 */
                for (RegulateResultBean c : mItemList) {
                    c.setInsploc(MyApplication.instance.getLongtitude() + "," + MyApplication.instance.getLatitude());//
                }
                resultBeanDao.saveInTx(mItemList);
                /**
                 * 判断一下检查任务是否已经传给服务器了，如果检查任务已经传给服务器，再将检查项的结果传给服务器
                 * 否则只保存在本地
                 */
                if (taskBean.getState1().equals("1")) {
                    saveItemResultApi.setParams(mItemList);
                    manager.doHttpDeal(saveItemResultApi);
                    return;
                }
                intentToNext();
                break;
        }
    }

    @Override
    public void onNext(String resulte, String method) {
        JSONObject o = JSON.parseObject(resulte);
        String code = o.getString(ConstantValue.CODE);
        if (!code.equals(ConstantValue.CODE_SUCCESS)) {
            return;
        }
        if (method.equals(saveItemResultApi.getMethod())) {
            taskBean.setState2("1");
            taskBean.setState3("1");
            taskBeanDao.update(taskBean);
            intentToNext();
        } else if (method.equals(deteleTaskApi.getMethod())) {
            intentToBack();
        } else if (method.equals(getEntRegulateRecordApi.getMethod())) {
            /**
             * 从服务器获取检验项目，然后保存到本地
             */
            List<TaskBean> list = o.getJSONArray(ConstantValue.RESULT).toJavaList(TaskBean.class);
            if (null == list && list.size() <= 0) {
                Toast.makeText(SiteCheckActivity.this, "未找到该任务的检查项，请开始生成检查任务", Toast.LENGTH_LONG).show();
                startActivity(new Intent(SiteCheckActivity.this, SiteActivity.class));
                return;
            }
            taskBean = list.get(0);
            taskId = taskBean.getId();
            if (null == taskBean || ConstantValue.OBJ_CHECK_STATUS_FINISH.equals(taskBean.getInspstatus())) {
                Toast.makeText(SiteCheckActivity.this, "该任务已经完成检查，请刷新企业信息", Toast.LENGTH_LONG).show();
                startActivity(new Intent(SiteCheckActivity.this, SiteActivity.class));
                return;
            }
            if (taskBean.getInspstatus() != ConstantValue.OBJ_CHECK_STATUS_FINISH) {
                TaskBean taskBean1 = taskBeanDao.queryBuilder().where(
                        TaskBeanDao.Properties.Id.eq(taskId)
                ).unique();
                if (null == taskBean1) {
                    taskBean.setState1("1");
                    taskBean.setState2("1");
                    taskBean.setState3("0");
                    taskBean.setState4("0");
                    taskBean.setState5("0");
                    taskBean.setEntname(object.getName());
                    taskBean.setRegulatorName(SharedPreferencesHelper.getInstance(this).getSharedPreference("userName", "").toString());
                    taskBeanDao.insertWithoutSettingPk(taskBean);
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
            mItemList.clear();
            mItemList.addAll(o.getJSONArray(ConstantValue.RESULT).toJavaList(RegulateResultBean.class));
            if (null != mItemList && mItemList.size() > 0) {
                tableId = mItemList.get(0).getInsptable();
            }
            List<RegulateResultBean> regulateResultBeans = resultBeanDao.queryBuilder().where(
                    RegulateResultBeanDao.Properties.Inspid.eq(taskId)
            ).list();
            if (null != regulateResultBeans && regulateResultBeans.size() > 0) {
                resultBeanDao.deleteInTx(regulateResultBeans);
            }
            resultBeanDao.saveInTx(mItemList);
            mResultAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onError(ApiException e, String method) {
        LogUtils.d(e.getMessage());
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
                mItemList.addAll(resultBeanDao.queryBuilder().where(
                        RegulateResultBeanDao.Properties.Inspid.eq(taskBean.getId())
                ).list());
                if (null != mItemList && !mItemList.isEmpty() && mItemList.size() > 0) {
                    mResultAdapter.notifyDataSetChanged();
                } else {
                    /**
                     * todo
                     * 如果没有任何检查项，直接删掉该任务
                     */
                }
            } else if (method.equals(getItemResultApi.getMethod())) {
                /**
                 * 更新检查项失败，直接使用本地的检查项
                 */

                mItemList.addAll(resultBeanDao.queryBuilder().where(
                        RegulateResultBeanDao.Properties.Inspid.eq(taskId)
                ).list());
                if (null != mItemList && !mItemList.isEmpty() && mItemList.size() > 0) {
                    mResultAdapter.notifyDataSetChanged();
                } else {
                    /**
                     * todo
                     * 如果没有任何检查项，直接删掉该任务
                     */
                }
            } else if (method.equals(saveItemResultApi.getMethod())) {
                taskBean.setState3("0");
                taskBeanDao.update(taskBean);
                intentToNext();
            } else if (method.equals(deteleTaskApi.getMethod())) {
                intentToBack();
            }
        }
    }

    @Override
    public void onBackPressed() {
        tipWindow.showAtLocation(getLayoutId(), Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onDestroy() {
//        tipWindow = null;
        reasonWindow = null;
        super.onDestroy();
    }


    private void deleteTask() {
        deteleTaskApi = new DeteleTaskApi();
//        taskBean.setId(taskId);
        taskBean.setInspstatus("1");
//        taskBean.setDeleteFlag("1");
//        taskBean.setOisuper(SharedPreferencesHelper.getInstance(this).getSharedPreference("extId", "").toString());
        SaveResultBean saveResultBean = new SaveResultBean();
        saveResultBean.setInsp(taskBean);
        deteleTaskApi.setBean(saveResultBean);
//        taskBeanDao.delete(taskBean);
        taskBeanDao.update(taskBean);
        resultBeanDao.deleteInTx(resultBeanDao.queryBuilder().where(
                RegulateResultBeanDao.Properties.Inspid.eq(taskBean.getId())
        ).list());
        int count = 0;
        if (!TextUtil.isEmpty(object.getInspcount())) {
            count--;
        }
        object.setInspcount(String.valueOf(count));
        object.setInspstatus(ConstantValue.OBJ_CHECK_STATUS_FINISH);
        regulateObjectBeanDao.update(object);
        manager.doHttpDeal(deteleTaskApi);
    }

    private String getResult() {
        String re = ConstantValue.RESULT_QUALIFIED;
        for (RegulateResultBean bean : mItemList) {
            if (ConstantValue.RESULT_UNQUALIFIED.equals(bean.getInspresult())) {
                return ConstantValue.RESULT_UNQUALIFIED;
            } else if (ConstantValue.RESULT_BASIC_QUALIFIED.equals(bean.getInspresult())) {
                re = ConstantValue.RESULT_BASIC_QUALIFIED;
            }
        }
        return re;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ConstantValue.REQUEST_CODE_SITE_CHECK) {
            Intent intent = new Intent();
//            intent.putExtra(ConstantValue.KEY_OBJECT_ID, data.getStringExtra(ConstantValue.KEY_OBJECT_ID));
            SiteCheckActivity.this.setResult(RESULT_OK, intent);
            finish();
        } else if (resultCode == RESULT_CANCELED && requestCode == ConstantValue.REQUEST_CODE_SITE_CHECK) {
            SiteCheckActivity.this.setResult(RESULT_CANCELED);
//            manager.doHttpDeal(getItemResultApi);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void intentToNext() {
        Intent intent = new Intent(this, SiteCheckUploadActivity.class);
        intent.putExtra(ConstantValue.KEY_TASK_ID, taskBean.getId());
        intent.putExtra(ConstantValue.KEY_TABLE_ID, tableId);
        intent.putExtra(ConstantValue.SITE_REGULATE_RESULT, getResult());
        intent.putExtra(ConstantValue.KEY_ITEMS, mItemList);
        intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, object);
        startActivityForResult(intent, ConstantValue.REQUEST_CODE_SITE_CHECK);
        dissmisPD();
        finish();
    }

    private void intentToBack() {
        Intent intent = new Intent(SiteCheckActivity.this, CheckTableSelect2Activity.class);
        intent.putExtra(ConstantValue.KEY_OBJECT_ID, objectId);
        dissmisPD();
        startActivity(intent);
        finish();
    }
}
