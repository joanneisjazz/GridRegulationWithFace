package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.jstech.gridregulation.bean.RegulateResult2Bean;
import com.jstech.gridregulation.bean.SaveResultBean;
import com.jstech.gridregulation.utils.LogUtils;
import com.jstech.gridregulation.utils.SystemUtil;
import com.jstech.gridregulation.utils.TextUtil;
import com.jstech.gridregulation.widget.MyPopupWindow;
import com.rxretrofitlibrary.greendao.DaoSession;
import com.rxretrofitlibrary.greendao.TaskBeanDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 选完检查项目后开始进行检查
 */
public class RegulateMapActivity extends BaseActivity implements
        CheckResult2Adapter.MethodInterface, View.OnClickListener, HttpOnNextListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar_subtitle)
    TextView tvNext;

    CheckResult2Adapter mResultAdapter;
    ArrayList<RegulateResultBean> mItemList;
    HttpManager manager;
    SaveItemResultApi saveItemResultApi;
    DeteleTaskApi deteleTaskApi;
    GetEntRegulateRecordApi getEntRegulateRecordApi;
    GetItemResultApi getItemResultApi;

    String taskId;
    String objectId;

//    RegulateResultBeanDao resultBeanDao;
    TaskBeanDao taskBeanDao;
//    RegulateObjectBeanDao regulateObjectBeanDao;
    String result = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_site_check;
    }

    @Override
    public void initView() {
        initDao();
        getData();
        tvNext.setText(R.string.next);
        tvNext.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mResultAdapter = new CheckResult2Adapter(this, mItemList, this);
        mResultAdapter.setHasStableIds(true);
        recyclerView.setAdapter(mResultAdapter);


        initPopupWindow();

    }

    private void initDao() {
        DaoSession session = MyApplication.getInstance().getSession();
//        resultBeanDao = session.getRegulateResultBeanDao();
        taskBeanDao = session.getTaskBeanDao();
//        regulateObjectBeanDao = session.getRegulateObjectBeanDao();
    }

    private void getData() {
        deteleTaskApi = new DeteleTaskApi();
        saveItemResultApi = new SaveItemResultApi();
        getItemResultApi = new GetItemResultApi();
        getEntRegulateRecordApi = new GetEntRegulateRecordApi();
        objectId = getIntent().getStringExtra(ConstantValue.KEY_OBJECT_ID);
        taskId = getIntent().getStringExtra(ConstantValue.KEY_TASK_ID);
        /**
         * 如果是从首页的地图跳进来的话，就先从数据库找到taskid，再找检查项和结果
         */
        manager = new HttpManager(this, this);
        mItemList = new ArrayList<>();
        /**
         * 如果获取到的taskid值是空的，说明是从地图页面的继续检查点击进来的
         * 先通过企业的id在手机本地的任务表中找任务的id
         * 如果在本地中有taskid，根据taskid去检查项的表中取出检查项
         * 如果在本地找不到taskid，就根据企业id去服务器找检查任务和检查项
         */
        if (!TextUtils.isEmpty(taskId)) {//taskId的值不为空
//            mItemList = (ArrayList<RegulateResultBean>) resultBeanDao.queryBuilder().where(RegulateResultBeanDao.Properties.Inspid.eq(taskId)).list();
            return;
        }
        List<TaskBean> taskBeans = taskBeanDao.queryBuilder()
                .where(TaskBeanDao.Properties.Entid.eq(objectId)).list();
        TaskBean taskBean = null;
        for (int i = 0; i < taskBeans.size(); i++) {
            if (!taskBeans.get(i).equals(ConstantValue.OBJ_CHECK_STATUS_FINISH)) {
                taskBean = taskBeans.get(i);
                break;
            }
        }
        if (null == taskBean || null == taskBean.getId() || "".equals(taskBean.getId())) {
            /**
             * 如果本地没有检查的数据，先去服务器取一下
             */
            getEntRegulateRecordApi = new GetEntRegulateRecordApi();
            getEntRegulateRecordApi.setParam(objectId);
            manager.doHttpDeal(getEntRegulateRecordApi);
        } else {
            taskId = taskBean.getId();
//            mItemList = (ArrayList<RegulateResultBean>) resultBeanDao.queryBuilder().where(RegulateResultBeanDao.Properties.Inspid.eq(taskId)).list();
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
//        resultBeanDao.update(data);
        if (result == ConstantValue.RESULT_UNQUALIFIED) {
            //选择不合格
            final String reason = data.getInspdesc();
            if (TextUtil.isEmpty(reason) || ConstantValue.NULL.equals(reason)) {
                edtReason.setText("");
            } else {
                edtReason.setText(reason);
            }
            reasonWindow.setPassButtonOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = edtReason.getText().toString();
                    if (null != s || !s.equals("")) {
                        data.setInspdesc(s);
                    } else {
                        data.setInspdesc("");
                    }
//                    resultBeanDao.update(data);
                    mResultAdapter.notifyDataSetChanged();
                    reasonWindow.dismiss();
                }
            });
            reasonWindow.setUnPassButtonOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.setInspdesc("无");
                    reasonWindow.dismiss();
