package com.jstech.gridregulation.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.RegulateResultAdapter;
import com.jstech.gridregulation.api.GetItemResultApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.rxretrofitlibrary.greendao.RegulateResultBeanDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.ArrayList;
import java.util.List;

public class RegulateItemResultActivity extends BaseActivity
        implements HttpOnNextListener {

    private RecyclerView recyclerView;

    private RegulateResultAdapter adapter;

    private List<RegulateResultBean> mData;
    private String taskId = "";

    private HttpManager manager;
    private RegulateResultBeanDao regulateResultBeanDao;
    private GetItemResultApi getItemResultApi;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recycler_view;
    }

    @Override
    public void initView() {
        setToolBarTitle("检查结果");
        setToolSubBarTitle("");

        taskId = getIntent().getStringExtra(ConstantValue.SGIN_PATH);
        regulateResultBeanDao = MyApplication.getInstance().getSession().getRegulateResultBeanDao();

        recyclerView = findViewById(R.id.recyclerview);

        mData = new ArrayList<>();

        adapter = new RegulateResultAdapter(mData, this, R.layout.item_regulate_item_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        getItemResultApi = new GetItemResultApi();
        getItemResultApi.setParam(taskId);
        manager = new HttpManager(this, this);
        manager.doHttpDeal(getItemResultApi);
    }


    @Override
    public void onNext(String resulte, String method) {
        JSONObject o = JSON.parseObject(resulte);
        if (!ConstantValue.isSuccess(o)) {
            return;
        }
//        mData.clear();
        List<RegulateResultBean> list = regulateResultBeanDao.queryBuilder().where(
                RegulateResultBeanDao.Properties.Inspid.eq(taskId)
        ).list();
        List<RegulateResultBean> online = o.getJSONArray(ConstantValue.RESULT).toJavaList(RegulateResultBean.class);

        if (null != online && online.size() > 0) {
            if (null != list) {
                regulateResultBeanDao.deleteInTx(list);
            }
            regulateResultBeanDao.saveInTx(online);
        }

        mData.addAll(regulateResultBeanDao.queryBuilder().where(
                RegulateResultBeanDao.Properties.Inspid.eq(taskId)
        ).list());
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onError(ApiException e, String method) {
//        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        mData.clear();
        mData.addAll(regulateResultBeanDao.queryBuilder().where(
                RegulateResultBeanDao.Properties.Inspid.eq(taskId)
        ).list());
        adapter.notifyDataSetChanged();
    }

}
