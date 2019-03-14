package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.CheckItemSelectAdapter;
import com.jstech.gridregulation.api.AddTaskApi;
import com.jstech.gridregulation.api.GetItemApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.utils.LogUtils;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.MyUrl;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckItemBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 选择检查项目
 */
public class CheckItemSelectActivity extends BaseActivity implements
        CheckItemSelectAdapter.SelectInterface, View.OnClickListener, HttpOnNextListener {

    @BindView(R.id.recyclerview_table)
    RecyclerView rvTable;
    @BindView(R.id.ckb_all_select)
    CheckBox ckbAllSelect;
    @BindView(R.id.btn_save)
    Button btnSave;

    ArrayList<CheckItemBean> mCheckItemBeanList = new ArrayList<>();
    ArrayList<CheckItemBean> mSelectedList = new ArrayList<>();

    CheckItemSelectAdapter mAdapter;

    HttpManager manager;
    GetItemApi getItemApi;
    AddTaskApi addTaskApi;
    String tableId = "";
    Long objectId;
    RegulateObjectBean objectBean;
    String taskId = "";
    MyApplication application;

    @Override
    protected int getLayoutId() {
        /**
         * 跟选择检查表用同一个页面
         */
        return R.layout.activity_check_table_select;
    }

    @Override
    public void initView() {
        application = (MyApplication) getApplication();
        objectId = getIntent().getLongExtra(ConstantValue.KEY_OBJECT_ID, -1);
        tableId = getIntent().getExtras().getString(ConstantValue.KEY_TABLE_ID);
//        objectBean = application.getSession().getRegulateObjectBeanDao().load(objectId);
//        initList();
        mAdapter = new CheckItemSelectAdapter(mCheckItemBeanList, this, R.layout.item_check_item_select, this);
        rvTable.setLayoutManager(new LinearLayoutManager(this));
        rvTable.setAdapter(mAdapter);
        ckbAllSelect.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        manager = new HttpManager(this, this);
        getItemApi = new GetItemApi();
        getItemApi.setParam(tableId);
        addTaskApi = new AddTaskApi();
        manager.doHttpDeal(getItemApi);
    }

    /**
     * 检查表单项选择
     *
     * @param position 检查项目的位置
     * @param
     */
    @Override
    public void select(int position) {
        if (mCheckItemBeanList.get(position).isSelected()) {
            mCheckItemBeanList.get(position).setSelected(false);
        } else {
            mCheckItemBeanList.get(position).setSelected(true);
        }
        int isExist = mSelectedList.indexOf(mCheckItemBeanList.get(position));
        //如果该检查项已经在选中列表里，需要在选中列表中删除该检查表
        if (isExist == -1) {
            mSelectedList.add(mCheckItemBeanList.get(position));
        } else {
            mSelectedList.remove(mCheckItemBeanList.get(position));
        }
        if (isAllSelected()) {
            ckbAllSelect.setChecked(true);
        } else {
            ckbAllSelect.setChecked(false);
        }
        mAdapter.notifyDataSetChanged();

    }

    /**
     * 判断是否全部选中
     *
     * @return
     */
    private boolean isAllSelected() {
        for (CheckItemBean b : mCheckItemBeanList) {
            if (!b.isSelected()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //保存
            case R.id.btn_save:
                if (mSelectedList.size() == 0) {
                    Toast.makeText(this, "请选择检查项目", Toast.LENGTH_SHORT).show();
                    return;
                }

                //增加检查任务
                addTask();
                break;
            //全部选中
            case R.id.ckb_all_select:
                selectAll();
                break;
            default:
                break;
        }
    }

    /**
     * 向服务器请求，新增一个检查任务
     */
    private void addTask() {
        MyApplication app = (MyApplication) getApplication();
        TaskBean addTaskBean = new TaskBean();
        addTaskBean.setEntid(objectBean.getId());
        addTaskBean.setOisuper(app.getUserBean().getUserExtId());
        addTaskBean.setEntcredit(objectBean.getEntcredit());
        addTaskBean.setEntregion(objectBean.getEntregion());
        addTaskBean.setEnttype(objectBean.getNature());
        addTaskBean.setInsptable(tableId);
        addTaskBean.setEntname(objectBean.getName());
        addTaskBean.setUpdateBy(app.getUserBean().getUserExtId());
        addTaskBean.setCreateBy(app.getUserBean().getUserExtId());
        addTaskApi.setParams(addTaskBean);
        manager.doHttpDeal(addTaskApi);
    }

    private void selectAll() {
        if (isAllSelected()) {
            //如果已经宣布选中，再点击时应该全部取消
            for (CheckItemBean b : mCheckItemBeanList) {
                b.setSelected(false);
            }
            mSelectedList.clear();
            ckbAllSelect.setChecked(false);
        } else {
            for (CheckItemBean b : mCheckItemBeanList) {
                b.setSelected(true);
            }
            mSelectedList.clear();
            mSelectedList.addAll(mCheckItemBeanList);
            ckbAllSelect.setChecked(true);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNext(String resulte, String method) {
        LogUtils.d(resulte);
        LogUtils.d(method);
        JSONObject o = JSON.parseObject(resulte);
        String code = o.getString(ConstantValue.CODE);
        if (method.equals(MyUrl.GET_ITEM)) {
            if ("200".equals(code)) {
                mCheckItemBeanList.clear();
                mCheckItemBeanList.addAll((ArrayList<CheckItemBean>) o.getJSONArray(ConstantValue.RESULT).toJavaList(CheckItemBean.class));
                mAdapter.notifyDataSetChanged();
                LogUtils.d(mCheckItemBeanList.size() + "");
            }
        } else if (method.equals(addTaskApi.getMethod())) {
            taskId = o.getString(ConstantValue.RESULT);
            //保存任务和选中的检查表检查项
            Intent intent = new Intent(this, SiteCheckActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("list", mSelectedList);
            LogUtils.d("taskId =" + o.getString("result"));
            bundle.putString(ConstantValue.KEY_TASK_ID, o.getString("result"));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onError(ApiException e, String method) {

    }

    public class SaveDataAscyn extends AsyncTask<List<CheckItemBean>, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            LogUtils.d("正在保存数据");
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
        }

        @Override
        protected Boolean doInBackground(List<CheckItemBean>... lists) {
            return null;
        }
    }
}