//                    resultBeanDao.update(data);
                    mResultAdapter.notifyDataSetChanged();
                }
            });
            reasonWindow.showAtLocation(getLayoutId(), Gravity.CENTER, 0, 0);
        }
        mResultAdapter.notifyDataSetChanged();

    }

    MyPopupWindow reasonWindow, tipWindow;
    EditText edtReason;
    TextView tvTip;

    private void initPopupWindow() {
        reasonWindow = new MyPopupWindow.Builder().setContext(this).
                setContentView(R.layout.layout_unqualified_reason_input).setTitle("请输入原因")
                .setwidth(SystemUtil.getWith(this) * 2 / 3)
                .setheight(SystemUtil.getHeight(this) / 2)
                .setFouse(true)
                .setAnimationStyle(R.style.Animation_CustomPopup)
                .setPass(getString(R.string.confrim))
                .setUnpass(getString(R.string.cancel))
                .setOutSideCancel(false)
                .setIsUnpassVisiable(true)
                .builder();
        edtReason = reasonWindow.getContentFrameLayout().findViewById(R.id.edit);
        tipWindow = new MyPopupWindow.Builder().setContext(this).
                setContentView(R.layout.layout_back_tip).setTitle(getResources().getString(R.string.tip))
                .setwidth(SystemUtil.getWith(this) * 2 / 3)
                .setheight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setFouse(true)
                .setAnimationStyle(R.style.Animation_CustomPopup)
                .setPass(getString(R.string.confrim))
                .setUnpass(getString(R.string.cancel))
                .setPassListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipWindow.dismiss();
                        deleteTask();
                    }
                })
                .setUnpassListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipWindow.dismiss();
                    }
                })
                .setOutSideCancel(false)
                .setIsUnpassVisiable(true)
                .builder();
        tvTip = tipWindow.getContentFrameLayout().findViewById(R.id.tv_content);
        tvTip.setText("请确认是否离开本页面？如果离开，检查结果将不会保存");
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
                //调用接口
                for (RegulateResultBean c : mItemList) {
                    c.setInsploc(MyApplication.instance.getLongtitude() + "," + MyApplication.instance.getLatitude());//经纬度还要修改
                }
                int i = isAllChecked();
                if (-1 == i) {
                    saveItemResultApi.setParams(mItemList);
                    manager.doHttpDeal(saveItemResultApi);
                } else {
                    Toast.makeText(RegulateMapActivity.this, mItemList.get(i).getItemcontent() + "还未选择检查结果", Toast.LENGTH_LONG).show();
                    recyclerView.smoothScrollToPosition(i);
                }

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
            int size = mItemList.size();
            for (int i = 0; i < size; i++) {
                mItemList.get(i).setStatus("2");//状态设置成完成
            }
//            resultBeanDao.updateInTx(mItemList);
            Intent intent = new Intent(this, SiteCheckUploadActivity.class);
            intent.putExtra(ConstantValue.KEY_TASK_ID, taskId);
            intent.putExtra(ConstantValue.SITE_REGULATE_RESULT, getResult());
            startActivity(intent);
            LogUtils.d(resulte);
        } else if (method.equals(deteleTaskApi.getMethod())) {
            /**
             * 更新检查任务表，删除本次任务
             * 更新检查任务关联的检查项表，删除所有该任务的检查项
             * 更新检查对象表，更新企业的检查状态为未检查
             */
            TaskBean taskBean = taskBeanDao.queryBuilder().where(TaskBeanDao.Properties.Id.eq(taskId)).unique();
            taskBeanDao.delete(taskBean);
//            resultBeanDao.deleteInTx(resultBeanDao.queryBuilder().where(RegulateResultBeanDao.Properties.Inspid.eq(taskId)).list());
//            RegulateObjectBean regulateObjectBean = regulateObjectBeanDao.queryBuilder().where(RegulateObjectBeanDao.Properties.Id.eq(taskBean.getEntId())).unique();
//            regulateObjectBean.setInspstatus(ConstantValue.OBJ_CHECK_STATUS_NULL);
//            regulateObjectBeanDao.update(regulateObjectBean);
            Intent intent = new Intent(this, CheckTableSelect2Activity.class);
            intent.putExtra(ConstantValue.KEY_OBJECT_ID, taskBean.getEntid());
            intent.putExtra(ConstantValue.REGULATE_ITEM_SELECT_AGAIN, true);
            startActivity(intent);
            finish();
        } else if (method.equals(getEntRegulateRecordApi.getMethod())) {
            /**
             * 从服务器获取检验项目，然后保存到本地
             */
            List<RegulateResult2Bean> list = o.getJSONArray(ConstantValue.RESULT).toJavaList(RegulateResult2Bean.class);
            if (null == list && list.size() <= 0) {
                return;
            }
            RegulateResult2Bean bean = list.get(0);
            if (null == bean) {
                return;
            }
            if (bean.getInspstatus() != ConstantValue.OBJ_CHECK_STATUS_FINISH) {
                taskId = bean.getId();
                //先保存一下任务
                TaskBean taskBean = new TaskBean();
                taskBean.setId(taskId);
//                taskBean.setStatus(bean.getInspstatus());
                taskBean.setTime(bean.getCreateDate());
                taskBean.setEntid(objectId);
                taskBean.setTableId(bean.getInsptable().toString());
                taskBean.setUserId(MyApplication.getInstance().getUserBean().getUserExtId());
                taskBeanDao.insertOrReplace(taskBean);

                getItemResultApi = new GetItemResultApi();
                getItemResultApi.setParam(taskId);
                manager.doHttpDeal(getItemResultApi);
            }

        } else if (method.equals(getItemResultApi.getMethod())) {
            /**
             * 获取到检验项目之后，保存到本地
             */
            mItemList.clear();
            mItemList.addAll(o.getJSONArray(ConstantValue.RESULT).toJavaList(RegulateResultBean.class));
//            resultBeanDao.insertOrReplaceInTx(mItemList);
            mResultAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public void onError(ApiException e, String method) {

        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        LogUtils.d(e.getMessage());
    }

    @Override
    public void onBackPressed() {
        tipWindow.showAtLocation(getLayoutId(), Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onDestroy() {
        tipWindow.dismiss();
        reasonWindow.dismiss();
        super.onDestroy();
    }


    private void deleteTask() {
        deteleTaskApi = new DeteleTaskApi();
        TaskBean innerBean = new TaskBean();
        innerBean.setId(taskId);
        innerBean.setOisuper(MyApplication.getInstance().getUserBean().getUserExtId());
        SaveResultBean saveResultBean = new SaveResultBean();
        saveResultBean.setInsp(innerBean);
        deteleTaskApi.setBean(saveResultBean);
        manager.doHttpDeal(deteleTaskApi);
    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager       设置RecyclerView对应的manager
     * @param mRecyclerView 当前的RecyclerView
     * @param n             要跳转的位置
     */
    public void moveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {


        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

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

}
